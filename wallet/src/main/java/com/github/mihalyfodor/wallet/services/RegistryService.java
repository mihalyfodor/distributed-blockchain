package com.github.mihalyfodor.wallet.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistryService {

    @Value("${server.port}")
    private String port;

    @Value("${registry.url}")
    private String registryUrl;

    public Integer login() {
        String URL = registryUrl + port;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(URL, null, String.class);
        return response.getStatusCodeValue();
    }

}
