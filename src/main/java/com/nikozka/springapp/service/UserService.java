package com.nikozka.springapp.service;

import com.nikozka.springapp.dtos.UserDTO;
import com.nikozka.springapp.dtos.UserUpdateDTO;
import com.nikozka.springapp.entity.UserEntity;
import com.nikozka.springapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
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
        if (userRepository.findByIin(user.getIin()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already exist");
        }
        UserEntity userEntity = convertToEntity(user);
        UserEntity savedUser = userRepository.save(userEntity);
        return convertToDTO(savedUser);

    }

    public void updateUser(String iin, UserUpdateDTO updatedUser) {
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

    private UserEntity convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }

    private UserDTO convertToDTO(UserEntity user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
