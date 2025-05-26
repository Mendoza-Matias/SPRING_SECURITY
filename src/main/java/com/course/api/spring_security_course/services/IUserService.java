package com.course.api.spring_security_course.services;

import com.course.api.spring_security_course.dto.SaveUser;
import com.course.api.spring_security_course.persistence.entity.User;

import java.util.Optional;

public interface IUserService {
    User registerCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String username);
}
