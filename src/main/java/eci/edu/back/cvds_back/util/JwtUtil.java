package eci.edu.back.cvds_back.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // 5 horas en milisegundos

    /**
     * Extracts the user ID from the given JWT token.
     *
     * @param token the JWT token from which the user ID is to be extracted
     * @return the user ID extracted from the token
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token from which the expiration date is to be extracted
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the provided JWT token using the given claims resolver function.
     *
     * @param <T>            The type of the claim to be extracted.
     * @param token          The JWT token from which the claim is to be extracted.
     * @param claimsResolver A function that defines how to extract the desired claim from the token's claims.
     * @return The extracted claim of type T.
     * @throws io.jsonwebtoken.JwtException If the token is invalid or cannot be parsed.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token the JWT token from which claims are to be extracted
     * @return the claims contained in the token
     * @throws io.jsonwebtoken.security.SecurityException if the token signature is invalid
     * @throws io.jsonwebtoken.ExpiredJwtException if the token has expired
     * @throws io.jsonwebtoken.MalformedJwtException if the token is malformed
     * @throws io.jsonwebtoken.UnsupportedJwtException if the token is unsupported
     * @throws IllegalArgumentException if the token is null or empty
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token the JWT token to be checked
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the specified user ID.
     *
     * @param userId The unique identifier of the user for whom the token is being generated.
     * @return A JWT token as a String.
     */
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }

    /**
     * Creates a JSON Web Token (JWT) with the specified claims and subject.
     *
     * @param claims  A map containing the claims to be included in the token payload.
     * @param subject The subject of the token, typically identifying the user or entity.
     * @return A compact string representation of the JWT.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Validates the given JWT token by checking if the extracted user ID matches
     * the provided user ID and if the token is not expired.
     *
     * @param token the JWT token to be validated
     * @param userId the user ID to be compared with the extracted user ID from the token
     * @return true if the token is valid (user ID matches and token is not expired),
     *         false otherwise
     */
    public Boolean validateToken(String token, String userId) {
        final String extractedUserId = extractUserId(token);
        return (extractedUserId.equals(userId) && !isTokenExpired(token));
    }
}