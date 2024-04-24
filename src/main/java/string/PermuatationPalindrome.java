package string;

import java.util.HashMap;
import java.util.Map;

/**
 * A small program to test if a given String is a Palindrome and if any permutation is a Palindrome.
 *
 * @author Sandeep Ganjir
 */
public class PermuatationPalindrome {
    public static void main(String[] args) {
        test("Girafarig");
        test("Mississippi");
        test("Sandeep");

        int len = longestPermutationPalindrome("Random Girafarig GeeksFGeeks");
        System.out.println("\nLongest Permutation Palindrome : " + len);
    }

    public static void test(String p_Str) {
        String cond = isPermutationPalindrome(p_Str) ? "" : "not ";
        String str1 = "\n\"" + p_Str + "\" is " + cond + "a Permutation Palindrome";
        System.out.println(str1);

        cond = isPalindrome(p_Str) ? "" : "not ";
        String str2 = "\"" + p_Str + "\" is " + cond + "a Palindrome";
        System.out.println(str2);
    }

    public static boolean isPalindrome(String p_Str) {
        int n = p_Str.length() - 1;
        for (int i = 0; i <= n / 2; i++) {
            String chr1 = "" + p_Str.charAt(i);
            String chr2 = "" + p_Str.charAt(n - i);
            if (!chr1.equalsIgnoreCase(chr2))
                return false;
        }
        return true;
    }

    public static boolean isPermutationPalindrome(String p_Str) {
        Map<String, Integer> charMap = new HashMap<>();
        int oddCount = 0;

        for (int i = 0; i < p_Str.length(); i++) {
            String chr = ("" + p_Str.charAt(i)).toUpperCase();
            if (charMap.get(chr) == null) {
                charMap.put(chr, 1);
                oddCount++;
            } else {
                int count = charMap.get(chr);
                oddCount += (++count % 2 == 1) ? 1 : -1;
                charMap.put(chr, count);
            }
        }

        if (oddCount <= 1)
            return true;
        return false;
    }

    public static int longestPermutationPalindrome(String p_Str) {
        int lengthPalin = 0;
        int n = p_Str.length();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                String subStr = p_Str.substring(i, j);
                if (isPermutationPalindrome(subStr))
                    if (lengthPalin < subStr.length())
                        lengthPalin = subStr.length();
            }
        }
        return lengthPalin;
    }
}
