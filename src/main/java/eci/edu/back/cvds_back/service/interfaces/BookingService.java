package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;

import java.util.List;

public interface BookingService {
    Booking getBooking(String bookingId) throws BookingServiceException;
    Booking saveBooking(BookingDTO bookingDTO) throws BookingServiceException;
    List<Booking> getAllBookings();
    List<Booking> getBookingsByReservedBy(String userId);
    void deleteBooking(String bookingId) throws BookingServiceException;

    // Métodos actualizados para trabajar con usuarios
    Booking makeReservation(String bookingId, String userId) throws BookingServiceException;
    Booking cancelReservation(String bookingId, String userId) throws BookingServiceException;
}