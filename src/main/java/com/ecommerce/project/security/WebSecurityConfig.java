package com.ecommerce.project.security;

import com.ecommerce.project.enums.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.jwt.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.project.security.jwt.AuthEntryPointJwt;
import com.ecommerce.project.security.jwt.AuthTokenFilter;

import java.util.Set;

/**
 * Configuration class for Spring Security setup in the application.
 * It configures various security-related aspects including authentication, authorization, and JWT token handling.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Injected dependencies for user authentication and JWT handling
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Bean definition for the AuthTokenFilter, which is responsible for validating JWT tokens in each request.
     *
     * @return the AuthTokenFilter instance
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Bean definition for DaoAuthenticationProvider, which provides user authentication
     * using an instance of UserDetailsService and a password encoder.
     *
     * @return the DaoAuthenticationProvider instance
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean definition for AuthenticationManager, which manages the authentication process.
     *
     * @param authConfig the AuthenticationConfiguration instance
     * @return the AuthenticationManager instance
     * @throws Exception if an error occurs during authentication manager creation
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Bean definition for PasswordEncoder, which is responsible for encoding and verifying passwords.
     * The BCryptPasswordEncoder is used to securely hash passwords.
     *
     * @return the PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean definition for SecurityFilterChain, which configures HTTP security,
     * including endpoints that should be publicly accessible and those that require authentication.
     *
     * @param http the HttpSecurity instance
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during security filter chain setup
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF and configure session management to be stateless (JWT-based)
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**").permitAll()  // Permit authentication routes
                                .requestMatchers("/v3/api-docs/**").permitAll()  // Permit Swagger API docs
//                                .requestMatchers("/api/v1/admin/**").permitAll()  // Permit admin routes
//                                .requestMatchers("/api/v1/public/**").permitAll()  // Permit public routes
                                .requestMatchers("/h2-console/**").permitAll() // Permit h2 console routes
                                .requestMatchers("/swagger-ui/**").permitAll()  // Permit Swagger UI
                                .requestMatchers("/api/v1/test/**").permitAll()  // Permit test API
                                .requestMatchers("/images/**").permitAll()  // Permit image access
                                .anyRequest().authenticated()  // All other requests need authentication
                );

        // Use the configured authentication provider
        http.authenticationProvider(authenticationProvider());

        // Add JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // Configure HTTP headers to prevent clickjacking attacks by allowing content to be displayed in a frame only if the frame is from the same origin (domain).
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));

        return http.build();
    }

    /**
     * Bean definition for WebSecurityCustomizer, which configures static resources to be ignored by Spring Security.
     * This includes Swagger UI, API docs, and other static resources.
     *
     * @return the WebSecurityCustomizer instance
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles

            // Check if ROLE_USER exists, if not, create and save it
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            // Check if ROLE_SELLER exists, if not, create and save it
            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            // Check if ROLE_ADMIN exists, if not, create and save it
            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            // Define sets of roles for different types of users
            Set<Role> userRoles = Set.of(userRole);  // User has only ROLE_USER
            Set<Role> sellerRoles = Set.of(sellerRole);  // Seller has only ROLE_SELLER
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);  // Admin has ROLE_USER, ROLE_SELLER, and ROLE_ADMIN

            // Create users if they don't already exist

            // Check if user1 exists, if not, create and save it with a password
            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            // Check if seller1 exists, if not, create and save it with a password
            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            // Check if admin exists, if not, create and save it with a password
            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users

            // If user1 exists, update their roles
            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);  // Assign userRoles to user1
                userRepository.save(user);  // Save the updated user
            });

            // If seller1 exists, update their roles
            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);  // Assign sellerRoles to seller1
                userRepository.save(seller);  // Save the updated seller
            });

            // If admin exists, update their roles
            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);  // Assign adminRoles to admin
                userRepository.save(admin);  // Save the updated admin
            });
        };
    }

}
