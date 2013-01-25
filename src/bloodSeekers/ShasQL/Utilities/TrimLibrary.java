package bloodSeekers.ShasQL.Utilities;

import java.util.ArrayList;

public class TrimLibrary {
	public static ArrayList<String> TrimAndFormat(String input) {
		ArrayList<String> output = new ArrayList<String>();
		for (int i = 0; i < input.length(); ++i) {
			String word = "";
			while (i < input.length()
					&& Character.isLetterOrDigit(input.charAt(i)))
				word += input.charAt(i++);
			if (!word.equals(""))
				output.add(word);
		}
		return output;
	}
}
