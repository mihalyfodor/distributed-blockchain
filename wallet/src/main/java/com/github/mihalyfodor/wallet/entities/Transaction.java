package com.github.mihalyfodor.wallet.entities;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


    public class Transaction {

        /**
         * Number of transactions that were generated.
         */
        private static int SEQUENCE = 0;

        /**
         * Hash of the transaction.
         */
        private String transactionId;

        /**
         * Sender's address. in a normal implementation this would be the public key.
         */
        private String sender;

        /**
         * Recipient's address. In a normal implementation this would be the public key.
         */
        private String recipient;

        /**
         * The amount of coins we wish to send.
         */
        private int value;

        /**
         * Signature for the transaction. In a normal implementation this would also be more complicated :)
         */
        private String signature;

        /**
         * Transaction outputs used as inputs for this transaction.
         */
        private List<TransactionInput> inputs = new ArrayList<TransactionInput>();

        /**
         * Outputs from this transaction.
         */
        private List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

        public Transaction(String sender, String recipient, int value, List<TransactionInput> inputs) {
            this.sender = sender;
            this.recipient = recipient;
            this.value = value;
            this.inputs = inputs;
        }

        public String calculateHash() {
            return Hashing.sha256().hashString(sender + recipient + value + Transaction.SEQUENCE, StandardCharsets.UTF_8)
                    .toString();
        }

        public void generateSignature() {
            this.signature = sender + recipient + value;
        }

        public boolean isSignatureValid() {
            return this.signature.equals(sender + recipient + value);
        }

        /**
         * @return the transactionId
         */
        public String getTransactionId() {
            return transactionId;
        }

        /**
         * @param transactionId the transactionId to set
         */
        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        /**
         * @return the sender
         */
        public String getSender() {
            return sender;
        }

        /**
         * @param sender the sender to set
         */
        public void setSender(String sender) {
            this.sender = sender;
        }

        /**
         * @return the recipient
         */
        public String getRecipient() {
            return recipient;
        }

        /**
         * @param recipient the recipient to set
         */
        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(int value) {
            this.value = value;
        }

        /**
         * @return the signature
         */
        public String getSignature() {
            return signature;
        }

        /**
         * @param signature the signature to set
         */
        public void setSignature(String signature) {
            this.signature = signature;
        }

        /**
         * @return the inputs
         */
        public List<TransactionInput> getInputs() {
            return inputs;
        }

        /**
         * @param inputs the inputs to set
         */
        public void setInputs(List<TransactionInput> inputs) {
            this.inputs = inputs;
        }

        /**
         * @return the outputs
         */
        public List<TransactionOutput> getOutputs() {
            return outputs;
        }

        /**
         * @param outputs the outputs to set
         */
        public void setOutputs(List<TransactionOutput> outputs) {
            this.outputs = outputs;
        }


    }

