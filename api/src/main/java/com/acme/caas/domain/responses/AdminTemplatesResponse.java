package com.acme.caas.domain.responses;

import com.acme.caas.domain.CaaSTemplate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AdminTemplatesResponse extends AdminControllerResponse{

    private List<CaaSTemplate> templateList;
}
