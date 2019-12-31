package bg.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class UtilHttp {

	public static String getStringFromStream(InputStream inputStream) throws IOException{
		int a ;
		String s="";
		while((a = inputStream.read()) >= 0) {
			s+= (char) a;
			System.out.print((char) a);
		}
		return s;
	}

}
