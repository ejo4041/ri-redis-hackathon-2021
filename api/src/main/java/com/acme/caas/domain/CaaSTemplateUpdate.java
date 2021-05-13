package com.acme.caas.domain;

import java.util.List;
import java.util.Map;

import lombok.*;

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

    private Map<String, Object> templateSettings; //the setting key/s that were updated

    private TemplateUpdateType updateType; // Create, update, or delete

    private TemplateUpdateField updateField; // what was update?

    public static enum TemplateUpdateField {
        NAME,
        SETTINGS,
        OBJECT,
    }

    public static enum TemplateUpdateType {
        CREATE,
        UPDATE,
        DELETE,
    }
}
