package sbin;

public class SBinUtil {
	public static double getExpectedError(int n, int d, int M, double beta) {
		double error = (1 - beta) / (beta * n) + (double) M * d / (4.0 * beta * beta * n * n);
		return error;
	}

	public static double getApproximatedExpectedError(double epsilon, double delta, int n, int d, double beta) {
		double error = (1 - beta) / (beta * n) + 8 * d * Math.log(4 * beta / delta) / n / n / epsilon / epsilon;
		return error;
	}

	public static int getM(double epsilon, double delta, double beta) {
		double e0 = epsilon;
		double a = delta;
		double b = beta;

		double part1 = 2 * (Math.exp(e0) - 1);
		double part2 = (1 + Math.exp(e0)) * ((1 + Math.exp(e0)) * Math.log((4 * b) / a) + Math.sqrt(2) * Math.sqrt(Math
				.exp(e0) * Math.log((4 * b) / a)
				* (2 + Math.cosh(e0) * (-2 + Math.log((4 * b) / a)) + Math.log((4 * b) / a) + 2 * Math.sinh(e0))));
		return (int) Math.ceil(1 / Math.pow((-1 + Math.exp(e0)), 2) * (part1 + part2));
	}

	public static double getLocalEpsilon(double epsilon, double beta) {
		double local_epsilon = Math.log((-1 + beta + Math.exp(epsilon / 2)) / beta);
		return local_epsilon;
	}
}
