package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot demo application for Levitas Validators.
 *
 * <p>Exposes REST endpoints that demonstrate GSTIN, PAN, IFSC, UPI VPA, and Aadhaar
 * validation using the levitas-validators library.
 *
 * <p>Run with: {@code mvn spring-boot:run} (from this directory, after installing the parent)
 * <p>Then: {@code curl http://localhost:8080/validate/gstin/29AAAAA0000A1ZX}
 */
@SpringBootApplication
public class DemoApplication {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
