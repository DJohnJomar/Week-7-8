//A java program that parses an arithmetic expression written also in java
//April 9, 2024
//3CS-A || Dimaunahan, Meneses

/*
 * The program follows the following BNF:
 * <arithmetic expression> =:: <data type> <identifier> = <expression>;
 *                             |<data type> <identifier> += <expression>; // The compounds operators
 *                             | <identifier> += <expression>; // The compounds operators
 *                             | <identifier> = <expression>;
 * <expression> =:: <term> {+ | - <term>}
 * term =:: <factor> {+ | / factor}
 * <factor> =:: (<expression) | <number> | <identifier> | <increment> | <decrement>
 * <number =:: <digit> {<digit>}[.<digit>]
 * <data type> =:: "int" |... |double
 * <digit> =:: "0"| ... | "9"
 * <letter> =:: "a" | ... | "Z"
 * <increment> =:: <digit> ++;
 * <decrement> =:: <digit> --; 
 */
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
    private static boolean hasDataType;

    public static void main(String[] args) throws Exception {
        setupHashMap();
        while (true) {
            try {
                System.out.println("Enter your Java Arithmetic Expression: ");
                input = reader.readLine();
                parseAssignment(input);
                System.out.println("Result size: " + result.size());
                for (String str : result) {
                    System.out.println(str);
                }
                result.clear();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Invalid Input");
                result.clear();
            }
        }
    }

    
    private static void parseAssignment(String input) throws SyntaxErrorException {
        //System.out.println("-----------------Parsing Assignment");
        String temp = "";
        index = 0;
        parseDataType(input);
        parseIdentifier(input); 
        checkForWhiteSpaces();
        if (index < input.length() && input.charAt(index) == '=') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseExpression(input);
            parseSemiColon(input);
        }else if(index < input.length() && isOperator(input.charAt(index))){
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
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
        //System.out.println("-----------------Done Parsing Assignment");
    }

    private static void parseExpression(String input) throws SyntaxErrorException {
       //System.out.println("-----------------Parsing Expression");
        String temp = "";
        checkForWhiteSpaces();
        parseTerm(input);
        while (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            temp = "";
            parseTerm(input);
        }
        checkForWhiteSpaces();
        //System.out.println("-----------------Done Parsing Expression");
    }

    private static void parseTerm(String input) throws SyntaxErrorException {
        //System.out.println("-----------------Parsing Term");
        String temp = "";
        checkForWhiteSpaces();
        parseFactor(input);
        while (index < input.length() && (input.charAt(index) == '*' || input.charAt(index) == '/')) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            temp = "";
            parseFactor(input);
        }
        checkForWhiteSpaces();
        //System.out.println("-----------------Done Parsing Term");
    }

    private static void parseFactor(String input) throws SyntaxErrorException {
       // System.out.println("-----------------Parsing Factor");
        String temp = "";
        checkForWhiteSpaces();
        if (index < input.length() && input.charAt(index) == '(') {
            // Parse expression within parentheses
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseExpression(input);
            checkForWhiteSpaces();
          //  System.out.println("-----------------Done Parsing Factor");
            if (index < input.length() && input.charAt(index) == ')') {
                // Check for closing parenthesis
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp = ")";
                checkForToken(temp);
                index++;
                checkForWhiteSpaces();
               // System.out.println("-----------------Done Parsing Factor");
            } else {
                throw new SyntaxErrorException("Expected ')' at index " + index);
            }
        } else if (Character.isDigit(input.charAt(index))) {
            // Parse number
            if (input.charAt(index + 1) == '+' && input.charAt(index + 2) == '+') {
                parseNumber(input);
                parseIncrement(input);
                System.out.println("-----------------Done Parsing Factor");
            } else if (input.charAt(index + 1) == '-' && input.charAt(index + 2) == '-') {
                parseNumber(input);
                parseDecrement(input);
            //    System.out.println("-----------------Done Parsing Factor");
            } else {
                parseNumber(input);
             //   System.out.println("-----------------Done Parsing Factor");
            }
        } else {
            // Parse identifier
            parseIdentifier(input);
        }
        checkForWhiteSpaces();
    }

    private static void parseIdentifier(String input) throws SyntaxErrorException {
      //  System.out.println("-----------------Parsing Identifier");
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
            System.out.println("temp: "+temp);
            throw new SyntaxErrorException("Expected identifier at index " + index);
        }
        checkForWhiteSpaces();
       // System.out.println("-----------------Done Parsing Identifier");
    }

    private static void parseNumber(String input) {
        System.out.println("-----------------Parsing Number");
        String temp = "";
        checkForWhiteSpaces();
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '.') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
        }
        result.add(temp + " : " + identifyNumericType(temp));
        checkForWhiteSpaces();
      //  System.out.println("-----------------Done Parsing Number");
    }

    private static void parseIncrement(String input){
      //  System.out.println("-----------------Parsing Increment");
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
      //  System.out.println("-----------------Done Parsing Increment");
    }
    private static void parseDecrement(String input){
      //  System.out.println("-----------------Parsing Decrement");
        String temp = "";
        checkForWhiteSpaces();
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '-') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
        }
        checkForToken(temp);
        checkForWhiteSpaces();
     //   System.out.println("-----------------Done Parsing Decrement");
    }

    private static void parseDataType(String input) throws SyntaxErrorException {
      //  System.out.println("-----------------Parsing Data Type");
        String temp = "";
        // Skip any leading whitespace
        checkForWhiteSpaces();

        if (index < input.length() && Character.isLetter(input.charAt(index))) {
            while (index < input.length() && Character.isLetterOrDigit(input.charAt(index)) && input.charAt(index) != '=') {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp += input.charAt(index);
                index++;
            }
            if (!temp.isEmpty()) {
                hasDataType = checkForToken(temp);//method returns a boolean value if there is a match
                if(!hasDataType){//If there is no data type, reset index to start
                    System.out.println("No Data Type");
                    index = 0;
                }
            }
        } 
        else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected data type keyword at index " + index);
        }

        // Skip any whitespace after the data type keyword
        checkForWhiteSpaces();
      //  System.out.println("-----------------Done Parsing Data Type");
    }

    private static void parseSemiColon(String input) throws SyntaxErrorException {
     //   System.out.println("-----------------Parsing Semicolon");
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
     //   System.out.println("-----------------Done Parsing Semi Colon");
    }
    public static void checkForWhiteSpaces(){
        while (index < input.length() && input.charAt(index) == ' ') {
            System.out.println("Skipped Character: "+input.charAt(index));
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
                System.out.println("Match: "+string+" and "+ map.get(key));
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
