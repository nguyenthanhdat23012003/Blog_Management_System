package com.example.blog_app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration class for loading default administrator properties
 * based on the current environment.
 *
 * <p>The properties are loaded from application configuration files
 * (e.g., {@code application.properties} or {@code application.yml})
 * using the {@link org.springframework.beans.factory.annotation.Value} annotation.</p>
 *
 * <p>Example property keys in configuration:</p>
 * <pre>
 * admin.default.dev.name=Development Admin
 * admin.default.dev.email=admin.dev@example.com
 * admin.default.dev.password=dev-password
 * admin.default.dev.about=Administrator for development environment
 * </pre>
 *
 * <p>The {@code ${environment}} placeholder dynamically resolves to
 * the current environment (e.g., dev, test, prod).</p>
 */
@Component
@Getter
public class AdminProperties {

    /**
     * The default administrator's name.
     */
    @Value("${admin.default.${environment}.name}")
    private String name;

    /**
     * The default administrator's email address.
     */
    @Value("${admin.default.${environment}.email}")
    private String email;

    /**
     * The default administrator's password.
     */
    @Value("${admin.default.${environment}.password}")
    private String password;

    /**
     * Additional information about the default administrator.
     */
    @Value("${admin.default.${environment}.about}")
    private String about;
}
