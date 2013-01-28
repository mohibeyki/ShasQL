package bloodSeekers.ShasQL.HashSearchLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import bloodSeekers.ShasQL.Database.DB;
import bloodSeekers.ShasQL.FileManager.BufferedFilesWriter;
import bloodSeekers.ShasQL.FileManager.FileManager;
import bloodSeekers.ShasQL.Hasher.Hasher;
import bloodSeekers.ShasQL.Utilities.TrimLibrary;

public class HashIndex {
	public static void indexAllFiles() throws Exception {
		ArrayList<File> files = FileManager.ListAllFiles(new File("root"));
		BufferedFilesWriter bufferedFilesWriter = BufferedFilesWriter
				.getInstance();
		for (File file : files)
			indexSingleFile(file.getPath());
		bufferedFilesWriter.flushAll();
	}

	public static void indexSingleFile(String fileAddress)
			throws FileNotFoundException, IOException, Exception {

		File file = new File(fileAddress);
		BufferedFilesWriter bufferedFilesWriter = BufferedFilesWriter
				.getInstance();

		if (!DB.getInstance().filesMap.containsKey(fileAddress))
			DB.getInstance().filesMap.put(fileAddress,
					DB.getInstance().maxIndex++);

		System.out.println("Processing : " + file.getPath());
		int index = DB.getInstance().filesMap.get(fileAddress);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		long totalChars = 0;
		TreeMap<Integer, Long> sectors = new TreeMap<Integer, Long>();
		String line = bufferedReader.readLine();

		while (line != null) {
			ArrayList<String> formatedWords = TrimLibrary.TrimAndFormat(line);
			for (String s : formatedWords) {
				int hashCode = Hasher.getHashCode(s);
				long sector = totalChars / FileManager.BLOCK_SIZE;
				if (!sectors.containsKey(hashCode))
					sectors.put(hashCode, -1l);
				if (sectors.get(hashCode) != sector) {
					String fileName = "hashFiles/"
							+ Hasher.getParentDirectory(hashCode) + "/"
							+ Hasher.getFileName(hashCode);
					bufferedFilesWriter.writeToFile(fileName, index + " "
							+ sector + "\n");
					sectors.put(hashCode, sector);
				}
				totalChars += s.length() + 1;
			}
			line = bufferedReader.readLine();
		}
		sectors.clear();
		bufferedReader.close();
	}
}