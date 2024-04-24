package string;
import java.util.*;

/**
 * Given a String of different symbols find the longest substring with equal frequency of these symbols.
 * Input : 00001201221120100200211220010001011110010222011021
 * Output : 120122112010020021122001
 *  @author Sandeep Ganjir
 */
public class EqualOccurence {
    public static void main(String[] args) {
        EqualOccurence instance = new EqualOccurence();
        instance._testCase();
    }

    private void _testCase() {
        final String str = _generateRandom(50, 3); //"10110111000";
        System.out.println("Longest substring with equal frequency of elements in given string");
        System.out.println(str + " : " + getLongestSubstringWithEqualFreq(str));
    }

    private String _generateRandom(int size, int range) {
        char[] Ar = new char[size];
        Random r = new Random();
        for (int i = 0; i < Ar.length; i++) {
            Ar[i] = (char)(r.nextInt(range) + '0');
        }
        return String.valueOf(Ar);
    }

    public String getLongestSubstringWithEqualFreq(String p_Str){
        Map<Character, Integer> countBySymbol = new HashMap<>();
        Map<String, Integer> minIndexByPattern = new HashMap<>();

        int maxLength = 0;
        int lowerIndex = 0;

        // Finding all unique elements
        for (int i = 0; i < p_Str.length(); i++)
            countBySymbol.put(p_Str.charAt(i), 0);
        minIndexByPattern.put(_getPattern(countBySymbol), 0);

        for (int i = 0; i < p_Str.length(); i++) {
            char charAtI = p_Str.charAt(i);
            countBySymbol.put(charAtI, countBySymbol.get(charAtI) + 1);

            String pattern = _getPattern(countBySymbol);
            if (minIndexByPattern.get(pattern) == null)
                minIndexByPattern.put(pattern, i+1);

            /* Idea here is that for a pattern to reappear frequency of elements
                must be equal between initial occurence and now */
            int length = i - minIndexByPattern.get(pattern) + 1;
            if (length > maxLength) {
                maxLength = length;
                lowerIndex = minIndexByPattern.get(pattern);
            }
        }
        return p_Str.substring(lowerIndex, lowerIndex + maxLength);
    }

    // Method to generate pattern from difference in frequency of elements
    private String _getPattern(Map<Character, Integer> countBySymbol) {
        String pattern = "";
        int min = Integer.MAX_VALUE;
        for (Integer value : countBySymbol.values())
            if (value < min)
                min = value;
        for (Integer value : countBySymbol.values())
            pattern += (value - min) + "_";
        return pattern;
    }
}
