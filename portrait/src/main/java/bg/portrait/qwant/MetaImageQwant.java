package bg.portrait.qwant;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.portrait.achi.MetaImage;

public class MetaImageQwant extends MetaImage {

	public MetaImageQwant(JSONObject json) {
		super(json);
	}

	@Override
	public String extractSrcFromJson(JSONObject json) {
		String src = json.getString("media");		
		return src;
	}
	@Override
	public String getType() {
		return "QWant";
	}
}
