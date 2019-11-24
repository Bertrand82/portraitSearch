package bg.faceRecognition;

public class UtilArray {

	/**
	 * The max of array
	 *
	 * @param an
	 *            array *
	 * @return the max of the array
	 */
	public static double max(double[] a) {
		double b = a[0];
		for (int i = 0; i < a.length; i++)
			if (a[i] > b)
				b = a[i];

		return b;
	}

	/**
	 * Divide each element in <code>v</code> by <code>b</code> No checking for
	 * division by zero.
	 *
	 * @param v
	 *            vector containing numbers.
	 * @param b
	 *            scalar used to divied each element in the v vector
	 *
	 * @return a vector having each element divided by <code>b</code> scalar.
	 *
	 */
	public static void divide(double[] v, double b) {

		for (int i = 0; i < v.length; i++)
			v[i] = v[i] / b;

	}

	/**
	 * The sum of the vector.
	 *
	 * @param a
	 *            vector with numbers
	 * @return a scalar with the sum of each element in the <code>a</code> vector
	 */
	public static double sum(double[] a) {

		double b = a[0];
		for (int i = 0; i < a.length; i++)
			b += a[i];

		return b;

	}

}
