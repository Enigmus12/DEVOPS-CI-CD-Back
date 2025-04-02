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

    @Override
    public List<Booking> generateRandomBookings(int min, int max) {
        int targetBookings = random.nextInt(max - min + 1) + min;
        return generateBookings(targetBookings);
    }

    @Override
    public List<Booking> generateExactBookings(int count) {
        return generateBookings(count);
    }

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

    public boolean isSlotBooked(Map<String, Map<LocalDate, Set<Integer>>> bookedSlots,
                                 String classroom, LocalDate date, int hour) {
        Map<LocalDate, Set<Integer>> classroomBookings = bookedSlots.get(classroom);
        if (classroomBookings == null) return false;

        Set<Integer> dateBookings = classroomBookings.get(date);
        if (dateBookings == null) return false;

        return dateBookings.contains(hour);
    }

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