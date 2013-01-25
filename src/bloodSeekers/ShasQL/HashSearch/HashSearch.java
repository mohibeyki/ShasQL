package bloodSeekers.ShasQL.HashSearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import bloodSeekers.ShasQL.FileManager.FileManager;
import bloodSeekers.ShasQL.Utilities.TrimLibrary;

public class HashSearch {
	public static void main(String[] args) {
		Scanner sc;
		ArrayList<File> files = FileManager.ListAllFiles(new File("root"));
		for (File f : files) {
			try {
				sc = new Scanner(f);
				while (sc.hasNext()) {
					ArrayList<String> formatedWords = TrimLibrary.TrimAndFormat(sc.next());
					for (String s : formatedWords) {
						System.out.println(s);
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		// try {
		// sc = new Scanner(new File("text"));
		// while (sc.hasNext()) {
		// TrimLibrary.Trim(sc.next());
		// }
		// sc.close();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }

	}
}
