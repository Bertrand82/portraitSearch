package imageFInder;

import java.io.File;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseRequestResponse {
	
	
	ParseRequestResponse(File file) throws Exception{	
		parseFile(file);
	}
	
	public void parseFile(File file) throws Exception{
		System.out.println("File "+file.exists());
		Document document= Jsoup.parse(file, "UTF-8", "http://example.com/");
		Elements elements_1 =document.getElementsByTag("a");
		Elements elements_2 =document.getElementsByClass("VFACy");
		Elements elements_3 =document.getElementsByTag("img");
		Elements elements_4 =document.select("div[role=link]");
		System.out.println("size : "+elements_2.size());
		elements_4.forEach(el -> process(el));
		System.out.println("size 1 a: "+elements_1.size());
		System.out.println("size 2 class: "+elements_2.size());
		System.out.println("size 3 img : "+elements_3.size());
		System.out.println("size 4 div : "+elements_4.size());
	
	}

	private void process(Element ei) {
		String text1="";
		String text2="";
		String href = "";
		String data_src="";
		Elements divText_1 = ei.select("div[class=WGvvNb]");
		text1=divText_1.text();
		Elements divText_2 = ei.select("div[class=fxgdke]");
		text2=divText_2.text();
		Elements as = ei.select("a[href]");
		if (as.size() >0) {
			Element a = as.get(0);
			href= a.attr("href");
		}
		Elements images =ei.getElementsByTag("img");
		if (images.size() >0) {
			Element image = images.get(0);
			data_src=image.attr("data-src");
		}
		System.out.println(" ::::: "+text1+" :::: "+text2 );
		System.out.println("  href "+href);
		System.out.println("  data-src "+data_src);
		
		
	}
	
	
	
}
