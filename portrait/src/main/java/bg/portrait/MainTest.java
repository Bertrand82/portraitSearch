package bg.portrait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.FileAppender;

public class MainTest {

	private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

	public static void main(String[] args) {
		
		System.out.println("GoogleImageCSE start");
		logger.info("------------------------------ start");
		//GoogleImageCSE g = new GoogleImageCSE();
		//g.search();
		System.out.println("GoogleImageCSE done !!!!");
		System.out.println("bye bye");
	}

}
