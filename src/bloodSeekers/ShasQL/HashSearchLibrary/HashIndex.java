package bloodSeekers.ShasQL.HashSearchLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import bloodSeekers.ShasQL.FileManager.BufferedFileOperator;
import bloodSeekers.ShasQL.FileManager.FileManager;
import bloodSeekers.ShasQL.Hasher.Hasher;
import bloodSeekers.ShasQL.Utilities.TrimLibrary;

public class HashIndex {
	public static void main(String[] args) throws Exception {
		FileManager.CreateFolders();
		ArrayList<File> files = FileManager.ListAllFiles(new File("root"));
		for (File file : files) {
			FileReader fr = new FileReader(file);
			long totalChars = 0;
			char[] buf = new char[FileManager.BLOCK_SIZE];
			while (fr.read(buf) > 0) {
				HashMap<Integer, TreeSet<Integer>> sectors = new HashMap<Integer, TreeSet<Integer>>();
				String line = new String(buf);
				ArrayList<String> formatedWords = TrimLibrary
						.TrimAndFormat(line);
				for (String s : formatedWords) {
					int hashCode = Hasher.getHashCode(s);
					if (!sectors.containsKey(hashCode))
						sectors.put(hashCode, new TreeSet<Integer>());
					sectors.get(hashCode).add(
							(int) (totalChars / FileManager.BLOCK_SIZE));
					totalChars += s.length() + 1;
				}
				WriteInfo(file, sectors);
				sectors.clear();
			}
			fr.close();
		}
	}

	private static void WriteInfo(File file,
			HashMap<Integer, TreeSet<Integer>> sectors)
			throws FileNotFoundException, IOException {
		BufferedFileOperator bufferedFileOperator = BufferedFileOperator
				.getInstance();

		for (Entry<Integer, TreeSet<Integer>> entry : sectors.entrySet()) {
			String fileName = "hashFiles/"
					+ Hasher.getParentDirectory(entry.getKey()) + "/"
					+ Hasher.getFileName(entry.getKey());
			bufferedFileOperator.writeToFile(fileName, file.getPath());
			for (int sec : entry.getValue())
				bufferedFileOperator.writeToFile(fileName,
						" " + Integer.toString(sec));
			bufferedFileOperator.writeToFile(fileName, "\n");
		}
	}
}