package com.github.mihalyfodor.wallet.services;

import com.github.mihalyfodor.wallet.entities.Blockchain;
import com.github.mihalyfodor.wallet.entities.Transaction;
import com.github.mihalyfodor.wallet.entities.TransactionInput;
import com.github.mihalyfodor.wallet.entities.TransactionOutput;
import com.github.mihalyfodor.wallet.entities.Wallet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class WalletService {

    public void updateTransactionOutputs(Wallet wallet, Collection<TransactionOutput> unspentTransactionOutputs) {
        wallet.getUnspentTransactionOutputs().clear();
        for (TransactionOutput output: unspentTransactionOutputs) {
            if (output.isOwnedBy(wallet.getAddress())) {
                wallet.getUnspentTransactionOutputs().put(output.getId(), output);
            }
        }
    }

    /**
     * Send coins from this wallet to another.
     *
     * @param recipient the address of the recipient wallet
     * @param value the amount of coins we send
     *
     * @return the transaction
     */
    public Transaction createTransaction(Wallet wallet, Blockchain blockchain, String recipient, int value) {

        // we can't send coins we don't have
        updateTransactionOutputs(wallet, blockchain.getUnspentTransactionOutputs().values());

        if (wallet.getBalance() < value) {
            return null;
        }

        List<TransactionInput> inputs = gatherTransactionInputs(wallet, blockchain, value);

        Transaction transaction = new Transaction(wallet.getAddress(), recipient, value, inputs);
        transaction.generateSignature();


        for (TransactionInput input: inputs) {
            wallet.getUnspentTransactionOutputs().remove(input.getTransactionOutputId());
        }

        return transaction;
    }

    /**
     * Collect all relevant transactions that were addressed to us and are not yet spent.
     * Find enough of them so we can send the coins.
     *
     * @param value the total we are looking for
     * @return the gathered transaction inputs
     */
    private List<TransactionInput> gatherTransactionInputs(Wallet wallet, Blockchain blockchain, int value) {
        List<TransactionInput> inputs = new ArrayList<>();

        int total = 0;
        for (TransactionOutput output: blockchain.getUnspentTransactionOutputs().values()) {

            if (output.isOwnedBy(wallet.getAddress())) {
                total = total + output.getValue();
                inputs.add(new TransactionInput(output.getId()));

                if (total > value) {
                    break;
                }
            }
        }
        return inputs;
    }
}
