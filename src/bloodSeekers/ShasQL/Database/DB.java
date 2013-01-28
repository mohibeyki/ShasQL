package bloodSeekers.ShasQL.Database;

import java.util.HashMap;

import bloodSeekers.ShasQL.BPTree.Main;
import bloodSeekers.ShasQL.FileManager.FileManager;
import bloodSeekers.ShasQL.HashSearchLibrary.HashIndex;

public class DB implements Database {

	public HashMap<String, Integer> filesMap;
	public int maxIndex;

	private static DB instance;

	public static DB getInstance() {
		if (instance == null)
			instance = new DB();
		return instance;
	}

	private DB() {
		filesMap = new HashMap<String, Integer>();
		maxIndex = 0;
		FileManager.CreateFolders();
	}

	@Override
	public void insert(String fileName) {
		try {
			HashIndex.indexSingleFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void find(String input) {

	}

	@Override
	public void delete(String input) {

	}

	@Override
	public void printBPlusTree() {
		Main.Root().printSorted();
	}

	@Override
	public void printSplayTree() {

	}

}
