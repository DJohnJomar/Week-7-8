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
 * <term> =:: <factor> {+ | / factor}
 * <factor> =:: (<expression>) | <increment> | <decrement> | <number> | <identifier>
 * <increment> =:: <digit> ++;
 * <decrement> =:: <digit> --; 
 * <number =:: <digit> {<digit>}[.<digit>]
 * <identifier> =:: <letter> {<letter>}
 * <data type> =:: "int" |... |double
 * <digit> =:: "0"| ... | "9"
 * <letter> =:: "a" | ... | "Z"
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
        setupHashMap();// Used to fill up the hashmap with corresponding lexeme:token pairs
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

    /*
     * Parses for:
     * <arithmetic expression> =:: <data type> <identifier> = <expression>;
     * |<data type> <identifier> += <expression>; // The compounds operators
     * | <identifier> += <expression>; // The compounds operators
     * | <identifier> = <expression>;
     */
    private static void parseAssignment(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Assignment");
        String temp = "";
        index = 0;
        parseDataType(input);
        parseIdentifier(input);
        skipForWhiteSpaces();

        //if data type -> identifier -> = order
        if (index < input.length() && input.charAt(index) == '=') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseExpression(input);
            parseSemiColon(input);
        } 
        /*
         * if data type -> identifier -> += order
         * isOperator() checks if current character is an operator, most compound characters start with an operator
         * followd by the equal (=) sign
         */
        else if (index < input.length() && isOperator(input.charAt(index))) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;

            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;

            parseExpression(input);
            parseSemiColon(input);
        }
        //If nothing matches, then it is an error 
        else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected '=' at index " + index);
        }
        // System.out.println("-----------------Done Parsing Assignment");
    }

    /*
     * Parses for:
     * <expression> =:: <term> {+ | - <term>}
     */
    private static void parseExpression(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Expression");
        String temp = "";
        skipForWhiteSpaces();
        parseTerm(input);

        //Parses other terms
        while (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            temp = "";
            parseTerm(input);
        }
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Expression");
    }

    /*
     * Parses for:
     * <term> =:: <factor> {+ | / factor}
     */
    private static void parseTerm(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Term");
        String temp = "";
        skipForWhiteSpaces();
        parseFactor(input);

        //Parses for other factors
        while (index < input.length() && (input.charAt(index) == '*' || input.charAt(index) == '/')) {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            temp = "";
            parseFactor(input);
        }
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Term");
    }

    /*
     * Parses for:
     * <factor> =:: (<expression>) | <increment> | <decrement> | <number> | <identifier>
     * 
     */
    private static void parseFactor(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Factor");
        String temp = "";
        skipForWhiteSpaces();

        //checks for (<expression>)
        if (index < input.length() && input.charAt(index) == '(') {
            // Parse expression within parentheses
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            checkForToken(temp);
            index++;
            parseExpression(input);
            skipForWhiteSpaces();
            // System.out.println("-----------------Done Parsing Factor");
            if (index < input.length() && input.charAt(index) == ')') {
                // Check for closing parenthesis
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp = ")";
                checkForToken(temp);
                index++;
                skipForWhiteSpaces();
                // System.out.println("-----------------Done Parsing Factor");
            } else {
                throw new SyntaxErrorException("Expected ')' at index " + index);
            }
        }

        //checks for <number> | <increment> | <decrement> 
        else if (Character.isDigit(input.charAt(index))) {
            //Parses <increment>
            if (input.charAt(index + 1) == '+' && input.charAt(index + 2) == '+') {
                parseIncrement(input);
                // System.out.println("-----------------Done Parsing Factor");
            } 
            //Parses decrement
            else if (input.charAt(index + 1) == '-' && input.charAt(index + 2) == '-') {
                parseDecrement(input);
                // System.out.println("-----------------Done Parsing Factor");
            } 
            //Parses <number>
            else {
                parseNumber(input);
                // System.out.println("-----------------Done Parsing Factor");
            }
        } else {
            //Parses identifier
            parseIdentifier(input);
        }
        skipForWhiteSpaces();
    }

    /*
     * Parses for:
     * <identifier> =:: <letter> {<letter>}
     */
    private static void parseIdentifier(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Identifier");
        String temp = "";
        skipForWhiteSpaces();

        //Gathers all letters to temp as long as current character is a letter/digit/"_"
        if (index < input.length() && Character.isLetter(input.charAt(index))) {
            while (index < input.length()
                    && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp += input.charAt(index);
                index++;
            }
            result.add(temp + " : Identifier");//Similar function to checkForToken();
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected identifier at index " + index);
        }
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Identifier");
    }

    /*
     * Parses for:
     * <number =:: <digit> {<digit>}[.<digit>]
     */
    private static void parseNumber(String input) {
        System.out.println("-----------------Parsing Number");
        String temp = "";
        skipForWhiteSpaces();

        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '.') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
        }
        result.add(temp + " : " + identifyNumericType(temp));//Similar function to checkForToken()
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Number");
    }

    /*
     * Parses for:
     * <increment> =:: <digit> ++;
     */
    private static void parseIncrement(String input) {
        // System.out.println("-----------------Parsing Increment");
        String temp = "";
        skipForWhiteSpaces();
        parseNumber(input);//Parses the number

        //Gets the increment symbol
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '+') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            System.out.println(temp);
            temp += input.charAt(index);
            index++;
        }
        checkForToken(temp);
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Increment");
    }

    /*
     * Parses for:
     * <decrement> =:: <digit> ;
     */
    private static void parseDecrement(String input) {
        // System.out.println("-----------------Parsing Decrement");
        String temp = "";
        skipForWhiteSpaces();
        parseNumber(input);//Parses the number

        //Gets the decrement symbol
        while (index < input.length() && Character.isDigit(input.charAt(index)) || input.charAt(index) == '-') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
        }
        checkForToken(temp);
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Decrement");
    }

    /*
     * Parses for:
     * <data type> =:: "int" |... |"double"
     */
    private static void parseDataType(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Data Type");
        String temp = "";
        skipForWhiteSpaces();

        if (index < input.length() && Character.isLetter(input.charAt(index))) {
            while (index < input.length() && Character.isLetterOrDigit(input.charAt(index))
                    && input.charAt(index) != '=') {
                System.out.println("Character at index " + index + ": " + input.charAt(index));
                temp += input.charAt(index);
                index++;
            }
            if (!temp.isEmpty()) {
                hasDataType = checkForToken(temp);// method returns a boolean value if there is a match
                if (!hasDataType) {// If there is no data type, reset index to start
                    System.out.println("No Data Type");
                    index = 0;
                }
            }
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected data type keyword at index " + index);
        }
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Data Type");
    }

    //Checks for the semicolon
    private static void parseSemiColon(String input) throws SyntaxErrorException {
        // System.out.println("-----------------Parsing Semicolon");
        String temp = "";
        skipForWhiteSpaces();

        if (index < input.length() && input.charAt(index) == ';') {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            temp += input.charAt(index);
            index++;
            checkForToken(temp);
        } else {
            System.out.println("Character at index " + index + ": " + input.charAt(index));
            throw new SyntaxErrorException("Expected semicolon at index " + index);
        }
        skipForWhiteSpaces();
        // System.out.println("-----------------Done Parsing Semi Colon");
    }

    public static void skipForWhiteSpaces() {
        while (index < input.length() && input.charAt(index) == ' ') {
            System.out.println("Skipped Character: " + input.charAt(index));
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
