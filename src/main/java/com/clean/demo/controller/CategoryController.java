package com.clean.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.entity.Category;
import com.clean.demo.repository.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ApiResponse<Iterable<Category>> findAllCategories() {
        return ApiResponse.success(categoryRepository.findAll(), "Founded");
    }

    @PostMapping
    public ApiResponse<Category> newCategory(@RequestBody Category category) {
        return ApiResponse.success(categoryRepository.save(category), "Category created");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ApiResponse.success(null, "Category deleted");
    }
}
