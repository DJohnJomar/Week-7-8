//A java program that parses an arithmetic expression written also in java
//April 9, 2024
//3CS-A || Dimaunahan, Meneses
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static HashMap<String, String> map = new HashMap<String, String>();
    private static ArrayList<String> result = new ArrayList<String>();
    private static int index;
    private static String input;

    public static void main(String[] args) throws Exception {
        setupHashMap();
        while (true) {
            try {
                System.out.println("Enter your Java Arithmetic Expression: ");
                input = reader.readLine();

                // analyzeExpression(string);
                parseAssignment(input);
                System.out.println("Result size: " + result.size());
                for (String str : result) {
                    System.out.println(str);
                }
                result.clear();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Invalid Input");
            }
            // System.out.println("Enter your Java Arithmetic Expression: ");
            // String input = reader.readLine();

            // //analyzeExpression(string);
            // parseAssignment(input);
            // System.out.println("Result size: " + result.size());
            // for (String str : result) {
            // System.out.println(str);
            // }
            // result.clear();
        }
    }

    private static void parseAssignment(String input) throws SyntaxErrorException {
        String temp = "";
        parseDataType(input); // Added parsing for data type
        parseIdentifier(input);
        if (input.charAt(index) == ' ') {
            index++;
        }
        if (index < input.length() && input.charAt(index) == '=') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseExpression(input);
            parseSemiColon(input);
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected '=' at index " + index);
        }
    }

    private static void parseExpression(String input) throws SyntaxErrorException {
        String temp = "";
        checkForWhiteSpaces();
        parseTerm(input);
        while (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseTerm(input);
        }
        checkForWhiteSpaces();
    }

    private static void parseTerm(String input) throws SyntaxErrorException {
        String temp = "";
        checkForWhiteSpaces();
        parseFactor(input);
        while (index < input.length() && (input.charAt(index) == '*' || input.charAt(index) == '/')) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseFactor(input);
        }
        checkForWhiteSpaces();
    }

    private static void parseFactor(String input) throws SyntaxErrorException {
        String temp = "";
        checkForWhiteSpaces();
        if (index < input.length() && input.charAt(index) == '(') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseExpression(input);
            System.out.println("heheCharacter at index " + index + ": " + input.charAt(index));
            checkForWhiteSpaces();
            if (index < input.length() && input.charAt(index) == ')') {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp = ")";
                checkForToken(temp);
                index++;
                checkForWhiteSpaces();
            } else {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                throw new SyntaxErrorException("Expected ')' at index " + index);
            }
        } else if (Character.isDigit(input.charAt(index))) {
            if(input.charAt(index+1) == '+' & input.charAt(index+2) == '+'){
                parseNumber(input);
                parseIncrement(input);
            }else if (input.charAt(index+1) == '-' & input.charAt(index+2) == '-'){
                parseNumber(input);
                parseDecrement(input);
            }else{
                parseNumber(input);
            }
        } else {
            parseIdentifier(input);
        }
        checkForWhiteSpaces();
    }

    private static void parseIdentifier(String input) throws SyntaxErrorException {
        String temp = "";
        checkForWhiteSpaces();
        if (index < input.length() && Character.isLetter(input.charAt(index))) {
            while (index < input.length()
                    && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp += input.charAt(index);
                index++;
            }
            result.add(temp + " : Identifier");
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected identifier at index " + index);
        }
        checkForWhiteSpaces();
    }

    private static void parseNumber(String input) {
        String temp = "";
        checkForWhiteSpaces();
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '.') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
        }
        result.add(temp + " : " + identifyNumericType(temp));
        checkForWhiteSpaces();
    }

    private static void parseIncrement(String input){
        String temp = "";
        checkForWhiteSpaces();
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '+') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            System.out.println(temp);
            temp += input.charAt(index);
            index++;
        }
        checkForToken(temp);
        checkForWhiteSpaces();
    }
    private static void parseDecrement(String input){
        String temp = "";
        checkForWhiteSpaces();
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '-') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
        }
        checkForToken(temp);
        checkForWhiteSpaces();
    }

    private static void parseDataType(String input) throws SyntaxErrorException {
        String temp = "";
        // Skip any leading whitespace
        checkForWhiteSpaces();

        if (index < input.length() && Character.isLetter(input.charAt(index))) {
            while (index < input.length() && Character.isLetterOrDigit(input.charAt(index))) {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp += input.charAt(index);
                index++;
            }
            if (!temp.isEmpty()) {
                checkForToken(temp);
            }
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected data type keyword at index " + index);
        }

        // Skip any whitespace after the data type keyword
        checkForWhiteSpaces();
    }

    private static void parseSemiColon(String input) throws SyntaxErrorException {
        String temp = "";
        checkForWhiteSpaces();
        if (index < input.length() && input.charAt(index) == ';') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
            checkForToken(temp);
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected semicolon at index " + index);
        }
        checkForWhiteSpaces();
    }
    public static void checkForWhiteSpaces(){
        while (index < input.length() && input.charAt(index) == ' ') {
            index++;
        }
    }

    public static String identifyNumericType(String str) {
        // Regular expressions to match different numeric types
        String byteRegex = "-?\\d+[bB]";
        String shortRegex = "-?\\d+[sS]";
        String intRegex = "-?\\d+";
        String longRegex = "-?\\d+[lL]";
        String floatRegex = "-?\\d+\\.\\d+[fF]?";
        String doubleRegex = "-?\\d+\\.\\d+([dD]|\\.)?";

        if (Pattern.matches(byteRegex, str)) {
            return "Byte Literal";
        } else if (Pattern.matches(shortRegex, str)) {
            return "Short Literal";
        } else if (Pattern.matches(intRegex, str)) {
            return "Integer Literal";
        } else if (Pattern.matches(longRegex, str)) {
            return "Long Literal";
        } else if (Pattern.matches(floatRegex, str)) {
            return "Float Literal";
        } else if (Pattern.matches(doubleRegex, str)) {
            return "Double Literal";
        } else {
            return "Not a numeric type";
        }
    }

    public static boolean checkForToken(String string) {
        boolean tokenMatch = false;
        for (String key : map.keySet()) {
            // System.out.println("String: "+string+" Key: "+ key);
            if (string.equals(key)) {
                result.add(string + " : " + map.get(key));
                tokenMatch = true;
                // System.out.println("Match: "+string+" and "+ map.get(key));
                break;
            }
        }
        return tokenMatch;
    }

    public static boolean isOperator(char character) {
        boolean isOperator = false;
        if (character == '=' || character == '+' || character == '-' || character == '*' || character == '/'
                || character == '%') {
            isOperator = true;
        }
        return isOperator;
    }

    public static void setupHashMap() {

        map.put("byte", "Keyword");
        map.put("short", "Keyword");
        map.put("int", "Keyword");
        map.put("long", "Keyword");
        map.put("float", "Keyword");
        map.put("double", "Keyword");
        map.put("=", "Equal Sign");
        map.put("+", "Plus Sign");
        map.put("-", "Minus Sign");
        map.put("*", "Multiplication Sign");
        map.put("/", "Division Sign");
        map.put("%", "Modulo Sign");
        map.put("++", "Increment sign");
        map.put("--", "Decrement Sign");
        map.put("+=", "Compound Addition");
        map.put("-=", "Compound Subtractions");
        map.put("*=", "Compound Multiplication");
        map.put("/=", "Compound Division");
        map.put("%=", "Compound Modulo");
        map.put("(", "Open Parenthesis");
        map.put(")", "Close Parenthesis");
        map.put(";", "Semicolon");
    }
}

class SyntaxErrorException extends Exception {
    public SyntaxErrorException(String message) {
        super(message);
    }
}
