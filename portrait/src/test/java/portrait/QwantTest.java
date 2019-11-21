package portrait;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import bg.portrait.qwant.MetaImageQwant;

public class QwantTest {

	@Test
	void test1() throws Exception{		
		try {
			String s = readRessource("qwant.json");
			System.out.println(s);
			JSONObject jsonObject = new JSONObject(s);
			String status = jsonObject.getString("status");
			JSONObject data = jsonObject.getJSONObject("data");
			JSONObject query = data.getJSONObject("query");
			int offset = query.getInt("offset");
			System.out.println("offset " + offset);
			JSONObject results = data.getJSONObject("result");
			JSONArray items = results.getJSONArray("items");
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				System.out.println("item.src : " + item.getString("media"));
				MetaImageQwant miq = new MetaImageQwant(item);
				miq.store();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	@Test
	void test0() throws Exception{		
		try {
			String s = readRessource("qwant.json");
			System.out.println(s);
			JSONObject jsonObject = new JSONObject(s);
			String status = jsonObject.getString("status");
			JSONObject data = jsonObject.getJSONObject("data");
			JSONObject query = data.getJSONObject("query");
			int offset = query.getInt("offset");
			System.out.println("offset " + offset);
			JSONObject results = data.getJSONObject("result");
			JSONArray items = results.getJSONArray("items");
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				System.out.println("item.src : " + item.getString("media"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public static String readRessource(String fileName) throws Exception{			
			Path path = Paths.get(QwantTest.class.getResource("/"+fileName).toURI());
			byte[] bb = Files.readAllBytes(path);
			String s = new String(bb, "UTF-8");
			return s;
	}
	
	public static String readFile(File file) throws Exception {
		byte[] bb = Files.readAllBytes(file.toPath());
		String s = new String(bb, "UTF-8");
		return s;
	}

}
