package com.acme.caas.service;

import com.acme.caas.domain.CaaSTemplate;

public interface RedisService {
    public void createTemplate(CaaSTemplate template) throws Exception;
}
