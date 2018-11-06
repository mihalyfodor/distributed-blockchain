package com.github.mihalyfodor.wallet.services;

import com.github.mihalyfodor.wallet.entities.WalletDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistryService {

    private static String REGISTER_ENDPOINT = "register";
    private static String UNREGISTER_ENDPOINT = "unregister";

    @Value("${server.port}")
    private String port;

    @Value("${registry.url}")
    private String registryUrl;

    public void registerWallet(String walletName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<WalletDescription> request = new HttpEntity<>(getDescription(walletName), headers);
            restTemplate.postForLocation(registryUrl + REGISTER_ENDPOINT, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterWallet() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForEntity(registryUrl + UNREGISTER_ENDPOINT + "/" + port, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WalletDescription getDescription(String walletName) {
        return WalletDescription.builder()
                .name(walletName)
                .port(port)
                .build();
    }

}
