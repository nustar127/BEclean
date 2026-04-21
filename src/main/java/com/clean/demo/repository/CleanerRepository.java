package com.clean.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clean.demo.entity.Cleaner;

public interface CleanerRepository extends JpaRepository<Cleaner, Long> {
}
