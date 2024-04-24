package string;

import java.util.Scanner;
import java.util.Stack;

/**
 * A small program to convert a prefix expression into a postfix.
 */
public class PrefixToPostfix {
    public static void main(String[] Args) throws Exception {
        PrefixToPostfix ins = new PrefixToPostfix();

        Scanner scanner =  new Scanner(System.in);
        System.out.print("Enter a prefix expression e.g. '+**34/621' : " );
        final String testStr = scanner.next();
        scanner.close();
        String result = ins.getPostOrderString(testStr);

        System.out.print("Its postfix representation is : ");
        System.out.println(result);
    }

    private Stack<String> stack = new Stack<>();

    public String getPostOrderString(String preOrderStr) throws InvalidExpressionException {
        String result = null;
        if (preOrderStr.length() > 0) {
            for (int i = 0; i < preOrderStr.length(); i++) {
                char charAti = preOrderStr.charAt(i);
                stack.push(String.valueOf(charAti));
                if ('0' <= charAti && charAti <= '9')
                    processExpression();
                else if (!isOperator(String.valueOf(charAti)))
                    throw new InvalidExpressionException(preOrderStr + " is not a valid PreOrder Expression");
            }
            if (stack.size() == 1)
                result = stack.pop();
            else
                throw new InvalidExpressionException(preOrderStr + " is not a valid PreOrder Expression");
        }
        return result;
    }

    private void processExpression() throws InvalidExpressionException {
        String rhs = stack.pop();
        if (!stack.isEmpty() && !isOperator(stack.peek())) {
            String lhs = stack.pop();
            if (stack.isEmpty())
                throw new InvalidExpressionException("Given string is not a valid PreOrder Expression");
            String opr = stack.pop();
            String expr = lhs + rhs + opr;
            stack.push(expr);
            processExpression();
        } else
            stack.push(rhs);
    }

    private boolean isOperator(String str) {
        return "+".equals(str) || "+".equals(str) || "*".equals(str) || "/".equals(str);
    }

    private class InvalidExpressionException extends Exception {
        static final long serialVersionUID = -1;
        public InvalidExpressionException(String preOrderStr) {
            super(preOrderStr);
        }
    }
}
