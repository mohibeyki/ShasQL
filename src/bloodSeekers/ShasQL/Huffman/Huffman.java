package bloodSeekers.ShasQL.Huffman;

import java.util.*;
import java.io.*;

import bloodSeekers.ShasQL.FileManager.FileManager;

public class Huffman {

	private static HashMap<Character, String> map;
	private static int bufIndex = 0;
	private static byte c = 0;
	private static ArrayList<Byte> arrayListBuffer = new ArrayList<Byte>();

	public static void main(String[] args) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile("orig", "r");
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				new FileOutputStream("Output"));

		int[] charFrq = new int[256];
		byte[] readBuffer = new byte[FileManager.BLOCK_SIZE];

		int size;
		while ((size = randomAccessFile.read(readBuffer)) > 0)
			for (int i = 0; i < size; ++i) {
				int c = readBuffer[i] + (readBuffer[i] < 0 ? 256 : 0);
				charFrq[c >= 'A' && c <= 'Z' ? c - 'A' + 'a' : c]++;
			}

		randomAccessFile.close();
		map = makeItHuffman(charFrq);

		while ((size = randomAccessFile.read(readBuffer)) >= 0)
			writeBinaryLine(readBuffer, bufferedOutputStream, size);

		randomAccessFile.close();
		writeBinaryLine(readBuffer, bufferedOutputStream, -1);
		bufferedOutputStream.close();
	}

	private static void writeBinaryLine(byte[] input, BufferedOutputStream bos,
			int size) throws IOException {
		if (size > 0) {
			for (int i = 0; i < size; ++i) {
				String s = map
						.get((char) (input[i] + (input[i] >= 0 ? 0 : 256)));
				for (int j = 0; j < s.length(); ++j) {
					c <<= 1;
					c += (s.charAt(j) == '0' ? 0 : 1);
					bufIndex++;
					if (bufIndex == 8) {
						arrayListBuffer.add(c);
						c = 0;
						bufIndex = 0;
					}
					if (arrayListBuffer.size() == FileManager.BLOCK_SIZE) {
						bos.write(bufToArray());
						arrayListBuffer.clear();
					}
				}
			}
		} else {
			while (bufIndex < 8) {
				c <<= 1;
				bufIndex++;
			}
			arrayListBuffer.add(c);
			bos.write(bufToArray());
			c = 0;
			bufIndex = 0;
		}
	}

	private static byte[] bufToArray() {
		byte[] b = new byte[arrayListBuffer.size()];
		for (int k = 0; k < arrayListBuffer.size(); ++k)
			b[k] = arrayListBuffer.get(k);
		return b;
	}

	private static HashMap<Character, String> makeItHuffman(int[] chars) {

		HashMap<Character, String> huffman = new HashMap<Character, String>();

		PriorityQueue<HuffmanNode> charsQueue = new PriorityQueue<HuffmanNode>();
		for (int i = 0; i < chars.length; ++i)
			charsQueue.add(new HuffmanNode((char) i, chars[i]));

		while (charsQueue.size() != 1) {
			HuffmanNode child = charsQueue.poll();
			charsQueue.add(new HuffmanNode(child, charsQueue.poll()));
		}

		charsQueue.peek().makeItHuffman(huffman);

		for (int i = 'A'; i <= 'Z'; ++i)
			huffman.put((char) i, huffman.get((char) (i + 32)));

		// charsQueue.peek().printHuffman();
		// System.out.println(charsQueue.peek().getHuffmanSize());

		return huffman;
	}

}
