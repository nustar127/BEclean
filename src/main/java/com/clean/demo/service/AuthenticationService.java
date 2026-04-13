package com.clean.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clean.demo.dto.JwtAuthenticationResponse;
import com.clean.demo.dto.SignInRequest;
import com.clean.demo.dto.SignUpRequest;
import com.clean.demo.dto.UserResponse;
import com.clean.demo.entity.Role;
import com.clean.demo.entity.User;

import lombok.RequiredArgsConstructor;
import com.clean.demo.dto.ApiResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserService userService;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        public ApiResponse<JwtAuthenticationResponse> signUp(SignUpRequest request) {

                var user = User.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.ROLE_USER)
                                .build();

                UserResponse userDto = UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .build();

                userService.create(user);

                var jwt = jwtService.generateToken(user);
                return ApiResponse.success(new JwtAuthenticationResponse(jwt, userDto), "User signed up");
        }

        public ApiResponse<JwtAuthenticationResponse> signIn(SignInRequest request) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()));

                var userDetails = userService
                                .userDetailsService()
                                .loadUserByUsername(request.getUsername());

                var user = (User) userDetails;

                UserResponse userDto = UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .build();

                var jwt = jwtService.generateToken(userDetails);
                return ApiResponse.success(new JwtAuthenticationResponse(jwt, userDto), "User signed in");
        }
}