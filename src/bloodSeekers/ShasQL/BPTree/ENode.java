package bloodSeekers.ShasQL.BPTree;

import java.util.*;

public class ENode {
	public ENode(ArrayList<Character> sector, int size) {
		this.sector = sector;
		this.size = size;
	}

	int size;
	ArrayList<Character> sector;
	static final int NODESIZE = 1900;
}
