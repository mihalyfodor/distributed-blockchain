package com.github.mihalyfodor.wallet.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blockchain {

    private List<Block> blocks = new ArrayList<Block>();

    private Map<String, TransactionOutput> unspentTransactionOutputs = new HashMap<String, TransactionOutput>();

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Map<String, TransactionOutput> getUnspentTransactionOutputs() {
        return unspentTransactionOutputs;
    }

    public void setUnspentTransactionOutputs(Map<String, TransactionOutput> unspentTransactionOutputs) {
        this.unspentTransactionOutputs = unspentTransactionOutputs;
    }
}
