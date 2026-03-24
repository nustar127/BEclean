package com.clean.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.entity.Service;
import com.clean.demo.repository.ServiceRepository;
import com.clean.demo.service.ServiceService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/services")
public class ServicesController {

    private final ServiceService serviceService;

    public ServicesController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("")
    private Iterable<Service> findAll() {
        return serviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Service> findById(@PathVariable("id") Long userId) {
        return serviceRepository.findById(userId);
    }

    @PostMapping("")
    public Service newService(@RequestBody Service service) {
        return serviceRepository.save(service);
    }

    @PutMapping("/{id}")
    public Service changeService(@RequestBody Service newService, @PathVariable Long id) {
        return serviceRepository.findById(id)
                .map(service -> {
                    service = serviceService.updateService(id, newService);
                    return serviceRepository.save(service);
                }).orElseGet(() -> {
                    return serviceRepository.save(newService);
                });
    }

    @DeleteMapping("/{id}")
    void deleteEmployee(@PathVariable Long id) {
        serviceRepository.deleteById(id);
    }
}
