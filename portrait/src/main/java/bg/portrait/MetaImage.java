package bg.portrait;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.portrait.util.HashImage;

public class MetaImage {

	private String srcImage;
	private JSONObject json;
	private BufferedImage image;
	private String hashMd5;

	public MetaImage(JSONObject json) {
		try {
			this.json = json;
			this.srcImage = processJson();
			this.image = processSrc(this.srcImage);
			this.hashMd5 = HashImage.getHashImage(image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
