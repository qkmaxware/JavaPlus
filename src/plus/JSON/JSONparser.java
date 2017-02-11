/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.JSON;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Colin Halseth
 */
public class JSONparser {
    
    public static class Token{
        public Pattern regex;
        public String value;
        public String name;
        
        public Token(String name, String regex){
            this.regex = Pattern.compile(regex);
            this.name = name;
        }
        public Token(Token t){
            this.regex = t.regex;
            this.name = t.name;
        }
    }
    
    private static Token[] JSONtokens = new Token[]{
      new Token("ObjectStart", "^\\{"),
      new Token("ObjectEnd", "^\\}"),
      new Token("ArrayStart", "^\\["),
      new Token("ArrayEnd", "^\\]"),
      new Token("Boolean", "^(?:TRUE|FALSE)/i"),
      new Token("Double", "^(?:-?\\d+\\.\\d*)"),
      new Token("Long", "^(?:-?\\d+)"),
      new Token("Null", "^NULL/i"),
      new Token("String", "^(?:([\"'])(?:(?=(\\\\?))\\2.)*?\\1)"),
      new Token("Mapping", "^\\:"),
      new Token("Comma", "^\\,")
    };
    
    public JSONproperty Parse(String in){
        //Tokenize
        LinkedList<Token> tokens = Tokenize(in, JSONtokens);
        if(tokens == null){
            return null;
        }
        
        //Create objects/primitizes/lists
        return ParseJSON(tokens);
    }
    
    private JSONproperty ParseJSON(LinkedList<Token> tokens){
        JSONproperty p = ParseObject(tokens);
        if(p != null)
            return p;
        p = ParseArray(tokens);
        if(p != null)
            return p;
        p = ParsePrimative(tokens);
        if(p != null)
            return p;
        return null;
    }
    
    
    private boolean ParseOpenArray(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name == "ArrayStart"){
            tokens.pop();
            return true;
        }
        return false;
    }
    
    private boolean ParseCloseArray(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name == "ArrayEnd"){
            tokens.pop();
            return true;
        }
        return false;
    }
    
    private JSONproperty ParseArray(LinkedList<Token> tokens){
        if(!ParseOpenArray(tokens)){
            return null;
        }
        
        JSONarray array = new JSONarray();
        JSONproperty p = ParseJSON(tokens);
        if(p == null)
            return null;
        
        array.Add(p);
        
        while(ParseComma(tokens)){
            p = ParseJSON(tokens);
            if(p == null)
                return null;

            array.Add(p);
        }
        
        if(!ParseCloseArray(tokens)){
            return null;
        }
        
        return array;
    }
    
    private JSONproperty ParsePrimative(LinkedList<Token> tokens){
        if(tokens.size() == 0)
            return null;
        
        if(tokens.peek().name.equals("Boolean")){
            return new JSONitem(Boolean.parseBoolean(tokens.poll().value));
        }
        else if(tokens.peek().name.equals("Double")){
            return new JSONitem(Double.parseDouble(tokens.poll().value));
        }
        else if(tokens.peek().name.equals("Long")){
            return new JSONitem(Long.parseLong(tokens.poll().value));
        }
        else if(tokens.peek().name.equals("Null")){
            tokens.poll();
            return new JSONitem(null);
        }
        else if(tokens.peek().name.equals("String")){
            return new JSONitem(tokens.poll().value);
        }
        
        return null;
    }
    
    private String ParsePropertyName(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("String")){
            return tokens.poll().value;
        }
        return null;
    }
    
    private boolean ParseMap(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("Mapping")){
            tokens.pop();
            return true;
        }
        return false;
    }
    
    private Object[] ParseProperty(LinkedList<Token> tokens){
        String name = ParsePropertyName(tokens);
        if(name == null)
            return null;
        boolean isMap = ParseMap(tokens);
        if(!isMap)
            return null;
        JSONproperty prop = ParseJSON(tokens);
        if(prop == null)
            return null;
        return new Object[]{name, prop};
    }
    
    private boolean ParseOpenCurl(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("ObjectStart")){
            tokens.pop();
            return true;
        }
        return false;
    } 
    
    private boolean ParseCloseCurl(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("ObjectEnd")){
            tokens.pop();
            return true;
        }
        return false;
    } 
    
    private boolean ParseComma(LinkedList<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("Comma")){
            tokens.pop();
            return true;
        }
        return false;
    }
    
    private Object[][] ParseProperties(LinkedList<Token> tokens){
        LinkedList<Object[]> properties = new LinkedList<Object[]>();
        Object[] prop = ParseProperty(tokens);
        if(prop != null)
            properties.add(prop);
        
        while(ParseComma(tokens)){
            prop = ParseProperty(tokens);
            if(prop != null)
                properties.add(prop);
        }
        
        Object[][] ars = new Object[properties.size()][];
        ars = properties.toArray(ars);
        return ars;
    }
    
    private JSONproperty ParseObject(LinkedList<Token> tokens){
        if(!ParseOpenCurl(tokens)){
            return null;
        }
        Object obj[][] = ParseProperties(tokens);
        JSONobject o = new JSONobject();
        for(int i = 0; i < obj.length; i++){
            o.Add((String)obj[i][0], (JSONproperty)obj[i][1]);
        }
        if(!ParseCloseCurl(tokens)){
            return null;
        }
        return o;
    }
    
    public LinkedList<Token> Tokenize(String in, Token[] tokens){
        in = in.trim();
        LinkedList<Token> list = new LinkedList<Token>();
        while(!in.isEmpty()){
            
            boolean hasmatch = false;
            for(Token tok : tokens){
                Matcher m = tok.regex.matcher(in);
                boolean is = m.find();
                if(is){
                    String match = in.substring(0, m.end());
                    in = in.substring(m.end()).trim();
                    hasmatch = true;
                    Token t = new Token(tok);
                    t.value = match.replaceAll("^[\"\']|[\"\']$", "");
                    list.add(t);
                    break;
                }
            }
            
            if(!hasmatch){
                System.out.println("Failed to match: " + in);
                return null;
            }
            
        }
        return list;
    }
    
}