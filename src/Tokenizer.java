import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    
    public static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                token.append(c);
            } else if (isOperator(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (c == '(' || c == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (!Character.isWhitespace(c)) {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }
        
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        
        return tokens;
    }
    
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    public static void main(String[] args) {
        String expression = "3.5 + (7 * 2) - 5 / 2";
        List<String> tokens = tokenize(expression);
        
        System.out.println("Tokens:");
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}