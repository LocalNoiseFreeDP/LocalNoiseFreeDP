package sbin;

import java.security.PrivateKey;

import lnf.LNFAbstractDataCollector;

public class SBinDataCollector extends LNFAbstractDataCollector {

	public SBinDataCollector(double epsilon, double delta, int d, int n, PrivateKey privateKey) {
		super(epsilon, delta, d, n, privateKey);

		int M = -1;
		double bestLoss = Double.MAX_VALUE;
		double step = 0.000001;
		for (double tbeta = step; tbeta < 1 + step; tbeta += step) {
			double t_local_epsilon = SBinUtil.getLocalEpsilon(epsilon, tbeta);
			int tM = SBinUtil.getM(t_local_epsilon, delta, tbeta);
			expectedError = SBinUtil.getExpectedError(n, d, tM, tbeta);
			if (expectedError < bestLoss) {
				bestLoss = expectedError;
				beta = tbeta;
				M = tM;
			}
		}

		expectedError = bestLoss;
		expectedApproximatedError = SBinUtil.getApproximatedExpectedError(epsilon, delta, n, d, beta);
		distribution = new SBinDummyDistribution(M);
		mu = M * 0.5;

	}

}
