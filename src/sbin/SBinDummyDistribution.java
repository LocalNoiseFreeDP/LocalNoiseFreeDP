package sbin;

import org.apache.commons.math3.distribution.BinomialDistribution;

import lnf.LNFAbstractDummyDistribution;

public class SBinDummyDistribution extends LNFAbstractDummyDistribution {

	BinomialDistribution distribution = null;

	public SBinDummyDistribution(int M) {
		distribution = new BinomialDistribution(M, 0.5);
	}

	@Override
	public int sample() {
		return distribution.sample();
	}

}
