package com.clean.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

}
