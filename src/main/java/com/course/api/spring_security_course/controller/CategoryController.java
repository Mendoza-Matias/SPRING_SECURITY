package com.course.api.spring_security_course.controller;

import com.course.api.spring_security_course.dto.CategoryDto;
import com.course.api.spring_security_course.dto.SaveCategory;
import com.course.api.spring_security_course.persistence.entity.Category;
import com.course.api.spring_security_course.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        List<Category> result = service.findAll();
        return ResponseEntity.ok(result.stream()
                .map(category -> CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .status(category.getStatus().name())
                        .build())
                .collect(Collectors.toList()));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Integer categoryId) {
        Category result = service.findById(categoryId);
        return ResponseEntity.ok(CategoryDto.builder()
                .id(result.getId())
                .name(result.getName())
                .status(result.getStatus().name())
                .build());
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody SaveCategory category) {
        Integer result = service.create(category);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Integer> update(@PathVariable Integer categoryId, @RequestBody SaveCategory category) {
        Integer result = service.update(categoryId, category);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{categoryId}/disable")
    public ResponseEntity<Integer> disableById(@PathVariable Integer categoryId) {
        Integer result = service.disableById(categoryId);
        return ResponseEntity.ok(result);
    }
}