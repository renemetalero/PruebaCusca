package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.ClientDtos;
import com.shoppingcart.api.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toEntity(ClientDtos.ClientRequest request) {
        return Client.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
    }

    public ClientDtos.ClientResponse toResponse(Client client) {
        return new ClientDtos.ClientResponse(client.getId(), client.getFirstName(), client.getLastName(), client.getEmail());
    }
}
