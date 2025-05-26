package com.curso.api.authorization_server.persistence.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.Module;

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

    @Column(name = "module_id")
    private long moduleId;
}
