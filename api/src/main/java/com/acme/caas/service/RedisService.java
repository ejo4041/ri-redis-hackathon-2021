package com.acme.caas.service;

import com.acme.caas.domain.CaaSTemplate;

import java.util.List;
import java.util.Map;

public interface RedisService {
    public CaaSTemplate createTemplate(CaaSTemplate caasTemplate) throws Exception;

    public void updateTemplate(CaaSTemplate caasTemplate) throws Exception;

    public void addTemplateData(String settingsId, String settingsKey, Object settingsValue) throws Exception;

    public void updateTemplateData(String settingsId, String settingsKey, Object settingsValue) throws Exception;

    public void deleteTemplateData(String settingsId, String settingsKey) throws Exception;

    public void updateTemplateName(String settingsId, String name) throws Exception;

    public CaaSTemplate getTemplate(String settingsId) throws Exception;

    public List<CaaSTemplate> getTemplates() throws Exception;

    public Object getTemplateSettings(String settingsId, String settingKey) throws Exception;

    public List<Object> getTemplateSettings(String settingsId, List<String> settingKeys) throws Exception;

    public void deleteTemplate(CaaSTemplate caasTemplate) throws Exception;

    public void deleteTemplate(String settingId) throws Exception;

    public void deleteTemplates() throws Exception;
}
