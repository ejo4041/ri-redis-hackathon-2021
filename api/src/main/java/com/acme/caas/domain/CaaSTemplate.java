package com.acme.caas.domain;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CaaSTemplate {

    private String adminName;
    private String templateName;
    private List<HashMap<String, Object>> templateData;
}
