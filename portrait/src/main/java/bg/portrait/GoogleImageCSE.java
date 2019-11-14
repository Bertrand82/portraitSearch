package bg.portrait;

import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.custom.CXGooogle;

public class GoogleImageCSE {

	private static String URL0 = "https://www.googleapis.com/customsearch/v1?";

	Client client = ClientBuilder.newClient();

	public void search() {
		search(1);
		search(11);
		search(21);

	}
	
	public void search(int i) {
		try {
			
			String url = getUrlIni(i);
			WebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			System.out.println("search  i:"+i +"  status : "+response.getStatus());
			String responseAsString = response.readEntity(String.class);
			if (response.getStatus() == 200) {
				JSONObject json = new JSONObject(responseAsString);				
				processResponse(json);
				int nextPage  = getNextPage(json);
				searchNextPage(nextPage);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchNextPage(int i) {
		
		
	}

	private int getNextPage(JSONObject json) {
		final JSONObject queriesJson = (JSONObject) json.get("queries");
		
		JSONArray nextPageJsonAray = (JSONArray) queriesJson.get("nextPage");
		
		JSONObject nextPageJson = (JSONObject) nextPageJsonAray.get(0);
		System.out.println("---  :: " + nextPageJson);
		int startIndex = (Integer) nextPageJson.get("startIndex");
		String totalResults = ""+nextPageJson.get("totalResults");
		return startIndex;
	}

	private void processResponse(JSONObject json) {
		System.out.println("Bingo");
		System.out.println("json:" + json);
		
		JSONArray itemsJsonAray = (JSONArray) json.get("items");

		itemsJsonAray.forEach(e -> processItem(e));
	}


	private void processItem(Object e) {
		JSONObject json = (JSONObject) e;
		JSONObject pageMApJson = (JSONObject) json.get("pagemap");
		if (pageMApJson.has("cse_image")) {
			JSONArray cse_image = (JSONArray) pageMApJson.get("cse_image");
			JSONObject srcJson = (JSONObject) cse_image.get(0);
			String src = "" + srcJson.get("src");
			System.out.println("cse_image " + src);
		} else {
			System.out.println(" No SRc!!!!");
		}
		for (String key : pageMApJson.keySet()) {
			// System.out.println("----------- kk :"+key);
		}

	}

	private static String getUrlIni(int startIndex) throws Exception {
		String s = URL0;
		s += "cx=" + URLEncoder.encode(CXGooogle.CX);
		s += "&key=AIzaSyDTF5H2ftyvGE7f1axSRvVnFepmbYouINI";
		s += "&q=" + URLEncoder.encode("portrait xviiième", "utf-8");
		s += "&start=" + startIndex;
		// s += "&count=100";
		System.out.println(s);
		return s;
	}

}
