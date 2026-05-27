package com.clean.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.user.UserResponse;
import com.clean.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        return ApiResponse.success(
                userService.findAll().stream()
                        .map(user -> UserResponse.createUser(user))
                        .toList(),
                "Found");
    }

    @PostMapping("/{id}/grant-admin")
    public ApiResponse<UserResponse> grantAdmin(@PathVariable Long id) {
        return ApiResponse.success(
                UserResponse.createUser(userService.grantAdminRole(id)),
                "Admin role granted");
    }
}
