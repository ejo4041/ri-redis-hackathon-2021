package com.acme.caas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.*;
import java.util.stream.Collectors;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
public class CaaSTemplate {

    private String settingsId;
    private String templateName;
    private Map<String, Object> templateSettings; //HashMap as a mapping for a free-form object with fields

//    public List<Map.Entry<String, Object>> getTemplateSettingsList() {
//        return Optional
//            .ofNullable(this.templateSettings)
//            .map(templateMap ->
//                {
//                    return templateMap.entrySet().stream().collect(Collectors.toList());
//                })
//            .orElse(List.of());
//    }
}
