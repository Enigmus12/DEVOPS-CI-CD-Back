package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking-service")
@CrossOrigin(origins = "*") // Permite acceso desde cualquier lado

public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public List<Booking> bookings() {
        return bookingService.getAllBookings();
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
        return  bookingService.getAllBookings();
    }

    @PutMapping("/bookings/make/{bookingId}")
    public Booking makeBookingReservation(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.updateBooking(bookingId, false);
    }

    @PutMapping("/bookings/cancel/{bookingId}")
    public Booking cancelBookingReservation(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.updateBooking(bookingId, true);
    }

}
