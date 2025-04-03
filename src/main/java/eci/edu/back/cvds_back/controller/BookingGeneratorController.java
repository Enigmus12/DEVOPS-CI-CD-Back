package eci.edu.back.cvds_back.controller;

import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.interfaces.BookingGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * BookingGeneratorController is a REST controller that provides endpoints for managing
 * and generating booking data. It includes functionality to generate random bookings,
 * generate a specific number of bookings, and clear all bookings from the system.
 *
 * Endpoints:
 * - POST /generate-service/generate-bookings: Generates a specified number of random bookings.
 * - POST /generate-service/generate-exact-bookings: Generates an exact number of bookings.
 * - DELETE /generate-service/clear-all-bookings: Deletes all bookings from the system.
 *
 * This controller uses the BookingGeneratorService to perform the underlying operations.
 *
 * Annotations:
 * - @RestController: Indicates that this class is a REST controller.
 * - @RequestMapping("/generate-service"): Maps all endpoints to the base path "/generate-service".
 * - @CrossOrigin(origins = "*"): Allows cross-origin requests from any origin.
 *
 * Dependencies:
 * - BookingGeneratorService: A service used to handle booking generation and deletion logic.
 */
@RestController
@RequestMapping("/generate-service")
@CrossOrigin(origins = "*")
public class BookingGeneratorController {

    @Autowired
    private BookingGeneratorService bookingGeneratorService;

    /**
     * Endpoint to generate a specified number of random bookings.
     *
     * @param min The minimum number of bookings to generate. Defaults to 100 if not provided.
     * @param max The maximum number of bookings to generate. Defaults to 1000 if not provided.
     * @return A ResponseEntity containing a map with a success message and the total number of bookings generated.
     */
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

    /**
     * Endpoint to generate a specified number of exact bookings.
     *
     * @param count The number of bookings to generate. Defaults to 100 if not provided.
     * @return A ResponseEntity containing a map with a success message and the total number of bookings generated.
     */
    @PostMapping("/generate-exact-bookings")
    public ResponseEntity<Map<String, Object>> generateExactBookings(
            @RequestParam(defaultValue = "100") int count) {

        List<Booking> generatedBookings = bookingGeneratorService.generateExactBookings(count);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully generated " + generatedBookings.size() + " bookings");
        response.put("totalGenerated", generatedBookings.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes all bookings from the system.
     *
     * This endpoint is mapped to the DELETE HTTP method at the path "/clear-all-bookings".
     * It invokes the service to clear all bookings and returns a response containing
     * the number of bookings that were successfully removed.
     *
     * @return ResponseEntity containing a map with a success message and the total number
     *         of bookings removed.
     */
    @DeleteMapping("/clear-all-bookings")
    public ResponseEntity<Map<String, Object>> clearAllBookings() {
        int count = bookingGeneratorService.clearAllBookings();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully cleared " + count + " bookings");
        response.put("totalRemoved", count);

        return ResponseEntity.ok(response);
    }
}