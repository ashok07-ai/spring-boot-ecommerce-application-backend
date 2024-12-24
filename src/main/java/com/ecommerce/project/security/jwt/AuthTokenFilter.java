package com.ecommerce.project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter is responsible for intercepting every HTTP request and validating the JWT (JSON Web Token)
 * present in the request headers. It extends `OncePerRequestFilter`, ensuring it runs only once per request.
 * <p>
 * Responsibilities:
 * 1. Parse the JWT token from the request header.
 * 2. Validate the token using the `JwtUtils` utility class.
 * 3. Retrieve user details from the token and set the authentication context in Spring Security.
 * <p>
 * This filter is crucial for securing endpoints and authenticating users based on the provided token.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * Filters incoming requests to validate and process JWT tokens.
     *
     * @param request     the HttpServletRequest object representing the client's request
     * @param response    the HttpServletResponse object representing the response to the client
     * @param filterChain the FilterChain to pass the request/response to the next filter
     * @throws ServletException if any servlet-specific error occurs
     * @throws IOException      if any I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            // Parse the JWT token from the request header
            String jwt = parseJwt(request);

            // If the token exists and is valid, authenticate the user
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Extract the username from the token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Load user details from the UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication token with the user's details and roles
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                // Set additional details from the request
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication object in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log any errors that occur during authentication
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the HTTP request header.
     *
     * @param request the HttpServletRequest object containing the headers
     * @return the JWT token if present, or null otherwise
     */
    private String parseJwt(HttpServletRequest request) {
        // ---------------------Generate Token --------------------------
        // String jwt = jwtUtils.getJwtFromHeader(request);
        String jwt = jwtUtils.getJwtFromCookies(request);

        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}

