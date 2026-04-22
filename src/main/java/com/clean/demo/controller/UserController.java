package com.clean.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.UserResponse;
import com.clean.demo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        return ApiResponse.success(
                userRepository.findAll().stream()
                        .map(user -> UserResponse.createUser(user))
                        .toList(),
                "Found");
    }
}
