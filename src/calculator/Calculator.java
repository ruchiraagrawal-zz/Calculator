package calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author ragrawal 
 * The below class handles calculation of numbers with BODMAS
 */

public class Calculator {

    static Stack<BigDecimal> valueStack = new Stack<BigDecimal>();
    static Stack<Character> operatorStack = new Stack<Character>();

    public static void main(String args[]) {
        System.out.println("Enter Expression to evaluate:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Calculator calculator = new Calculator();
        try {
            System.out.println("Result is:");
            System.out.println(calculator.calculate(input));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * 
     * @param string
     * @return BigDecimal The below method performs main calculate method by
     *         creating 2 stacks valueStack and operatorStack and performs
     *         operations till operatorStack is empty and returns back
     *         valueStack's last element
     */
    public BigDecimal calculate(String string) throws Exception  {
        char[] tokens = string.toCharArray();
        boolean isNegative = false;
        for (int i = 0; i < tokens.length; i++) {
            if (isNum(tokens[i])) {
                String s = new String();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    s = s.concat(String.valueOf(tokens[i]));
                    i++;
                }
                if (isNegative) { //to add negative sign in the value stack if preceding sign is -
                    throw new UnsupportedOperationException("Negative number is not supported");
                }
                valueStack.push(new BigDecimal(s));
                i--;
            }
            else if (isLeftParanthese(tokens[i])) {
                operatorStack.push(tokens[i]);
            }
            else if (isRightParanthese(tokens[i])) {
                valueStack.push(perform(operatorStack.pop(), valueStack.pop(), valueStack.pop()));
                operatorStack.pop(); // to pop opening bracket
            }
            else if (isOperator(tokens[i])) {
                if (tokens[i] == '-' && ((!operatorStack.isEmpty() && isOperatorForNegative(operatorStack.peek())))) {
                    isNegative = true;
                    continue;
                }
                while (!operatorStack.isEmpty() && hasLowPrecedenceInStack(tokens[i], operatorStack.peek())) {
                    valueStack.push(perform(operatorStack.pop(), valueStack.pop(), valueStack.pop()));  //valueStack has value if operator stack has low precedence than token
                }
                operatorStack.push(tokens[i]);
            }
        }
        while (!operatorStack.isEmpty()) {
            valueStack.push(perform(operatorStack.pop(), valueStack.pop(), valueStack.pop()));
        }
        return valueStack.pop();
    }

    /**
     * 
     * @param character token
     * @return boolean true to check if number is negative
     */
    private static boolean isOperatorForNegative(Character token) {
        return token == '+' || token == '-' || token == '*' || token == '/' || token == '(' || token == ')'
                || token == '^';
    }

    /**
     * 
     * @param operator
     * @param operand1
     * @param operand2
     * @return BigDecimal value after performing operation on operand1 and
     *         operand2
     */
    private static BigDecimal perform(Character operator, BigDecimal operand1, BigDecimal operand2) {

        switch (operator) {
            case '+':
                return operand1.add(operand2);
            case '-':
                return operand1.subtract(operand2);
            case '*':
                return operand1.multiply(operand2);
            case '/':
                if (operand1.compareTo(BigDecimal.ZERO) == 0) {
                    throw new UnsupportedOperationException("Cannot Divide by 0");
                }
                return operand2.divide(operand1, RoundingMode.DOWN);
            case '^':
                BigDecimal exponent = new BigDecimal(1);
                for (int i = 0; i < operand1.intValue(); i++) {
                    exponent = exponent.multiply(operand2);
                }
                return exponent;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 
     * @param Character
     *            token
     * @return boolean true if number
     */
    private static boolean isNum(Character token) {
        return token >= '0' && token <= '9';
    }

    /**
     * 
     * @param character
     *            token
     * @return boolean true if its an operator
     */
    private static boolean isOperator(Character token) {
        return token == '+' || token == '-' || token == '*' || token == '/' || token == '^';
    }

    /**
     * 
     * @param token
     * @return boolean if its an opening parantheses
     */
    private static boolean isLeftParanthese(Character token) {
        return token == '(';
    }

    /**
     * 
     * @param token
     * @return boolean if its an closing parantheses
     */
    private static boolean isRightParanthese(Character token) {
        return token == ')';
    }

    /**
     * 
     * @param tokenOp
     * @param stackOp
     * @return boolean false if tokenOperator has higher precedence than
     *         stackOperator so that operator gets pushed if it has higher precedence than stack's top element
     */
    private static boolean hasLowPrecedenceInStack(Character tokenOp, Character stackOp) {

        if (stackOp == '(' || stackOp == ')')
            return false;
        if (tokenOp == '^' && (stackOp == '*' || stackOp == '/'))
            return false;
        if (stackOp == '*' && tokenOp == '/')
            return false;
        if ((stackOp == '+' || stackOp == '-') && (tokenOp == '*' || tokenOp == '/' || tokenOp == '^'))
            return false;
        return true;
    }

}
