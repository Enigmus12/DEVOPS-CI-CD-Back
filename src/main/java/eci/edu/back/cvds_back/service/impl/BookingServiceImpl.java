package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingRepository;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the BookingService interface, providing methods to manage bookings.
 * This service interacts with the BookingRepository to perform CRUD operations and
 * enforce business rules related to bookings.
 * 
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Retrieving bookings by ID or user.</li>
 *   <li>Saving new bookings with validation checks.</li>
 *   <li>Deleting bookings by ID.</li>
 *   <li>Activating and canceling reservations with user-specific constraints.</li>
 * </ul>
 * 
 * <p>Validation rules enforced by this service:</p>
 * <ul>
 *   <li>Booking IDs must be unique.</li>
 *   <li>Priority must be between 1 and 5.</li>
 *   <li>Bookings in the same classroom must not overlap within a 2-hour interval.</li>
 *   <li>Only the user who made a reservation can cancel it.</li>
 * </ul>
 * 
 * <p>Exceptions:</p>
 * <ul>
 *   <li>{@link BookingServiceException} is thrown for any business rule violations or errors.</li>
 * </ul>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>{@code @Service}: Marks this class as a Spring service component.</li>
 *   <li>{@code @Autowired}: Injects the BookingRepository dependency.</li>
 * </ul>
 */
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Retrieves a booking by its unique identifier.
     *
     * @param bookingId The unique identifier of the booking to retrieve.
     * @return The Booking object corresponding to the provided bookingId.
     * @throws BookingServiceException If an error occurs while retrieving the booking.
     */
    @Override
    public Booking getBooking(String bookingId) throws BookingServiceException {
        return bookingRepository.findById(bookingId);
    }

    /**
     * Saves a new booking based on the provided BookingDTO.
     *
     * @param bookingDTO The data transfer object containing booking details.
     * @return The saved Booking entity.
     * @throws BookingServiceException If:
     *         <ul>
     *           <li>The booking ID already exists in the repository.</li>
     *           <li>The priority is not between 1 and 5.</li>
     *           <li>There is an existing booking in the same classroom within a 2-hour interval.</li>
     *         </ul>
     */
    @Override
    public Booking saveBooking(BookingDTO bookingDTO) throws BookingServiceException {
        if (bookingRepository.existsById(bookingDTO.getBookingId())) {
            throw new BookingServiceException("Error: El bookingId '" + bookingDTO.getBookingId() + "' ya existe.");
        }

        if (bookingDTO.getPriority() < 1 || bookingDTO.getPriority() > 5) {
            throw new BookingServiceException("Error: La prioridad debe estar entre 1 y 5.");
        }

        List<Booking> existingBookings = bookingRepository.findAll();
        LocalDate newDate = bookingDTO.getBookingDate();
        LocalTime newTime = bookingDTO.getBookingTime();
        String newClassRoom = bookingDTO.getBookingClassRoom();

        for (Booking existingBooking : existingBookings) {
            if (existingBooking.getBookingDate().equals(newDate) &&
                    existingBooking.getBookingClassRoom().equals(newClassRoom)) {

                // Calcular la diferencia en horas entre las reservas
                long difference = Math.abs(existingBooking.getBookingTime().until(newTime, java.time.temporal.ChronoUnit.HOURS));

                if (difference < 2) {
                    throw new BookingServiceException("Error: No se puede reservar en el mismo salón dentro de un intervalo de 2 horas.");
                }
            }
        }

        Booking booking = new Booking(bookingDTO);
        bookingRepository.save(booking);
        return booking;
    }

    /**
     * Retrieves a list of all bookings from the repository.
     *
     * @return a list of all {@link Booking} objects.
     */
    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Retrieves a list of bookings that are reserved by a specific user.
     *
     * @param userId The ID of the user whose bookings are to be retrieved.
     * @return A list of bookings where the reservedBy field matches the given userId.
     */
    @Override
    public List<Booking> getBookingsByReservedBy(String userId) {
        return bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getReservedBy() != null && booking.getReservedBy().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Deletes a booking with the specified booking ID.
     *
     * @param bookingId The unique identifier of the booking to be deleted.
     * @throws BookingServiceException If an error occurs during the deletion process.
     */
    @Override
    public void deleteBooking(String bookingId) throws BookingServiceException {
        bookingRepository.deleteById(bookingId);
    }

    /**
     * Activates a previously disabled booking and assigns it to a user.
     *
     * @param bookingId The unique identifier of the booking to be activated.
     * @param userId The unique identifier of the user making the reservation.
     * @return The updated Booking object with the reservation details.
     * @throws BookingServiceException If the booking is already active.
     */
    @Override
    public Booking makeReservation(String bookingId, String userId) throws BookingServiceException {
        Booking booking = bookingRepository.findById(bookingId);

        if (!booking.isDisable()) {
            throw new BookingServiceException("La reserva ya está activa.");
        }

        booking.setDisable(false);
        booking.setReservedBy(userId); // Asignamos el userId a la reserva
        bookingRepository.update(booking);
        return booking;
    }

    /**
     * Cancels a reservation based on the provided booking ID and user ID.
     *
     * @param bookingId The unique identifier of the booking to be canceled.
     * @param userId The unique identifier of the user attempting to cancel the booking.
     * @return The updated Booking object with the reservation canceled.
     * @throws BookingServiceException If the reservation is already canceled or if the user
     *         attempting to cancel the reservation is not the one who made it.
     */
    @Override
    public Booking cancelReservation(String bookingId, String userId) throws BookingServiceException {
        Booking booking = bookingRepository.findById(bookingId);

        if (booking.isDisable()) {
            throw new BookingServiceException("La reserva ya está cancelada.");
        }

        // Verificar que el usuario que cancela sea quien hizo la reserva
        if (booking.getReservedBy() != null && !booking.getReservedBy().equals(userId)) {
            throw new BookingServiceException("Solo el usuario que realizó la reserva puede cancelarla.");
        }

        booking.setDisable(true);
        // Eliminamos el reservedBy para que no quede asociado a ningún usuario
        booking.setReservedBy(null);
        bookingRepository.update(booking);
        return booking;
    }
}