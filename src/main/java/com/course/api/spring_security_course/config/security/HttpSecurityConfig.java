package com.course.api.spring_security_course.config.security;

import com.course.api.spring_security_course.config.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta clase es una clase de configuración de Spring
@EnableWebSecurity // Habilita la configuración de seguridad web en la aplicación
public class HttpSecurityConfig {

    @Autowired // Inyecta el AuthenticationProvider configurado en otro lugar de la aplicación
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean // Define un bean de Spring que será gestionado por el contenedor de Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Deshabilita la protección CSRF (Cross-Site Request Forgery) ya que no es necesaria para APIs stateless
                .csrf(csrf -> csrf.disable())

                // Configura la gestión de sesiones para ser STATELESS, lo que significa que no se crearán sesiones en el servidor
                .sessionManagement(sesConfig -> sesConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Establece el proveedor de autenticación que se utilizará para autenticar las solicitudes
                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //ejecutar filtro antes

                // Configura las reglas de autorización para las solicitudes HTTP
                .authorizeHttpRequests(authorizeRequests -> {
                    // Permite el acceso público a las siguientes rutas:

                    // Permite solicitudes POST a la ruta "customers" sin autenticación
                    authorizeRequests.requestMatchers(HttpMethod.POST, "/customers").permitAll();

                    authorizeRequests.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();

                    authorizeRequests.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

                    // Todas las demás solicitudes deben estar autenticadas
                    authorizeRequests.anyRequest().authenticated();
                })
                .build(); // Construye y devuelve la cadena de filtros de seguridad configurada
    }
}