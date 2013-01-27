package bloodSeekers.ShasQL.HashSearchLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.StringTokenizer;

import bloodSeekers.ShasQL.FileManager.FileManager;
import bloodSeekers.ShasQL.Hasher.Hasher;

public class HashSearch {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		String key = sc.next();
		FindWord(sc, key);
		sc.close();
	}

	private static void FindWord(Scanner sc, String key)
			throws FileNotFoundException {
		int hashCode = Hasher.getHashCode(key);
		System.out.println(Hasher.getHashCode(key));
		File file = new File("hashFiles/" + Hasher.getParentDirectory(hashCode)
				+ "/" + Hasher.getFileName(hashCode));
		if (!file.exists()) {
			sc.close();
			System.out.println("Word not found!");
			return;
		}
		Scanner fileReader = new Scanner(file);
		while (fileReader.hasNext()) {
			StringTokenizer st = new StringTokenizer(fileReader.nextLine());
			String fileName = st.nextToken();
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			System.out.println(fileName);
			try {
				while (st.hasMoreTokens()) {
					byte[] b = new byte[2 * FileManager.BLOCK_SIZE];
					long part = Long.parseLong(st.nextToken());
					System.out.println("Part : " + part);
					raf.seek(part * FileManager.BLOCK_SIZE);
					raf.read(b);
					String s = new String(b).toLowerCase();
					int index = s.indexOf(key.toLowerCase());
					while (index != -1) {
						System.out.println("Found it in file : " + fileName
								+ " @ "
								+ (FileManager.BLOCK_SIZE * part + index));
						System.out.println(s.substring((index < 30 ? 0
								: index - 30), Math.min(index + 40,
								FileManager.BLOCK_SIZE * 2)));
						index = s.indexOf(key.toLowerCase(), index + 1);
						if (index != -1)
							System.out
									.println("There was another one in this sector!");
					}
				}
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fileReader.close();
	}
}
