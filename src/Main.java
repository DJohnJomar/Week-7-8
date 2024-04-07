import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static HashMap<String, String> map = new HashMap<String, String>();
    static ArrayList<String> result = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        setupHashMap();
        while (true) {
            try {
                System.out.println("Enter your Java Arithmetic Expression: ");
                String string = reader.readLine();

                analyzeExpression(string);
                System.out.println("Result size: " + result.size());
                for (String str : result) {
                    System.out.println(str);
                }
            } catch (Exception e) {
                System.out.println("Invalid Input");
            }
        }
    }

    public static void analyzeExpression(String expression) {
        String temp = "";
        int length = expression.length();
        int index = 0;
        boolean keywordExist = false;
        // traverse through the expression
        // check for data type
        while (index < length) {
            if (expression.charAt(index) != ' ') {
                temp += expression.charAt(index);
                index++;
                if (checkForToken(temp) == true) {
                    temp = "";
                    keywordExist = true;
                    break;
                }
            } else {
                index++;
            }
        }
        // if there is no keyword, reset index and temp
        if (keywordExist == false) {
            index = 0;
            temp = "";
        }
        // check for identifier
        // while (index < length) {
        //     if (expression.charAt(index) != ' ') {
        //         temp += expression.charAt(index);
        //         // set up a skip, used to detect if it is next to an operator thus being it an identifier
        //         int skip = 1;
        //         while (expression.charAt(index + skip) == ' ') {
        //             skip++;
        //         }
        //         if (isOperator(expression.charAt(index + skip))) {
        //             result.add(temp + " : Identifier");
        //             temp = "";
        //             index += skip;
        //             break;
        //         }
        //         index++;
        //     } else {
        //         index++;
        //     }
        // }
        
        // Check for initial operator
        // if (expression.charAt(index) != ' ') {
        //     temp += expression.charAt(index);
        //     if (isOperator(expression.charAt(index + 1))) {
        //         temp += expression.charAt(index + 1);
        //     }
        //     index++;
        // }
        // Check for the rest
        while (index < length) {

            if (expression.charAt(index) != ' ') {
                temp += expression.charAt(index);
                System.out.println(temp);
                if (checkForToken(temp) == true) {
                    temp = "";
                } else if (Character.isDigit(expression.charAt(index)) == true) {
                    while (expression.charAt(index + 1) != ' ' && isOperator(expression.charAt(index + 1)) != true
                            && expression.charAt(index + 1) != ';') {
                        index++;
                        if (expression.charAt(index) != ' ') {
                            temp += expression.charAt(index);
                        }
                    }

                    result.add(temp + " : " + identifyNumericType(temp));
                    temp = "";
                } else {
                    result.add(temp + " : Identifier");
                    temp = "";
                }
            }
            index++;
        }
    }

    public static void checkIfValid() {
        // Check if there are operators
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
