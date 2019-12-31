package bg.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class UtilString {

	
	public static String toString(InputStream in) {
		int i=0;
		String s ="";
		try {
			while((i=in.read()) >=0) {
				char c = (char) i;
				s+= c;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
