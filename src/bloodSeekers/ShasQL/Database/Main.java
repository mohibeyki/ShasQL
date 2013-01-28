package bloodSeekers.ShasQL.Database;

public class Main {

	public static void main(String[] args) {
		DB db = DB.getInstance();
		db.insert("root/text");
	}

}
