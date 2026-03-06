package com.shoppingcart.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients", indexes = {
        @Index(name = "uk_clients_email", columnList = "email", unique = true),
        @Index(name = "idx_clients_last_name", columnList = "last_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 180)
    private String email;
}
