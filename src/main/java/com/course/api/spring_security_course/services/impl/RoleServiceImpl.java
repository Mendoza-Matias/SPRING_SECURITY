package com.course.api.spring_security_course.services.impl;

import com.course.api.spring_security_course.persistence.entity.security.Role;
import com.course.api.spring_security_course.persistence.repository.IRoleRepository;
import com.course.api.spring_security_course.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Value("${security.default.role}")
    private String defaultRole;

    @Override
    public Role findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }
}
