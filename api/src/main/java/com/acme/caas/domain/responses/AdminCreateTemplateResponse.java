package com.acme.caas.domain.responses;

import com.acme.caas.domain.CaaSTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AdminCreateTemplateResponse extends AdminTemplateResponse {

    private String settingsId;
}
