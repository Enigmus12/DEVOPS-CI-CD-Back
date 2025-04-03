package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import eci.edu.back.cvds_back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookingController is a REST controller that provides endpoints for managing
 * bookings and reservations. It handles operations such as retrieving bookings,
 * creating new bookings, making reservations, and canceling reservations.
 * 
 * Endpoints:
 * - GET /booking-service/bookings: Retrieves all bookings.
 * - GET /booking-service/my-reservations: Retrieves bookings made by the authenticated user.
 * - GET /booking-service/bookings/{bookingId}: Retrieves a specific booking by its ID.
 * - POST /booking-service/bookings: Creates a new booking.
 * - DELETE /booking-service/bookings/{bookingId}: Deletes a booking by its ID.
 * - PUT /booking-service/bookings/make/{bookingId}: Makes a reservation for a booking.
 * - PUT /booking-service/bookings/cancel/{bookingId}: Cancels a reservation for a booking.
 * 
 * Authorization:
 * - Some endpoints require an Authorization header with a Bearer token to
 *   authenticate the user and extract their user ID.
 * 
 * Dependencies:
 * - BookingService: Service layer for booking-related operations.
 * - JwtUtil: Utility for extracting user information from JWT tokens.
 * 
 * Cross-Origin Resource Sharing (CORS):
 * - Allows access from any origin using the @CrossOrigin annotation.
 * 
 * Exceptions:
 * - BookingServiceException: Thrown when an error occurs in booking operations.
 */
@RestController
@RequestMapping("/booking-service")
@CrossOrigin(origins = "*") // Permite acceso desde cualquier lado
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Retrieves a list of all bookings.
     *
     * @return a list of {@link Booking} objects representing all bookings.
     */
    @GetMapping("/bookings")
    public List<Booking> bookings() {
        return bookingService.getAllBookings();
    }

    /**
     * Retrieves a list of bookings made by the currently authenticated user.
     *
     * @param authHeader The Authorization header containing the Bearer token for authentication.
     * @return A list of Booking objects associated with the authenticated user.
     * 
     * @throws IllegalArgumentException if the Authorization header is missing or invalid.
     */
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

    /**
     * Retrieves a booking by its unique identifier.
     *
     * @param bookingId The unique identifier of the booking to retrieve.
     * @return The Booking object corresponding to the provided bookingId.
     * @throws BookingServiceException If an error occurs while retrieving the booking.
     */
    @GetMapping("/bookings/{bookingId}")
    public Booking booking(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.getBooking(bookingId);
    }

    /**
     * Handles the HTTP POST request to create a new booking.
     *
     * @param booking The booking data transfer object (DTO) containing the details
     *                of the booking to be created.
     * @return The created Booking object after being saved by the booking service.
     * @throws BookingServiceException If an error occurs while saving the booking.
     */
    @PostMapping("/bookings")
    public Booking booking(@RequestBody BookingDTO booking) throws BookingServiceException {
        return bookingService.saveBooking(booking);
    }

    /**
     * Deletes a booking with the specified booking ID and returns the updated list of all bookings.
     *
     * @param bookingId The ID of the booking to be deleted.
     * @return A list of all remaining bookings after the specified booking is deleted.
     * @throws BookingServiceException If an error occurs while deleting the booking.
     */
    @DeleteMapping("/bookings/{bookingId}")
    public List<Booking> deleteBooking(@PathVariable String bookingId) throws BookingServiceException {
        bookingService.deleteBooking(bookingId);
        return bookingService.getAllBookings();
    }

    /**
     * Handles the HTTP PUT request to make a booking reservation.
     *
     * @param bookingId The ID of the booking to be reserved.
     * @param authHeader The Authorization header containing the Bearer token for authentication.
     * @return The updated Booking object after the reservation is made.
     * @throws BookingServiceException If authentication fails or the reservation cannot be completed.
     *
     * This method extracts the user ID from the provided Bearer token in the Authorization header.
     * If the token is invalid or missing, an exception is thrown. The user ID is then used to
     * associate the reservation with the authenticated user by calling the booking service.
     */
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

    /**
     * Cancels a booking reservation based on the provided booking ID.
     * 
     * @param bookingId The ID of the booking to be canceled.
     * @param authHeader The authorization header containing the Bearer token.
     * @return The updated Booking object after cancellation.
     * @throws BookingServiceException If an error occurs during the cancellation process.
     * 
     * This method extracts the user ID from the Bearer token in the authorization
     * header and ensures that the cancellation is performed either by the user who
     * made the reservation or by an administrator.
     */
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