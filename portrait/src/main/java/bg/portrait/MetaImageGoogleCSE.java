package bg.portrait;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.portrait.achi.MetaImage;

public class MetaImageGoogleCSE extends MetaImage {

	public MetaImageGoogleCSE(JSONObject json) {
		super(json);
	}

	@Override
	public String extractSrcFromJson(JSONObject json) {
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
