package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.ClientDtos;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.mapper.ClientMapper;
import com.shoppingcart.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientDtos.ClientResponse create(ClientDtos.ClientRequest request) {
        Client client = clientMapper.toEntity(request);
        return clientMapper.toResponse(clientRepository.save(client));
    }

    public List<ClientDtos.ClientResponse> getAll() {
        return clientRepository.findAll().stream().map(clientMapper::toResponse).toList();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
    }
}
