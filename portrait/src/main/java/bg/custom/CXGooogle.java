package bg.custom;
/**
 * 
 * creer un cx via :
 * via https://cse.google.com/all
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class CXGooogle {

	// 
	public static final File fileProperties = new File("param.properties");
	public static Properties properties = new Properties();
	public static String CX ;
	public static String KEY_CX = "GoogleCx";
	static {
		try {
			FileReader  reader =new FileReader(fileProperties);
			properties.load(reader);
			CX = properties.getProperty(KEY_CX,"WARNING_CX_NO_DEFINE");
			System.err.println("CX : "+CX);
		} catch (Exception e) {
			System.err.println("File Properties exists :"+fileProperties.exists()+"  "+fileProperties.getAbsolutePath());
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] a) {
		System.err.println("done");
	}
	
}
