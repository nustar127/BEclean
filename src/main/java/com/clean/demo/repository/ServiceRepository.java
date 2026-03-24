package com.clean.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {

}