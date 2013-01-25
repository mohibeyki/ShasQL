package bloodSeekers.ShasQL.BPTree;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

public class BPNode implements Serializable {

	@Expose
	private static final long serialVersionUID = -5002211178486941590L;
	@Expose
	protected static BPNode root;

	@Override
	public String toString() {

		String string = "{";
		for (int i = 0; i < this.children.size() - 1; ++i)
			string += this.children.get(i).toString() + ",";

		string += this.children.size() > 0 ? this.children.get(
				this.children.size() - 1).toString() : " " + "}";
		return string;
	}

	public void SerilizeMe() {
		try {
			ObjectOutput OO = new ObjectOutputStream(new FileOutputStream(
					new File("db.ShasQL")));
			OO.writeObject(root);
			OO.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Hibernate() {
		try {
			FileWriter fileWriter = new FileWriter(new File("Hibernation"));
			fileWriter.write(root.toString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SaveJSon() {
		try {
			Gson gson = new GsonBuilder()
					.excludeFieldsWithoutExposeAnnotation().create();
			FileWriter fileWriter = new FileWriter(new File("JSon"));
			fileWriter.write(gson.toJson(root));
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void LoadJSon() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		try {
			Scanner sc = new Scanner(new FileInputStream("JSon"));
			root = gson.fromJson(sc.nextLine(), BPNode.class);
			System.out.println(root.value.size());
			System.out.println(root.value);
			System.out.println(root.toString());
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Expose
	static final int NODESIZE = 8;

	protected BPNode parent;

	@Expose
	protected ArrayList<BPNode> children;
	@Expose
	protected ArrayList<Integer> value;

	@Expose
	public int size;
	@Expose
	public ArrayList<Byte> sector;

	public static BPNode Root() {
		if (root == null)
			root = new BPLeaf();
		return root;
	}

	protected BPNode() {
		children = new ArrayList<BPNode>();
		value = new ArrayList<Integer>();
		sector = new ArrayList<Byte>();

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

	public void printsorted() {
		for (int i = 0; i < children.size(); i++)
			children.get(i).printsorted();
	}

	public BPNode getParent() {
		if (parent != null)
			return parent.getParent();
		return this;
	}

	public void add(int v) {
		BPNode tmp = binarySearch(v);
		if (tmp == null)
			System.err.println("Runtime failure");
		tmp.add(v);
	}

	protected int binarySearchPlace(int v) {
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
		if (v < value.get(s))
			return children.get(s);
		else
			return children.get(s + 1);
	}

	protected void addNode(int v, BPNode node) {
		if (value.size() >= NODESIZE)
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

	private void split(int v, BPNode node) {
		BPNode tmp = new BPNode();
		int m = value.get(NODESIZE / 2);
		tmp.value = new ArrayList<Integer>(value.subList(0, NODESIZE / 2));
		tmp.children = new ArrayList<BPNode>(this.children.subList(0,
				NODESIZE / 2 + 1));
		for (int i = 0; i < tmp.children.size(); i++)
			tmp.children.get(i).parent = tmp;
		this.value = new ArrayList<Integer>(value.subList(NODESIZE / 2 + 1,
				value.size()));
		this.children = new ArrayList<BPNode>(this.children.subList(
				NODESIZE / 2 + 1, children.size()));
		if (v < m)
			tmp.addNode(v, node);
		else
			this.addNode(v, node);
		updateParent(tmp, m);
	}

	private void updateParent(BPNode tmp, int m) {
		if (parent == null) {
			parent = new BPNode();
			parent.value.add(m);
			tmp.parent = this.parent;
			parent.children.add(tmp);
			parent.children.add(this);
			root = parent;
			Main.count++;
			// System.out.println("EZAFE");
			// System.out.println();
			// System.out.println();
		} else {
			tmp.parent = parent;
			parent.addNode(m, tmp);
		}
	}
}
