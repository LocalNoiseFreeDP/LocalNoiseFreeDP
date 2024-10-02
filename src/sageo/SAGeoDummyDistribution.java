package sageo;

import java.util.Random;

import lnf.LNFAbstractDummyDistribution;

public class SAGeoDummyDistribution extends LNFAbstractDummyDistribution {

	// The original domain of the PMF is [0, infinity), but it corresponds up to the
	// point where the CDF exceeds the targetCumulativeValue.
	private static double targetCumulativeValue = 0.9999999;
	private Random random;
	private double[] cumulativeProbabilities;

	public SAGeoDummyDistribution(int nu, double kappa, double ql, double qr) {
		this.random = new Random();
		int size = getSize(nu, ql, qr);
		cumulativeProbabilities = new double[size];
		double cumulativeProbability = 0.0;
		for (int k = 0; k < size; k++) {
			double probability = -1;
			if (k <= nu - 1) {
				probability = 1.0 / kappa * Math.pow(ql, nu - k);
			} else {
				probability = 1.0 / kappa * Math.pow(qr, k - nu);
			}
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

	private static int getSize(double mu, double ql, double qr) {
		int result = (int) Math.ceil(-1 + mu
				+ Math.log(
						((1 + Math.pow(ql, 1 + mu) * (-1 + qr) - ql * qr) * (-1 + targetCumulativeValue)) / (-1 + ql))
						/ Math.log(qr));
		return result;
	}

}
