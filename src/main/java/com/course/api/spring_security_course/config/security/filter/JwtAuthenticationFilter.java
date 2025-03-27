package com.course.api.spring_security_course.config.security.filter;

import com.course.api.spring_security_course.services.IUserService;
import com.course.api.spring_security_course.services.auth.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 *   JwtAuthenticationFilter tiene la responsabilidad de verificar si el token JWT
 *   presente en la solicitud es válido y si está asociado con un usuario autenticado en la aplicación.
 * */


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1 obtener encabezado http
        String authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(authorizationHeader) || authorizationHeader.startsWith("Bearer ")) { //Bearer jwt
            filterChain.doFilter(request, response);
            return;
        }
        //2 obtener token jwt desde el encabezado
        String jwt = authorizationHeader.split("")[1];

        //3 obtener el subject -> validar el token , formato y fecha de expiracion
        String username = jwtService.extractUsername(jwt);

        //4 setear objeto authentication dentro del security context holder

        UserDetails userDetails = userService.findByUsername(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5 ejecutar el registro de filtros
        filterChain.doFilter(request, response); //continuar con la cadena de filtros
    }
}
