package bloodSeekers.ShasQL.Utilities;

public class StringUtilities {

	public static int indexOf(String input, String phrase) {
		input = input.toLowerCase();
		phrase = phrase.toLowerCase();
		for (int i = 0; i < input.length(); ++i)
			for (int j = 0; j < phrase.length()
					&& input.charAt(i + j) == phrase.charAt(j); ++j)
				if (j == phrase.length())
					return i;
		return -1;
	}

	public static int indexOf(char[] input, String phrase) {
		phrase = phrase.toLowerCase();
		for (int i = 0; i < input.length - phrase.length(); ++i)
			for (int j = 0; j < phrase.length()
					&& ((input[i + j] <= 'Z' && input[i + j] >= 'A') ? input[i
							+ j] + 'a' - 'A' : input[i + j]) == phrase
							.charAt(j); ++j)
				if (j == phrase.length() - 1)
					return i;
		return -1;
	}
}
