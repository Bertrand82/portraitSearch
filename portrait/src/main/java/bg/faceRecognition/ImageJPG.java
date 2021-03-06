package bg.faceRecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * JPG File reader/writer. Uses native com.sun libraries (which may deprecate at
 * any time)
 * 
 * @version 1.0
 */
public class ImageJPG implements IImage {

	private byte bytes[] = null; // bytes which make up binary PPM image
	private double doubles[] = null;
	private File file = null; // filename for PPM image
	private int height = 0;
	private int width = 0;

	public ImageJPG(File file) throws Exception {
		this.file = file;
		readImage();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * Get the data as double array. Data is of any type that has been read from the
	 * file (usually 8bit RGB put into an 64bit double)
	 *
	 * @return The data of the image.
	 */
	public double[] getDouble() {
		return doubles;
	}

	/**
	 * Write to <code>fn</code> file the <code>data</code> using the
	 * <code>width, height</code> variables. Data is assumed to be 8bit RGB.
	 *
	 * @throws FileNotFoundException
	 *             if the directory/image specified is wrong
	 * @throws IOException
	 *             if there are problems reading the file.
	 */
	public static void writeImage(String fn, byte[] data, int width, int height) throws FileNotFoundException, IOException {

		FileOutputStream fOut = new FileOutputStream(fn);
		JPEGImageEncoder jpeg_encode = JPEGCodec.createJPEGEncoder(fOut);

		int ints[] = new int[data.length];
		for (int i = 0; i < data.length; i++)
			ints[i] = 255 << 24 | (int) (data[i] & 0xff) << 16 | (int) (data[i] & 0xff) << 8 | (int) (data[i] & 0xff);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, ints, 0, width);

		jpeg_encode.encode(image);
		fOut.close();
	}

	/**
	 * Read the image from the specified file.
	 *
	 * @throws FileNotFoundException
	 *             pretty obvious
	 * @throws IOException
	 *             filesystem related problems
	 */
	private void readImage() throws Exception {

		FileInputStream fIn = new FileInputStream(file);
		JPEGImageDecoder jpeg_decode = JPEGCodec.createJPEGDecoder(fIn);
		BufferedImage image = jpeg_decode.decodeAsBufferedImage();

		width = image.getWidth();
		height = image.getHeight();

		int[] rgbdata = new int[width * height];

		image.getRGB(0, 0, width, height, rgbdata, 0, width);

		bytes = new byte[rgbdata.length];
		doubles = new double[rgbdata.length];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (rgbdata[i] & 0xFF);
			doubles[i] = (double) (rgbdata[i]);
		}
	}
}
