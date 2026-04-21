package com.clean.demo.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clean.demo.dto.JwtAuthenticationResponse;
import com.clean.demo.dto.SignInRequest;
import com.clean.demo.dto.SignUpCleanerRequest;
import com.clean.demo.dto.SignUpCustomerRequest;
import com.clean.demo.dto.UserResponse;
import com.clean.demo.entity.Customer;
import com.clean.demo.entity.Role;
import com.clean.demo.entity.Cleaner;
import com.clean.demo.entity.User;

import lombok.RequiredArgsConstructor;
import com.clean.demo.dto.ApiResponse;
import com.clean.demo.dto.CleanerResponse;
import com.clean.demo.dto.CustomerResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserService userService;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        // public ApiResponse<JwtAuthenticationResponse> signUp(SignUpRequest request) {

        //         var user = User.builder()
        //                         .username(request.getUsername())
        //                         .email(request.getEmail())
        //                         .firstName(request.getFirstName())
        //                         .lastName(request.getLastName())
        //                         .password(passwordEncoder.encode(request.getPassword()))
        //                         .role(Role.ROLE_ADMIN)
        //                         .build();

        //         userService.create(user);
        //         UserResponse userDto = UserResponse.createUser(user);

        //         var jwt = jwtService.generateToken(user);
        //         return ApiResponse.success(new JwtAuthenticationResponse(jwt, userDto), "User signed up");
        // }

        public ApiResponse<JwtAuthenticationResponse> signUpCustomer(SignUpCustomerRequest request) {

                var user = Customer.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .phone(request.getPhone())
                                .address(request.getAddress())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(List.of(Role.ROLE_USER))
                                .build();

                userService.create(user);
                CustomerResponse customerDto = CustomerResponse.createCustomer(user);

                var jwt = jwtService.generateToken(user);
                return ApiResponse.success(new JwtAuthenticationResponse(jwt, customerDto), "Customer signed up");
        }

        public ApiResponse<JwtAuthenticationResponse> signUpCleaner(SignUpCleanerRequest request) {

                var user = Cleaner.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .phone(request.getPhone())
                                .rating(request.getRating())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(List.of(Role.ROLE_USER))
                                .build();

                userService.create(user);
                CleanerResponse customerDto = CleanerResponse.createCustomer(user);

                var jwt = jwtService.generateToken(user);
                return ApiResponse.success(new JwtAuthenticationResponse(jwt, customerDto), "Cleaner signed up");
        }

        public ApiResponse<JwtAuthenticationResponse> signIn(SignInRequest request) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()));

                var userDetails = userService
                                .userDetailsService()
                                .loadUserByUsername(request.getUsername());

                var user = (User) userDetails;

                UserResponse responseDto;

                if (user instanceof Customer customer) {
                        responseDto = CustomerResponse.createCustomer(customer);
                } else {
                        responseDto = UserResponse.createUser(user);
                }

                var jwt = jwtService.generateToken(userDetails);
                return ApiResponse.success(new JwtAuthenticationResponse(jwt, responseDto), "User signed in");
        }
}