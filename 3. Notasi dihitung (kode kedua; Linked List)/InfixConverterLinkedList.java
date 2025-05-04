import java.util.Scanner;

// Kelas untuk merepresentasikan node dalam linked list
class Node<T> {
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}

// Implementasi Stack menggunakan Linked List
class LinkedListStack<T> {
    private Node<T> top;
    private int size;

    public LinkedListStack() {
        this.top = null;
        this.size = 0;
    }

    // Menambahkan elemen ke stack
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // Mengambil elemen teratas dari stack
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    // Melihat elemen teratas tanpa mengambilnya
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return top.data;
    }

    // Mengecek apakah stack kosong
    public boolean isEmpty() {
        return top == null;
    }

    // Mendapatkan ukuran stack
    public int size() {
        return size;
    }
}

public class InfixConverterLinkedList {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Program Konversi dan Evaluasi Notasi Infix (Linked List) ===");
            
            // Meminta input dari user
            System.out.print("Masukkan notasi infix: ");
            String infixExpression = scanner.nextLine();
            
            // Validasi notasi infix
            if (isValidInfix(infixExpression)) {
                System.out.println("Notasi infix valid: " + infixExpression);
                
                // Konversi ke notasi postfix
                String postfixExpression = infixToPostfix(infixExpression);
                System.out.println("Notasi postfix: " + postfixExpression);
                
                // Konversi ke notasi prefix
                String prefixExpression = infixToPrefix(infixExpression);
                System.out.println("Notasi prefix: " + prefixExpression);
                
                // Evaluasi hasil operasi postfix
                double postfixResult = evaluatePostfix(postfixExpression);
                System.out.println("Hasil evaluasi postfix: " + postfixResult);
                
                // Evaluasi hasil operasi prefix
                double prefixResult = evaluatePrefix(prefixExpression);
                System.out.println("Hasil evaluasi prefix: " + prefixResult);
            } else {
                System.out.println("Notasi infix tidak valid!");
            }
        }
    }

    // Fungsi untuk mengecek apakah notasi infix valid
    public static boolean isValidInfix(String expression) {
        expression = expression.replaceAll("\\s+", "");

        if (expression.isEmpty()) {
            return false;
        }

        if (isOperator(expression.charAt(0)) && expression.charAt(0) != '(') {
            return false;
        }

        if (isOperator(expression.charAt(expression.length() - 1)) &&
                expression.charAt(expression.length() - 1) != ')') {
            return false;
        }

        LinkedListStack<Character> parenthesesStack = new LinkedListStack<>();
        boolean expectOperand = true;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == '(') {
                parenthesesStack.push(ch);
                expectOperand = true;
            } else if (ch == ')') {
                if (parenthesesStack.isEmpty() || parenthesesStack.pop() != '(') {
                    return false;
                }
                expectOperand = false;
            } else if (isOperator(ch)) {
                if (expectOperand) {
                    return false;
                }
                expectOperand = true;
            } else if (Character.isDigit(ch)) {
                if (!expectOperand && i > 0 && Character.isDigit(expression.charAt(i - 1))) {
                } else if (!expectOperand) {
                    return false;
                }
                expectOperand = false;
            } else {
                return false;
            }
        }

        return parenthesesStack.isEmpty();
    }

    public static String infixToPostfix(String infix) {
        infix = infix.replaceAll("\\s+", "");
        StringBuilder postfix = new StringBuilder();
        LinkedListStack<Character> stack = new LinkedListStack<>();

        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            if (Character.isDigit(ch)) {
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
                    number.append(infix.charAt(i++));
                }
                i--;
                postfix.append(number).append(" ");
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                }
            } else if (isOperator(ch)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(ch) && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(ch);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                stack.pop();
            } else {
                postfix.append(stack.pop()).append(" ");
            }
        }

        return postfix.toString().trim();
    }

    public static String infixToPrefix(String infix) {
        infix = infix.replaceAll("\\s+", "");
        StringBuilder reversedInfix = new StringBuilder();
        for (int i = infix.length() - 1; i >= 0; i--) {
            char ch = infix.charAt(i);
            switch (ch) {
                case '(' -> reversedInfix.append(')');
                case ')' -> reversedInfix.append('(');
                default -> reversedInfix.append(ch);
            }
        }

        String postfix = infixToPostfix(reversedInfix.toString());

        StringBuilder prefix = new StringBuilder();
        String[] tokens = postfix.split("\\s+");

        for (int i = tokens.length - 1; i >= 0; i--) {
            prefix.append(tokens[i]).append(" ");
        }

        return prefix.toString().trim();
    }

    public static double evaluatePostfix(String postfix) {
        LinkedListStack<Double> stack = new LinkedListStack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (!isOperator(token.charAt(0)) || token.length() > 1) {
                stack.push(Double.valueOf(token));
            } else {
                double operand2 = stack.pop();
                double operand1 = stack.pop();

                switch (token.charAt(0)) {
                    case '+' -> stack.push(operand1 + operand2);
                    case '-' -> stack.push(operand1 - operand2);
                    case '*' -> stack.push(operand1 * operand2);
                    case '/' -> stack.push(operand1 / operand2);
                    case '^' -> stack.push(Math.pow(operand1, operand2));
                }
            }
        }

        return stack.pop();
    }

    public static double evaluatePrefix(String prefix) {
        LinkedListStack<Double> stack = new LinkedListStack<>();
        String[] tokens = prefix.split("\\s+");

        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];

            if (!isOperator(token.charAt(0)) || token.length() > 1) {
                stack.push(Double.valueOf(token));
            } else {
                double operand1 = stack.pop();
                double operand2 = stack.pop();

                switch (token.charAt(0)) {
                    case '+' -> stack.push(operand1 + operand2);
                    case '-' -> stack.push(operand1 - operand2);
                    case '*' -> stack.push(operand1 * operand2);
                    case '/' -> stack.push(operand1 / operand2);
                    case '^' -> stack.push(Math.pow(operand1, operand2));
                }
            }
        }

        return stack.pop();
    }

    public static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '(' || ch == ')';
    }

    public static int precedence(char operator) {
        switch (operator) {
            case '+', '-' -> {
                return 1;
            }
 case '*', '/' -> {
     return 2;
            }
            case '^' -> {
                return 3;
            }
        }
        return -1;
    }
}
