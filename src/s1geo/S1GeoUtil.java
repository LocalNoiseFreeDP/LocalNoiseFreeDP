package s1geo;

public class S1GeoUtil {

	public static double getExpectedError(int n, int d, double ql, double qr, double beta) {
		double error = (1 - beta) / (beta * n) + qr * d / (Math.pow(1 - qr, 2) * beta * beta * n * n);
		return error;
	}

	public static double getApproximatedExpectedError(double epsilon, int n, int d) {
		double error = 2.0 / epsilon / n + 8 * d / n / n / epsilon / epsilon;
		return error;
	}
}
