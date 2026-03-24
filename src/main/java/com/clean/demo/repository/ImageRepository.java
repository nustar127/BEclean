package com.clean.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

}