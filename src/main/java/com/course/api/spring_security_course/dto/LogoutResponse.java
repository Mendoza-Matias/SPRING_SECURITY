package com.course.api.spring_security_course.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LogoutResponse {
    private String message;
}
