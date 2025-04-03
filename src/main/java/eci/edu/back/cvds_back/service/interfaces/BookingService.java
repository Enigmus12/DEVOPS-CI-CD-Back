package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;

import java.util.List;

/**
 * Service interface for managing bookings.
 */
public interface BookingService {

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking to retrieve.
     * @return The booking associated with the given ID.
     * @throws BookingServiceException If an error occurs while retrieving the booking.
     */
    Booking getBooking(String bookingId) throws BookingServiceException;

    /**
     * Saves a new booking based on the provided booking data.
     *
     * @param bookingDTO The data transfer object containing booking details.
     * @return The saved booking.
     * @throws BookingServiceException If an error occurs while saving the booking.
     */
    Booking saveBooking(BookingDTO bookingDTO) throws BookingServiceException;

    /**
     * Retrieves all bookings.
     *
     * @return A list of all bookings.
     */
    List<Booking> getAllBookings();

    /**
     * Retrieves bookings made by a specific user.
     *
     * @param userId The ID of the user whose bookings are to be retrieved.
     * @return A list of bookings made by the specified user.
     */
    List<Booking> getBookingsByReservedBy(String userId);

    /**
     * Deletes a booking by its ID.
     *
     * @param bookingId The ID of the booking to delete.
     * @throws BookingServiceException If an error occurs while deleting the booking.
     */
    void deleteBooking(String bookingId) throws BookingServiceException;

    /**
     * Makes a reservation for a booking by associating it with a user.
     *
     * @param bookingId The ID of the booking to reserve.
     * @param userId The ID of the user making the reservation.
     * @return The updated booking with the reservation details.
     * @throws BookingServiceException If an error occurs while making the reservation.
     */
    Booking makeReservation(String bookingId, String userId) throws BookingServiceException;

    /**
     * Cancels a reservation for a booking by dissociating it from a user.
     *
     * @param bookingId The ID of the booking to cancel the reservation for.
     * @param userId The ID of the user whose reservation is to be canceled.
     * @return The updated booking with the reservation removed.
     * @throws BookingServiceException If an error occurs while canceling the reservation.
     */
    Booking cancelReservation(String bookingId, String userId) throws BookingServiceException;
}