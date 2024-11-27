package com.example.blog_app.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for retrieving information about the current authenticated user.
 *
 * <p>This class provides methods to access the user's email or other details
 * from the {@link SecurityContextHolder}.</p>
 */
public class SecurityUtils {

    /**
     * Retrieves the email of the currently authenticated user from the security context.
     *
     * <p>If the user is not authenticated or the authentication object does not contain
     * user details, this method returns {@code null}.</p>
     *
     * @return the email of the current user, or {@code null} if the user is not authenticated
     */
    public static String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Email of the user
        }
        return null; // Guest or unauthenticated
    }
}
