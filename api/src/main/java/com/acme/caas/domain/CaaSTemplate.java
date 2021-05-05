package com.acme.caas.domain;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CaaSTemplate {

    private String settingsId;
    private String templateName;
    private Map<String, Object> templateSettings; //HashMap as a mapping for a free-form object with fields

}
