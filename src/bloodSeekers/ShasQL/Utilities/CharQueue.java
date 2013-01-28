package bloodSeekers.ShasQL.Utilities;

public class CharQueue {
	char[] mem;
	int start, end;
	int maxSize;

	public CharQueue(int initialSize) {
		mem = new char[initialSize + 1];
		start = end = 0;
		maxSize = initialSize;
	}

	public void Enque(char c) throws Exception {
		if (end - start == maxSize)
			throw new Exception("Queue is full");
		end++;
		end %= maxSize;
		mem[end] = c;
	}

	public char Dequeue() throws Exception {
		if (end > start) {
			start++;
			start %= maxSize;
			return mem[start];
		}
		throw new Exception("Queue is emtpy");
	}
	public int size() {
		return end - start;
	}
}
