package eci.edu.back.cvds_back.util;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;






class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String testUserId;
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        testUserId = "testUser123";
        validToken = jwtUtil.generateToken(testUserId);
    }

    @Test
    void testGenerateToken() {
        assertNotNull(validToken, "Generated token should not be null");
    }

    @Test
    void testExtractUserId() {
        String extractedUserId = jwtUtil.extractUserId(validToken);
        assertEquals(testUserId, extractedUserId, "Extracted user ID should match the original user ID");
    }

    @Test
    void testExtractExpiration() {
        Date expirationDate = jwtUtil.extractExpiration(validToken);
        assertNotNull(expirationDate, "Expiration date should not be null");
        assertTrue(expirationDate.after(new Date()), "Expiration date should be in the future");
    }

    @Test
    void testValidateToken_ValidToken() {
        assertTrue(jwtUtil.validateToken(validToken, testUserId), "Token should be valid for the correct user ID");
    }

    @Test
    void testValidateToken_InvalidUserId() {
        assertFalse(jwtUtil.validateToken(validToken, "wrongUserId"), "Token should be invalid for a wrong user ID");
    }


    @Test
    void testExtractClaim_InvalidToken() {
        String invalidToken = validToken + "tampered";
        assertThrows(SignatureException.class, () -> jwtUtil.extractUserId(invalidToken), "Tampered token should throw SignatureException");
    }
    @Test
    void testValidateToken_ExpiredToken() {
        // Crear un token expirado manualmente
        JwtUtil jwtUtilExpired = new JwtUtil() {
            @Override
            public Date extractExpiration(String token) {
                return new Date(System.currentTimeMillis() - 1000); // Expirado en el pasado
            }
        };
        String expiredToken = jwtUtilExpired.generateToken(testUserId);

        assertFalse(jwtUtilExpired.validateToken(expiredToken, testUserId), "Expired token should be invalid");
    }
    
}