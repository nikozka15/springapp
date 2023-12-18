package com.nikozka.springapp.dtos;

import com.nikozka.springapp.validator.ValidIIN;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
    @Schema(description = "First name of the user", example = "John", required = true)
    @NotNull
    @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstName;
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    @NotNull
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastName;
    @Schema(description = "Individual Identification Number", example = "1234567890", required = true)
    @ValidIIN
    private String iin;

    public UserDTO(String firstName, String lastName, String iin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.iin = iin;
    }

    public UserDTO() {
    }

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

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }
}
