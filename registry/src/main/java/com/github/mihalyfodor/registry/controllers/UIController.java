package com.github.mihalyfodor.registry.controllers;

import com.github.mihalyfodor.registry.services.RegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UIController {

    private final RegistryService registryService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("wallets", registryService.getAllWallets());
        return "index";
    }
}
