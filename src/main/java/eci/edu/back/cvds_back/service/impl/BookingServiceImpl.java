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

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByReservedBy(String userId) {
        return bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getReservedBy() != null && booking.getReservedBy().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBooking(String bookingId) throws BookingServiceException {
        bookingRepository.deleteById(bookingId);
    }

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