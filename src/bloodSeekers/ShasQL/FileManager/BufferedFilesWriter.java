package bloodSeekers.ShasQL.FileManager;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import bloodSeekers.ShasQL.Utilities.CharQueue;

public class BufferedFilesWriter {

	private static BufferedFilesWriter instance;
	private HashMap<String, CharQueue> fileBuffers;
	private boolean EOF;

	private BufferedFilesWriter() {
		fileBuffers = new HashMap<String, CharQueue>();
	}

	public static BufferedFilesWriter getInstance() {
		if (instance == null)
			instance = new BufferedFilesWriter();
		return instance;
	}

	private void flush(String fileName) throws Exception {
		File file = new File(fileName);
		if (fileBuffers.get(file.getPath()) != null
				&& fileBuffers.get(file.getPath()).size() > 0) {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(file,
						"rw");
				String buf = "";
				while (fileBuffers.get(file.getPath()).size() > 0)
					buf += fileBuffers.get(file.getPath()).Dequeue();
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

	public void flushAll() throws Exception {
		for (Entry<String, CharQueue> entry : fileBuffers.entrySet()) {
			flush("" + entry.getKey());
		}
	}

	public void writeToFile(String fileName, String data) throws Exception {
		if (!fileBuffers.containsKey(fileName))
			fileBuffers.put(fileName, new CharQueue(20000));

		for (char c : data.toCharArray())
			fileBuffers.get(fileName).Enque(c);

		if (fileBuffers.get(fileName).size() > FileManager.BLOCK_SIZE) {
			RandomAccessFile randomAccessFile;
			try {
				randomAccessFile = new RandomAccessFile(new File(fileName),
						"rw");
				randomAccessFile.seek(randomAccessFile.length());
				CharQueue tmp = fileBuffers.get(fileName);
				while (tmp.size() > FileManager.BLOCK_SIZE) {
					String buf = "";
					for (int i = 0; i < FileManager.BLOCK_SIZE; ++i) {
						buf += tmp.Dequeue();
					}
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
