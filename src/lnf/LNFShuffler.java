package lnf;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import encryption.EncryptionUtil;

public class LNFShuffler {

	private int d;
	private double beta;
	private ArrayList<byte[]> allValues;
	private ArrayList<byte[]> sampledValues;
	private ArrayList<byte[]> permutatedValues;
	protected LNFAbstractDummyDistribution distribution;
	private PublicKey pk;

	public LNFShuffler(int d, double beta, LNFAbstractDummyDistribution distribution, PublicKey pk) {
		this.d = d;
		this.beta = beta;
		this.distribution = distribution;
		allValues = new ArrayList<byte[]>();
		sampledValues = new ArrayList<byte[]>();
		this.pk = pk;
	}

	public void receiveValue(byte[] value) {
		allValues.add(value);
	}

	public void sampleAndAddFakeValues() {

		for (byte[] value : allValues) {
			if (Math.random() < beta) {
				sampledValues.add(value);
			}
		}

		for (int i = 0; i < d; i++) {
			// System.out.println("LNFShuffle#sampleAndAddFakeValues: " + i);
			int zi = distribution.sample();
			for (int j = 0; j < zi; j++) {
				byte[] encryptedValue;
				try {
					encryptedValue = EncryptionUtil.encrypt(pk, i);
					sampledValues.add(encryptedValue);
				} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
						| NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void permutation() {
		Collections.shuffle(sampledValues);
		permutatedValues = new ArrayList<byte[]>();
		for (byte[] value : sampledValues) {
			permutatedValues.add(value);
		}
	}

	public ArrayList<byte[]> getPermutatedValues() {
		return permutatedValues;
	}

}
