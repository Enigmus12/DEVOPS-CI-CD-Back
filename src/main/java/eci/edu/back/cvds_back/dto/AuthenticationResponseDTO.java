package eci.edu.back.cvds_back.dto;

import eci.edu.back.cvds_back.model.User;

/**
 * Represents the response of an authentication process.
 * This DTO (Data Transfer Object) contains information about the authentication status,
 * the authenticated user, a generated token, and an optional message providing additional details.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li><b>authenticated</b>: A boolean indicating whether the authentication was successful.</li>
 *   <li><b>user</b>: The {@link User} object associated with the authentication response.</li>
 *   <li><b>token</b>: A string representing the authentication token generated for the user.</li>
 *   <li><b>message</b>: A string providing additional information about the authentication response.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *   <li><b>isAuthenticated</b>: Returns whether the authentication was successful.</li>
 *   <li><b>setAuthenticated</b>: Sets the authentication status.</li>
 *   <li><b>getUser</b>: Returns the user associated with the authentication response.</li>
 *   <li><b>setUser</b>: Sets the user associated with the authentication response.</li>
 *   <li><b>getToken</b>: Returns the authentication token.</li>
 *   <li><b>setToken</b>: Sets the authentication token.</li>
 *   <li><b>getMessage</b>: Returns the additional message about the authentication response.</li>
 *   <li><b>setMessage</b>: Sets the additional message about the authentication response.</li>
 * </ul>
 *
 * <p>Constructor:</p>
 * <ul>
 *   <li><b>AuthenticationResponseDTO(boolean authenticated, User user, String token, String message)</b>: 
 *       Initializes the DTO with the specified authentication status, user, token, and message.</li>
 * </ul>
 */
public class AuthenticationResponseDTO {
    private boolean authenticated;
    private User user;
    private String token;
    private String message;

    /**
     * Constructs an AuthenticationResponseDTO object with the specified parameters.
     *
     * @param authenticated Indicates whether the authentication was successful.
     * @param user The user associated with the authentication response.
     * @param token The authentication token generated for the user.
     * @param message A message providing additional information about the authentication response.
     */
    public AuthenticationResponseDTO(boolean authenticated, User user, String token, String message) {
        this.authenticated = authenticated;
        this.user = user;
        this.token = token;
        this.message = message;
    }

    public boolean isAuthenticated() {return authenticated;}
    public void setAuthenticated(boolean authenticated) {this.authenticated = authenticated;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
}