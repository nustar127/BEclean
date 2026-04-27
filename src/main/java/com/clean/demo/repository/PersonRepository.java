package com.clean.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.clean.demo.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT COUNT(c) FROM Customer c")
    Integer sumCustomersAmount();
}
