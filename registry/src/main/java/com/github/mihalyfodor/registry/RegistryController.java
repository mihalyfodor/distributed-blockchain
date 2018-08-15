package com.github.mihalyfodor.registry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("registry")
public class RegistryController {

    private RegistryService registryService;

    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostMapping("login/{address}")
    public void login(@PathVariable String address) {
        registryService.addWallet(address);
    }

    @GetMapping("list")
    public Set<String> listWallets() {
        return registryService.getAllWallets();
    }

}
