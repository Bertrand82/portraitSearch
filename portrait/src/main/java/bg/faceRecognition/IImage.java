package bg.faceRecognition;

public interface IImage {

	public int getHeight();

	public int getWidth();

	public byte[] getBytes();

	/**
	 * Get the data as double array. Data is of any type that has been read from the
	 * file (usually 8bit RGB put into an 64bit double)
	 *
	 * @return The data of the image.
	 */
	public double[] getDouble();
}
