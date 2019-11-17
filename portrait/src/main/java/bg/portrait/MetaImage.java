package bg.portrait;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.opencv.FaceDetection;
import bg.portrait.util.HashImage;

public class MetaImage {

	private String srcImage;
	private JSONObject json;
	private BufferedImage image;
	private String hashMd5;
	public static File DATA_ROOT = new File("/IMAGES");

	public MetaImage(JSONObject json) {
		try {
			this.json = json;
			this.srcImage = processJson();
			this.image = processSrc(this.srcImage);
			this.hashMd5 = HashImage.getHashImage(image);
		} catch (Exception e) {
			System.err.println("MetaImageException "+e.getClass().getName()+"  "+e.getMessage());
		}
	}

	private BufferedImage processSrc(String urlStr) throws Exception {
		BufferedImage image = null;

		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "xxxxxx");
		image = ImageIO.read(connection.getInputStream());

		return image;
	}

	@Override
	public String toString() {
		return "MetaImage [srcImage=" + srcImage + ", image=" + image + "]+\n"+hashMd5;
	}

	private String processJson() {
		JSONObject pageMApJson = (JSONObject) json.get("pagemap");
		if (pageMApJson.has("cse_image")) {
			JSONArray cse_image = (JSONArray) pageMApJson.get("cse_image");
			JSONObject srcJson = (JSONObject) cse_image.get(0);
			String src = (String) srcJson.get("src");
			return src;
		}
		return null;
	}
	
	public boolean isValid() {
		return ! (this.hashMd5== null);
	}

	public void store() {
		try {
			if (isValid()) {
				File dir = getDirRoot();
				dir.mkdirs();
				String format ="jpg";
				File fImage = new File(dir,hashMd5+"_master."+format);
				ImageIO.write(image, format, fImage);
				FaceDetection.getInstance().processFile(fImage);
			}else {
				System.err.println("NoValid!!! ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private File getDirRoot() {
		File dir0 = new File(DATA_ROOT,""+this.hashMd5.charAt(0));
		File dir1 = new File(dir0,""+this.hashMd5.charAt(1));
		File dir2 = new File(dir1,""+this.hashMd5.charAt(2));
		File dir3 = new File(dir2,""+this.hashMd5.charAt(3));
		File dir = new File(dir3, this.hashMd5);
		return dir;
	}

	
}
