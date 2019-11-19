package portrait;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.io.Files;

class SerpwowTest {
	 String text="";
	@Test
	void test() throws Exception{
		assertTrue(true);
		Path path = Paths.get(this.getClass().getResource("/serpwow.json").toURI());
		
		List<String> lines  =Files.readLines( path.toFile() , Charset.defaultCharset());
		lines.stream().forEach(e->text+=e+"\n");
		System.out.println(text);
	}

}
