package bg.portrait.googleCSE;

import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bg.custom.CXGooogle;
import bg.portrait.achi.MetaImage;

public class GoogleImageCSE {
	private static final Logger logger = LoggerFactory.getLogger(GoogleImageCSE.class);
	private static final Logger loggerGoogleCSE = LoggerFactory.getLogger("GoogleCSE");

	private static String URL0 = "https://www.googleapis.com/customsearch/v1?";

	Client client = ClientBuilder.newClient();

	public void search() {
		int nextPage = 1;
		while (nextPage >0) {
			nextPage = searchNextPage(nextPage);
		}
		
		System.err.println("search done : nextPage:" + nextPage+"  nImage :"+nbImages+"  nextPage_Z_1 :"+nextPage_Z_1);
		
	}

	public int search(int i) {
		int nextPage = -2;
		try {
			logger.debug("search " + i);
			String url = getUrlIni(i);
			WebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			loggerGoogleCSE.info("search  i:" + i + "  status : " + response.getStatus()+"  url : "+url);
			String responseAsString = response.readEntity(String.class);
			if (response.getStatus() == 200) {
				JSONObject json = new JSONObject(responseAsString);
				processResponse(json);
				nextPage = getNextPage(json);
				
			}

		} catch (Exception e) {
			logger.error("Excep "+e.getClass()+"   "+e.getMessage());;
		}
		return nextPage;
	}
	private int nextPage_Z_1=0;
	private int searchNextPage(int ii) {
		logger.info("Start New Research :" + ii+"  nImage "+nbImages+"  nbImagesInvalides:"+nbImagesInvalides+"  nextPage_Z_1 :"+nextPage_Z_1);
		nextPage_Z_1=ii;
		int next =0;
		if (ii > 0) {
			next =search(ii);
		} else {
			logger.info("No more page !!! ");
		}
		return next;
	}

	private int getNextPage(JSONObject json) {
		try {
			final JSONObject queriesJson = (JSONObject) json.get("queries");

			JSONArray nextPageJsonAray = (JSONArray) queriesJson.get("nextPage");

			JSONObject nextPageJson = (JSONObject) nextPageJsonAray.get(0);
			logger.debug("---  :: " + nextPageJson);
			int startIndex = (Integer) nextPageJson.get("startIndex");
			String totalResults = "" + nextPageJson.get("totalResults");
			return startIndex;
		} catch (JSONException e) {
			logger.error("next Page Exception " + json,e);			
			return -1;
		}
	}

	public void processResponse(JSONObject json) {

		logger.info("json:" + json);
		JSONArray itemsJsonAray = (JSONArray) json.get("items");
		itemsJsonAray.forEach(e -> processItem(e));
		logger.info("nbImages:" + nbImages+"  nbImagesInvalides :"+nbImagesInvalides+"   length :"+itemsJsonAray.length());
	}
	
	int nbImages =0;
	int nbImagesInvalides =0;
	
	private void processItem(Object e) {
		JSONObject json = (JSONObject) e;
		MetaImage metaImage = new MetaImageGoogleCSE(json);
		logger.info(""+metaImage);
		metaImage.store();
		if (metaImage.isValid()) {
			nbImages++;
		}else {
			nbImagesInvalides++;
		}
	}

	private static String getUrlIni(int startIndex) throws Exception {
		String s = URL0;
		s += "cx=" + URLEncoder.encode(CXGooogle.CX);
		s += "&key=AIzaSyDTF5H2ftyvGE7f1axSRvVnFepmbYouINI";
		s += "&q=" + URLEncoder.encode("portrait xviiième", "utf-8");
		s += "&start=" + startIndex;
		return s;
	}

}
