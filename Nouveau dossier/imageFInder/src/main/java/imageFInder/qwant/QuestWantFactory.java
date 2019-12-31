package imageFInder.qwant;

public class QuestWantFactory {

	public static QuestQwant createQuest() {
		QuestQwant qq = new QuestQwant();
		qq.setQ("portrait");
		return qq;
	}
}
