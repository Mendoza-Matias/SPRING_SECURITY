package com.course.api.spring_security_course.controller;

import com.course.api.spring_security_course.dto.RegisteredUser;
import com.course.api.spring_security_course.dto.SaveUser;
import com.course.api.spring_security_course.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody SaveUser newUser) {
        RegisteredUser response = authenticationService.registerCustomer(newUser);
        return ResponseEntity.ok(response);
    }
}