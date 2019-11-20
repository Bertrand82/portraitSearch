package bg.portrait.serpwow;

import org.json.JSONObject;

import bg.portrait.achi.MetaImage;

public class MetaImageSerpwow extends MetaImage{

	public MetaImageSerpwow(JSONObject json) {
		super(json);
	}

	@Override
	public String extractSrcFromJson(JSONObject json) {
		System.out.println("" +json);
		String srcImage = json.getString("image");
		System.out.println(srcImage);
		return srcImage;
	}

	@Override
	public String getType() {
		return "Serpwow";
	}

}
