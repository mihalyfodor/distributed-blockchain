package com.github.mihalyfodor.wallet.controllers;

import com.github.mihalyfodor.wallet.services.RegistryService;
import com.github.mihalyfodor.wallet.types.LoginModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UIController {

    private static LoginModel loginModel;

    private final RegistryService registryService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("loginModel", new LoginModel());
        return "index";
    }

    @PostMapping("/register")
    public String login(@ModelAttribute LoginModel loginModel) {
        registryService.registerWallet(loginModel.getWalletName());
        UIController.loginModel = loginModel;
        return "wallet";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        registryService.unRegisterWallet();
        return index(model);
    }

}
