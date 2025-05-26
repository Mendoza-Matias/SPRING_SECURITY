package com.course.api.spring_security_course.services.impl;

import com.course.api.spring_security_course.dto.SaveCategory;
import com.course.api.spring_security_course.persistence.entity.Category;
import com.course.api.spring_security_course.persistence.enums.CategoryStatus;
import com.course.api.spring_security_course.persistence.repository.ICategoryRepository;
import com.course.api.spring_security_course.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryRepository repository;

    //Obtiene todas las categorías existentes.
    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    //Busca una categoría por su ID.
    @Override
    public Category findById(Integer categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    //Crea una nueva categoría.
    @Override
    public Integer create(SaveCategory category) {
        if (repository.existsByName(category.getName())) {
            throw new RuntimeException("Category name already exists");
        }
        Category newCategory = Category.builder()
                .name(category.getName())
                .status(CategoryStatus.ENABLED)
                .build();
        return repository.save(newCategory).getId();
    }

    //Actualiza una categoría existente.
    @Override
    public Integer update(Integer categoryId, SaveCategory category) {
        Category categoryExist = repository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryExist.setName(category.getName());
        return repository.save(categoryExist).getId();
    }

    //Desactiva una categoría por su ID.
    @Override
    public Integer disableById(Integer categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (isEnabled(category)) {
            category.setStatus(CategoryStatus.DISABLE);
        }
        return repository.save(category).getId();
    }

    //Verifica si una categoría está habilitada.
    private boolean isEnabled(Category category) {
        return category.getStatus() == CategoryStatus.ENABLED;
    }
}