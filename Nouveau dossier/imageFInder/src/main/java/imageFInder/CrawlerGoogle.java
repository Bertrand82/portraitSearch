package imageFInder;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.HttpsURLConnection;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import bg.util.UtilHttp;
import bg.util.UtilString;

public class CrawlerGoogle {
	static int ID_Stat = 0;
	String urlStr_1 = "https://www.google.com/search?q=portrait+xviiie+si%C3%A8cle&rlz=1C1CHBF_frFR787FR787&oq=portrait+xvii&aqs=chrome.2.69i59j69i57j0l4.16723j0j7&sourceid=chrome&ie=UTF-8";
	String urlStr_2 = "https://www.google.com/search?q=portrait+xviiie+si%C3%A8cle&tbm=isch&ved=2ahUKEwjMy8zl-cvlAhWL04UKHWRzDA0Q2-cCegQIABAC&oq=portrait+xviiie+si%C3%A8cle&gs_l=mobile-gws-wiz-img.12...0.0..2795578...0.0..0.0.0.......0.mAIuo3n6MJ4&ei=Vq-9XczIJIunlwTk5rFo&bih=374&biw=853";

	CrawlerGoogle() throws Exception {
		for (int i=0; i<3;i++) {
			search(i);
		}
	}

	public void search(int i) throws Exception {
		String urlStr = getUrlstr(i);
		System.out.println(urlStr);
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		// user-agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Mobile
		// Safari/537.36
		conn.setRequestProperty("user-agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Mobile Safari/537.36");

		InputStream in =null;
		try {
			in = conn.getInputStream();
			System.out.println("send request");
		} catch (Exception e) {
			InputStream err =conn.getErrorStream();
			System.err.println("err : "+UtilString.toString(err));
			e.printStackTrace();
		}
		// String s = UtilHttp.getStringFromStream(in);
		// System.out.println("----->>>"+s);
		File dirTemp = new File("TEMP");
		dirTemp.mkdirs();
		File fileTemp = new File(dirTemp, "REAQUEST_"+ID_Stat+++"_" + System.currentTimeMillis() + ".html");
		Path path = Paths.get(fileTemp.toURI());
		Files.copy(in, path, REPLACE_EXISTING);
		System.out.println("done :"+path.getFileName());
		ParseRequestResponse parsed = new ParseRequestResponse(path.toFile());
	}

	/**
	 * voir https://serpapi.com/search-api
	 * 
	 * ijn Optional
	 * 
	 * Parameter defines the page number for Google Images. There are 100 images per
	 * page. This parameter is equivalent to start (offset) = ijn * 100. This
	 * parameter works only for Google Images (set tbm to isch).
	 * 
	 * 
	 * ei=Nh3FXbG-O8-UgQaO76uYBw&yv=3
&tbm=isch
&q=msa
&vet=10ahUKEwixsaDIj9rlAhVPSsAKHY73CnMQuT0ISygB.Nh3FXbG-O8-UgQaO76uYBw.i&
ved=0ahUKEwixsaDIj9rlAhVPSsAKHY73CnMQuT0ISygB&ijn=1
          &start=100
&asearch=ichunk
&async=_id:rg_s,_pms:s,_jsfs:Ffpdje,_fmt:pc
&safe=active
&ssui=on
	 * @return
	 */
	public static  String getUrlstr(int i) {
		String urlStr = "https://www.google.com/search?";
		urlStr += "q=portrait+xviiie+si%C3%A8cle";
		urlStr += "&tbm=isch"; // Google image
		urlStr += "&ved=2ahUKEwjMy8zl-cvlAhWL04UKHWRzDA0Q2-cCegQIABAC";// apport avec algo de recherche
		urlStr += "&oq=portrait+xviiie+si%C3%A8cle";
		urlStr += "&gs_l=mobile-gws-wiz-img.12...0.0..2795578...0.0..0.0.0.......0.mAIuo3n6MJ4";// location
		urlStr += "&ei=Vq-9XczIJIunlwTk5rF"; // time stamp
		urlStr += "&ijn="+i;
		urlStr += "&start="+(i*100);
		if(i > 0) {
		urlStr += "&asearch=ichunk";
		urlStr += "&ssui=on";
		}
		urlStr += "&bih=374";// height of the browser in pixels
		urlStr += "&biw=853";// width of the browser in pixels
		return urlStr;
	}
}
