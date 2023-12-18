package com.nikozka.springapp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiDocConfig(@Value("${app.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Application")
                        .description("The Users API provides endpoints to manage user-related operations. Retrieve a list of all users, get details about a specific user by their Individual Identification Number (IIN), create new users, update existing user information, and delete users by their IIN.")
                        .version(appVersion)
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact()
                                .name("Veronika Shyrshova")
                                .email("vshyrshova@gmail.com")));
    }
}
