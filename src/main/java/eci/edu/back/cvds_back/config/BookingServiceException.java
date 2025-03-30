/**
 * Custom exception class for handling booking service-related errors.
 * This exception is thrown when an error occurs during booking service operations.
 */
package eci.edu.back.cvds_back.config;

public class BookingServiceException extends Exception {
    /**
         * Constructs a new BookingServiceException with the specified error message.
         *
         * @param message the detail message describing the specific booking service error
         */
    public BookingServiceException(String message){
        super(message);
    }
}
