package com.course.api.spring_security_course.services;

import java.util.List;

import com.course.api.spring_security_course.dto.SaveProduct;
import com.course.api.spring_security_course.persistence.entity.Product;

public interface IProductService {

    List<Product> findAll();

    Product findById(Integer productId);

    Integer create(SaveProduct product);

    Integer update(Integer productId, SaveProduct product);

    Integer disableById(Integer productId);
}
