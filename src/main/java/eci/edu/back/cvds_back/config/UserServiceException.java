/**
 * Custom exception class for handling user service-related errors.
 * Extends RuntimeException to provide a way to throw unchecked exceptions
 * specific to user service operations.
 */
package eci.edu.back.cvds_back.config;

public class UserServiceException extends RuntimeException {
  /**
   * Constructs a new UserServiceException with the specified error message.
   *
   * @param message the detail message describing the specific user service error
   */
  public UserServiceException(String message) {
    super(message);
  }
}
