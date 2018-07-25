package com.github.mihalyfodor.wallet.services;

import com.github.mihalyfodor.wallet.entities.Block;
import com.github.mihalyfodor.wallet.entities.Blockchain;
import com.github.mihalyfodor.wallet.entities.Constants;
import com.github.mihalyfodor.wallet.entities.Transaction;
import com.github.mihalyfodor.wallet.entities.TransactionOutput;
import com.github.mihalyfodor.wallet.entities.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for operations on our blockchain.
 *
 * At this point we can:
 * - retrieve it from the server
 * - add blocks to it with a simple string payload
 * - validate its integrity
 * - hack it to force integrity failure
 * 
 * @author Mihaly Fodor
 *
 */
@Service
@SessionScope
public class BlockchainService {

    private WalletService walletService;
    private TransactionService transactionService;

    private Blockchain blockchain = new Blockchain();

    private Map<String,  Wallet> wallets = new HashMap<>();

    public BlockchainService(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    /**
	 * Initialize the chain with a genesis block.
	 */
	private void initializeChain() {
		blockchain.getBlocks().add(new Block( "0"));
	}

	private void initializeWallets() {
        wallets.put(Constants.ALICE, new Wallet(Constants.ALICE));
        wallets.put(Constants.BOB, new Wallet(Constants.BOB));
        wallets.put(Constants.BANK, new Wallet(Constants.BANK));
    }

    /**
     * Create the very first transaction. We need to set most of the fields manually
     * - create the transaction itself
     * - create the single output (we are creating money from nothing)
     * - create the block for it
     */
    private void addOriginTransaction(String targetAddress, int coins) {

        Transaction genesisTransaction = new Transaction(wallets.get(Constants.BANK).getAddress(), targetAddress, coins, new ArrayList<>());
        genesisTransaction.generateSignature();
        genesisTransaction.setTransactionId(Constants.GENESIS_HASH);

        TransactionOutput genesisOutput = new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId());
        genesisTransaction.getOutputs().add(genesisOutput);
        blockchain.getUnspentTransactionOutputs().put(genesisOutput.getId(), genesisOutput);

        Block genesisBlock = new Block(Constants.GENESIS_HASH);
        genesisBlock.getTransactions().add(genesisTransaction);
        blockchain.getBlocks().add(genesisBlock);
    }
	
    /**
     * Validate the chain. If we have just the genesis block, that is valid. Otherwise we
     * traverse the chain with two variables, and compare the hashes as follows:
     * - the hashcode needs to be able to be regenerated
     * - the prevHash codes need to be continuous
     * - the block must have been mined if not genesis block
     *
     * @return chain validity
     */
    public Boolean isChainValid() {

        if (blockchain.getBlocks().size() <= 1) {
            return true;
        }

        Block prevBlock = null;

        for (Block currentBlock : blockchain.getBlocks()) {

            // skip testing the genesis block, we have no prevBlock in this case
            if (prevBlock == null) {
                prevBlock = currentBlock;
                continue;
            }

            // verify against tampering. If we cannot regenerate the hash correctly the chain is not valid anymore.
            boolean currentHashCorrect = currentBlock.getHash().equals(currentBlock.calculateHash());

            // similarly if the previous hash reference is incorrect it is also a problem
            boolean prevHashCorrect = prevBlock.getHash().equals(currentBlock.getPreviousHash());

            // also each block must have been mined for the chain to be valid
            boolean hashMinedCorrectly = currentBlock.getHash().substring( 0, Block.LEADING_ZEROES.length()).equals(Block.LEADING_ZEROES);

            if (!currentHashCorrect || !prevHashCorrect || !hashMinedCorrectly) {
                return false;
            }

            prevBlock = currentBlock;
        }

        return true;
    }


	public List<Block> getBlockchain() {
	    if (blockchain.getBlocks().isEmpty()) {
	        initializeChain();
            initializeWallets();
            addOriginTransaction(Constants.ALICE, 100);
        }
		return blockchain.getBlocks();
	}

	public Collection<Wallet> getWallets() {
        wallets.values().forEach(e -> walletService.updateTransactionOutputs(e, blockchain.getUnspentTransactionOutputs().values()));
        return wallets.values();
    }

	public void hackBlockchain() {
        blockchain.getUnspentTransactionOutputs()
                .values()
                .stream()
                .filter(e -> e.getRecipient().equals("Bob"))
                .forEach(e -> e.setValue(e.getValue() * 10));
    }

    public boolean sendMoney(String from, String to, Integer amount) {
        Block lastBlock = blockchain.getBlocks().get(blockchain.getBlocks().size() - 1);
        Block newBlock = new Block(lastBlock.getHash());
        Transaction tx = walletService.createTransaction(wallets.get(from), blockchain, to, amount);
        boolean txSuccesful = transactionService.addTransaction(blockchain, newBlock, tx);
        if (txSuccesful) {
            blockchain.getBlocks().add(newBlock);
            return true;
        }
        return false;
    }

}
