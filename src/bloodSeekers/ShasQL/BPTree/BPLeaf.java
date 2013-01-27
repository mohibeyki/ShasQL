package bloodSeekers.ShasQL.BPTree;

import java.util.*;

public class BPLeaf extends BPNode {

	private static final long serialVersionUID = 1179730276595841822L;
	ArrayList<Integer> info;

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < info.size() - 1; ++i) {
			s += info.get(i) + ",";
		}
		return s + (info.size() > 0 ? info.get(info.size() - 1) : "") + "]";
	}

	public BPLeaf() {
		super();
		info = new ArrayList<Integer>();
	}

	public void add(int v) {
		addNode(v);
	}

	public void printsorted() {
		for (int i = 0; i < info.size(); i++)
			System.out.print(info.get(i) + " ");
	}

	protected void addNode(int v) {
		if (value.size() >= NODESIZE)
			split(v);
		else {
			int place = binarySearchPlace(v);
			if (place >= value.size()) {
				value.add(v);
				info.add(v);
			} else {
				value.add(place, v);
				info.add(place, v);
			}
		}
	}

	private void split(int v) {
		BPLeaf tmp = new BPLeaf();
		int m = value.get(NODESIZE / 2);
		tmp.value.addAll(value.subList(0, NODESIZE / 2));
		tmp.info.addAll(info.subList(0, NODESIZE / 2));
		value = new ArrayList<Integer>(
				value.subList(NODESIZE / 2, value.size()));
		this.info = new ArrayList<Integer>(info.subList(NODESIZE / 2,
				info.size()));
		if (v < m)
			tmp.addNode(v);
		else
			this.addNode(v);
		updateParent(tmp, m);
	}

	private void updateParent(BPLeaf tmp, int m) {
		if (parent == null) {
			parent = new BPNode();
			tmp.parent = parent;
			parent.value.add(m);
			parent.children.add(tmp);
			parent.children.add(this);
			root = parent;
			Main.count++;
		} else {
			tmp.parent = parent;
			parent.addNode(m, tmp);
		}
	}
}
