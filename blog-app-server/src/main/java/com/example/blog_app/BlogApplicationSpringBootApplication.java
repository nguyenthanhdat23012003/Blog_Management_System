package com.example.blog_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplicationSpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApplicationSpringBootApplication.class, args);
		System.out.println("Server is Running");
	}
}
