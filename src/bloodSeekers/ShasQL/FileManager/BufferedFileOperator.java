package bloodSeekers.ShasQL.FileManager;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class BufferedFileOperator {

	private static BufferedFileOperator instance;
	private HashMap<String, Queue<Character>> fileBuffers;
	private Queue<Character> readBuffer;
	private String outputBuffer;
	private FileReader fileInputStream;
	private boolean EOF;
	private File input;
	private BufferedReader br;

	private BufferedFileOperator() {
		fileBuffers = new HashMap<String, Queue<Character>>();
		readBuffer = new LinkedList<Character>();
	}

	public static BufferedFileOperator getInstance() {
		if (instance == null)
			instance = new BufferedFileOperator();
		return instance;
	}

	public boolean open(File file) throws Exception {
		input = file;
		br = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
		if (!file.exists())
			throw new Exception("File not found");
		if (fileBuffers.get(file.getPath()) != null
				&& fileBuffers.get(file).size() > 0) {
			flush(file);
		}
		try {
			fileInputStream = new FileReader(file);
			if (fileInputStream != null)
				return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void close() {
		try {
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void flush(File file) {
		if (fileBuffers.get(file.getPath()) != null
				&& fileBuffers.get(file.getPath()).size() > 0) {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(file,
						"rw");
				String buf = "";
				while (fileBuffers.get(file.getPath()).size() > 0)
					buf += fileBuffers.get(file.getPath()).poll();
				randomAccessFile.seek(randomAccessFile.length());
				randomAccessFile.writeBytes(buf);
				randomAccessFile.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean hasNext() {
		return !EOF;
	}

	public String next() {
		outputBuffer = "";
		if (EOF)
			return "";
		while (true) {
			while (EOF || readBuffer.size() > 0) {
				if (readBuffer.size() == 0)
					return outputBuffer;
				char c = readBuffer.poll();
				if (!EOF && c != ' ' && c != '\n')
					outputBuffer += c;
				else
					return outputBuffer;
			}
			char[] buf = new char[FileManager.BLOCK_SIZE];
			int size;
			try {
				size = fileInputStream.read(buf);
				if (size < 0)
					EOF = true;
				for (int i = 0; i < size; ++i)
					readBuffer.add(buf[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String nextLine() throws IOException {
		return br.readLine();
	}

	public void flushAll() {
		for (Entry<String, Queue<Character>> entry : fileBuffers.entrySet()) {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(
						new File(entry.getKey()), "rw");
				String tmp = "";
				while (entry.getValue().size() > 0)
					tmp += entry.getValue().poll();
				randomAccessFile.writeBytes(tmp);
				randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void writeToFile(String fileName, String data) {
		if (!fileBuffers.containsKey(fileName))
			fileBuffers.put(fileName, new LinkedList<Character>());

		for (char c : data.toCharArray())
			fileBuffers.get(fileName).offer(c);

		if (fileBuffers.get(fileName).size() > FileManager.BLOCK_SIZE) {
			RandomAccessFile randomAccessFile;
			try {
				randomAccessFile = new RandomAccessFile(new File(fileName),
						"rw");
				randomAccessFile.seek(randomAccessFile.length());
				Queue<Character> tmp = fileBuffers.get(fileName);
				while (tmp.size() > FileManager.BLOCK_SIZE) {
					String buf = "";
					for (int i = 0; i < FileManager.BLOCK_SIZE; ++i)
						buf += tmp.poll();
					randomAccessFile.writeBytes(buf);
				}
				fileBuffers.put(fileName, tmp);
				randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
