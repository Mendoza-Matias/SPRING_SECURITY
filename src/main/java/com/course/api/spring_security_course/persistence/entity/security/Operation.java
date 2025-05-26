package com.course.api.spring_security_course.persistence.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operations")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; //FIND_ALL_PRODUCTS

    private String path; //   /products/{productId}/

    private String httpMethod;

    private boolean permitAll;

    @ManyToOne //muchas operaciones van a tener un modulo
    @JoinColumn(name = "module_id")
    private Module module;
}
