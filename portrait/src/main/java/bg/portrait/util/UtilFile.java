package bg.portrait.util;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

public class UtilFile {

	public static File DATA_ROOT = new File("/IMAGES");
	public static File DATA_CACHE = new File("D:\\imagesCacheTemp");

	/**
	 * Si le name est de la forme : f0cacf283612cc0ad09dad545baccf2f_1_crop.jpg
	 * Return le hashMd5 sinon null
	 * 
	 * @param name
	 * @return
	 */
	public static String getHashFromFile(File file) {
		try {
			File fileCano = file.getCanonicalFile();
			return getHashFromFileName(fileCano.getName());
		} catch (IOException e) {
			return null;
		}
	}

	public static String getHashFromFileName(String name) {
		if (name == null) {
			return null;
		}
		int i_ = name.indexOf('_');
		if (i_ < 0) {
			return null;
		}
		String hash = name.substring(0, i_);
		return hash;
	}

	public static File getDirRootFromHashMd5(String hashMd5) {
		return getDirRootFromHashMd5(hashMd5, DATA_ROOT);
	}

	public static File getDirRootFromHashMd5(String hashMd5, File dirRoot) {
		File dir0 = new File(dirRoot, "" + hashMd5.charAt(0));
		File dir1 = new File(dir0, "" + hashMd5.charAt(1));
		File dir2 = new File(dir1, "" + hashMd5.charAt(2));
		File dir3 = new File(dir2, "" + hashMd5.charAt(3));
		File dir = new File(dir3, hashMd5);
		return dir;
	}

	public static String hashMd5(String name) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(name.getBytes());
		byte[] hash = md.digest();
		String hashStr = toStringHexa(hash);
		return hashStr;
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
