package bg.portrait.qwant;

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

public class QWant {
	private static final Logger logger = LoggerFactory.getLogger(QWant.class);
	private static final Logger loggerQWant = LoggerFactory.getLogger("QWant");

	
	Client client = ClientBuilder.newClient();

	public void search() {
		int nextPage = 1;
		int i =0;
		while ((nextPage >0) && (i <=20)) {
			nextPage = searchNextPage(nextPage);
			i++;
		}
		
		System.err.println("Qwant search done : nextPage:" + nextPage+"  nImage :"+nbImages+"  nextPage_Z_1 :"+nextPage_Z_1);
		
	}
    private int count =50;
	public int search(int offset) {
		int nextPage = -2;
		String responseAsString = null;
		try {
			logger.debug("search offset :" + offset);
			String url = getUrlIni_V0(offset,count);
			WebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			String user_agent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Mobile Safari/537.36";
			invocationBuilder.header("user-agent", user_agent);
			Response response = invocationBuilder.get();
			loggerQWant.info("search  offset :" + offset + "  status : " + response.getStatus()+"  url : "+url);
			responseAsString = response.readEntity(String.class);
			loggerQWant.info("response :"+responseAsString);
			if (response.getStatus() == 200) {
				JSONObject jsonObject = new JSONObject(responseAsString);
				String status = ""+jsonObject.getString("status");
				if (status.equals("error")) {
					System.err.println("Status Error !!");
					return -3;
				}else {
					nextPage = nextPage_Z_1+count;
				}
				JSONObject data = jsonObject.getJSONObject("data");
				JSONObject query = data.getJSONObject("query");
				int offsetRead = query.getInt("offset");
				System.out.println("offsetRead " + offsetRead);
				JSONObject results = data.getJSONObject("result");
				JSONArray items = results.getJSONArray("items");
				if (items.length()==0) {
					System.err.println("No items !!");
					loggerQWant.info("No item");
					
					return -2;
				}
				for (int ii = 0; ii < items.length(); ii++) {
					JSONObject item = items.getJSONObject(ii);
					System.out.println("item.src : " + item.getString("media"));
					MetaImageQwant miq = new MetaImageQwant(item);
					miq.store();
				}
			}

		} catch (Exception e) {
			logger.error("Excep "+e.getClass()+"   "+e.getMessage()+"   "+responseAsString);;
		}
		
		return nextPage;
	}
	private int nextPage_Z_1=0;
	
	private int searchNextPage(int ii) {
		logger.info("Start New Research qwant :" + ii+"  nImage "+nbImages+"  nbImagesInvalides:"+nbImagesInvalides+"  nextPage_Z_1 :"+nextPage_Z_1);
		nextPage_Z_1=ii;
		int next =0;
		if (ii > 0) {
			next =search(ii);
		} else {
			logger.info("No more page Qwant!!! ");
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
		MetaImage metaImage = new MetaImageQwant(json);
		logger.info(""+metaImage);
		metaImage.store();
		if (metaImage.isValid()) {
			nbImages++;
		}else {
			nbImagesInvalides++;
		}
	}
	// https://api.qwant.com/api/search/images?count=50&q=portrait%20xviiieme&t=images&safesearch=1&uiv=4
	private static String URL_V0 ="https://api.qwant.com/api/search/images?";
	private static String getUrlIni_V0(int startIndex,int count) throws Exception {
		String s = URL_V0;
		s += "count="+count;
		s += "&q=" + URLEncoder.encode("portrait xviiième", "utf-8");
		//s += "&start=" + startIndex;
		s += "&t=image";
		s+="&safesearch=1&uiv=4";
		s+="&offset="+startIndex;
		return s;
	}
	
}
