package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generate-service")
@CrossOrigin(origins = "*")
public class BookingGeneratorController {

    @Autowired
    private BookingGeneratorService bookingGeneratorService;

    @PostMapping("/generate-bookings")
    public ResponseEntity<Map<String, Object>> generateBookings(
            @RequestParam(defaultValue = "100") int min,
            @RequestParam(defaultValue = "1000") int max) {

        List<Booking> generatedBookings = bookingGeneratorService.generateRandomBookings(min, max);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully generated " + generatedBookings.size() + " bookings");
        response.put("totalGenerated", generatedBookings.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-exact-bookings")
    public ResponseEntity<Map<String, Object>> generateExactBookings(
            @RequestParam(defaultValue = "100") int count) {

        List<Booking> generatedBookings = bookingGeneratorService.generateExactBookings(count);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully generated " + generatedBookings.size() + " bookings");
        response.put("totalGenerated", generatedBookings.size());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear-all-bookings")
    public ResponseEntity<Map<String, Object>> clearAllBookings() {
        int count = bookingGeneratorService.clearAllBookings();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully cleared " + count + " bookings");
        response.put("totalRemoved", count);

        return ResponseEntity.ok(response);
    }
}