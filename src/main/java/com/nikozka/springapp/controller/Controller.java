package com.nikozka.springapp.controller;

import com.nikozka.springapp.dtos.UserDTO;
import com.nikozka.springapp.dtos.UserUpdateDTO;
import com.nikozka.springapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users API")
public class Controller {
    private final UserService userService;

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))})
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@ParameterObject Pageable pageable) {
        List<UserDTO> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get user by Iin", description = "Retrieves a user by their Iin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @GetMapping("/{iin}")
    public ResponseEntity<UserDTO> getUserByIin(@PathVariable String iin) {
        UserDTO user = userService.getUserByInn(iin);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @Operation(summary = "Create a new user", description = "Creates a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class))),
            @ApiResponse(responseCode = "409", description = "User is already exist",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO user) {
        UserDTO createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user by Iin", description = "Updates an existing user by Iin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @PutMapping("{iin}")
    public ResponseEntity<Void> updateUser(@PathVariable String iin, @RequestBody @Valid UserUpdateDTO updatedUser) {
        userService.updateUser(iin, updatedUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete user by Iin", description = "Deletes an existing user by Iin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @DeleteMapping("/{iin}")
    public ResponseEntity<Void> deleteUser(@PathVariable String iin) {
        userService.deleteUser(iin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
