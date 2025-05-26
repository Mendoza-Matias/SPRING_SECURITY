package com.course.api.spring_security_course.controller;

import com.course.api.spring_security_course.dto.RegisteredUser;
import com.course.api.spring_security_course.dto.SaveUser;
import com.course.api.spring_security_course.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AuthenticationService authenticationService;

    @PreAuthorize("permitAll") //endpoint publico
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody SaveUser newUser) {
        RegisteredUser registeredUser = authenticationService.registerOneCustomer(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}