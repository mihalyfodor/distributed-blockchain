package com.github.mihalyfodor.wallet.entities;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A Blockchain is made up of a chain of blocks. Each block has its own digital
 * signature, a hash in this case and also knows the hash of the previous block.
 * Besides this structure any data can be stored on a block. That will be a
 * simple string from now.
 * 
 * @author Mihaly Fodor
 */
public class Block {

    /**
     * Leading zeroes used for verifying proof of work.
     */
    public static String LEADING_ZEROES = "00000";

	/**
	 * Digital signature of the block.
	 */
	private String hash;

	/**
	 * Digital signature of the previous block.
	 */
	private String previousHash;

	/**
	 * Timestamp of when the block was created. Used in generating the digital signature.
	 */
	private long timestamp;

    /**
     * Small flag we use to generate a different hash when mining.
     */
    private int nonce;

	private List<Transaction> transactions = new ArrayList<>();

	public Block(String previousHash) {
		super();
		this.previousHash = previousHash;
		this.timestamp = System.currentTimeMillis();
		this.setHash(calculateHash());
	}

	/**
	 * One way of generating a digital signature is using a SHA-256 algorithm. Instead of implementing one
	 * ourselves, we are using the one from google. 
	 *
	 * The new signature is based on the previous hash, the data, and the timestamp of the block's creation.
	 *
	 * @return
	 */
	public String calculateHash() {
		return Hashing.sha256().hashString(previousHash + transactions.hashCode() + timestamp + nonce, StandardCharsets.UTF_8)
				.toString();
	}

    /**
     * Mining a Block using Proof of Work.
     *
     * Essentially proof of work is solving a problem to create a new block. We can understand that as
     * generating a new hashcode, still based on the basic fields, until we get one that has a given
     * number of leading zeroes.
     */
    public void mineBlock() {
        while(!hash.substring( 0, LEADING_ZEROES.length()).equals(LEADING_ZEROES)) {
            nonce++;
            hash = calculateHash();
        }
    }
	
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the previousHash
	 */
	public String getPreviousHash() {
		return previousHash;
	}

	/**
	 * @param previousHash the previousHash to set
	 */
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}


	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isGenesisBlock() {
	    return previousHash.equals(Constants.GENESIS_HASH);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
