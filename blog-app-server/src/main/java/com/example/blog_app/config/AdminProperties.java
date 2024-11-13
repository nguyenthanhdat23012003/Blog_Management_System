package com.example.blog_app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AdminProperties {

    @Value("${admin.default.${environment}.name}")
    private String name;

    @Value("${admin.default.${environment}.email}")
    private String email;

    @Value("${admin.default.${environment}.password}")
    private String password;

    @Value("${admin.default.${environment}.about}")
    private String about;
}
