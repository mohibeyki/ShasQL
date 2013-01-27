package bloodSeekers.ShasQL.BPTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import bloodSeekers.ShasQL.FileManager.FileManager;

public class Main {

	public static int count = 0;
	public static BPNode root;
	public static final int NODESIZE = 1024;
	public static RandomAccessFile leafRAF;
	public static int partitionIndex = 0;

	public static BPNode Root() {
		if (root == null) {
			root = new BPNode();
			root.children.add(new BPLeaf(partitionIndex));
			try {
				leafRAF = new RandomAccessFile(new File("Leaves"), "rw");
				// resets the file
				if (leafRAF.length() != FileManager.BLOCK_SIZE * 5 * 1024) {
					leafRAF.close();
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							"Leaves"));
					char[] buf = new char[FileManager.BLOCK_SIZE];
					for (int i = 0; i < 1024 * 5; ++i)
						bw.write(buf);
					bw.close();
					leafRAF = new RandomAccessFile("Leaves", "rw");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return root;
	}

	public static void main(String[] args) throws Exception {
		int n = 100;
		count = 1;

		File db = new File("db.ShasQL");
		if (db.exists()) {

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					"db.ShasQL"));
			root = (BPNode) ois.readObject();
			ois.close();
			leafRAF = new RandomAccessFile(new File("Leaves"), "rw");
			Root().printSorted();
		}
		else {
			for (int i = 0; i < n; i++) {
				int k = (int) (Math.random() * 10000);
				Root().add(k);
			}
			Root().printSorted();
			System.out.println();
			ObjectOutputStream ow = new ObjectOutputStream(
					new FileOutputStream("db.ShasQL"));
			ow.writeObject(root);
			ow.close();
		}
	}
}
