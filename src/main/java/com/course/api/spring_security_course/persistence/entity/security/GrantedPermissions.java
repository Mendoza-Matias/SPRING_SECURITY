package com.course.api.spring_security_course.persistence.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "granted_permissions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GrantedPermissions { //tabla intermedia

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;
}
