package com.clean.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class ServicesController {

    @GetMapping("")
    private ResponseEntity<String> findAll() {
        return ResponseEntity.ok("{}");
    }

    @GetMapping("/{id}") 
    public ResponseEntity<String> findById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(String.format("{ \"userId\": %d }", userId));
    }
}
