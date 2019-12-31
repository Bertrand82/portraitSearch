package imageFInder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bg.util.GoogleSearchUtil;

class GoogleUtilTest {

	@Test
	void test() {
		//String ei ="Vq-9XczIJIunlwTk5rF";
		String ei ="qWwlUqD4MIrRrQfD9ICIDw";
	  GoogleSearchUtil.decodeEi(ei);
	}

}
