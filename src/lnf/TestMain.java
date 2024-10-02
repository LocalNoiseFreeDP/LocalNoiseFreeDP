package lnf;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import encryption.ENCRYPTION_MODE;
import encryption.EncryptionUtil;
import s1geo.S1GeoDataCollector;
import sageo.SAGeoDataCollector;
import sbin.SBinDataCollector;

public class TestMain {

	static int numSimulations = 1;

	public static void main(String args[]) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {

		double epsilons[] = { 5, 4, 3, 2, 1, 0.5, 0.2, 0.1 };
		double deltas[] = { Math.pow(0.1, 12) };
		String dataNames[] = { "rfid", "localization", "foursquare", "census" };
		// RSA, ECIES, Unencrypted
		ENCRYPTION_MODE eMode = ENCRYPTION_MODE.Unencrypted;

		for (String dataName : dataNames) {
			for (double epsilon : epsilons) {
				for (double delta : deltas) {
					runTest(epsilon, delta, dataName, SBinDataCollector.class, eMode);
					runTest(epsilon, delta, dataName, SAGeoDataCollector.class, eMode);
					runTest(epsilon, delta, dataName, S1GeoDataCollector.class, eMode);
				}
			}
		}
	}

	public static <T1 extends LNFAbstractDataCollector> void runTest(double epsilon, double delta, String dataName,
			Class<T1> targetDataCollector, ENCRYPTION_MODE eMode) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {

		int orgData[] = Util.getOrgVals(dataName);
		int n = orgData.length;
		int d = Util.getCategoryNum(orgData);

		double sumMSE = 0.0;
		double sumMAE = 0.0;
		double expectedError = 0.0;
		double approximatedExpectedError = 0.0;

		PublicKey publicKey = null;
		PrivateKey privateKey = null;

		if (eMode != ENCRYPTION_MODE.Unencrypted) {
			KeyPair keyPair = EncryptionUtil.getKeyPair(eMode);
			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();
		}

		for (int sim = 0; sim < numSimulations; sim++) {
			// Initialize users
			LNFUser users[] = new LNFUser[n];
			for (int i = 0; i < n; i++) {
				users[i] = new LNFUser(orgData[i], publicKey);
			}

			// Initialize the data collector
			T1 dataCollector = targetDataCollector
					.getDeclaredConstructor(double.class, double.class, int.class, int.class, PrivateKey.class)
					.newInstance(epsilon, delta, d, n, privateKey);
			// Initialize the shuffler
			LNFShuffler shuffler = new LNFShuffler(d, dataCollector.getBeta(), dataCollector.getDistribution(),
					publicKey);

			expectedError = dataCollector.getExpectedError();
			approximatedExpectedError = dataCollector.getExpectedApproximatedError();

			// Send input values
			for (LNFUser user : users) {
				byte[] v = user.getValue();
				shuffler.receiveValue(v);
			}

			// Sampling and adding fake values
			shuffler.sampleAndAddFakeValues();
			shuffler.permutation();

			// Data collector receives the sampled values
			ArrayList<byte[]> values = shuffler.getPermutatedValues();
			dataCollector.receives(values);
			double frequency[] = dataCollector.getFrequency();
			double frequency_thresholding[] = Util.significance_threshold(frequency, n, expectedError);

			// For evaluation
			double[] originalFrequency = Util.getOrgFrequency(users, d);

			sumMSE += Util.getMSE(originalFrequency, frequency_thresholding);
			sumMAE += Util.getMAE(originalFrequency, frequency_thresholding);
		}

		System.out.println(targetDataCollector.getPackage().getName() + "\t" + dataName + "\t" + epsilon + "\t" + delta
				+ "\t" + (sumMSE / numSimulations) + "\t" + (sumMAE / numSimulations) + "\t" + expectedError + "\t"
				+ approximatedExpectedError);
	}

}
