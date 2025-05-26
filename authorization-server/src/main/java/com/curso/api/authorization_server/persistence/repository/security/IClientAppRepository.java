package com.curso.api.authorization_server.persistence.repository.security;

import com.curso.api.authorization_server.persistence.entity.security.ClientApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClientAppRepository extends JpaRepository<ClientApp, Integer> {
    Optional<ClientApp> findByClientId(String clientId);
}
