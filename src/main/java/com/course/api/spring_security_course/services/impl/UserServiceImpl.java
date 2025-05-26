package com.course.api.spring_security_course.services.impl;

import com.course.api.spring_security_course.dto.SaveUser;
import com.course.api.spring_security_course.persistence.entity.User;
import com.course.api.spring_security_course.persistence.entity.security.Role;
import com.course.api.spring_security_course.persistence.enums.RoleEnum;
import com.course.api.spring_security_course.persistence.repository.IUserRepository;
import com.course.api.spring_security_course.services.IRoleService;
import com.course.api.spring_security_course.services.IUserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    @Override
    public User registerCustomer(SaveUser newUser) {
        validatePassword(newUser.getPassword(), newUser.getRepeatedPassword());

        Role defaultRole = roleService.findDefaultRole();

        User user = User.builder()
                .username(newUser.getUsername())
                .name(newUser.getName())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .role(defaultRole)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatePassword(String password, String repeatPassword) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(repeatPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (!password.equals(repeatPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }
}