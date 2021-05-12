package com.acme.caas.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class User {

    private String username;
    private String password;
    private boolean authenticated;
    private String jwt;
}
