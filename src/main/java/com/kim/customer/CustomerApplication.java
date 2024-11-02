package com.kim.customer;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(

				title = "Customer microservice REST API Documentation",
				description = "Customer microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Rono Giddy",
						email = "kim@gmail.com",
						url = "https://www.kimGid.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.kimGid.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Customer microservice REST API Documentation",
				url = "https://www.KimGid.com/swagger-ui.html"
		)
)
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

}
