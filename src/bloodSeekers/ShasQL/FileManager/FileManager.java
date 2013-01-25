package bloodSeekers.ShasQL.FileManager;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

	public static final int BLOCK_SIZE = 4 * 1024;

	public static long Partition(File file) {
		return file.length() / BLOCK_SIZE;
	}

	public static ArrayList<File> ListAllFiles(File file) {
		ArrayList<File> tmp = new ArrayList<File>();
		if (file.isFile())
			tmp.add(file);
		else if (file.isDirectory()) {
			File[] listOfFiles = file.listFiles();
			if (listOfFiles != null)
				for (int i = 0; i < listOfFiles.length; i++)
					tmp.addAll(ListAllFiles(listOfFiles[i]));
		}
		return tmp;
	}
}
