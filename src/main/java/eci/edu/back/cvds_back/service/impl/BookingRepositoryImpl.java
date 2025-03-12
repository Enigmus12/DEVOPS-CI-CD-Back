package eci.edu.back.cvds_back.service.impl;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingRepositoryImpl implements BookingRepository {
    @Autowired
    private BookingMongoRepository bookingMongoRepository;

    @Override
    public void save(Booking booking) {
        bookingMongoRepository.save(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingMongoRepository.findAll();
    }

    @Override
    public Booking findById(String bookingId) throws BookingServiceException{
        Optional<Booking> booking = bookingMongoRepository.findById(bookingId);
        if(booking.isEmpty()) throw new BookingServiceException("Booking Not found");
        return booking.get();
    }

    @Override
    public void deleteById(String bookingId) throws BookingServiceException {
        bookingMongoRepository.deleteById(bookingId);
    }

    @Override
    public void update(Booking booking) throws BookingServiceException {
        if (!bookingMongoRepository.existsById(booking.getBookingId())) {
            throw new BookingServiceException("Booking Not Found");
        }
        bookingMongoRepository.save(booking);
    }

    @Override
    public boolean existsById(String bookingId) {
        return bookingMongoRepository.existsById(bookingId);
    }

}