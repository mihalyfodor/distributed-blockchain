package com.github.mihalyfodor.wallet.controllers;

import com.github.mihalyfodor.wallet.entities.Wallet;
import com.github.mihalyfodor.wallet.services.BlockchainService;
import com.github.mihalyfodor.wallet.entities.Block;
import com.github.mihalyfodor.wallet.services.RegistryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/wallet/")
public class BlockchainController {

    private BlockchainService blockchainService;
    private RegistryService registryService;

    public BlockchainController(BlockchainService blockchainService, RegistryService registryService) {
        this.blockchainService = blockchainService;
        this.registryService = registryService;
    }

    @GetMapping("blockchain")
    public List<Block> getBlockchain() {
        return blockchainService.getBlockchain();
    }

    @PostMapping("send/{from}/{to}/{amount}")
    public ResponseEntity addBlock(@PathVariable String from, @PathVariable String to, @PathVariable Integer amount) {
        if (blockchainService.sendMoney(from, to, amount)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @PostMapping("hack")
    public void hack() {
        blockchainService.hackBlockchain();
    }

    @GetMapping("validity")
    public Boolean getBlockchainValidity() {
        return blockchainService.isChainValid();
    }

    @GetMapping("all")
    public Collection<Wallet> getWallets() {
        return blockchainService.getWallets();
    }

    @PostMapping("login")
    public void login() {
        registryService.login();
    }


}
