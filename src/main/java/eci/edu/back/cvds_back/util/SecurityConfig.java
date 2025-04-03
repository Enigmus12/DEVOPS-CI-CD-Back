package eci.edu.back.cvds_back.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired JwtRequestFilter jwtRequestFilter;

/**
 * Configures the security filter chain for the application.
 *
 * <p>This method sets up the security rules and filters for handling HTTP requests.
 * It disables CSRF protection, defines authorization rules for specific endpoints,
 * configures session management to be stateless, and adds a custom JWT filter
 * before the UsernamePasswordAuthenticationFilter.</p>
 *
 * @param http the {@link HttpSecurity} object used to configure security settings
 * @return the configured {@link SecurityFilterChain} instance
 * @throws Exception if an error occurs during the configuration
 *
 * <ul>
 *     <li>CSRF protection is disabled to allow stateless authentication.</li>
 *     <li>Public access is granted to the following endpoints:
 *         <ul>
 *             <li><code>/user-service/login</code></li>
 *             <li><code>/user-service/register</code></li>
 *             <li>All endpoints under <code>/generate-service/**</code></li>
 *             <li>All endpoints under <code>/booking-service/**</code></li>
 *         </ul>
 *     </li>
 *     <li>All other requests require authentication.</li>
 *     <li>Session management is configured to be stateless to support token-based authentication.</li>
 *     <li>A custom JWT filter is added to process JWT tokens before the standard authentication filter.</li>
 * </ul>
 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user-service/login", "/user-service/register").permitAll()
                        .requestMatchers("/generate-service/**").permitAll()
                        .requestMatchers("/booking-service/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}