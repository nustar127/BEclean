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
import com.clean.demo.entity.ServiceRequirement;
import com.clean.demo.repository.ServiceRepository;
import com.clean.demo.repository.ServiceRequirementRepository;
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

    @Autowired
    private ServiceRequirementRepository serviceReqRepository;

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
    void deleteEService(@PathVariable Long id) {
        serviceRepository.deleteById(id);
    }

    @DeleteMapping("")
    void deleteEServices(@RequestBody Iterable<Long> ids) {
        serviceRepository.deleteAllById(ids);
    }

    @GetMapping("/requirements")
    private Iterable<ServiceRequirement> findAllRequqrements() {
        return serviceReqRepository.findAll();
    }

    @PostMapping("/requirements")
    public ServiceRequirement newServiceReq(@RequestBody ServiceRequirement serviceRequirement) {
        return serviceReqRepository.save(serviceRequirement);
    }

    @PutMapping("/requirements/{id}")
    public ServiceRequirement changeServiceReq(@RequestBody Double amount, @PathVariable Long id) {
        return serviceReqRepository.findById(id)
                .map(serviceRequirement -> {
                    serviceRequirement.setRequiredAmount(amount);
                    return serviceReqRepository.save(serviceRequirement);
                }).orElseGet(() -> {
                    return null;
                });
    }

    @DeleteMapping("/requirements/{id}")
    void deleteServiceReq(@PathVariable Long id) {
        serviceReqRepository.deleteById(id);
    }
}
