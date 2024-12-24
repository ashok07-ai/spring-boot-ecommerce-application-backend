package com.ecommerce.project.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the AuthenticationEntryPoint interface and is used to handle unauthorized access attempts
 * in a Spring Security application. It is triggered whenever an unauthenticated user tries to access a secured resource.
 * <p>
 * The main responsibility of this class is to return a custom JSON response with relevant error details
 * when the application encounters an unauthorized access attempt.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Handles an unauthorized access attempt by providing a custom JSON error response.
     *
     * @param request       the HttpServletRequest object that triggered the exception
     * @param response      the HttpServletResponse object for returning the error response
     * @param authException the exception thrown when authentication fails
     * @throws IOException      if an input or output error occurs
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Log the unauthorized access attempt with the exception message
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Set the content type to JSON and the response status to 401 (Unauthorized)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Prepare a custom error response body
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED); // HTTP status code 401
        body.put("error", "Unauthorized");                      // Error type
        body.put("message", authException.getMessage());        // Detailed exception message
        body.put("path", request.getServletPath());             // Path of the request that caused the error

        // Serialize the error response to JSON and write it to the response output stream
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
