package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BookingRepository interface that provides methods
 * for managing Booking entities using a MongoDB repository.
 * 
 * This class uses the BookingMongoRepository to perform CRUD operations
 * on Booking entities. It includes methods for saving, retrieving, updating,
 * and deleting bookings, as well as checking for the existence of a booking
 * by its ID.
 * 
 * Methods:
 * - save(Booking booking): Saves a booking entity to the database.
 * - findAll(): Retrieves all booking entities from the database.
 * - findById(String bookingId): Retrieves a booking entity by its ID.
 *   Throws BookingServiceException if the booking is not found.
 * - deleteById(String bookingId): Deletes a booking entity by its ID.
 * - update(Booking booking): Updates an existing booking entity.
 *   Throws BookingServiceException if the booking does not exist.
 * - existsById(String bookingId): Checks if a booking entity exists by its ID.
 * 
 * Exceptions:
 * - BookingServiceException: Thrown when a booking is not found during
 *   retrieval, deletion, or update operations.
 * 
 * Dependencies:
 * - BookingMongoRepository: The MongoDB repository used for data access.
 * 
 * Annotations:
 * - @Service: Marks this class as a Spring service component.
 * - @Autowired: Injects the BookingMongoRepository dependency.
 */
@Service
public class BookingRepositoryImpl implements BookingRepository {
    @Autowired
    private BookingMongoRepository bookingMongoRepository;

    /**
     * Saves the given booking entity to the database.
     *
     * @param booking the booking entity to be saved
     */
    @Override
    public void save(Booking booking) {
        bookingMongoRepository.save(booking);
    }

    /**
     * Retrieves all booking records from the database.
     *
     * @return a list of all bookings.
     */
    @Override
    public List<Booking> findAll() {
        return bookingMongoRepository.findAll();
    }

    /**
     * Retrieves a booking by its unique identifier.
     *
     * @param bookingId The unique identifier of the booking to retrieve.
     * @return The Booking object associated with the given bookingId.
     * @throws BookingServiceException If no booking is found with the given bookingId.
     */
    @Override
    public Booking findById(String bookingId) throws BookingServiceException{
        Optional<Booking> booking = bookingMongoRepository.findById(bookingId);
        if(booking.isEmpty()) throw new BookingServiceException("Booking Not found");
        return booking.get();
    }

    /**
     * Deletes a booking record by its unique identifier.
     *
     * @param bookingId The unique identifier of the booking to be deleted.
     * @throws BookingServiceException If an error occurs during the deletion process.
     */
    @Override
    public void deleteById(String bookingId) throws BookingServiceException {
        bookingMongoRepository.deleteById(bookingId);
    }

    /**
     * Updates an existing booking in the repository.
     *
     * @param booking The booking object containing updated information.
     * @throws BookingServiceException If the booking does not exist in the repository.
     */
    @Override
    public void update(Booking booking) throws BookingServiceException {
        if (!bookingMongoRepository.existsById(booking.getBookingId())) {
            throw new BookingServiceException("Booking Not Found");
        }
        bookingMongoRepository.save(booking);
    }

    /**
     * Checks if a booking exists in the repository by its unique identifier.
     *
     * @param bookingId the unique identifier of the booking to check
     * @return true if a booking with the given ID exists, false otherwise
     */
    @Override
    public boolean existsById(String bookingId) {
        return bookingMongoRepository.existsById(bookingId);
    }

}