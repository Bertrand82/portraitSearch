package bg.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.Kernel;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

public class UtilFilterImage {
	static File dirResult = new File("RESULT");
	static {
		dirResult.mkdirs();
	}

	public static void main(String[] args) throws Exception {
		File f = new File("img2.jpg");
		System.out.println("f : " + f.exists());
		BufferedImage ima = ImageIO.read(f);

		BufferedImage ima2 = filtreImage(ima, filterBlue);
		System.out.println("ima2 : " + ima2);
		BufferedImage ima3 = convolution(ima, getKernelBlur7());
		System.out.println("ima3 : " + ima3);
		BufferedImage ima4 = convolution(ima, getKernelSobelX());
		System.out.println("ima4 : " + ima3);
		BufferedImage ima5 = convolution(ima, getKernelSobelY());
		System.out.println("ima5: " + ima3);
		BufferedImage ima6 = edgeDetection(ima);
		System.out.println("ima5: " + ima6);

		boolean b2 = ImageIO.write(ima2, "PNG", new File(dirResult, "ima2.png"));
		System.out.println("write : " + b2);
		boolean b3 = ImageIO.write(ima3, "PNG", new File(dirResult, "ima3.png"));
		System.out.println("write : " + b3);
		boolean b4 = ImageIO.write(ima4, "PNG", new File(dirResult, "ima4.png"));
		System.out.println("write : " + b4);
		boolean b5 = ImageIO.write(ima5, "PNG", new File(dirResult, "ima5.png"));
		System.out.println("write : " + b5);
		boolean b6 = ImageIO.write(ima6, "PNG", new File(dirResult, "ima6.png"));
		System.out.println("write : " + b6);
	}

	private static BufferedImage edgeDetection(BufferedImage ima) {
		BufferedImage imaX = convolution(ima, getKernelSobelX());

		BufferedImage imaY = convolution(ima, getKernelSobelY());
		BufferedImage imaRetour = new BufferedImage(ima.getWidth(), ima.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < imaRetour.getWidth(); x++) {
			for (int y = 0; y < imaRetour.getHeight(); y++) {
				Color colorx =new Color(imaX.getRGB(x, y));
				Color colory =new Color(imaY.getRGB(x, y));
				int r= Math.max(colorx.getRed(), colory.getRed());
				int g= Math.max(colorx.getGreen(), colory.getGreen());
				int b= Math.max(colorx.getBlue(), colory.getBlue());
				Color c=new Color(r, g, b);
				imaRetour.setRGB(x, y, c.getRGB());
			}
		}
		return imaRetour;
	}

	static Kernel getKernelSobelX() {
		int s1 = 3;
		int s2 = 3;
		float[][] sobel_x = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

		Kernel kernel = getKernel(sobel_x);
		System.out.println("kernel " + toString(kernel));
		return kernel;
	}

	static Kernel getKernelSobelY() {
		float[][] sobel_y = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
		// float[] filter = {-1.0f,-2.f,-1.0f,0.f,0f,0f,1f,2f,1f};
		Kernel kernel = getKernel(sobel_y);
		System.out.println("kernel " + toString(kernel));
		return kernel;
	}

	private static Kernel getKernel(float[][] doubleArray) {
		int s1 = doubleArray.length;
		int s2 = doubleArray[0].length;

		float[] filter = new float[s1 * s2];
		int index = 0;
		for (float[] array : doubleArray) {
			for (float s : array) {
				filter[index++] = s;
			}
		}
		Kernel kernel = new Kernel(s1, s2, filter);
		return kernel;
	}

	public static BufferedImage filtreImage(final Image imageInput, ImageFilter filter) {
		final ImageProducer imgProducer = new FilteredImageSource(imageInput.getSource(), filter);
		Image ima = Toolkit.getDefaultToolkit().createImage(imgProducer);
		return toBufferedImage(ima);
	}

	final static ImageFilter filterBlue = new RGBImageFilter() {
		public final int filterRGB(final int x, final int y, final int rgb) {
			int result = rgb | 0xFF;
			return result;
		}
	};

	static Kernel getKernelBlur7() {
		int s1 = 7;
		int s2 = 7;
		float level = .1f / 9f;

		float[] filter = new float[s1 * s2];

		for (int i = 0; i < s1 * s2; i++) {
			filter[i] = level;
		}
		Kernel kernel = new Kernel(s1, s2, filter);
		System.out.println("kernel " + toString(kernel));
		return kernel;
	}

	private static String toString(Kernel kernel) {
		DecimalFormat decimalFormat = new DecimalFormat("00.000");
		String s = "";
		s += " w :" + kernel.getWidth() + " h " + kernel.getHeight() + "\n";
		float[] k = new float[kernel.getWidth() * kernel.getHeight()];
		k = kernel.getKernelData(k);
		for (int i = 0; i < kernel.getWidth(); i++) {
			for (int j = 0; j < kernel.getHeight(); j++) {
				s += (decimalFormat.format(k[j * kernel.getWidth() + i]) + " ; ");
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img
	 *            The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	public static BufferedImage convolution(BufferedImage src, Kernel kernel) {
		BufferedImage bufferedImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);

		BufferedImageOp op = new ConvolveOp(kernel);
		bufferedImage = op.filter(src, null);

		return bufferedImage;
	}

}
