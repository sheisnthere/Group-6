import java.util.Scanner;
import java.util.Stack;

class InfixConverter {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Program Konversi dan Evaluasi Notasi Infix ===");
            
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
        // Menghapus semua spasi
        expression = expression.replaceAll("\\s+", "");

        if (expression.isEmpty()) {
            return false;
        }

        // Cek apakah karakter pertama adalah operator (invalid)
        if (isOperator(expression.charAt(0)) && expression.charAt(0) != '(') {
            return false;
        }

        // Cek apakah karakter terakhir adalah operator (invalid)
        if (isOperator(expression.charAt(expression.length() - 1)) &&
                expression.charAt(expression.length() - 1) != ')') {
            return false;
        }

        Stack<Character> parenthesesStack = new Stack<>();

        // Cek pola operand dan operator
        boolean expectOperand = true;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Cek kurung
            if (ch == '(') {
                parenthesesStack.push(ch);
                expectOperand = true;
            } else if (ch == ')') {
                if (parenthesesStack.isEmpty() || parenthesesStack.pop() != '(') {
                    return false;
                }
                expectOperand = false;
            }
            // Cek operator
            else if (isOperator(ch)) {
                if (expectOperand) {
                    return false; // Operator tidak boleh berurutan
                }
                expectOperand = true;
            }
            // Cek operand (angka)
            else if (Character.isDigit(ch)) {
                if (!expectOperand && i > 0 && Character.isDigit(expression.charAt(i - 1))) {
                    // Melanjutkan digit angka yang sama, bukan operand baru
                } else if (!expectOperand) {
                    return false; // Operand tidak boleh berurutan kecuali digit multi-digit
                }
                expectOperand = false;
            } else {
                return false; // Karakter tidak dikenal
            }
        }

        // Pastikan semua kurung sudah tertutup
        return parenthesesStack.isEmpty();
    }

    // Fungsi untuk mengkonversi notasi infix ke postfix menggunakan stack
    public static String infixToPostfix(String infix) {
        // Menghapus semua spasi
        infix = infix.replaceAll("\\s+", "");

        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            // Jika karakter adalah operand (angka), tambahkan ke output
            if (Character.isDigit(ch)) {
                // Cek multi-digit
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
                    number.append(infix.charAt(i++));
                }
                i--; // Mengembalikan indeks karena increment terakhir terlalu jauh
                postfix.append(number).append(" ");
            }
            // Jika karakter adalah '(', push ke stack
            else if (ch == '(') {
                stack.push(ch);
            }
            // Jika karakter adalah ')', pop semua operator dari stack sampai '('
            else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }

                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // Membuang tanda '('
                }
            }
            // Jika karakter adalah operator
            else if (isOperator(ch)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(ch) && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(ch);
            }
        }

        // Pop semua operator yang tersisa di stack
        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                stack.pop(); // Membuang tanda '(' jika masih ada
            } else {
                postfix.append(stack.pop()).append(" ");
            }
        }

        return postfix.toString().trim();
    }

    // Fungsi untuk mengkonversi notasi infix ke prefix menggunakan stack
    public static String infixToPrefix(String infix) {
        // Menghapus semua spasi
        infix = infix.replaceAll("\\s+", "");

        // Balik string infix dan ganti kurung
        StringBuilder reversedInfix = new StringBuilder();
        for (int i = infix.length() - 1; i >= 0; i--) {
            char ch = infix.charAt(i);
            switch (ch) {
                case '(':
                    reversedInfix.append(')');
                    break;
                case ')':
                    reversedInfix.append('(');
                    break;
                default:
                    reversedInfix.append(ch);
                    break;
            }
        }

        // Konversi infix yang dibalik ke postfix
        String postfix = infixToPostfix(reversedInfix.toString());

        // Balik hasil postfix untuk mendapatkan prefix
        StringBuilder prefix = new StringBuilder();
        String[] tokens = postfix.split("\\s+");

        for (int i = tokens.length - 1; i >= 0; i--) {
            prefix.append(tokens[i]).append(" ");
        }

        return prefix.toString().trim();
    }

    // Fungsi untuk mengevaluasi ekspresi postfix menggunakan stack
    public static double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();

        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            // Jika token adalah operand (angka), push ke stack
            if (!isOperator(token.charAt(0)) || token.length() > 1) {
                stack.push(Double.valueOf(token));
            }
            // Jika token adalah operator
            else {
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

    // Fungsi untuk mengevaluasi ekspresi prefix menggunakan stack
    public static double evaluatePrefix(String prefix) {
        Stack<Double> stack = new Stack<>();

        String[] tokens = prefix.split("\\s+");

        // Evaluasi dari kanan ke kiri (akhir ke awal array)
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];

            // Jika token adalah operand (angka), push ke stack
            if (!isOperator(token.charAt(0)) || token.length() > 1) {
                stack.push(Double.valueOf(token));
            }
            // Jika token adalah operator
            else {
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

    // Fungsi untuk mengecek apakah karakter adalah operator
    public static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '(' || ch == ')';
    }

    // Fungsi untuk mendapatkan precedence (tingkat prioritas) operator
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
