package bg.portrait.achi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import bg.opencv.FaceDetection;
import bg.portrait.util.HashImage;
import bg.portrait.util.UtilFile;

abstract public class MetaImage {
	private static final Logger logger = LoggerFactory.getLogger(MetaImage.class);

	private String srcImage;
	private JSONObject json;
	private BufferedImage image;
	private String hashMd5;
	
	public MetaImage(JSONObject json) {
		try {
			this.json = json;
			this.srcImage = extractSrcFromJson(json);
			this.image = processSrc(this.srcImage);
			this.hashMd5 = HashImage.getHashImage(image);
		} catch (Exception e) {
			logger.info("MetaImageException " + e.getClass().getName() + "  " + e.getMessage());
		}
	}

	private BufferedImage processSrc(String urlStr) throws Exception {
		if (urlStr == null) {
			return null;
		}
		BufferedImage image = null;

		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "xxxxxx");
		image = ImageIO.read(connection.getInputStream());

		return image;
	}

	@Override
	public String toString() {
		return "MetaImage [srcImage=" + srcImage + ", image=" + image + "]+\n" + hashMd5;
	}

	public abstract String extractSrcFromJson(JSONObject json);

	public boolean isValid() {
		return !(this.hashMd5 == null);
	}

	public void store() {
		try {
			if (isValid()) {
				File dir = getDirRoot();
				dir.mkdirs();
				String format = "jpg";
				File fImage = new File(dir, hashMd5 + "_master." + format);
				ImageIO.write(image, format, fImage);
				storeMetadata(dir);
				FaceDetection.getInstance().processFile(fImage);
			} else {
				System.err.println("NoValid!!! " + diagnostic());
			}
		} catch (IOException e) {
			logger.warn("Exception e : " + e.getMessage());
			;
		}

	}

	private String diagnostic() {
		String s = "";
		if (srcImage == null) {
			s += " No Image";
		}
		if (image == null) {
			s += " No Face";
		}
		return s;
	}

	private void storeMetadata(File dir) throws IOException {
		File dirMetaData = new File(dir, "META_DATA");
		dirMetaData.mkdirs();
		File fileJson = new File(dirMetaData, "metadata.json");
		JSONObject jsonMetadata = new JSONObject();
		jsonMetadata.accumulate("type", getType());
		jsonMetadata.accumulate("response", json);

		Files.write("" + jsonMetadata, fileJson, StandardCharsets.UTF_8);

	}

	abstract public String getType();

	private File getDirRoot() {
		return UtilFile.getDirRootFromHashMd5(this.hashMd5);
	}

}
