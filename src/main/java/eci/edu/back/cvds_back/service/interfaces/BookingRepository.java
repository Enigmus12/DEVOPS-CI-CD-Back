package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.model.Booking;

import java.util.List;

public interface BookingRepository {
    void save(Booking booking);
    List<Booking> findAll();
    Booking findById(String bookingId) throws BookingServiceException;
    void deleteById(String bookingId) throws BookingServiceException;
    void update(Booking booking) throws BookingServiceException;
    boolean existsById(String bookingId);
}