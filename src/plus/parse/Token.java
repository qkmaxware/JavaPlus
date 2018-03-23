/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse;

import java.util.regex.Pattern;

/**
 *
 * @author Colin Halseth
 */
public class Token{
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
        
        public boolean Equals(Token other){
            return name.equals(other.name);
        }
        
        public String toString(){
            return name+": "+value;
        }
    }