package com.course.api.spring_security_course.config.security.filter;

import com.course.api.spring_security_course.exception.ObjectNotFoundException;
import com.course.api.spring_security_course.persistence.entity.User;
import com.course.api.spring_security_course.persistence.entity.security.JwtToken;
import com.course.api.spring_security_course.persistence.repository.IJwTokenRepository;
import com.course.api.spring_security_course.services.IUserService;
import com.course.api.spring_security_course.services.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Filtro que intercepta cada solicitud HTTP para validar el token JWT antes de que llegue al controlador.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //quitar este filtro en oauth

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IJwTokenRepository jwTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extrae el token JWT desde la cabecera Authorization
        String jwt = jwtService.extractJwtFromRequest(request);

        // Si el token es nulo o está vacío, continúa con el siguiente filtro
        if (jwt == null || !StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verifica que el token exista en la base de datos y sea válido
        Optional<JwtToken> token = jwTokenRepository.findByToken(jwt);
        boolean isValid = validateToken(token);

        // Si el token no es válido, continúa sin autenticar
        if (!isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el nombre de usuario (subject) del token
        String username = jwtService.extractUsername(jwt);

        // Busca el usuario en la base de datos
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        // Crea el token de autenticación de Spring Security con las autoridades del usuario
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );

        // Establece detalles adicionales del request como IP, sesión, etc.
        authToken.setDetails(new WebAuthenticationDetails(request));

        // Guarda el token de autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Valida si el token está activo y no ha expirado
    private boolean validateToken(Optional<JwtToken> token) {
        if (!token.isPresent()) {
            return false;
        }

        JwtToken jwtToken = token.get();
        Date now = new Date();

        boolean isValid = jwtToken.isValid() && jwtToken.getExpiration().after(now);

        // Si el token está vencido o es inválido, lo marca como inválido en la base de datos
        if (!isValid) {
            updateToken(jwtToken);
        }

        return isValid;
    }

    // Marca el token como inválido y lo guarda en la base de datos
    private void updateToken(JwtToken jwtToken) {
        jwtToken.setValid(false);
        jwTokenRepository.save(jwtToken);
    }
}
