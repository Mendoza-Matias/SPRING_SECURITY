package com.course.api.spring_security_course.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY; // Clave secreta para firmar el token

    @Value("${security.jwt.expirations-in-minutes}")
    private Long EXPIRATION_IN_MINUTES; // Tiempo de expiración del token en minutos

    /**
     * Genera un token JWT con los detalles del usuario y los claims adicionales.
     */
    public String generateToken(UserDetails user, Map<String, Object> claims) {
        Date issuedAt = new Date(System.currentTimeMillis()); // Fecha de emisión del token
        Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_IN_MINUTES * 60 * 1000)); // Fecha de expiración

        return Jwts.builder()
                .header()
                .type("jwt")
                .and()
                .subject(user.getUsername()) // Nombre de usuario como sujeto del token
                .issuedAt(issuedAt) // Fecha de emisión
                .claims(claims) // Claims adicionales (payload)
                .expiration(expiration) // Fecha de expiración
                .signWith(generateKey(), Jwts.SIG.HS256) // Firma del token
                .compact(); // Convierte el token en un String
    }

    /*
     * Genera una clave secreta a partir de la clave configurada.
     */
    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey); // Convierte la clave secreta en bytes
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     */
    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject(); // Extrae el subject (username) del token
    }

    /*
     * Extrae todos los claims (payload) del token JWT.
     */
    private Claims extractAllClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey()) // Configura la clave para validar la firma
                .build()
                .parseSignedClaims(jwt) // Parsea el token
                .getPayload(); // Obtiene el payload (claims)
    }
}