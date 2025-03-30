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
 * Controller for managing booking generation operations.
 *
 * This controller provides endpoints to generate random bookings, generate a specific
 * number of bookings, and clear all bookings from the system. It interacts with the
 * BookingGeneratorService to perform these operations.
 *
 * Endpoints:
 * - POST /generate-service/generate-bookings: Generates a random number of bookings
 *   within a specified range.
 * - POST /generate-service/generate-exact-bookings: Generates an exact number of bookings.
 * - DELETE /generate-service/clear-all-bookings: Deletes all bookings from the system.
 *
 * Annotations:
 * - @RestController: Indicates that this class is a RESTful controller.
 * - @RequestMapping("/generate-service"): Maps all endpoints in this controller to the
 *   "/generate-service" base URL.
 * - @CrossOrigin(origins = "*"): Allows cross-origin requests from any origin.
 *
 * Dependencies:
 * - BookingGeneratorService: Service used to handle booking generation and deletion logic.
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
     * @param count the number of bookings to generate, defaults to 100 if not provided
     * @return a ResponseEntity containing a map with a success message and the total number of bookings generated
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
     * This endpoint handles HTTP DELETE requests to the "/clear-all-bookings" URL.
     * It invokes the bookingGeneratorService to remove all existing bookings and
     * returns a response containing the total number of bookings removed.
     *
     * @return ResponseEntity containing a map with a success message and the total
     *         number of bookings removed.
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