package bloodSeekers.ShasQL.Hasher;

public class Hasher {

	public static String getParentDirectory(int hashCode) {
		String code = Integer.toString(hashCode);
		return code.length() > (hashCode > 0 ? 2 : 3) ? code.substring(0, (hashCode > 0 ? 2 : 3)) : "0";
	}

	public static String getFileName(int hashCode) {
		String code = Integer.toString(hashCode);
		return code.substring(Math.min(code.length(), hashCode > 0 ? 2 : 3));
	}

	public static int getHashCode(String name) {
		int hash = 0;
		for (char c : name.toCharArray()) {
			hash += c;
			hash *= 199;
			hash %= 2147483647;
		}
		return hash;
	}
}
