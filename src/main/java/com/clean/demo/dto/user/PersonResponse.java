package com.clean.demo.dto.user;

import com.clean.demo.entity.Person;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PersonResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public static PersonResponse createPerson(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .phone(person.getPhone())
                .build();
    }
}
