package com.example.testing.customer;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    private UUID id;
    @NotBlank
    private String name;
    private String phoneNumber;
}
