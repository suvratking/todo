package com.suvrat.todo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Suvrata Kumar Hada",
                        email = "suvrat777@gmail.com"
                ),
                description = "OpenApi documentation for Todo App",
                title = "OpenApi specification - tofo",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8082"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://192.168.1.44:8082"
                )
        }
)
public class OpenApiConfig {
}