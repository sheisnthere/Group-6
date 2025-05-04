import java.util.Scanner;
import java.util.Stack;

public class InfixValidator {

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static boolean isValidInfix(String expr) {
        expr = expr.replaceAll("\\s+", "");
        if (expr.isEmpty()) return false;

        char[] tokens = expr.toCharArray();
        boolean expectOperand = true;

        for (int i = 0; i < tokens.length; i++) {
            char c = tokens[i];

            if (Character.isDigit(c)) {
                expectOperand = false;
                while (i + 1 < tokens.length && Character.isDigit(tokens[i + 1])) {
                    i++;
                }
            } else if (isOperator(c)) {
                if (expectOperand) return false;
                expectOperand = true;
            } else if (c == '(') {
                expectOperand = true;
            } else if (c == ')') {
                if (expectOperand) return false;
                expectOperand = false;
            } else {
                return false;
            }
        }

        return !expectOperand && isBalanced(expr);
    }

    public static boolean isBalanced(String expr) {
        Stack<Character> stack = new Stack<>();
        for (char c : expr.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Masukkan notasi infix:");
            String input = scanner.nextLine();
            
            if (isValidInfix(input)) {
                System.out.println("Notasi infix valid.");
            } else {
                System.out.println("Notasi infix TIDAK valid.");
            }
        }
    }
}
