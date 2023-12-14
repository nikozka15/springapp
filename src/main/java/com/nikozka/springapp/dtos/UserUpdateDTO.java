package com.nikozka.springapp.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Schema(description = "First name of the user", example = "John", required = true)
    @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe", required = true)
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private String lastName;

    public UserUpdateDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserUpdateDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

