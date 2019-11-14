package bg.portrait.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

public class HashImage {

	public static void main(String[] args) throws Exception {
		File fileImage = new File("D:/input.jpg");
		System.out.println(" file " + fileImage.exists());
		BufferedImage buffImg = ImageIO.read(fileImage);
		String hash = getHashImage(buffImg);
		System.out.println(hash);
	}
	
	public static String getHashImage(BufferedImage image)  throws Exception{
		
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			byte[] data = outputStream.toByteArray();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data);
			byte[] hash = md.digest();
			String r = toStringHexa(hash);
			return r;
		
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
