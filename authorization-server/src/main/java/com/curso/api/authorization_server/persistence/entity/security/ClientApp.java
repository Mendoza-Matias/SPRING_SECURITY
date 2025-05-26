package com.curso.api.authorization_server.persistence.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String clientId;

    private String clientSecret;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> clientAuthenticationMethods;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorizationGrantTypes;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> redirectUris;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> scopes;

    private Integer durationInMinutes;

    private Boolean requiredProofKey;
}
