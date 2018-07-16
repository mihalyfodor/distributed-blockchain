package com.github.mihalyfodor.wallet.services;

import com.github.mihalyfodor.wallet.entities.Block;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	private List<Block> blockchain = new ArrayList<Block>();
	
	/**
	 * Initialize the chain with a genesis block.
	 */
	private void initializeChain() {
		blockchain.add(new Block("Genesis Block", "0"));
	}
	
	/**
	 * Add a block to the chain with the given data. Will only work if the chain is not empty.
	 * 
	 * @param data data to add
	 */
	public void addBlock(String data) {
		if (!blockchain.isEmpty()) {
			Block prevBlock = blockchain.get(blockchain.size() - 1);
			Block newBlock = new Block(data, prevBlock.getHash());
			blockchain.add(newBlock);
		}
	}
	
	
	/**
	 * Validate the chain. If we have just the genesis block, that is valid. Otherwise we 
	 * traverse the chain with two variables, and compare the hashes as follows:
	 * - the hashcode needs to be able to be regenerated
	 * - the prevHash codes need to be continuous
	 * 
	 * @return chain validity
	 */
	public Boolean isChainValid() {
		
		if (blockchain.size() <= 1) {
			return true;
		}
		
		Block prevBlock = null;
		
		for (Block currentBlock : blockchain) {
			
			// skip testing the genesis block, we have no prevBlock in this case
			if (prevBlock == null) {
				prevBlock = currentBlock;
				continue;
			}
			
			// verify against tampering. If we cannot regenerate the hash correctly the chain is not valid anymore.
			boolean currentHashCorrect = currentBlock.getHash().equals(currentBlock.calculateHash());
			
			// similarly if the previous hash reference is incorrect it is also a problem
			boolean prevHashCorrect = prevBlock.getHash().equals(currentBlock.getPreviousHash());
			
			if (!currentHashCorrect || !prevHashCorrect) {
				return false;
			}
			
			prevBlock = currentBlock;
		}
		
		return true;
	}

	public List<Block> getBlockchain() {
	    if (blockchain.isEmpty()) {
	        initializeChain();
        }
		return blockchain;
	}

	public void hackBlockchain() {
	    int number = new Random().nextInt(blockchain.size());
	    blockchain.get(number).setData("hacked");
    }

}
