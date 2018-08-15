package com.github.mihalyfodor.registry;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistryService {

    private static Set<String> wallets = new HashSet<>();

    public void addWallet(String walletName) {
        this.wallets.add(walletName);
    }

    public Set<String> getAllWallets() {
        return wallets;
    }

}
