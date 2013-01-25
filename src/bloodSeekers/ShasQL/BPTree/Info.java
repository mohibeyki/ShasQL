package bloodSeekers.ShasQL.BPTree;

import java.util.ArrayList;

public class Info {
	private int fileIndex;
	private ArrayList<Integer> partiotions;

	public Info(int fileIndex) {
		this.setFileIndex(fileIndex);
	}

	public int getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}

	public ArrayList<Integer> getPartiotions() {
		return partiotions;
	}

	public void setPartiotions(ArrayList<Integer> partiotions) {
		this.partiotions = partiotions;
	}
}
