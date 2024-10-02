package lnf;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import encryption.EncryptionUtil;

public abstract class LNFAbstractDataCollector {

	protected double epsilon;
	protected double delta;
	protected int d;
	protected int n;
	protected double beta;
	protected ArrayList<byte[]> permutatedValues;
	protected double[] frequency;
	protected LNFAbstractDummyDistribution distribution;
	protected double expectedError;
	protected double expectedApproximatedError;
	protected double mu;
	private PrivateKey privateKey;

	protected LNFAbstractDataCollector(double epsilon, double delta, int d, int n, PrivateKey privateKey) {
		this.epsilon = epsilon;
		this.delta = delta;
		this.d = d;
		this.n = n;
		frequency = new double[d];
		this.privateKey = privateKey;
	}

	public void receives(ArrayList<byte[]> permutatedValues) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		this.permutatedValues = permutatedValues;
		calcFreqDist();
		adjustFreqDist();
	}

	public void calcFreqDist() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		for (byte[] encryptedValue : permutatedValues) {
			int value = EncryptionUtil.decrypt(privateKey, encryptedValue);
			frequency[value]++;
		}

		// for (int i = 0; i < permutatedValues.length; i++) {
		// frequency[permutatedValues[i]]++;
		// }
	}

	protected void adjustFreqDist() {
		for (int i = 0; i < d; i++) {
			frequency[i] = 1 / (n * beta) * (frequency[i] - mu);
		}
	}

	public double[] getFrequency() {
		return frequency;
	}

	public LNFAbstractDummyDistribution getDistribution() {
		return distribution;
	}

	public double getBeta() {
		return beta;
	}

	public double getExpectedError() {
		return expectedError;
	}

	public double getExpectedApproximatedError() {
		return expectedApproximatedError;
	}
}
