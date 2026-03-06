package com.shoppingcart.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ClientDtos {
    public record ClientRequest(@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Email String email) {
    }

    public record ClientResponse(Long id, String firstName, String lastName, String email) {
    }
}
