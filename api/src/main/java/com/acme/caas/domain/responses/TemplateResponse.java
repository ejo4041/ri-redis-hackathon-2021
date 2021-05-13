package com.acme.caas.domain.responses;

import com.acme.caas.domain.CaaSTemplate;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TemplateResponse extends ControllerResponse {

    private CaaSTemplate template;
}
