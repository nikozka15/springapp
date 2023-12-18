package com.nikozka.springapp.service;

import com.nikozka.springapp.dtos.UserDTO;
import com.nikozka.springapp.dtos.UserUpdateDTO;
import com.nikozka.springapp.entity.UserEntity;
import com.nikozka.springapp.repository.UserRepository;
//import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
//    @Mock
//    private Validator validator;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserService userService;
  //  private final String invalidIin = "1234567890";
    private final String validIin = "3483310183";
    private final UserDTO userDTO = new UserDTO("John", "Doe", validIin);
   // private final UserDTO invalidUserDTO = new UserDTO("I", "User", invalidIin);
    private final UserUpdateDTO userUpdateDTO = new UserUpdateDTO("John", "Doe");
   // private final UserUpdateDTO invalidUserUpdateDTO = new UserUpdateDTO("Invalid", "User");

    @Test
    void getUserByInnTestUserFound() {
        UserEntity userEntity = getEntity();

        when(userRepository.findByIin(anyString())).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUserByInn(validIin);

        Assertions.assertAll("UserDTO properties",
                () -> Assertions.assertNotNull(result, "Result should not be null"),
                () -> Assertions.assertEquals(userEntity.getFirstName(), result.getFirstName(), "First name should match"),
                () -> Assertions.assertEquals(userEntity.getLastName(), result.getLastName(), "Last name should match"),
                () -> Assertions.assertEquals(userEntity.getIin(), result.getIin(), "IIN should match")
        );
    }

    @Test
    void getUserByInnTestUserNotFound() {
        when(userRepository.findByIin(validIin)).thenReturn(null);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.getUserByInn(validIin));

        Assertions.assertAll("ResponseStatusException properties",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus(), "Status should be NOT_FOUND"),
                () -> Assertions.assertEquals("User not found for provided IIN", exception.getReason(), "Reason should be 'User not found'")
        );
    }

    @Test
    void createUserTestValidUserNotExists() {
        UserEntity userEntity = getEntity();

       // when(validator.validate(userDTO)).thenReturn(Set.of());
        when(userRepository.findByIin(validIin)).thenReturn(null);
        when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        Assertions.assertAll("UserDTO properties",
                () -> Assertions.assertNotNull(result, "Result should not be null"),
                () -> Assertions.assertEquals(userDTO.getFirstName(), result.getFirstName(), "First name should match"),
                () -> Assertions.assertEquals(userDTO.getLastName(), result.getLastName(), "Last name should match"),
                () -> Assertions.assertEquals(userDTO.getIin(), result.getIin(), "IIN should match")
        );
    }

//    @Test
//    void createUserTestInvalidUser() { // only when using programmatic validation
//
//      //  when(validator.validate(invalidUserDTO)).thenReturn(Set.of(createConstraintViolation(invalidUserDTO)));
//
//        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
//                () -> userService.createUser(invalidUserDTO));
//
//        Assertions.assertAll("ResponseStatusException properties",
//                () -> Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus(), "Status should be UNPROCESSABLE_ENTITY"),
//                () -> Assertions.assertEquals("Invalid user", exception.getReason(), "Reason should be 'Invalid user'")
//        );
//    }

    @Test
    void createUserTestUserIsAlreadyExist() {
        UserEntity userEntity = getEntity();

      //  when(validator.validate(userDTO)).thenReturn(Set.of());
        when(userRepository.findByIin(validIin)).thenReturn(userEntity);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.createUser(userDTO));

        Assertions.assertAll("ResponseStatusException properties",
                () -> Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus(), "Status should be CONFLICT"),
                () -> Assertions.assertEquals("User is already exist", exception.getReason(), "Reason should be 'User is already exist'")
        );
    }

//    @Test
//    void updateUserTestInvalidUser() { // only when using programmatic validation
//
//      //  when(validator.validate(invalidUserUpdateDTO)).thenReturn(Set.of(createConstraintViolationUserUpdateDto(invalidUserUpdateDTO)));
//
//        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
//                () -> userService.updateUser(validIin, invalidUserUpdateDTO));
//
//        Assertions.assertAll("ResponseStatusException properties",
//                () -> Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus(), "Status should be UNPROCESSABLE_ENTITY"),
//                () -> Assertions.assertEquals("Invalid user", exception.getReason(), "Reason should be 'Invalid user'")
//        );
//    }
    @Test
    void updateUserTestValidUserNotExists() {

       // when(validator.validate(userUpdateDTO)).thenReturn(Set.of());
        when(userRepository.findByIin(validIin)).thenReturn(null);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(validIin, userUpdateDTO));

        Assertions.assertAll("ResponseStatusException properties",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus(), "Status should be NOT_FOUND"),
                () -> Assertions.assertEquals("User not found", exception.getReason(), "Reason should be 'User not found'")
        );
    }
    @Test
    void updateUserTestValidUserFound() {
        UserEntity userEntity = getEntity();

       // when(validator.validate(userUpdateDTO)).thenReturn(Set.of());
        when(userRepository.findByIin(validIin)).thenReturn(userEntity);

        userService.updateUser(validIin, userUpdateDTO);

        verify(userRepository).save(argThat(entity ->
                entity.getFirstName().equals(userUpdateDTO.getFirstName()) &&
                        entity.getLastName().equals(userUpdateDTO.getLastName())
        ));
    }
    @Test
    void deleteUserTestUserFoundAndSuccessfullyDeleted() {
        UserEntity existingUserEntity = getEntity();

        when(userRepository.findByIin(validIin)).thenReturn(existingUserEntity);

        userService.deleteUser(validIin);

        verify(userRepository).deleteByIin(validIin);
    }
    @Test
    void deleteUserTestUserNotFound() {
        when(userRepository.findByIin(validIin)).thenReturn(null);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> userService.deleteUser(validIin));

        Assertions.assertAll("ResponseStatusException properties",
                () -> Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus(), "Status should be NOT_FOUND"),
                () -> Assertions.assertEquals("User not found", exception.getReason(), "Reason should be 'User not found'")
        );
    }

    private ConstraintViolationImpl<UserDTO> createConstraintViolation(UserDTO userDTO) {
        return (ConstraintViolationImpl<UserDTO>) ConstraintViolationImpl.forBeanValidation(null,
                null, null, "Error", UserDTO.class, userDTO,
                null, null, null, null, null);
    }
    private ConstraintViolationImpl<UserUpdateDTO> createConstraintViolationUserUpdateDto(UserUpdateDTO userUpdateDTO) {
        return (ConstraintViolationImpl<UserUpdateDTO>) ConstraintViolationImpl.forBeanValidation(null,
                null, null, "Error", UserUpdateDTO.class, userUpdateDTO,
                null, null, null, null, null);
    }

    private UserEntity getEntity() {
        UserEntity userEntity = new UserEntity("John", "Doe", validIin);
        userEntity.setId(1L);
        return userEntity;
    }
}
