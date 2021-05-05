package com.acme.caas.domain.responses;

import com.acme.caas.domain.CaaSTemplate;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class AdminTemplateResponse extends AdminControllerResponse{

    private CaaSTemplate template;
}
