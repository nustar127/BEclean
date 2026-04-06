package com.clean.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.ServiceRequirement;

public interface ServiceRequirementRepository extends CrudRepository<ServiceRequirement, Long> {
    
}
