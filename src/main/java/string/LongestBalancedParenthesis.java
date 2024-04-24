package string;
import java.util.*;

/**
 * Given a String of parenthesis, we have to find the length of longest
 * substring with balanced parenthesis.
 * Input : "))(()()(()())((()(()"
 * Output : 10
 *  @author Sandeep Ganjir
 */
public class LongestBalancedParenthesis {
    public static void main(String[] Args) {
        String testStr = "))(()()(()())((()(()";
        LongestBalancedParenthesis ins = new LongestBalancedParenthesis();

        System.out.println("Given string is : " + testStr);
        System.out.println("Longest Balanced Parenthesis substring length is : " + ins.getLongestBalancedParenthesis(testStr));

        System.out.print("\nUsing a trick solution we remove the invalid braces leaving only the valid substrings. ");
        System.out.println("We can then find the longest substring using str.split(\"_\").");
        System.out.println(ins.trickSolution(testStr));
    }

    public int getLongestBalancedParenthesis(String str) {
        int maxLen = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '(') {
                if (!stack.isEmpty() && stack.peek()>0)
                    stack.push(-1 * stack.pop());
                else
                    stack.push(0);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.peek()>0) {
                    stack.empty();
                } else {
                    int top = stack.pop();
                    int curLen = -1*top + 2;
                    if (!stack.isEmpty()) {
                        top = stack.pop();
                        if (top == 0)
                            curLen *= -1;
                        else
                            curLen = (curLen + Math.abs(top)) * (top/Math.abs(top));
                    }
                    stack.push(curLen);
                    if (maxLen < Math.abs(curLen))
                        maxLen =  Math.abs(curLen);
                }
            } else
                throw new RuntimeException("Invalid Input String");
        }
        return maxLen;
    }

    public String trickSolution(String str) {
        char[] chAr = str.toCharArray();

        for (int i = 0, counter = 0; i < chAr.length; i++) {
            if (chAr[i] == '(')
                counter++;
            if (chAr[i] == ')')
                counter--;
            if (counter < 0) {
                chAr[i] = '_';
                counter = 0;
            }
        }

        for (int i = chAr.length - 1, counter = 0; i >= 0; i--) {
            if (chAr[i] == '(')
                counter--;
            if (chAr[i] == ')')
                counter++;
            if (counter < 0) {
                chAr[i] = '_';
                counter = 0;
            }
        }

        return String.valueOf(chAr);
    }
}
