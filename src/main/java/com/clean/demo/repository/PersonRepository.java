package com.clean.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clean.demo.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
