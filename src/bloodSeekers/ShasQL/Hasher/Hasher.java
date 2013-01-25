package bloodSeekers.ShasQL.Hasher;

public class Hasher {

	@SuppressWarnings("unused")
	private static int getHashCode(String name) {
		int hash = 0;
		for (char c : name.toCharArray()) {
			hash += c;
			hash *= 199;
			hash %= 2147483647;
		}
		return hash;
	}
}
