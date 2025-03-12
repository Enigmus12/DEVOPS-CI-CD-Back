package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.model.Booking;

import java.util.List;

public interface BookingGeneratorService {

    List<Booking> generateRandomBookings(int min, int max);
    List<Booking> generateExactBookings(int count);
    int clearAllBookings();
}