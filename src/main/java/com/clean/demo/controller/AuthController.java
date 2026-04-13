package com.clean.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.JwtAuthenticationResponse;
import com.clean.demo.dto.SignInRequest;
import com.clean.demo.dto.SignUpRequest;
import com.clean.demo.dto.SignUpCustomerRequest;
import com.clean.demo.dto.SignUpCleanerRequest;
import com.clean.demo.service.AuthenticationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ApiResponse<JwtAuthenticationResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-up/customer")
    public ApiResponse<JwtAuthenticationResponse> signUpCustomer(@RequestBody @Valid SignUpCustomerRequest request) {
        return authenticationService.signUpCustomer(request);
    }

    @PostMapping("/sign-up/cleaner")
    public ApiResponse<JwtAuthenticationResponse> signUpCleaner(@RequestBody @Valid SignUpCleanerRequest request) {
        return authenticationService.signUpCleaner(request);
    }

    @PostMapping("/sign-in")
    public ApiResponse<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}