package com.acme.caas.domain;

import lombok.*;

import java.util.List;

/**
 * When an update occurs, this gets pushed into the redis update channel to alert subscribers
 * that an update has occurred. updateType indicates whether the name, settings, or the entire object
 * was updated. Based on what was updated (from the updateType), you can access the new name, settings,
 * or object from the various key fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CaaSTemplateUpdate {
    private String settingsId;

    private String name; //what was the name updated to

    private List<String> settingKeys; //the setting key/s that were updated

    private TemplateUpdateType updateType; // what was updated?

    public static enum TemplateUpdateType{
        NAME,
        SETTINGS,
        OBJECT
    }
}