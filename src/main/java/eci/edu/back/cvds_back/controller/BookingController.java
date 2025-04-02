package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import eci.edu.back.cvds_back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking-service")
@CrossOrigin(origins = "*") // Permite acceso desde cualquier lado
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/bookings")
    public List<Booking> bookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/my-reservations")
    public List<Booking> myReservations(@RequestHeader("Authorization") String authHeader) {
        String token = null;
        String userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwtUtil.extractUserId(token);
        }

        return bookingService.getBookingsByReservedBy(userId);
    }

    @GetMapping("/bookings/{bookingId}")
    public Booking booking(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.getBooking(bookingId);
    }

    @PostMapping("/bookings")
    public Booking booking(@RequestBody BookingDTO booking) throws BookingServiceException {
        return bookingService.saveBooking(booking);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public List<Booking> deleteBooking(@PathVariable String bookingId) throws BookingServiceException {
        bookingService.deleteBooking(bookingId);
        return bookingService.getAllBookings();
    }

    @PutMapping("/bookings/make/{bookingId}")
    public Booking makeBookingReservation(
            @PathVariable String bookingId,
            @RequestHeader("Authorization") String authHeader) throws BookingServiceException {

        String token = null;
        String userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwtUtil.extractUserId(token);
        }

        // Si no hay token o no se pudo extraer el userId, lanzar excepción
        if (userId == null) {
            throw new BookingServiceException("Se requiere autenticación para realizar una reserva");
        }

        // Llamar al servicio con el userId para que lo asocie al hacer la reserva
        return bookingService.makeReservation(bookingId, userId);
    }

    @PutMapping("/bookings/cancel/{bookingId}")
    public Booking cancelBookingReservation(
            @PathVariable String bookingId,
            @RequestHeader("Authorization") String authHeader) throws BookingServiceException {

        String token = null;
        String userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwtUtil.extractUserId(token);
        }

        // Al cancelar, verificamos que sea el mismo usuario que hizo la reserva o un admin
        return bookingService.cancelReservation(bookingId, userId);
    }
}