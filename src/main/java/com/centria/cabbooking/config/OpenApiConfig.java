package com.centria.cabbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger Configuration
 * Fully compatible with Spring Boot 3.5.10
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cabBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cab Booking API")
                        .description("RESTful API for Online Cab Booking Application")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Centria Dev Team")
                                .email("dev@centria.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}