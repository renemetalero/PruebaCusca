package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.ClientDtos;
import com.shoppingcart.api.entity.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientMapperTest {

    private final ClientMapper mapper = new ClientMapper();

    @Test
    void shouldMapRequestToEntity() {
        ClientDtos.ClientRequest request = new ClientDtos.ClientRequest("John", "Doe", "john@doe.com");

        Client entity = mapper.toEntity(request);

        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("john@doe.com", entity.getEmail());
    }

    @Test
    void shouldMapEntityToResponse() {
        Client entity = Client.builder().id(1L).firstName("John").lastName("Doe").email("john@doe.com").build();

        ClientDtos.ClientResponse response = mapper.toResponse(entity);

        assertEquals(1L, response.id());
        assertEquals("John", response.firstName());
    }
}
