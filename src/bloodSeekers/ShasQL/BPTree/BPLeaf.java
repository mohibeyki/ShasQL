package bloodSeekers.ShasQL.BPTree;

import java.io.IOException;
import java.util.ArrayList;

import bloodSeekers.ShasQL.FileManager.FileManager;

public class BPLeaf extends BPNode {

	private static final long serialVersionUID = -1757214169710601924L;

	public BPLeaf(int index) {
		super();
		value = new ArrayList<Integer>();
		this.index = index;
		cluster = new byte[MAXN];
		size = 0;
	}

	public static final int MAXN = FileManager.BLOCK_SIZE;
	public static final int NODESIZE = 990;
	public int index;

	public ArrayList<Integer> value;
	public byte[] cluster;
	public int size;

	public int add(int i, BPNode parent) {
		try {
			Main.leafRAF.seek(index * MAXN);
			this.size = readSize();
			if (size < 0)
				size = 0;
			if (size > 0)
				Main.leafRAF.read(cluster, 0, size);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		this.parent = parent;
		createValueList();
		int u =addNode(i);
		value = new ArrayList<Integer>();
		cluster = new byte[MAXN];
		size = 0;
		return u;
	}

	public int readSize() throws IOException {
		byte tmp = (byte) Main.leafRAF.read();
		int u = 0;
		u = (tmp < 0 ? tmp + 256 : tmp);
		tmp = (byte) Main.leafRAF.read();
		int k = (tmp < 0 ? tmp + 256 : tmp);
		u = u * 256 + k;
		tmp = (byte) Main.leafRAF.read();
		k = (tmp < 0 ? tmp + 256 : tmp);
		u = u * 256 + k;
		tmp = (byte) Main.leafRAF.read();
		k = (tmp < 0 ? tmp + 256 : tmp);
		u = u * 256 + k;
		return u;
	}

	public void addToCluster(int i) {
		cluster[size++] = (byte) ((i >> 24) % 256);
		cluster[size++] = (byte) ((i >> 16) % 256);
		cluster[size++] = (byte) ((i >> 8) % 256);
		cluster[size++] = (byte) (i % 256);
	}

	public void printByte(byte[] c) {
		for (int i = 0; i < c.length; i++)
			System.out.print(c[i] + " ");
		System.out.println();
	}

	public void createClusterString() {
		size = 0;
		cluster = new byte[FileManager.BLOCK_SIZE - 4];
		for (int i = 0; i < value.size(); i++)
			addToCluster(value.get(i));
		try {
			Main.leafRAF.seek(this.index * MAXN);
			Main.leafRAF.write(getSizeByte());
			Main.leafRAF.write(cluster);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getSizeByte() {
		byte[] tmp = new byte[4];
		tmp[0] = (byte) ((size >> 24) % 256);
		tmp[1] = (byte) ((size >> 16) % 256);
		tmp[2] = (byte) ((size >> 8) % 256);
		tmp[3] = (byte) (size % 256);
		return tmp;
	}

	public void createValueList() {
		value = new ArrayList<Integer>();
		for (int i = 0; i < size;) {
			int u = 0;
			u = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			int k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			value.add(u);
		}
	}

	public void printSorted() {
		try {
			Main.leafRAF.seek(index * MAXN);
			this.size = readSize();
			if (size > 0)
				Main.leafRAF.read(cluster, 0, size);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < size;) {
			int u = 0;
			u = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			int k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			System.out.print(u + " ");
		}
		cluster = new byte[0];
		size = 0;
	}

	public String toString() {
		String tmp = "";
		try {
			Main.leafRAF.seek(index * MAXN);
			this.size = readSize();
			if (size > 0)
				Main.leafRAF.read(cluster, 0, size);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < size;) {
			int u = 0;
			u = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			int k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			k = (cluster[i] < 0 ? cluster[i++] + 256 : cluster[i++]);
			u = u * 256 + k;
			tmp += u + " ";
		}
		cluster = new byte[0];
		size = 0;
		return tmp;
	}

	public int binarySearchPlace(int v) {
		int s = 0;
		int e = value.size();
		while (e - s > 1) {
			int m = (e + s) / 2;
			if (value.get(m) <= v)
				s = m;
			else
				e = m;
		}
		if (e == 0)
			return 0;
		if (value.get(s) <= v)
			return e;
		else
			return s;
	}

	public int addNode(int v) {
		
		for(int i=0; i< value.size(); i++)
		{
			if( value.get(i) == v)
				return v;
		}
		if (value.size() >= NODESIZE)
			split(v);
		else {
			int place = binarySearchPlace(v);
			if (place >= value.size()) {
				value.add(v);
				// info.add(v);
			} else {
				value.add(place, v);
				// info.add(place, v);
			}
			createClusterString();
			return v;
		}
		return -1;
	}

	public void split(int v) {
		incName();
		BPLeaf tmp = new BPLeaf(Main.partitionIndex);
		int m = value.get(NODESIZE / 2);
		tmp.value.addAll(value.subList(0, NODESIZE / 2));
		// tmp.info.addAll(info.subList(0, NODESIZE / 2));
		value = new ArrayList<Integer>(
				value.subList(NODESIZE / 2, value.size()));
		// this.info = new ArrayList<Integer>(info.subList(NODESIZE / 2,
		// info.size()));
		if (v < m) {
			tmp.addNode(v);
			this.createClusterString();
		} else {
			this.addNode(v);
			tmp.createClusterString();
		}

		updateParent(tmp, m);
	}

	public void updateParent(BPLeaf tmp, int m) {
		if (parent == null) {
			parent = new BPNode();
			tmp.parent = parent;
			parent.value.add(m);
			parent.children.add(tmp);
			parent.children.add(this);
			Main.root = parent;
			Main.count++;
		} else {
			tmp.parent = parent;
			parent.addNode(m, tmp);
		}
	}
}
