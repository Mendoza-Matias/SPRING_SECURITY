package com.course.api.spring_security_course.controller;

import com.course.api.spring_security_course.dto.ProductDto;
import com.course.api.spring_security_course.dto.SaveProduct;
import com.course.api.spring_security_course.persistence.entity.Product;
import com.course.api.spring_security_course.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService service;

    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        List<Product> result = service.findAll();
        return ResponseEntity.ok(result.stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .status(product.getStatus().name())
                        .category(null)
                        .build())
                .collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findById(@PathVariable Integer productId) {
        Product result = service.findById(productId);
        return ResponseEntity.ok(ProductDto.builder()
                .id(result.getId())
                .name(result.getName())
                .price(result.getPrice())
                .status(result.getStatus().name())
                .category(null)
                .build());
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_PRODUCT')")
    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody SaveProduct product) {
        Integer result = service.create(product);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    @PutMapping("/{productId}")
    public ResponseEntity<Integer> update(@PathVariable Integer productId, @RequestBody SaveProduct product) {
        Integer result = service.update(productId, product);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_PRODUCT')")
    @PutMapping("/{productId}/disable")
    public ResponseEntity<Integer> disable(@PathVariable Integer productId) {
        Integer result = service.disableById(productId);
        return ResponseEntity.ok(result);
    }
}