package com.clean.demo.dto;

import com.clean.demo.entity.Cleaner;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CleanerResponse extends UserResponse {
    private Double rating;
    private Double experience;

    public static CleanerResponse createCustomer(Cleaner user) {
        return CleanerResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .experience(user.getExperience())
                .rating(user.getRating())
                .roles(
                        user.getRoles().stream()
                                .map(Enum::name)
                                .toList())
                .build();
    }
}
