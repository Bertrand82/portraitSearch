package imageFInder.qwant;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.HttpsURLConnection;

public class CrawlerQwant {
	public CrawlerQwant() throws Exception{
		search();
	}

	public void search() throws Exception {
		String urlStr = getUrlstr();
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("user-agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Mobile Safari/537.36");

		InputStream in = conn.getInputStream();
		System.out.println("send request");
		// String s = UtilHttp.getStringFromStream(in);
		// System.out.println("----->>>"+s);
		File dirTemp = new File("TEMP");
		dirTemp.mkdirs();
		File fileTemp = new File(dirTemp, "REAQUEST_QWANT_" + System.currentTimeMillis() + ".html");
		Path path = Paths.get(fileTemp.toURI());
		Files.copy(in, path, REPLACE_EXISTING);
	}

	private String getUrlstr() {
		String urlStr = "https://api.qwant.com/api/search/images";
		urlStr += "q=portrait+xviiie+si%C3%A8cle";
		urlStr += "&t=images";
		urlStr += "&safesearch=1";
		urlStr += "&uiv=4";
		return urlStr;
	}

}
