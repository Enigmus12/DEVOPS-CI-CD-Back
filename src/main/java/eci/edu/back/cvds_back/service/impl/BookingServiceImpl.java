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

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Override
    public Booking getBooking(String bookingId) throws BookingServiceException {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public Booking saveBooking(BookingDTO bookingDTO) throws BookingServiceException {
        if (bookingRepository.existsById(bookingDTO.getBookingId())) {
            throw new BookingServiceException("Error: El bookingId '" + bookingDTO.getBookingId() + "' ya existe.");
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



    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void deleteBooking(String bookingId) throws BookingServiceException {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public Booking updateBooking(String bookingId, boolean status) throws BookingServiceException {
        Booking booking = bookingRepository.findById(bookingId);

        if (status && booking.isDisable()) {
            throw new BookingServiceException("La reserva ya está cancelada.");
        }

        if (!status && !booking.isDisable()) {
            throw new BookingServiceException("La reserva ya está activa.");
        }

        booking.setDisable(status);
        bookingRepository.update(booking);
        return booking;
    }


}