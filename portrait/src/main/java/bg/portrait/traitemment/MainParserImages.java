package bg.portrait.traitemment;

public class MainParserImages {

	public static void main(String[] args) {
		ParserImages parser = new ParserImages();
		parser.getListProcess().add(new ProcessCopyImage());
		parser.parse();
		System.out.println(parser.synthese());
	}

}
