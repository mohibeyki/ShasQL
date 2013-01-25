
package bloodSeekers.ShasQL.Utilities;

public class Pair<T extends Comparable<T>, L extends Comparable<L>> implements Comparable<Pair<T, L>> {
	public T first;
	public L second;
	
	public Pair(T first, L second) {
		this.first = first;
		this.second = second;
	}

	public int compareTo(Pair<T, L> o) {
		int res = this.first.compareTo(o.first);
		if (res != 0)
			return res;
		return (this.second).compareTo(o.second);
	}
}
