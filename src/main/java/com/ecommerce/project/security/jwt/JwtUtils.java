package com.ecommerce.project.security.jwt;

import com.ecommerce.project.security.jwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * Utility class for handling JSON Web Token (JWT) operations such as generation, validation,
 * parsing, and retrieval of user details from tokens. This class centralizes the logic related
 * to JWTs and integrates with the application security layer.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // JWT secret key used for signing and verifying tokens, injected from application properties
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    // JWT expiration time in milliseconds, injected from application properties
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Inject the JWT cookie name from application properties
    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;

    /**
     * Retrieves the JWT token from cookies in the incoming HTTP request.
     *
     * @param request The HTTPServletRequest containing cookies
     * @return The value of the JWT token if the cookie is found, otherwise null
     */
    public String getJwtFromCookies(HttpServletRequest request) {
        // Retrieve the cookie by name using WebUtils
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        return cookie != null ? cookie.getValue() : null; // Return cookie value if present
    }


    /**
     * Generates a JWT token and creates a response cookie for the user.
     *
     * @param userDetails The UserDetailsImpl object containing the user's details
     * @return A ResponseCookie containing the JWT token
     */
    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        // Generate the JWT token based on the username
        String jwt = generateTokenFromUsername(userDetails.getUsername());

        // Create a ResponseCookie with the token, setting it to HTTP only
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api/v1") // Set the cookie's path to the API endpoint
                .httpOnly(false) // Indicates whether the cookie is accessible only via HTTP
                .build();

        return cookie; // Return the generated cookie
    }
    // ---------------------Generate Token --------------------------
//    /**
//     * Retrieves the JWT token from the "Authorization" header in the HTTP request.
//     *
//     * @param request the HttpServletRequest object containing headers
//     * @return the JWT token if present and properly formatted, null otherwise
//     */
//    public String getJwtFromHeader(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        logger.debug("Authorization Header: {}", bearerToken);
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            // Remove the "Bearer " prefix and return the token
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    // ---------------------Generate Token --------------------------
//    /**
//     * Generates a JWT token for a given user, using the username as the subject.
//     *
//     * @param userDetails the UserDetails object containing user information
//     * @return a signed JWT token
//     */
//    public String generateTokenFromUsername(UserDetails userDetails) {
//        String username = userDetails.getUsername();
//        return Jwts.builder()
//                .subject(username) // Set the username as the token's subject
//                .issuedAt(new Date()) // Set the current time as the issue time
//                .expiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Set the expiration time
//                .signWith(key()) // Sign the token using the secret key
//                .compact();
//    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api/v1")
                .build();
        return cookie;
    }

    /**
     * Generates a JWT token for a given user, using the username as the subject.
     *
     * @param username the username
     * @return a signed JWT token
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username) // Set the username as the token's subject
                .issuedAt(new Date()) // Set the current time as the issue time
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Set the expiration time
                .signWith(key()) // Sign the token using the secret key
                .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token the JWT token
     * @return the username encoded in the token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key()) // Use the secret key to verify the token
                .build()
                .parseSignedClaims(token) // Parse the claims from the token
                .getPayload()
                .getSubject(); // Retrieve the subject (username)
    }

    /**
     * Creates and returns the key used for signing and verifying JWT tokens.
     *
     * @return a Key object generated from the secret key
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Validates a JWT token to ensure it is properly signed, not expired, and conforms to expectations.
     *
     * @param authToken the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            logger.debug("Validating JWT token");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true; // Token is valid
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false; // Token is invalid
    }
}

