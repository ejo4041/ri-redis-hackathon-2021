package com.acme.caas.domain.responses;

import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class AdminControllerResponse {

    private String message;
}
