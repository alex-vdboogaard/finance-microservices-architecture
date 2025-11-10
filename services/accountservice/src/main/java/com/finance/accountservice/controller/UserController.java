package com.finance.accountservice.controller;

import java.net.URI;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.accountservice.dto.CreateUserRequest;
import com.finance.accountservice.dto.UserResponse;
import com.finance.accountservice.exception.UserNotFoundException;
import com.finance.accountservice.service.UserService;
import com.finance.common.dto.ApiResponse;
import com.finance.common.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        log.info("Received request to fetch all users");
        List<UserResponse> users = userService.getUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .data(users)
                .meta(ApiResponse.Meta.builder()
                        .message("Successfully fetched all users")
                        .build())
                .build();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request)
            throws CloneNotSupportedException {
        UserResponse user = userService.createUser(request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .data(user)
                .meta(ApiResponse.Meta.builder()
                        .message("Successfully created user")
                        .build())
                .build();
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> findUser(@Valid Long userId) throws UserNotFoundException {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .data(userService.getUser(userId))
                .meta(ApiResponse.Meta.builder()
                        .message("Successfully retrieved user")
                        .build())
                .build();

        return ResponseEntity.status(HttpStatus.SC_SUCCESS).body(response);
    }

}
