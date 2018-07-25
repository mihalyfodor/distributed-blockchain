package com.github.mihalyfodor.wallet.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * A representation of the coins we own. The amount is given from the sum of transactions that
 * were addressed to us and we did not spend.
 *
 * @author Mihaly Fodor
 *
 */
public class Wallet {

    /**
     * The address of the wallet. This should normally be a private and public key pair.
     */
    private String address;

    /**
     * Map keeping track of the transactions we did not spend.
     */
    private Map<String, TransactionOutput> unspentTransactionOutputs = new HashMap<String, TransactionOutput>();

    /**
     * Creating a wallet needs only an owner
     *
     * @param address name of the wallet/owner
     */
    public Wallet(String address) {
        this.address = address;
    }

    public Map<String, TransactionOutput> getUnspentTransactionOutputs() {
        return unspentTransactionOutputs;
    }

    public void setUnspentTransactionOutputs(Map<String, TransactionOutput> unspentTransactionOutputs) {
        this.unspentTransactionOutputs = unspentTransactionOutputs;
    }


    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }


    public int getBalance() {
        return this.getUnspentTransactionOutputs()
                .values()
                .stream()
                .mapToInt(TransactionOutput::getValue)
                .sum();
    }
}