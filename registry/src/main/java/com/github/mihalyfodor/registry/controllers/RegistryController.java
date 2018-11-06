package com.github.mihalyfodor.registry.controllers;

import com.github.mihalyfodor.registry.services.RegistryService;
import com.github.mihalyfodor.registry.types.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class RegistryController {

    private final RegistryService registryService;

    @PostMapping("/register")
    public void register(@RequestBody Wallet wallet) {
        registryService.addWallet(wallet);
    }

    @GetMapping("/unregister/{port}")
    public void unregister(@PathVariable String port) {
        registryService.removeWallet(port);
    }

    @GetMapping("/list")
    public Collection<Wallet> list() {
        return registryService.getAllWallets();
    }

}
