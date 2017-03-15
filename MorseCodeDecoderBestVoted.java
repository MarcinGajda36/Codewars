import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MorseCodeDecoderBestVoted {
    public static String decodeBits(String bits) {
        String trimmedBits = bits.replaceAll("^0+|0+$", "");
        int rate = getRate(trimmedBits);

        String morseCode = "";
        for (String word : trimmedBits.split("0{"+ (7 * rate) +"}")) {
            for (String letter : word.split("0{"+ (3 * rate) +"}")) {
                for (String dot : letter.split("0{" + rate + "}")) {
                    morseCode += dot.length() > rate ? '-' : '.';
                }
                morseCode += ' ';
            }
            morseCode += "  ";
        }
        return morseCode;
    }

    private static int getRate(String bits) {
        int rate = Integer.MAX_VALUE;
        Matcher matcher = Pattern.compile("1+|0+").matcher(bits);
        while (matcher.find()) {
            rate = Math.min(rate, matcher.group().length());
        }
        return rate;
    }

    public static String decodeMorse(String morseCode) {
        String decoded = "";
        for (String word : morseCode.trim().split("   ")) {
            for (String letter : word.split(" ")) {
                decoded += MorseCode.get(letter);
            }
            decoded += ' ';
        }
        return decoded.trim();
    }
}