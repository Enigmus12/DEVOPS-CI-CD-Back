package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;

import java.util.List;

public interface BookingService {
    Booking getBooking(String bookingId) throws BookingServiceException;
    Booking saveBooking(BookingDTO booking) throws BookingServiceException;
    List<Booking> getAllBookings();
    void deleteBooking(String bookingId) throws BookingServiceException;
    Booking updateBooking(String bookingId, boolean status) throws BookingServiceException;
}