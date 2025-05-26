package com.course.api.spring_security_course.services;

import java.util.List;

import com.course.api.spring_security_course.dto.SaveCategory;
import com.course.api.spring_security_course.dto.SaveProduct;
import com.course.api.spring_security_course.persistence.entity.Category;
import com.course.api.spring_security_course.persistence.entity.Product;

public interface ICategoryService {

    List<Category> findAll();

    Category findById(Integer categoryId);

    Integer create(SaveCategory category);

    Integer update(Integer categoryId, SaveCategory category);

    Integer disableById(Integer categoryId);
}
