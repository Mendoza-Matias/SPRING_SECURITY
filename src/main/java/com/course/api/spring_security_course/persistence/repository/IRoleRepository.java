package com.course.api.spring_security_course.persistence.repository;

import com.course.api.spring_security_course.persistence.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name); //devolver un OPTIONAL
}
