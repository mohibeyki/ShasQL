package bloodSeekers.ShasQL.Huffman;

import java.util.HashMap;

public class HuffmanNode implements Comparable<HuffmanNode> {
	public HuffmanNode left;
	public HuffmanNode right;
	public int size;
	public char ch;
	public String code;

	public HuffmanNode(HuffmanNode left, HuffmanNode right) {
		this.left = left;
		this.right = right;
		this.size = (left == null ? 0 : left.size)
				+ (right == null ? 0 : right.size);
		this.code = "";
		this.ch = 0;
	}

	public HuffmanNode(char ch, int size) {
		this.left = null;
		this.right = null;
		this.size = size;
		this.ch = ch;
		this.code = "";
	}

	public int compareTo(HuffmanNode o) {
		return size - o.size;
	}

	public void print() {
		if (left != null)
			left.print();
		System.out.println(size);
		if (right != null)
			right.print();
	}

	public void printHuffman() {
		if (left != null)
			left.printHuffman();
		if (this.ch != 0)
			System.out.println((char) ch + " " + (int) ch + " " + code);
		if (right != null)
			right.printHuffman();
	}

	public int getHuffmanSize() {
		int t = 0;
		if (this.left != null)
			t += this.left.getHuffmanSize();
		if (this.ch != 0)
			t += this.code.length() * this.size;
		if (this.right != null)
			t += this.right.getHuffmanSize();

		return t;
	}

	public void makeItHuffman(HashMap<Character, String> huffman) {
		if (this.ch != 0)
			huffman.put(this.ch, this.code);
		if (this.left != null) {
			this.left.code = this.code + "0";
			this.left.makeItHuffman(huffman);
		}
		if (right != null) {
			right.code = this.code + "1";
			right.makeItHuffman(huffman);
		}
	}

}
