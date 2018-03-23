/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse;

import java.util.LinkedList;
import java.util.regex.Matcher;

/**
 *
 * @author Colin Halseth
 */
public class Tokenizer {
    
    private Token[] tokens;
    
    public Tokenizer(Token[] tokens){
        this.tokens = tokens;
    }
    
    public RQueue<Token> Tokenize(String in){
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
                    t.value = match;
                    list.add(t);
                    break;
                }
            }
            
            if(!hasmatch){
                System.out.println("Failed to match: " + in);
                return null;
            }
            
        }
        return new RQueue(list);
    }
    
}
