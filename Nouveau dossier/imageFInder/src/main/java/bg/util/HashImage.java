package bg.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

public class HashImage {

	public static void main(String[] args) throws Exception {

		File fileImage = new File("D:/input.jpg");
		System.out.println(" file " + fileImage.exists());
		BufferedImage buffImg = ImageIO.read(fileImage);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(buffImg, "png", outputStream);
		byte[] data = outputStream.toByteArray();

		System.out.println("Start MD5 Digest ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data);
		byte[] hash = md.digest();
		System.out.println(toStringHexa(hash));
	} 

	static String toStringHexa(byte[] b) throws Exception {
		String hexString = "";
		for (int i = 0; i < b.length; i++) {
			int a = (b[i] & 0xff) + 0x100;
			hexString += Integer.toString(a, 16).substring(1);
		}
		return hexString;
	}

}
