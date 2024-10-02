package s1geo;

import java.util.Random;

import lnf.LNFAbstractDummyDistribution;

public class S1GeoDummyDistribution extends LNFAbstractDummyDistribution {

	// The original domain of the PMF is [0, infinity), but it corresponds up to the
	// point where the CDF exceeds the targetCumulativeValue.
	private static double targetCumulativeValue = 0.9999999;
	private Random random;
	private double[] cumulativeProbabilities;

	public S1GeoDummyDistribution(double qr) {
		this.random = new Random();
		int size = getSize(qr);
		cumulativeProbabilities = new double[size];
		double cumulativeProbability = 0.0;
		for (int k = 0; k < size; k++) {
			double probability = (1.0 - qr) * Math.pow(qr, k);
			cumulativeProbability += probability;
			cumulativeProbabilities[k] = cumulativeProbability;
		}
	}

	@Override
	public int sample() {
		double r = random.nextDouble();
		for (int k = 0; k < cumulativeProbabilities.length; k++) {
			if (r <= cumulativeProbabilities[k]) {
				return k;
			}
		}
		return cumulativeProbabilities.length;
	}

	private static int getSize(double qr) {
		int result = (int) Math.ceil(-1 + Math.log(1 - targetCumulativeValue) / Math.log(qr));
		return result;
	}

}
