package com.openclassrooms.mddapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.*"})
@ComponentScan(basePackages = {"com.*"})
@OpenAPIDefinition(
        info = @Info(
                title = "Student API",

                version = "1.0",
                description = "API documentation for managing students"
        ),security = @SecurityRequirement(name = "bearerAuth")
)
@SecuritySchemes({
        @SecurityScheme(name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT")
})
public class MddApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MddApiApplication.class, args);
	}

}
