package com.course.api.spring_security_course.controller;

import com.course.api.spring_security_course.dto.AuthenticationRequest;
import com.course.api.spring_security_course.dto.AuthenticationResponse;
import com.course.api.spring_security_course.persistence.entity.User;
import com.course.api.spring_security_course.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt) {
        Boolean isTokenValid = authenticationService.validateToken(jwt);
        return ResponseEntity.ok().body(isTokenValid);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> findByProfile() {
        User user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok().body(user);
    }
}