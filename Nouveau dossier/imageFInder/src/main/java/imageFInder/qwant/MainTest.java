package imageFInder.qwant;

public class MainTest {

	public static void main(String[] args) {
		RestClient restClient = new RestClient();
		QuestQwant qq = QuestWantFactory.createQuest();
		restClient.getImages(qq);
		System.out.println("done ____________");
	}

}
