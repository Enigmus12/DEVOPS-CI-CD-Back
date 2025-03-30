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

    /**
     * Retrieves a list of all bookings.
     * @return List of all bookings.
     */
    @GetMapping("/bookings")
    public List<Booking> bookings() {
        return bookingService.getAllBookings();
    }

    /**
     * Retrieves a specific booking by its ID.
     * @param bookingId The ID of the booking to retrieve.
     * @return The booking with the specified ID.
     * @throws BookingServiceException If the booking cannot be retrieved.
     */
    @GetMapping("/bookings/{bookingId}")
    public Booking booking(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.getBooking(bookingId);
    }

    /**
     * Creates a new booking.
     * @param booking The BookingDTO object containing booking details.
     * @return The created booking.
     * @throws BookingServiceException If the booking cannot be created.
     */
    @PostMapping("/bookings")
    public Booking booking(@RequestBody BookingDTO booking) throws BookingServiceException {
        return bookingService.saveBooking(booking);
    }

    /**
     * Deletes a booking by its ID.
     * @param bookingId The ID of the booking to delete.
     * @return List of all remaining bookings after deletion.
     * @throws BookingServiceException If the booking cannot be deleted.
     */
    @DeleteMapping("/bookings/{bookingId}")
    public List<Booking> deleteBooking(@PathVariable String bookingId) throws BookingServiceException {
        bookingService.deleteBooking(bookingId);
        return bookingService.getAllBookings();
    }

    /**
     * Marks a booking as reserved.
     * @param bookingId The ID of the booking to update.
     * @return The updated booking with reservation status set to false.
     * @throws BookingServiceException If the booking cannot be updated.
     */
    @PutMapping("/bookings/make/{bookingId}")
    public Booking makeBookingReservation(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.updateBooking(bookingId, false);
    }

    /**
     * Cancels a booking reservation.
     * @param bookingId The ID of the booking to update.
     * @return The updated booking with reservation status set to true.
     * @throws BookingServiceException If the booking cannot be updated.
     */
    @PutMapping("/bookings/cancel/{bookingId}")
    public Booking cancelBookingReservation(@PathVariable String bookingId) throws BookingServiceException {
        return bookingService.updateBooking(bookingId, true);
    }
}
