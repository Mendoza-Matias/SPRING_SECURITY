package com.curso.api.authorization_server.service;

import com.curso.api.authorization_server.exception.ObjectNotFoundException;
import com.curso.api.authorization_server.mapper.ClientAppMapper;
import com.curso.api.authorization_server.persistence.entity.security.ClientApp;
import com.curso.api.authorization_server.persistence.repository.security.IClientAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterClientService implements RegisteredClientRepository {

    @Autowired
    private IClientAppRepository clientAppRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
    }

    @Override
    public RegisteredClient findById(String id) {
        ClientApp client = clientAppRepository.findByClientId(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found"));
        return ClientAppMapper.toRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return this.findById(clientId);
    }
}
