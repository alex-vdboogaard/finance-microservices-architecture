package com.finance.accountservice.mapper;

import java.util.List;

import com.finance.accountservice.dto.UserResponse;
import com.finance.accountservice.model.User;

public final class UserMapper {
    private UserMapper() {
    };

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGovermentId());
    }

    public static List<UserResponse> toListResponse(List<User> users) {
        return users.stream().map(UserMapper::toResponse).toList();
    }
}
