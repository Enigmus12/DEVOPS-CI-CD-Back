package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.model.Booking;

import java.util.List;

/**
 * Service interface for generating and managing bookings.
 */
public interface BookingGeneratorService {

    /**
     * Generates a list of random bookings within the specified range.
     *
     * @param min The minimum number of bookings to generate.
     * @param max The maximum number of bookings to generate.
     * @return A list of randomly generated bookings.
     */
    List<Booking> generateRandomBookings(int min, int max);

    /**
     * Generates a list of bookings with an exact count.
     *
     * @param count The exact number of bookings to generate.
     * @return A list of bookings with the specified count.
     */
    List<Booking> generateExactBookings(int count);

    /**
     * Clears all existing bookings.
     *
     * @return The number of bookings that were cleared.
     */
    int clearAllBookings();
}