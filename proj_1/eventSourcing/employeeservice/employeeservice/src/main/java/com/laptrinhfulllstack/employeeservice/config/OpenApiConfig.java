package com.laptrinhfulllstack.employeeservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Employee Api Documentation - LT Fullstack", description = "Api documentation for Employee Service", version = "1.0", contact = @Contact(name = "Phong pro", email = "phongluong3366@gmail.com", url = "https://laptringft.vercel.app"), license = @License(name = "MIT Lincense", url = "https://laptringft.vercel.app/license"), termsOfService = "https://laptringft.vercel.app/terms"), servers = {
        @Server(description = "Local ENV", url = "http://localhost:9002"),
        @Server(description = "Dev ENV", url = "https://employee-service.dev.com"),
        @Server(description = "Prod ENV", url = "https://employee-service.prod.com"),
})
public class OpenApiConfig {

}
