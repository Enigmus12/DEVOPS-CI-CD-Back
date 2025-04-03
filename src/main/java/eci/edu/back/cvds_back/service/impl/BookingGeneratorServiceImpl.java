package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingGeneratorService;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of the BookingGeneratorService interface that provides methods
 * for generating, managing, and clearing bookings for classrooms.
 * 
 * This service generates random or exact bookings, clears all existing bookings,
 * and ensures that bookings do not conflict with existing ones. It uses a 
 * combination of randomization and validation to create bookings in available 
 * time slots.
 * 
 * Dependencies:
 * - BookingService: Used to interact with the booking data layer.
 * 
 * Features:
 * - Generate random bookings within a specified range.
 * - Generate a specific number of bookings.
 * - Clear all existing bookings.
 * - Validate and ensure no conflicting bookings are created.
 * 
 * Thread Safety:
 * - Uses an AtomicInteger (labCounter) to ensure thread-safe generation of unique booking IDs.
 * 
 * Methods:
 * - {@link #generateRandomBookings(int, int)}: Generates a random number of bookings within a range.
 * - {@link #generateExactBookings(int)}: Generates an exact number of bookings.
 * - {@link #clearAllBookings()}: Deletes all existing bookings and returns the count of deleted bookings.
 * - {@link #getRandomAvailableBooking(Map)}: Finds a random available booking slot.
 * - {@link #isSlotBooked(Map, String, LocalDate, int)}: Checks if a specific slot is already booked.
 * - {@link #createBookingDTO(Booking)}: Creates a BookingDTO object from a temporary booking.
 * 
 * Internal Logic:
 * - Maintains a map of booked slots to avoid conflicts.
 * - Iterates through classrooms, dates, and hours to find available slots.
 * - Generates unique booking IDs using a counter.
 * - Handles exceptions during booking creation and deletion.
 * 
 * Usage:
 * This service is intended to be used in scenarios where automated booking 
 * generation is required, such as testing or populating a system with sample data.
 * 
 * Note:
 * - The service assumes a predefined set of classrooms and valid booking hours.
 * - Bookings are generated for up to 30 days from the current date.
 */
@Service
public class BookingGeneratorServiceImpl implements BookingGeneratorService {

    @Autowired
    private BookingService bookingService;

    private final Random random = new Random();
    private final AtomicInteger labCounter = new AtomicInteger(1);

    public final String[] classrooms = {
            "A101", "A102", "B201", "B202", "C301",
            "C302", "D401", "D402", "E501", "E502"
    };

    public final int[] validHours = {7, 9, 11, 13, 15, 17, 19};

    /**
     * Generates a random number of bookings within the specified range.
     *
     * @param min The minimum number of bookings to generate.
     * @param max The maximum number of bookings to generate.
     * @return A list of randomly generated bookings.
     * @throws IllegalArgumentException If min is greater than max.
     */
    @Override
    public List<Booking> generateRandomBookings(int min, int max) {
        int targetBookings = random.nextInt(max - min + 1) + min;
        return generateBookings(targetBookings);
    }

    /**
     * Generates a specified number of exact bookings.
     *
     * @param count the number of bookings to generate
     * @return a list of generated bookings
     */
    @Override
    public List<Booking> generateExactBookings(int count) {
        return generateBookings(count);
    }

    /**
     * Clears all bookings by retrieving all existing bookings and deleting them one by one.
     * If an error occurs while deleting a booking, it logs the error and continues with the next booking.
     *
     * @return The total number of bookings that were attempted to be deleted.
     */
    @Override
    public int clearAllBookings() {
        List<Booking> allBookings = bookingService.getAllBookings();
        int count = allBookings.size();

        for (Booking booking : allBookings) {
            try {
                bookingService.deleteBooking(booking.getBookingId());
            } catch (BookingServiceException e) {
                System.out.println("Error deleting booking " + booking.getBookingId() + ": " + e.getMessage());
            }
        }

        return count;
    }

    /**
     * Generates a specified number of bookings while avoiding conflicts with existing bookings.
     *
     * @param targetBookings The number of bookings to generate.
     * @return A list of generated bookings.
     * 
     * This method initializes a counter for lab usage and maintains a map of booked slots
     * to track existing bookings. It attempts to generate new bookings by finding available
     * slots and saving them. If a conflict occurs during booking, it skips the conflicting
     * booking and continues. The method stops generating bookings either when the target
     * number of bookings is reached or when the maximum number of attempts is exceeded.
     *
     * The generated bookings are saved using the booking service, and the booked slots map
     * is updated accordingly to reflect the new bookings.
     *
     * Note: If no more available slots are found, the method will terminate early and return
     * the bookings generated up to that point.
     *
     * @throws BookingServiceException If an error occurs while saving a booking.
     */
    private List<Booking> generateBookings(int targetBookings) {
        List<Booking> generatedBookings = new ArrayList<>();

        initializeLabCounter();

        Map<String, Map<LocalDate, Set<Integer>>> bookedSlots = new HashMap<>();

        for (String classroom : classrooms) {
            bookedSlots.put(classroom, new HashMap<>());
        }

        List<Booking> existingBookings = bookingService.getAllBookings();
        for (Booking booking : existingBookings) {
            String classroom = booking.getBookingClassRoom();
            LocalDate date = booking.getBookingDate();
            int hour = booking.getBookingTime().getHour();

            if (!bookedSlots.containsKey(classroom)) {
                bookedSlots.put(classroom, new HashMap<>());
            }

            if (!bookedSlots.get(classroom).containsKey(date)) {
                bookedSlots.get(classroom).put(date, new HashSet<>());
            }

            bookedSlots.get(classroom).get(date).add(hour);
        }

        int attempts = 0;
        int maxAttempts = targetBookings * 5;

        while (generatedBookings.size() < targetBookings && attempts < maxAttempts) {
            attempts++;

            try {
                Booking tempBooking = getRandomAvailableBooking(bookedSlots);
                if (tempBooking == null) {
                    System.out.println("No more available slots. Generated " + generatedBookings.size() + " bookings.");
                    break;
                }

                BookingDTO bookingDTO = createBookingDTO(tempBooking);
                Booking booking = bookingService.saveBooking(bookingDTO);
                generatedBookings.add(booking);

                String classroom = tempBooking.getBookingClassRoom();
                LocalDate date = tempBooking.getBookingDate();
                int hour = tempBooking.getBookingTime().getHour();

                if (!bookedSlots.get(classroom).containsKey(date)) {
                    bookedSlots.get(classroom).put(date, new HashSet<>());
                }
                bookedSlots.get(classroom).get(date).add(hour);

            } catch (BookingServiceException e) {
                System.out.println("Skipping conflicting booking: " + e.getMessage());
            }
        }

        return generatedBookings;
    }

    /**
     * Initializes the lab counter by analyzing existing bookings and determining
     * the highest lab number currently in use. The lab counter is then set to one
     * greater than the highest lab number found.
     * 
     * This method retrieves all bookings from the booking service and checks for
     * booking IDs that start with "lab". It extracts the numeric part of the ID,
     * parses it, and keeps track of the maximum lab number encountered. If no
     * valid lab numbers are found, the counter is initialized to 1.
     * 
     * Note: Any non-numeric or malformed booking IDs starting with "lab" are
     * ignored.
     */
    private void initializeLabCounter() {
        List<Booking> existingBookings = bookingService.getAllBookings();
        int maxLabNumber = 0;

        for (Booking booking : existingBookings) {
            String bookingId = booking.getBookingId();
            if (bookingId.startsWith("lab")) {
                try {
                    int labNumber = Integer.parseInt(bookingId.substring(3));
                    maxLabNumber = Math.max(maxLabNumber, labNumber);
                } catch (NumberFormatException e) {
                }
            }
        }

        labCounter.set(maxLabNumber + 1);
    }

    /**
     * Retrieves a random available booking from the provided map of booked slots.
     * 
     * This method generates a list of available booking slots for the next 30 days
     * for all classrooms and valid hours. It checks if a slot is already booked
     * and, if not, creates a temporary booking object for that slot. A random
     * booking is then selected from the list of available slots.
     * 
     * @param bookedSlots A map containing the booked slots, where the key is the
     *                    classroom name, and the value is another map with dates
     *                    as keys and sets of booked hours as values.
     * @return A randomly selected available booking, or {@code null} if no
     *         available slots are found.
     */
    public Booking getRandomAvailableBooking(Map<String, Map<LocalDate, Set<Integer>>> bookedSlots) {
        List<Booking> availableSlots = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (String classroom : classrooms) {
            for (int day = 0; day < 30; day++) {
                LocalDate date = today.plusDays(day);

                for (int hour : validHours) {
                    if (!isSlotBooked(bookedSlots, classroom, date, hour)) {
                        BookingDTO dto = new BookingDTO();
                        dto.setBookingClassRoom(classroom);
                        dto.setBookingDate(date);
                        dto.setBookingTime(LocalTime.of(hour, 0));
                        dto.setBookingId("temp");
                        dto.setDisable(true);
                        dto.setPriority(1);

                        Booking tempBooking = new Booking(dto);
                        availableSlots.add(tempBooking);
                    }
                }
            }
        }

        if (availableSlots.isEmpty()) {
            return null;
        }
        return availableSlots.get(random.nextInt(availableSlots.size()));
    }

    /**
     * Checks if a specific time slot is booked for a given classroom on a specific date.
     *
     * @param bookedSlots A map containing booking information, where the key is the classroom name,
     *                    and the value is another map with dates as keys and sets of booked hours as values.
     * @param classroom   The name of the classroom to check.
     * @param date        The date to check for bookings.
     * @param hour        The hour to check for bookings (in 24-hour format).
     * @return {@code true} if the specified time slot is booked, {@code false} otherwise.
     */
    public boolean isSlotBooked(Map<String, Map<LocalDate, Set<Integer>>> bookedSlots,
                                 String classroom, LocalDate date, int hour) {
        Map<LocalDate, Set<Integer>> classroomBookings = bookedSlots.get(classroom);
        if (classroomBookings == null) return false;

        Set<Integer> dateBookings = classroomBookings.get(date);
        if (dateBookings == null) return false;

        return dateBookings.contains(hour);
    }

    /**
     * Creates a BookingDTO object based on the provided Booking instance.
     * 
     * This method generates a unique booking ID, copies relevant booking details
     * from the input Booking object, assigns a random priority between 1 and 5, 
     * and sets the booking as disabled by default.
     * 
     * @param tempBooking The Booking object containing the details to be copied 
     *                    into the BookingDTO.
     * @return A BookingDTO object populated with the details from the input 
     *         Booking object and additional generated values.
     */
    private BookingDTO createBookingDTO(Booking tempBooking) {
        BookingDTO bookingDTO = new BookingDTO();

        String bookingId = "lab" + labCounter.getAndIncrement();
        bookingDTO.setBookingId(bookingId);

        bookingDTO.setBookingDate(tempBooking.getBookingDate());
        bookingDTO.setBookingTime(tempBooking.getBookingTime());

        bookingDTO.setBookingClassRoom(tempBooking.getBookingClassRoom());

        int priority = random.nextInt(5) + 1;
        bookingDTO.setPriority(priority);

        bookingDTO.setDisable(true);

        return bookingDTO;
    }



}