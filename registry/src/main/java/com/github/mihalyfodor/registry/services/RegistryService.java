package com.github.mihalyfodor.registry.services;

import com.github.mihalyfodor.registry.types.Wallet;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RegistryService {

    private static Map<String, Wallet> wallets = new HashMap<>();

    public void addWallet(Wallet wallet) {
        this.wallets.put(wallet.getPort(), wallet);
    }

    public void removeWallet(String port) {
        this.wallets.remove(port);
    }

    public Collection<Wallet> getAllWallets() {
        return wallets.values();
    }

}
