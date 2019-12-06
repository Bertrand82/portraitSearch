package bg.faceRecognition;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class UtilArray {

	/**
	 * Divide each element in array v by b 
	 * No checking for division by zero.
	 *
	 * @param v  array containing numbers.
	 * @param b  scalar used to divied each element in the v vector
	 *
	 * @return the same array  with each element divided by b.
	 *
	 */
	public static void divide(double[] v, double b) {

		for (int i = 0; i < v.length; i++)
			v[i] = v[i] / b;

	}

	public static double sum(final double[] a) {
		return DoubleStream.of(a).sum();
	}

	public static double sum(final Double[] a) {
		final double b = Arrays.asList(a).stream().mapToDouble(e -> e).sum();
		return b;
	}

	public static double max(final double[] a) {
		return DoubleStream.of(a).max().getAsDouble();
	}

	public static double max(final Double[] a) {
		final double b = Arrays.asList(a).stream().mapToDouble(e -> e).max().getAsDouble();
		return b;
	}

	public static double[] toDouble(byte[] b) {
		double[] result = new double[b.length];
		for (int i = 0; i < b.length; i++) {
			result[i] = (double) (b[i] & 0xFF);
		}
		return result;
	}

	public static double[] toDouble(int[] b) {
		double[] result = new double[b.length];
		for (int i = 0; i < b.length; i++) {
			result[i] = (double) (b[i] & 0xFF);
		}
		return result;
	}

}
