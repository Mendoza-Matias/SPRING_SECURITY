package com.curso.api.authorization_server.config.security;

import com.curso.api.authorization_server.persistence.repository.security.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Indica que esta clase es una clase de configuración de Spring
public class SecurityBeansInjector {

    @Autowired // Inyecta el repositorio de usuarios para acceder a la base de datos
    private IUserRepository repository;

    @Bean // Define un bean para el AuthenticationProvider, que es la estrategia de autenticación
    public AuthenticationProvider authenticationProvider() {
        // Crea una instancia de DaoAuthenticationProvider, que es una estrategia de autenticación basada en base de datos
        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();

        // Configura el codificador de contraseñas para que coincida con el utilizado al almacenar las contraseñas
        authenticationStrategy.setPasswordEncoder(passwordEncoder());

        // Configura el servicio de detalles de usuario para obtener la información del usuario desde la base de datos
        authenticationStrategy.setUserDetailsService(userDetailsService());

        return authenticationStrategy;
    }

    @Bean // Define un bean para el PasswordEncoder, que se utiliza para codificar y verificar contraseñas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Define un bean para el UserDetailsService, que carga los detalles del usuario desde la base de datos
    public UserDetailsService userDetailsService() {
        // Retorna una función lambda que busca un usuario por su nombre de usuario en la base de datos
        return (username) -> {
            // Busca el usuario en el repositorio y lanza una excepción si no se encuentra
            return repository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        };
    }

}