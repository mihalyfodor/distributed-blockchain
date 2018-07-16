package com.github.mihalyfodor.wallet.controllers;

import com.github.mihalyfodor.wallet.services.BlockchainService;
import com.github.mihalyfodor.wallet.entities.Block;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallet/")
public class BlockchainController {

    private BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("blockchain")
    public List<Block> getBlockchain() {
        return blockchainService.getBlockchain();
    }

    @PostMapping("blockchain")
    public void addBlock(@RequestBody String data) {
        blockchainService.addBlock(data);
    }

    @PostMapping("hack")
    public void hack() {
        blockchainService.hackBlockchain();
    }

    @GetMapping("validity")
    public Boolean getBlockchainValidity() {
        return blockchainService.isChainValid();
    }


}
