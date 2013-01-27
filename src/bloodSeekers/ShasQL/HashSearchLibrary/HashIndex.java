package bloodSeekers.ShasQL.HashSearchLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeSet;

import bloodSeekers.ShasQL.FileManager.FileManager;
import bloodSeekers.ShasQL.Hasher.Hasher;
import bloodSeekers.ShasQL.Utilities.TrimLibrary;

public class HashIndex {
	public static void main(String[] args) {
		FileManager.CreateFolders();
		ArrayList<File> files = FileManager.ListAllFiles(new File("root"));
		for (File file : files) {
			IndexFile(file);
		}
	}

	private static void IndexFile(File file) {
		Scanner sc;
		try {
			int totalChars = 0;
			sc = new Scanner(file);
			HashMap<Integer, TreeSet<Integer>> sectors = new HashMap<Integer, TreeSet<Integer>>();
			while (sc.hasNext()) {
				
				String line = sc.next();
				ArrayList<String> formatedWords = TrimLibrary
						.TrimAndFormat(line);
				for (String s : formatedWords) {
					int hashCode = Hasher.getHashCode(s);
					if (!sectors.containsKey(hashCode))
						sectors.put(hashCode, new TreeSet<Integer>());
					sectors.get(hashCode).add(
							totalChars / FileManager.BLOCK_SIZE);
					totalChars += s.length() + 1;
				}
				
				WriteInfo(file, sectors);
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void WriteInfo(File file,
			HashMap<Integer, TreeSet<Integer>> sectors)
			throws FileNotFoundException, IOException {

		for (Entry<Integer, TreeSet<Integer>> entry : sectors.entrySet()) {
			RandomAccessFile raf = new RandomAccessFile(new File("hashFiles/"
					+ Hasher.getParentDirectory(entry.getKey()) + "/"
					+ Hasher.getFileName(entry.getKey())), "rw");
			raf.seek(raf.length());
			raf.writeBytes(file.getPath());
			for (int sec : entry.getValue())
				raf.writeBytes(" " + Integer.toString(sec));
			raf.writeBytes("\n");
			raf.close();
		}
	}
}
