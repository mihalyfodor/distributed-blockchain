package com.github.mihalyfodor.wallet.services;

import com.github.mihalyfodor.wallet.entities.Block;
import com.github.mihalyfodor.wallet.entities.Blockchain;
import com.github.mihalyfodor.wallet.entities.Transaction;
import com.github.mihalyfodor.wallet.entities.TransactionInput;
import com.github.mihalyfodor.wallet.entities.TransactionOutput;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    /**
     * Add a transaction to the block and mine it. Won't work if the transaction fails when processing
     * or we are attempting to add a transaction to the genesis block.
     *
     * @param transaction the transaction we are adding.
     * @return transaction processing and adding successful not
     */
    public boolean addTransaction(Blockchain blockchain, Block block, Transaction transaction) {

        if (transaction == null) {
            return false;
        }

        boolean transactionSuccesful = processTransaction(transaction, blockchain);

        if ( block.isGenesisBlock() || !transactionSuccesful ) {
            return false;
        }

        block.getTransactions().add(transaction);

        return true;
    }

    /**
     * Validate and process the transaction
     *
     * @return true or false, depending if the transaction was successful or not
     */
    private boolean processTransaction(Transaction transaction, Blockchain blockchain) {

        if (!transaction.isSignatureValid()) {
            return false;
        }

        // grab all the unspent transaction inputs
        for (TransactionInput input : transaction.getInputs()) {
            TransactionOutput unspentTransactionOutput = blockchain.getUnspentTransactionOutputs().get(input.getTransactionOutputId());
            // update the transaction output for an input
            input.setUnspentTransactionOutput(unspentTransactionOutput);
        }

        // find out how much money we can send
        int sumOfUnspentInputs = transaction.getInputs().stream()
                .filter(e -> e.getUnspentTransactionOutput() != null)
                .mapToInt(e -> e.getUnspentTransactionOutput().getValue())
                .sum();

        int leftOverValue = sumOfUnspentInputs - transaction.getValue();

        transaction.setTransactionId(transaction.calculateHash());

        // send the money to the recipient
        TransactionOutput recipientReceived = new TransactionOutput(transaction.getRecipient(), transaction.getValue(), transaction.getTransactionId());
        transaction.getOutputs().add(recipientReceived);

        TransactionOutput senderReceived = new TransactionOutput(transaction.getSender(), leftOverValue, transaction.getTransactionId());
        transaction.getOutputs().add(senderReceived);

        // update the transaction outputs
        for (TransactionOutput output : transaction.getOutputs()) {
            blockchain.getUnspentTransactionOutputs().put(output.getId(), output);
        }

        // update the transaction inputs
        for (TransactionInput input : transaction.getInputs()) {
            if (input.getUnspentTransactionOutput() != null) {
                blockchain.getUnspentTransactionOutputs().remove(input.getUnspentTransactionOutput().getId());
            }
        }

        return true;
    }
}
