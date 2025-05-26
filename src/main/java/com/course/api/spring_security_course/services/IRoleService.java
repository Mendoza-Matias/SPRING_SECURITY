package com.course.api.spring_security_course.services;

import com.course.api.spring_security_course.persistence.entity.security.Role;

public interface IRoleService {
    Role findDefaultRole();
}
