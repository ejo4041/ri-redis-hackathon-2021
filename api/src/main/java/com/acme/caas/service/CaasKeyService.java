package com.acme.caas.service;

import java.util.List;

public interface CaasKeyService {
    public void addKey(String key) throws Exception;

    public void deleteKey(String key);

    public List<String> getKeys();

    public String generateKey() throws Exception;
}
