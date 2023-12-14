package com.nikozka.springapp.service;

import com.nikozka.springapp.dtos.UserDTO;
import com.nikozka.springapp.dtos.UserUpdateDTO;
import com.nikozka.springapp.entity.UserEntity;
import com.nikozka.springapp.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, Validator validator, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> getAllUsers(Pageable pageable) {
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        return userPage.map(this::convertToDTO).getContent();
    }

    public UserDTO getUserByInn(String iin) {
        UserEntity userEntity = userRepository.findByIin(iin);
        if (userEntity != null) {
            return convertToDTO(userEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for provided IIN");
        }
    }

    public UserDTO createUser(UserDTO user) {
        if (isInvalidUser(user)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid user");
        }
        if (userRepository.findByIin(user.getIin()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already exist");
        }
        UserEntity userEntity = convertToEntity(user);
        UserEntity savedUser = userRepository.save(userEntity);
        return convertToDTO(savedUser);

    }

    public void updateUser(String iin, UserUpdateDTO updatedUser) {
        if (isInvalidUser(updatedUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid user");
        }
        UserEntity existingUser = userRepository.findByIin(iin);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(String iin) {
        UserEntity userEntity = userRepository.findByIin(iin);
        if (userEntity != null) {
            userRepository.deleteByIin(iin);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private <T> boolean isInvalidUser(T user) {
        Set<ConstraintViolation<T>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            logViolations(violations);
            return true;
        }
        return false;
    }

    private <T> void logViolations(Set<ConstraintViolation<T>> violations) {
        String violationMessages = violations.stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining(", "));

        logger.warn("Validation Errors: {} ", violationMessages);
    }

    private UserEntity convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }

    private UserDTO convertToDTO(UserEntity user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
