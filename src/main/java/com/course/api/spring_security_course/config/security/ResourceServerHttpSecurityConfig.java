package com.course.api.spring_security_course.config.security;

import com.course.api.spring_security_course.config.security.filter.JwtAuthenticationFilter;
import com.course.api.spring_security_course.persistence.enums.RoleEnum;
import com.course.api.spring_security_course.persistence.enums.RolePermissionsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta clase es una clase de configuración de Spring
@EnableWebSecurity // Habilita la configuración de seguridad web en la aplicación
@EnableMethodSecurity(prePostEnabled = true) //Habilita otra configuración por anotaciones de autorización
public class ResourceServerHttpSecurityConfig {

    @Autowired // Inyecta el AuthenticationProvider configurado en otro lugar de la aplicación
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthorizationManager<RequestAuthorizationContext> authorizationManager;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean // Define un bean de Spring que será gestionado por el contenedor de Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain filterChain = http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) configuracion basica jwt
                .authorizeHttpRequests(authRequest -> {
                    authRequest.anyRequest().access(authorizationManager);
                })
                //configuracion adicion
                .oauth2ResourceServer(oauth2ResourceServerConfig -> {
                    oauth2ResourceServerConfig.jwt(
                            jwtConfigurer -> jwtConfigurer.decoder(JwtDecoders.fromIssuerLocation(issuerUri))
                    );//peticion de decodificacion
                })
                .build();

        return filterChain;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("permissions");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    private static void buildRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*Autorizacion de endpoints de productos*/
        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());//autorización por roles

        authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}").hasAuthority(RolePermissionsEnum.READ_ONE_PRODUCT.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(RolePermissionsEnum.CREATE_PRODUCT.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}").hasAuthority(RolePermissionsEnum.UPDATE_PRODUCT.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disable").hasAuthority(RolePermissionsEnum.DISABLE_PRODUCT.name());

        /*Autorizacion de endpoint de categorias*/
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories").hasAuthority(RolePermissionsEnum.READ_ALL_CATEGORY.name());//permisos
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}").hasAuthority(RolePermissionsEnum.READ_ONE_CATEGORY.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/categories").hasAuthority(RolePermissionsEnum.CREATE_CATEGORY.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}").hasAuthority(RolePermissionsEnum.UPDATE_CATEGORY.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disable").hasAuthority(RolePermissionsEnum.DISABLE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile").hasAuthority(RolePermissionsEnum.READ_MY_PROFILE.name());

        /*Autorizacion de endpoints publicos*/
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

    //Autorizacion de endpoints publicos
    private static void buildRequestMatchersV2(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
    }
}