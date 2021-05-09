package com.acme.caas.service;

import com.acme.caas.domain.CaaSTemplate;
import com.acme.caas.domain.CaaSTemplateUpdate;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface LiveUpdateService {

    public void publishNameUpdate(String settingsId, String templateName, CaaSTemplateUpdate.TemplateUpdateType updateType);

    public void publishFullUpdate(String settingsId, CaaSTemplateUpdate.TemplateUpdateType updateType);

    public void publishSettingsUpdate(String settingsId, List<String> settingKeys, CaaSTemplateUpdate.TemplateUpdateType updateType);

}
