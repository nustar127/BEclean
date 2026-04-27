package com.clean.demo.dto.user;

import java.util.List;

import com.clean.demo.entity.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends PersonResponse {
    private String username;
    private List<String> roles;

    public static UserResponse createUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(
                        user.getRoles().stream()
                                .map(Enum::name)
                                .toList())
                .build();
    }
}
