/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse.JSON;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plus.parse.RQueue;
import plus.parse.Token;
import plus.parse.Tokenizer;

/**
 *
 * @author Colin Halseth
 */
public class JSONparser {
    
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
        Tokenizer tokenizer = new Tokenizer(JSONtokens);
        RQueue<Token> tokens = tokenizer.Tokenize(in);
        if(tokens == null){
            return null;
        }
        
        //Create objects/primitizes/lists
        return ParseJSON(tokens);
    }
    
    private JSONproperty ParseJSON(RQueue<Token> tokens){
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
    
    
    private boolean ParseOpenArray(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name == "ArrayStart"){
            tokens.next();
            return true;
        }
        return false;
    }
    
    private boolean ParseCloseArray(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name == "ArrayEnd"){
            tokens.next();
            return true;
        }
        return false;
    }
    
    private JSONproperty ParseArray(RQueue<Token> tokens){
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
    
    private JSONproperty ParsePrimative(RQueue<Token> tokens){
        if(tokens.size() == 0)
            return null;
        
        if(tokens.peek().name.equals("Boolean")){
            return new JSONitem(Boolean.parseBoolean(tokens.pollFirst().value));
        }
        else if(tokens.peek().name.equals("Double")){
            return new JSONitem(Double.parseDouble(tokens.pollFirst().value));
        }
        else if(tokens.peek().name.equals("Long")){
            return new JSONitem(Long.parseLong(tokens.pollFirst().value));
        }
        else if(tokens.peek().name.equals("Null")){
            tokens.pollFirst();
            return new JSONitem(null);
        }
        else if(tokens.peek().name.equals("String")){
            return new JSONitem(tokens.pollFirst().value);
        }
        
        return null;
    }
    
    private String ParsePropertyName(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("String")){
            return tokens.pollFirst().value;
        }
        return null;
    }
    
    private boolean ParseMap(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("Mapping")){
            tokens.next();
            return true;
        }
        return false;
    }
    
    private Object[] ParseProperty(RQueue<Token> tokens){
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
    
    private boolean ParseOpenCurl(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("ObjectStart")){
            tokens.next();
            return true;
        }
        return false;
    } 
    
    private boolean ParseCloseCurl(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("ObjectEnd")){
            tokens.next();
            return true;
        }
        return false;
    } 
    
    private boolean ParseComma(RQueue<Token> tokens){
        if(tokens.size() > 0 && tokens.peek().name.equals("Comma")){
            tokens.next();
            return true;
        }
        return false;
    }
    
    private Object[][] ParseProperties(RQueue<Token> tokens){
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
    
    private JSONproperty ParseObject(RQueue<Token> tokens){
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
    
}