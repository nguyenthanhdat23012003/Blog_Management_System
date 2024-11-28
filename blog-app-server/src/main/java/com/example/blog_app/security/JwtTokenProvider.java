package com.example.blog_app.security;

import com.example.blog_app.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for managing JWT tokens.
 *
 * <p>This class provides methods for generating, validating, and extracting information
 * from JSON Web Tokens (JWTs). It also facilitates user authentication based on the JWT token.</p>
 */
@Component
public class JwtTokenProvider {

    /**
     * The secret key used for signing the JWT.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * The expiration time for the JWT in milliseconds.
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final JwtUserDetailsService jwtUserDetailsService;

    /**
     * Constructs a new instance of {@link JwtTokenProvider} with the required dependencies.
     *
     * @param jwtUserDetailsService the service for loading user details
     */
    public JwtTokenProvider(JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    /**
     * Generates a JWT token for the given user email.
     *
     * <p>The generated token includes the user's email as the subject, an issued date,
     * an expiration date, and is signed using a secret key.</p>
     *
     * @param email the email of the user for whom the token is being generated
     * @return the generated JWT token as a {@link String}
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(user.getEmail()) // Sets the email as the subject of the token
                .claim("id", user.getId())
                .setIssuedAt(now) // Sets the issue time
                .setExpiration(expiryDate) // Sets the expiration time
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Signs the token with the secret key
                .compact();
    }

    /**
     * Extracts the email (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the email contained in the token
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // The subject represents the email
    }

    /**
     * Validates the provided JWT token.
     *
     * <p>Checks whether the token is well-formed and has not expired.</p>
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Token is invalid or expired
            return false;
        }
    }

    /**
     * Retrieves an {@link Authentication} object based on the JWT token.
     *
     * <p>The {@link Authentication} object contains user details and authorities
     * extracted from the token.</p>
     *
     * @param token the JWT token
     * @return the {@link Authentication} object for the token
     */
    public Authentication getAuthentication(String token) {
        // Extract the email (username) from the token
        String email = getEmailFromToken(token);

        // Load user details using JwtUserDetailsService
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);

        // Create and return an Authentication object with authorities
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
