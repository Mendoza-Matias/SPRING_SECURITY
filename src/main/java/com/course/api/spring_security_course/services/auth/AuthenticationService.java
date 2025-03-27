package com.course.api.spring_security_course.services.auth;

import com.course.api.spring_security_course.dto.AuthenticationRequest;
import com.course.api.spring_security_course.dto.AuthenticationResponse;
import com.course.api.spring_security_course.dto.RegisteredUser;
import com.course.api.spring_security_course.dto.SaveUser;
import com.course.api.spring_security_course.persistence.entity.User;
import com.course.api.spring_security_course.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisteredUser registerCustomer(SaveUser newUser) {
        User user = userService.registerCustomer(newUser);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return generateResponse(user, jwt);
    }

    private RegisteredUser generateResponse(User user, String jwt) {
        return RegisteredUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .rol(user.getRole().name())
                .jwt(jwt)
                .build();
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("authorities", user.getAuthorities());
        return extraClaims;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authentication);

        UserDetails user = userService.findByUsername(authenticationRequest.getUsername());
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));

        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    public Boolean validateToken(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User findLoggedInUser() {
        Authentication auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String username = (String) auth.getPrincipal(); //nombre de usuario - usuario autenticado

        return userService.findByUsername(username); //retorno el usuario
    }
}