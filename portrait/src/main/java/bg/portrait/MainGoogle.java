package bg.portrait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.FileAppender;

public class MainGoogle {

	private static final Logger logger = LoggerFactory.getLogger(MainGoogle.class);

	public static void main(String[] args) {
		
		System.out.println("GoogleImageCSE start");
		logger.info("------------------------------ start");
		GoogleImageCSE g = new GoogleImageCSE();
		g.search();
		System.out.println("GoogleImageCSE done !!!!");
	}

}
