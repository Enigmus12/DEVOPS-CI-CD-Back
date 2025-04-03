package eci.edu.back.cvds_back.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JwtRequestFilter is a custom filter that intercepts incoming HTTP requests to validate
 * and process JSON Web Tokens (JWT) for authentication purposes. It extends the 
 * OncePerRequestFilter class, ensuring that the filter is executed once per request.
 *
 * <p>This filter performs the following tasks:
 * <ul>
 *   <li>Extracts the JWT token from the "Authorization" header of the HTTP request.</li>
 *   <li>Validates the extracted token using the JwtUtil utility class.</li>
 *   <li>If the token is valid, retrieves the user ID and sets the authentication in the 
 *       SecurityContext with appropriate authorities.</li>
 *   <li>If the token is invalid or missing, the request proceeds without authentication.</li>
 * </ul>
 *
 * <p>Key Components:
 * <ul>
 *   <li><b>JwtUtil:</b> A utility class used to extract and validate the JWT token.</li>
 *   <li><b>SecurityContextHolder:</b> Used to store the authentication details for the current request.</li>
 *   <li><b>UsernamePasswordAuthenticationToken:</b> Represents the authentication token for the user.</li>
 * </ul>
 *
 * <p>Usage:
 * <ul>
 *   <li>This filter is typically used in Spring Security configurations to secure endpoints
 *       by validating JWT tokens.</li>
 *   <li>It ensures that only requests with valid tokens are authenticated and authorized.</li>
 * </ul>
 *
 * <p>Note:
 * <ul>
 *   <li>The filter does not handle token generation or user login; it only validates tokens
 *       for incoming requests.</li>
 *   <li>Ensure that the JwtUtil class is properly implemented to handle token extraction
 *       and validation.</li>
 * </ul>
 *
 * @see OncePerRequestFilter
 * @see JwtUtil
 * @see SecurityContextHolder
 * @see UsernamePasswordAuthenticationToken
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Filters incoming HTTP requests to validate and process JWT tokens for authentication.
     *
     * @param request  the HTTP request to be processed
     * @param response the HTTP response to be sent
     * @param chain    the filter chain to pass the request and response to the next filter
     * @throws ServletException if an error occurs during the filtering process
     * @throws IOException      if an I/O error occurs during the filtering process
     *
     * This method extracts the JWT token from the "Authorization" header of the request,
     * validates it, and sets the authentication in the SecurityContext if the token is valid.
     * If the token is invalid or missing, the request proceeds without authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String userId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                userId = jwtUtil.extractUserId(jwt);
            } catch (Exception e) {
                logger.error("Error validating JWT token", e);
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, userId)) {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("USER"));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}

