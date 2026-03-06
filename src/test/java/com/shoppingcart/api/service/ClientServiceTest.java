package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.ClientDtos;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.mapper.ClientMapper;
import com.shoppingcart.api.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClient() {
        ClientDtos.ClientRequest request = new ClientDtos.ClientRequest("John", "Doe", "john@doe.com");
        Client client = Client.builder().build();
        Client saved = Client.builder().id(1L).build();
        ClientDtos.ClientResponse response = new ClientDtos.ClientResponse(1L, "John", "Doe", "john@doe.com");

        when(clientMapper.toEntity(request)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(saved);
        when(clientMapper.toResponse(saved)).thenReturn(response);

        ClientDtos.ClientResponse result = clientService.create(request);

        assertEquals(1L, result.id());
    }

    @Test
    void shouldReturnAllClients() {
        Client entity = Client.builder().id(1L).build();
        when(clientRepository.findAll()).thenReturn(List.of(entity));
        when(clientMapper.toResponse(any())).thenReturn(new ClientDtos.ClientResponse(1L, "J", "D", "a@a.com"));

        List<ClientDtos.ClientResponse> result = clientService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.findById(99L));
    }
}
