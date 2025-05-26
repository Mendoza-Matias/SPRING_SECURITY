package com.course.api.spring_security_course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisteredUser {
    private Integer id;
    private String username;
    private String name;
    private String rol;
    private String jwt;
}
