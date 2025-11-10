package com.finance.accountservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finance.accountservice.dto.CreateUserRequest;
import com.finance.accountservice.dto.UserResponse;
import com.finance.accountservice.exception.UserNotFoundException;
import com.finance.accountservice.mapper.UserMapper;
import com.finance.accountservice.model.User;
import com.finance.accountservice.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    UserMapper mapper;

    public UserResponse createUser(CreateUserRequest request) throws CloneNotSupportedException {
        if (userRepository.findByEmail(request.email()) != null) {
            throw new CloneNotSupportedException("User with this email already exists");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .govermentId(request.governmentId())
                .password(passwordEncoder.encode(request.password())) // hash the password
                .build();

        return UserMapper.toResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        return UserMapper.toListResponse(userRepository.findAll());
    }

    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}
