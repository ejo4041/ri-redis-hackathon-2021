package com.acme.caas.domain.responses;

import com.acme.caas.domain.CaaSTemplate;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TemplatesResponse extends ControllerResponse {

    private List<CaaSTemplate> templateList;
}
