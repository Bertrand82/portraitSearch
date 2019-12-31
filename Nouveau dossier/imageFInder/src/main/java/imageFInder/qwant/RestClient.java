package imageFInder.qwant;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {
	private static final String REST_URI = "https://api.qwant.com/api/search/images";
	private Client client = ClientBuilder.newClient();

	public void getImages(QuestQwant qq) {
		try {
			System.out.println(" start :"+qq);
			System.out.println(" REST_URI :"+REST_URI);
			WebTarget webTarget = client.target(REST_URI);
			
			Invocation.Builder inv = webTarget.request(MediaType.APPLICATION_JSON).					
					accept(MediaType.APPLICATION_JSON)					
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36" );;
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonStr = objectMapper.writeValueAsString(qq);
			System.out.println("jsonStr :"+jsonStr);
			Entity<QuestQwant> entity = Entity.entity(qq,MediaType.APPLICATION_JSON);
			Entity<String> entity2 = Entity.entity(jsonStr,MediaType.APPLICATION_JSON);
			
			System.out.println(" start 21 "+entity.getVariant());
			System.out.println(" start 22 "+entity2.getVariant());
			Response response =inv.post(entity2);
			System.out.println("output status "+response.getStatus());
			System.out.println("status :"+ response.getStatus());
			if (response.getStatus() == 200) {
				Object output = response.getEntity();
				System.out.println("output "+output);
			}else {
				System.out.println(""+ response.getStatusInfo());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}