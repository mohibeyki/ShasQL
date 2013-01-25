package bloodSeekers.ShasQL.BPTree;

public class Main {

	public static int count = 0;

	public static void main(String[] args) {

		// FileManager fm = new FileManager();
		// try {
		// System.out.println(fm.Partition(new
		// File("/home/mohi/Documents/text")));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		int n = 30;
		count = 1;
		for (int i = 0; i < n; i++)
			BPNode.Root().add(i + 1);

		// BPNode.Root().SerilizeMe();
		// BPNode.Root().Hibernate();
		// BPNode.Root().SaveJSon();

		// BPNode.Root().LoadJSon();

		// BPNode.Root().Hibernate();

		// System.out.println(BPNode.Root().toString());

		// System.out.println();
		// BPNode.Root().printsorted();
		// System.out.println(count);
	}

}
