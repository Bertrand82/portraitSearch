package bg.util;

import java.util.Base64;

public class GoogleSearchUtil {

	
	public static void decodeEi(String ei) {
		byte[] decodedBytes = Base64.getDecoder().decode(ei);
		for (byte b : decodedBytes) {
			System.out.println(Integer.toHexString(0+b));
		}
	}
}
