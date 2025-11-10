package com.finance.accountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.accountservice.dto.CreateUserRequest;
import com.finance.accountservice.dto.UserAndAccounts;
import com.finance.accountservice.dto.UserResponse;
import com.finance.accountservice.service.UserService;
import com.finance.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
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
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findUser(@PathVariable("id") Long userId) {
        UserResponse user = userService.getUser(userId);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .data(user)
                .meta(ApiResponse.Meta.builder()
                        .message("Successfully retrieved user")
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<ApiResponse<UserAndAccounts>> findUserWithAccounts(@PathVariable("id") Long userId) {
        UserAndAccounts userWithAccounts = userService.getUserWithAccounts(userId);
        ApiResponse<UserAndAccounts> response = ApiResponse.<UserAndAccounts>builder()
                .data(userWithAccounts)
                .meta(ApiResponse.Meta.builder()
                        .message("Successfully retrieved user with accounts")
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }
}
