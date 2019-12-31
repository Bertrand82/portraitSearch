package imageFInder.google;

import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientAsyncExecutor;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class GoogleImageCSE {

	private static String URL0 = "https://www.googleapis.com/customsearch/v1?";
	private static String cx = "001202559208537819548:inl72ma8jwu";
	Client client = ClientBuilder.newClient();
	public void search() {
		search(1);
		search(11);
		search(21);

	}
	
	public void search(int i) {
		try {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  i:"+i);
			String url = getUrlIni(i);
			WebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();

			System.out.println(" status " + response.getStatus());
			//System.out.println(" Entity " + response.getEntity());
			String responseAsString = response.readEntity(String.class);
			//System.out.println(" Entity ::\n" + responseAsString);
			if (response.getStatus() == 200) {
				JSONObject json = new JSONObject(responseAsString);				
				processResponse(json);
				JSONObject nextPageJson  = processNextPage(json);
				searchNextPage(nextPageJson);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchNextPage(JSONObject nextPageJson) {
		System.out.println("searchNextPage ");
		WebTarget webTarget = client.target(URL0);
		//Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON).;
		
	}

	private JSONObject processNextPage(JSONObject json) {
		final JSONObject queriesJson = (JSONObject) json.get("queries");
		for (String key : queriesJson.keySet()) {
			//System.out.println("-----------queriesJson  key :" + key);
		}
		JSONArray nextPageJsonAray = (JSONArray) queriesJson.get("nextPage");
		JSONObject nextPageJson = (JSONObject) nextPageJsonAray.get(0);
		System.out.println("---  :: " + nextPageJson);
		return nextPageJson;
	}

	private void processResponse(JSONObject json) {
		System.out.println("Bingo");
		System.out.println("json:" + json);
		for (String key : json.keySet()) {
			//System.out.println("----------- key :" + key);
		}
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
		s += "cx=" + URLEncoder.encode(cx);
		s += "&key=AIzaSyDTF5H2ftyvGE7f1axSRvVnFepmbYouINI";
		s += "&q=" + URLEncoder.encode("portrait xviiième", "utf-8");
		s += "&start=" + startIndex;
		// s += "&count=100";
		System.out.println(s);
		return s;
	}

}
