import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static HashMap<String, String> map = new HashMap<String, String>();
    static ArrayList<String> result = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        setupHashMap();
        System.out.println("Enter your Arithmetic Expression: ");
        String string = reader.readLine();
        checkLexemesAndTokens(string);
        for (String str : result) {
            System.out.println(str);
        }
    }

    public static void checkLexemesAndTokens(String string){
        String temp = "";
        int length = string.length();
        //Looping through each character of the string and then storing those characters to temp
        for(int index = 0; index<length; index++){
           
            temp+=string.charAt(index);
            //Checking if temp exists on the hashmap
            for(String key: map.keySet()){
                System.out.println(key);
                //Checks if it is a compound arithmetic operation
                if(temp == "+" || temp == "-" || temp == "*" || temp == "/" || temp == "%"){
                    System.out.println("Here");
                    if(string.charAt(index+1) == '='){
                        
                        temp+=string.charAt(index+1);
                        result.add(temp+" : "+map.get(temp));                        
                        index +=1;
                        temp = "";
                        continue;
                    }

                }
                // if(temp == keys){
                // }
            }
        }
    }

    public static void setupHashMap(){
        
        map.put("byte", "Keyword");
        map.put("short","Keyword");
        map.put("int", "Keyword");
        map.put("long","Keyword");
        map.put("float","Keyword");
        map.put("double","Keyword");
        map.put("=","Equal Sign");
        map.put("+","Plus Sign");
        map.put("-","Minus Sign");
        map.put("*","Multiplication Sign");
        map.put("/","Division Sign");
        map.put("%", "Modulo Sign");
        map.put("++","Increment sign");
        map.put("--","Decrement Sign");
        map.put("+=","Compound Addition");
        map.put("-=","Compound Subtractions");
        map.put("*=","Compound Multiplication");
        map.put("/=","Compound Division");
        map.put("%=", "Compound Modulo");
        map.put("(", "Open Parenthesis");
        map.put(")", "Close Parenthesis");
    }
}
