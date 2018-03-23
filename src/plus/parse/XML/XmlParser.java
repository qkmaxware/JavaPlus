/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse.XML;

import java.util.LinkedList;
import plus.math.Pair;
import plus.parse.ParseException;
import plus.parse.RQueue;
import plus.parse.Token;
import plus.parse.Tokenizer;

/**
 *
 * @author Colin Halseth
 */
public class XmlParser {
    
    private static Token[] tokens = new Token[]{
        new Token("OpenDiamond","^\\<"),
        new Token("CloseDiamond","^(\\>)"),
        new Token("ForceCloseDiamond","^\\/\\>"),
        new Token("CloseTag","^\\/"),
        new Token("XmlDirective", "^[?!]"),
        new Token("Identifier","^[a-zA-Z_]\\w*"),
        new Token("Equals","^\\="),
        new Token("String", "^([\"])(?:(?=(\\\\?))\\2.)*?\\1"),
        new Token("Content", "^([^<>]+)")
    };

    public XmlDocument Parse(String text){
        Tokenizer tokenizer = new Tokenizer(tokens);
        RQueue<Token> myTokens = tokenizer.Tokenize(text);
        
        XmlDocument doc = new XmlDocument();
        
        try{
            LinkedList<XmlElement> elems = ParseElements(myTokens);
            if(elems != null)
                for(XmlElement el : elems)
                    doc.AddElement(el);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return doc;
    }
    
    private LinkedList<XmlElement> ParseElements(RQueue<Token> myTokens) throws ParseException{
        
        LinkedList<XmlElement> list = new LinkedList<XmlElement>();
        
        while(myTokens.size() > 0){
           int r = myTokens.RestorePoint();
           
           XmlElement elem = ParseXmlNode(myTokens);
           if(elem != null){
               list.add(elem);
               continue;
           }
           myTokens.RestoreTo(r);
           
           XmlDirective dir = ParseXmlDirective(myTokens);
           if(dir != null){
               //doc.AddElement(elem);
               continue;
           }
           myTokens.RestoreTo(r);
           
           break;
        }
        if(list.isEmpty())
            return null;
        
        return list;
    }

    /**
     * Is the next token a member of the given token species
     * @param queue
     * @param tok
     * @return 
     */
    private boolean IsToken(RQueue<Token> queue, Token tok){
        if(queue.isEmpty())
            return false;
        else{
            boolean b = queue.peek().Equals(tok);
            if(b)
                queue.pollFirst();
            return b;
        }
    }
    
    private boolean ParseOpenDiamond(RQueue<Token> queue){
        return IsToken(queue, tokens[0]);
    }
    
    private boolean ParseCloseDiamond(RQueue<Token> queue){
        return IsToken(queue, tokens[1]);
    }
    
    private boolean ParseForceCloseDiamond(RQueue<Token> queue){
        return IsToken(queue, tokens[2]);
    }
    
    private boolean ParseCloseTag(RQueue<Token> queue){
        return IsToken(queue, tokens[3]);
    }
    
    private boolean ParseDirectiveFlag(RQueue<Token> queue){
        return IsToken(queue, tokens[4]);
    }
    
    private boolean ParseEquals(RQueue<Token> queue){
        return IsToken(queue, tokens[6]);
    }
    
    public String ParseIdentifier(RQueue<Token> queue){
        if(!queue.isEmpty() &&queue.peek().Equals(tokens[5])){
            String s = queue.peek().value;
            queue.pollFirst();
            return s;
        }
        return null;
    }
    
    public String ParseString(RQueue<Token> queue){
        if(!queue.isEmpty() &&queue.peek().Equals(tokens[7])){
            String s = queue.peek().value;
            queue.pollFirst();
            return s;
        }
        return null;
    }
    
    public String ParseText(RQueue<Token> queue){
        if(!queue.isEmpty() && queue.peek().Equals(tokens[8])){
            String s = queue.peek().value;
            queue.pollFirst();
            return s;
        }
        return null;
    }
    
    public Pair<String,String> ParseAttribute(RQueue<Token> queue) throws ParseException{
        String s = ParseIdentifier(queue);
        if(s == null)
            return null;

        if(!ParseEquals(queue))
            return null;
        
        String v = ParseString(queue);
        v = v.replaceAll("^\\\"|\\\"$", ""); //remove ""
        if(v == null)
            throw new ParseException("Invalid attribute, missing value in pair");
        
        return new Pair<String,String>(s,v);
    }
    
    public LinkedList<Pair<String,String>> ParseAttributes(RQueue<Token> queue)throws ParseException{
        LinkedList<Pair<String,String>> list = new LinkedList<Pair<String,String>>();
        while(true){
            Pair<String,String> attr = ParseAttribute(queue);
            if(attr == null){
                break;
            }
            list.add(attr);
        }
        return list;
    }
    
    public XmlDirective ParseExplicitDirective(RQueue<Token> queue) throws ParseException{
        if(!ParseOpenDiamond(queue)){
            return null;
        }
        
        //Will have a start directive
        if(!ParseDirectiveFlag(queue)){
            return null;
        }
        
        //Parse tag name
        String s = ParseIdentifier(queue);
        if(s == null)
            return null;
        
        //Parse attributes
        LinkedList<Pair<String,String>> attrs = ParseAttributes(queue);
        
        //Might have an end directive
        ParseDirectiveFlag(queue);

        if(!ParseCloseDiamond(queue)){
            return null;
        }
        
        XmlDirective dir = new XmlDirective();
        //TODO
        
        return dir;
    }
    
    public XmlDirective ParseXmlDirective(RQueue<Token> queue) throws ParseException{
        int r = queue.RestorePoint();
        
        XmlDirective dir = ParseExplicitDirective(queue);
        if(dir != null)
            return dir;
        queue.RestoreTo(r);
        
        return null;
    }
    
    public Pair<XmlElement,Boolean> ParseXmlOpenTag(RQueue<Token> queue) throws ParseException{
        if(!ParseOpenDiamond(queue)){
            return null;
        }
        
        //Parse tag name
        String s = ParseIdentifier(queue);
        if(s == null)
            return null;
        
        //Parse attributes
        LinkedList<Pair<String,String>> attrs = ParseAttributes(queue);
        
         XmlElement elem = new XmlElement();
        //TODO
        elem.nodeType = s;
        for(Pair<String,String> pair : attrs)
            elem.AddAttribute(pair.GetLeft(), pair.GetRight());
        
        if(ParseForceCloseDiamond(queue)){
            return new Pair<XmlElement, Boolean>(elem, true);
        }

        if(!ParseCloseDiamond(queue)){
            return null;
        }
        
        return new Pair<XmlElement, Boolean>(elem, false);
    }
    
    public String ParseXmlCloseTag(RQueue<Token> queue) throws ParseException{
        if(!ParseOpenDiamond(queue)){
            return null;
        }
        
        //Parse '/' symbol
        if(!ParseCloseTag(queue)){
            return null;
        }
        
        //Parse tag name
        String s = ParseIdentifier(queue);
        if(s == null)
            return null;
        
        //Parse attributes
        LinkedList<Pair<String,String>> attrs = ParseAttributes(queue);

        if(!ParseCloseDiamond(queue)){
            return null;
        }
        
        return s;
    }
    
    public Pair<LinkedList<XmlElement>,String> ParseContent(RQueue<Token> queue) throws ParseException{
        LinkedList<XmlElement> xml = new LinkedList<XmlElement>();
        String string = null;
        
        while(true){
            LinkedList<XmlElement> children = ParseElements(queue);
            if(children != null){
                xml.addAll(children);
                continue;
            }
            
            String s = ParseText(queue);
            if(s != null){
                if(string == null)
                    string = s;
                else
                    string += s;
                continue;
            }
            
            s = ParseIdentifier(queue);
            if(s != null){
                if(string == null)
                    string = s;
                else
                    string += s;
                continue;
            }
            
            break;
        }
        
        return new Pair<LinkedList<XmlElement>,String>(xml,string);
    }
    
    public XmlElement ParseXmlNode(RQueue<Token> queue) throws ParseException{
        Pair<XmlElement,Boolean> tag = ParseXmlOpenTag(queue);
        if(tag == null)
            return null;
        
        XmlElement elem = tag.GetLeft();
        if(elem == null)
            return null;
        
        //Force close, no content
        if(tag.GetRight())
            return elem;
        
        Pair<LinkedList<XmlElement>,String> contents = ParseContent(queue);
        
        for(XmlElement el : contents.GetLeft()){
            elem.AddElement(el);
        }
        
        if(contents.GetRight() != null)
            elem.AddContent(contents.GetRight());
        
        String c = ParseXmlCloseTag(queue);
        if(c == null)
            throw new ParseException("Missing required close tag");
        if(!c.equals(elem.nodeType))
            return null;
        
        return elem;
    }
    
}
