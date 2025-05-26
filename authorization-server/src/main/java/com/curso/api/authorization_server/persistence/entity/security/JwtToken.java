package com.curso.api.authorization_server.persistence.entity.security;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2048)
    private String token;

    private Date expiration;

    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
