package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.ClientDtos;
import com.shoppingcart.api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ClientDtos.ClientResponse> create(@Valid @RequestBody ClientDtos.ClientRequest request) {
        return ResponseEntity.ok(clientService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ClientDtos.ClientResponse>> getAll() {
        return ResponseEntity.ok(clientService.getAll());
    }
}
