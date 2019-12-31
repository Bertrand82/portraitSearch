package imageFInder.bing;

import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchAPI;
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImageObject;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImagesModel;

public class CrawlerBing {
	static String Cle_1 = "656f21e29a684d53a05afb0c46c24aac";

	static String Cle_2 = "8b6d6db7690f48759b58309bbce6d532";
	final String subscriptionKey = Cle_2;

	public void search() {
		final String subscriptionKey = "COPY_YOUR_KEY_HERE";
		String searchTerm = "portrait xviiieme";
		// Image search client
		BingImageSearchAPI client = BingImageSearchManager.authenticate(subscriptionKey);
		ImagesModel imageResults = client.bingImages().search().withQuery(searchTerm).withMarket("fr").execute();
		processImagesResults(imageResults);
	}

	private void processImagesResults(ImagesModel imageResults) {
		if (imageResults == null) {
			System.out.println("imageResults is null!!!!");
		} else {
			int i = 0;
			System.out.println("images size :" + imageResults.value().size());
			for (ImageObject io : imageResults.value()) {
				System.out.println(i++ + "-------------------------- name :" + io.name());
				System.out.println("thumbnailUrl :" + io.thumbnailUrl());
				System.out.println("contentUrl :" + io.contentUrl());
				System.out.println("Description :" + io.description());
				System.out.println("alternateName :" + io.alternateName());
			}
			// Image results
		}

	}
}
