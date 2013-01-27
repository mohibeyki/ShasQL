package bloodSeekers.ShasQL.FileManager;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class BufferedFileOperator {

	private HashMap<String, Queue<Character>> fileBuffers;
	private Queue<Character> readBuffer;
	private String outputBuffer;
	private FileReader fileInputStream;
	private boolean EOF;

	public boolean openFile(String fileName) {
		if (fileBuffers.get(fileName) != null
				&& fileBuffers.get(fileName).size() > 0) {
			Flush(fileName);
		}
		try {
			fileInputStream = new FileReader(new File(fileName));
			if (fileInputStream != null)
				return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void Flush(String fileName) {
		if (fileBuffers.get(fileName) != null
				&& fileBuffers.get(fileName).size() > 0) {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(
						fileName, "rw");
				String buf = "";
				while (fileBuffers.get(fileName).size() > 0)
					buf += fileBuffers.get(fileName).poll();
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
		if (EOF)
			return "";
		while (true) {
			while (readBuffer.size() > 0) {
				char c = readBuffer.poll();
				if (Character.isLetterOrDigit(c))
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
					readBuffer.add(buf[size]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void FlushAll() {
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

	public void WriteToFile(String fileName, String data) {
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
				while (fileBuffers.get(fileName).size() > FileManager.BLOCK_SIZE) {
					String buf = "";
					for (int i = 0; i < FileManager.BLOCK_SIZE; ++i)
						buf += fileBuffers.get(fileName).poll();
					randomAccessFile.writeBytes(buf);
				}
				randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
