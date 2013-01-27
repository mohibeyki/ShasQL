package bloodSeekers.ShasQL.BPTree;

import java.io.Serializable;
import java.util.ArrayList;

public class BPNode implements Serializable {

	private static final long serialVersionUID = -8635151291066995914L;

	public ArrayList<BPNode> children;
	public ArrayList<Integer> value;
	public BPNode parent;

	public static void incName() {
		Main.partitionIndex++;
	}

	public String toString() {
		String tmp = "";
		for (int i = 0; i < value.size(); ++i)
			tmp += value.get(i) + " " + children.get(i).toString();
		return tmp;
	}

	public BPNode() {
		children = new ArrayList<BPNode>();
		value = new ArrayList<Integer>();
	}

	public void print() {
		System.out.println(this + " " + this.parent);
		for (int i = 0; i < value.size(); i++)
			System.out.print(value.get(i) + " ");
		System.out.println();
		for (int i = 0; i < children.size(); i++)
			System.out.print(children.get(i) + " ");
		System.out.println();
		for (int i = 0; i < children.size(); i++)
			children.get(i).print();
	}

	public void printSorted() {
		for (int i = 0; i < children.size(); i++) {
			System.out.println("SD "
					+ Boolean.toString(children.get(i) instanceof BPLeaf));
			children.get(i).printSorted();
		}
	}

	public BPNode getParent() {
		if (parent != null)
			return parent.getParent();
		return this;
	}

	public void add(int i) throws Exception {
		BPNode tmp = binarySearch(i);
		if (tmp == null)
			throw new Exception(
					"Something went wrong @ add, Binary search has a problem!");
		tmp.add(i, this);
	}

	public void add(int i, BPNode parent) throws Exception {
		BPNode tmp = binarySearch(i);
		if (tmp == null)
			throw new Exception(
					"Something went wrong @ add, Binary search has a problem!");
		this.parent = parent;
		tmp.add(i, this);
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

	public BPNode binarySearch(int v) {
		int s = 0;
		int e = value.size();
		while (e - s > 1) {
			int m = (e + s) / 2;
			if (value.get(m) <= v)
				s = m;
			else
				e = m;
		}
		if (value.size() == 0 || v < value.get(s))
			return children.get(s);
		else
			return children.get(s + 1);
	}

	public void addNode(int v, BPNode node) {
		if (value.size() >= Main.NODESIZE)
			split(v, node);
		else {
			int place = binarySearchPlace(v);
			if (place >= value.size()) {
				value.add(v);
				children.add(children.size() - 1, node);

			} else {
				value.add(place, v);
				children.add(place, node);
			}
			node.parent = this;
		}
	}

	public void split(int v, BPNode node) {
		BPNode tmp = new BPNode();
		int m = value.get(Main.NODESIZE / 2);
		tmp.value = new ArrayList<Integer>(value.subList(0, Main.NODESIZE / 2));
		tmp.children = new ArrayList<BPNode>(this.children.subList(0,
				Main.NODESIZE / 2 + 1));
		for (int i = 0; i < tmp.children.size(); i++)
			tmp.children.get(i).parent = tmp;
		this.value = new ArrayList<Integer>(value.subList(Main.NODESIZE / 2 + 1,
				value.size()));
		this.children = new ArrayList<BPNode>(this.children.subList(
				Main.NODESIZE / 2 + 1, children.size()));
		if (v < m)
			tmp.addNode(v, node);
		else
			this.addNode(v, node);
		updateParent(tmp, m);
	}

	public void updateParent(BPNode tmp, int m) {
		if (parent == null) {
			parent = new BPNode();
			parent.value.add(m);
			tmp.parent = this.parent;
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
