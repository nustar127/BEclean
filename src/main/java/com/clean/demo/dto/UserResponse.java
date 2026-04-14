package com.clean.demo.dto;

import java.util.List;

import com.clean.demo.entity.User;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private List<String> roles;
    private String phone;

    public static UserResponse createUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(
                        user.getRoles().stream()
                                .map(Enum::name)
                                .toList())
                .build();
    }
}
