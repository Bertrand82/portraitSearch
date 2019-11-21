package bg.portrait.qwant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.FileAppender;

public class MainQWant {

	private static final Logger logger = LoggerFactory.getLogger(MainQWant.class);

	public static void main(String[] args) {
		
		System.out.println("MainQWant start");
		logger.info("------------------------------ start");
		QWant g = new QWant();
		g.search();
		System.out.println("MainQWant done !!!!");
	}

}
