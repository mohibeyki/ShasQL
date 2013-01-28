package bloodSeekers.ShasQL.Database;

public interface Database {
	public void insert(String fileName);

	public void find(String input);

	public void delete(String input);

	public void printBPlusTree();

	public void printSplayTree();
}