package com.clean.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
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
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Iterable<Service>> findAll() {
        return ApiResponse.success(serviceRepository.findAll(), "Founded");
    }

    @GetMapping("/{id}")
    public ApiResponse<Service> findById(@PathVariable("id") Long id) {
        return serviceRepository.findById(id)
                .map(service -> ApiResponse.success(service, "Founded"))
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    @PostMapping("")
    public ApiResponse<Service> newService(@RequestBody Service service) {
        return ApiResponse.success(serviceRepository.save(service), "Service created");
    }

    @PutMapping("/{id}")
    public ApiResponse<Service> changeService(@RequestBody Service newService, @PathVariable Long id) {
        Service updated = serviceService.updateService(id, newService);
        return ApiResponse.success(updated, "Service updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteService(@PathVariable Long id) {
        serviceRepository.deleteById(id);
        return ApiResponse.success(null, "Service deleted");
    }

    @DeleteMapping("")
    public ApiResponse<Void> deleteServices(@RequestBody List<Long> ids) {
        serviceRepository.deleteAllById(ids);
        return ApiResponse.success(null, "Services deleted");
    }

    @GetMapping("/requirements")
    public ApiResponse<Iterable<ServiceRequirement>> findAllRequirements() {
        return ApiResponse.success(serviceReqRepository.findAll(), "Founded");
    }

    @PostMapping("/requirements")
    public ApiResponse<Iterable<ServiceRequirement>> newServiceReq(@RequestBody List<ServiceRequirement> requirements) {
        return ApiResponse.success(serviceReqRepository.saveAll(requirements), "Requirements saved");
    }

    @PutMapping("/requirements/{id}")
    public ApiResponse<ServiceRequirement> changeServiceReq(@RequestBody Double amount, @PathVariable Long id) {
        return serviceReqRepository.findById(id)
                .map(req -> {
                    req.setRequiredAmount(amount);
                    return ApiResponse.success(serviceReqRepository.save(req), "Amount updated");
                })
                .orElseThrow(() -> new RuntimeException("Requirement not found"));
    }

    @DeleteMapping("/requirements/{id}")
    public ApiResponse<Void> deleteServiceReq(@PathVariable Long id) {
        serviceReqRepository.deleteById(id);
        return ApiResponse.success(null, "Requirement deleted");
    }
}
