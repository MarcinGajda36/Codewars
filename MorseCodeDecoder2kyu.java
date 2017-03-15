/*
public class SolutionTest {
    @Test
    public void testSomething() {
        assertEquals(MorseCodeDecoder.decodeMorse(MorseCodeDecoder.decodeBitsAdvanced("0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000")), "HEY JUDE");
    }
}
*/
public class MorseCodeDecoder {
	public static String SOS = "...---...";

	// [0] = template, [1] = length, [2] = meaning
	private static String[] dot = { "", "1", "." };
	private static String[] dash = { "", "3", "-" };

	private static String[] pauseBits = { "", "1", "" };
	private static String[] pauseChars = { "", "3", " " };
	private static String[] pauseWords = { "", "7", "   " };

	/**
	 * Given a string in Morse Code, returns the English translation.
	 *
	 * Accept dots, dashes and spaces, returns human-readable message.
	 */
	public static String decodeMorse(String morseCode) {
		while (morseCode.startsWith(" ")) {
			morseCode = morseCode.substring(1);
		}

		if (morseCode.length() == 0)
			return "";

		String result = "";

		if (morseCode == SOS)
			return "SOS";

		if (!morseCode.contains("   ")) {
			result = decodeWord(morseCode);
		} else
			result = decodeSentence(morseCode);

		return result;
	}

	/**
	 * Given a string of bits, which may or may not begin or end with '0's, and
	 * which may have some variation in the length of the time unit used,
	 * returns the Morse Code translation of this message.
	 *
	 * Accepts 0s and 1s, return dots, dashes and spaces
	 *
	 */
	public static String decodeBitsAdvanced(String bits) {
		System.out.println(bits);
		resetTranslator(); // Needs to reset or it will just keep growing every
							// test

		// removing 0 at front
		while (bits.startsWith("0")) {
			bits = bits.substring(1);
		}

		// removing 0 behind
		if (bits.lastIndexOf("1") < bits.length() - 1) {
			bits = bits.substring(0, (bits.lastIndexOf("1") + 1));
		}
		// checking if anything left
		if (bits.length() == 0)
			return "";

		bits += "0"; // my program needs 0 at the end if there is only 1 char :(

		// finding transmition rate
		int transmitionRate = findTransmitionRate(bits.substring(0, bits.length() - 1));

		// adjusting translator for different transmitionRate
		adjustTranslator(transmitionRate);

		// System.out.println("transmitionRate: " +transmitionRate);
		System.out.println("translated : " + globalCustomRateTranslate(bits));
		// globalCustomRateTranslate(bits);
		return globalCustomRateTranslate(bits);
	}

	private static String decodeSentence(String sentence) {
		String result = "";
		String[] words = sentence.split("   ");

		for (int i = 0; i < words.length; i++) {
			result += decodeWord(words[i]);
			if (i < words.length - 1)
				result += " ";
		}
		return result;
	}

	private static String decodeWord(String word) {
		String result = "";
		String[] letters = word.split(" ");
		for (String l : letters) {
			result += MorseCode.get(l);
		}
		return result;
	}

	private static String globalCustomRateTranslate(String bits) {
		String morseSentence;
		morseSentence = bits.replace(pauseWords[0], pauseWords[2]);
		morseSentence = morseSentence.replace(pauseChars[0], pauseChars[2]);
		morseSentence = morseSentence.replace(dash[0], dash[2]);
		morseSentence = morseSentence.replace(dot[0], dot[2]);
		morseSentence = morseSentence.replace(pauseBits[0], pauseBits[2]);

		// just to be sure
		morseSentence = morseSentence.replace("1", ".");
		morseSentence = morseSentence.replace("0", "");

		return morseSentence;
	}

	private static int findTransmitionRate(String bits) {
		int rate = 0;
		String morseWord = "";
		// breaking to 0 and 1
		char[] bits01 = bits.toCharArray();
		// making letters and chars
		String bitsChars = "";
		int pointer = 0;
		while (pointer < bits01.length) {
			if (bits01[Math.max(pointer - 1, 0)] == bits01[pointer]) {
				bitsChars += bits01[pointer++];
			} else {
				bitsChars += " ";
				bitsChars += bits01[pointer++];
			}
		}
		String[] lettersArray = bitsChars.split(" ");

		if (lettersArray.length == 1)
			return lettersArray[0].length();

		for (int i = 1; i < lettersArray.length; i++) {
			if (lettersArray[i].length() != 0 && lettersArray[i - 1].length() != 0) {
				rate = Math.min(lettersArray[i].length(), lettersArray[i - 1].length());
			} else if (lettersArray[i - 1].length() != 0) {
				rate = lettersArray[i - 1].length();
			} else {
				rate = lettersArray[i].length();
			}
		}
		return rate;
	}

	private static void adjustTranslator(int transmitionRate) {
		for (int i = Integer.parseInt(dot[1]) * transmitionRate; i > 0; i--)
			dot[0] += "1";
		for (int i = Integer.parseInt(dash[1]) * transmitionRate; i > 0; i--)
			dash[0] += "1";
		for (int i = Integer.parseInt(pauseBits[1]) * transmitionRate; i > 0; i--)
			pauseBits[0] += "0";
		for (int i = Integer.parseInt(pauseChars[1]) * transmitionRate; i > 0; i--)
			pauseChars[0] += "0";
		for (int i = Integer.parseInt(pauseWords[1]) * transmitionRate; i > 0; i--)
			pauseWords[0] += "0";

	}

	private static void resetTranslator() {
		dot[0] = "";
		dash[0] = "";
		pauseBits[0] = "";
		pauseChars[0] = "";
		pauseWords[0] = "";
	}

	private static String singleCharTranslator(String toTransform) {
		switch (toTransform) {
		case (pauseBits[0]):
			return pauseBits[2];
		case (pauseChars[0]):
			return pauseChars[2];
		case (pauseWords[0]):
			return pauseWords[2];
		case (dot[0]):
			return dot[2];
		case (dash[0]):
			return dash[2];
		default:
			return ".";
		}
	}

}
