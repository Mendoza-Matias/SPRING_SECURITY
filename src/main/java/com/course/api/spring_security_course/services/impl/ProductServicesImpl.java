package com.course.api.spring_security_course.services.impl;

import com.course.api.spring_security_course.dto.SaveProduct;
import com.course.api.spring_security_course.persistence.entity.Product;
import com.course.api.spring_security_course.persistence.enums.ProductStatus;
import com.course.api.spring_security_course.persistence.repository.IProductRepository;
import com.course.api.spring_security_course.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServicesImpl implements IProductService {

    @Autowired
    private IProductRepository repository;

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(Integer productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Integer create(SaveProduct product) {
        Product newProduct = Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .category(null)
                .status(ProductStatus.ENABLED)
                .build();
        return repository.save(newProduct).getId();
    }

    @Override
    public Integer update(Integer productId, SaveProduct saveProduct) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setCategory(null);
        return repository.save(product).getId();
    }

    @Override
    public Integer disableById(Integer productId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (isEnable(product)) {
            product.setStatus(ProductStatus.DISABLE);
        }
        return repository.save(product).getId();
    }

    private boolean isEnable(Product product) {
        return product.getStatus() == ProductStatus.ENABLED;
    }
}