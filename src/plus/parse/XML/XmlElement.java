/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import plus.system.functional.Action;
import plus.system.functional.Action1;
import plus.system.functional.Action2;

/**
 *
 * @author Colin Halseth
 */
public class XmlElement {
    
    public String nodeType;
    public final HashMap<String, String> attributes = new HashMap<String,String>();
    public final ArrayList<XmlElement> children = new ArrayList<XmlElement>();
    public final ArrayList<String> content = new ArrayList<String>();
    public XmlElement parent;
    
    public int CountElements(){
        return this.children.size();
    }
    
    public int CountAttributes(){
        return attributes.size();
    }
    
    public void ForEachElement(Action1<XmlElement> fn){
        for(XmlElement el : children){
            fn.Invoke(el);
        }
    }
    
    public void ForEachAttribute(Action2<String,String> fn){
        for(Entry<String,String> el : attributes.entrySet()){
            fn.Invoke(el.getKey(), el.getValue());
        }
    }
    
    public void AddElement(XmlElement el){
        children.add(el);
        el.parent = this;
    }
    
    public XmlElement GetElement(int elem){
        return this.children.get(elem);
    }
    
    public void AddAttribute(String attr, String value){
        this.attributes.put(attr, value);
    }
    
    public String GetAttribute(String attr){
        return this.attributes.get(attr);
    }
    
    public void AddContent(String attr){
        this.content.add(attr);
    }
    
    public String GetContent(){
        return String.join("\n", this.content);
    }
    
    public XmlElement FindElement(String str){
        for(XmlElement el : this.children){
            if(el.nodeType.equals(str))
                return el;
        }
        return null;
    }
    
    public XmlElement FindElementWithAttribute(String str){
        for(XmlElement el : this.children){
            if(el.GetAttribute(str) != null)
                return el;
        }
        return null;
    }
    
    public XmlElement FindElementWithValue(String attr, String value){
        for(XmlElement el : this.children){
            if(el.GetAttribute(attr).equals(value))
                return el;
        }
        return null;
    }
    
    public XmlElement[] FindAllElements(String name){
        LinkedList<XmlElement> list = new LinkedList<XmlElement>();
        for(XmlElement el : this.children){
            if(el.nodeType.equals(name))
               list.add(el);
        }
        return list.toArray(new XmlElement[0]);
    }
    
    public XmlElement[] FindAllElementsWithAttribute(String name){
        LinkedList<XmlElement> list = new LinkedList<XmlElement>();
        for(XmlElement el : this.children){
            if(el.GetAttribute(name) != null)
               list.add(el);
        }
        return list.toArray(new XmlElement[0]);
    }
    
    public XmlElement[] FindAllElementsWithValue(String attr, String value){
        LinkedList<XmlElement> list = new LinkedList<XmlElement>();
        for(XmlElement el : this.children){
            if(el.GetAttribute(attr).equals(value))
               list.add(el);
        }
        return list.toArray(new XmlElement[0]);
    }
    
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("<");
        b.append(nodeType);
        
        for(Entry pair : attributes.entrySet()){
            b.append(" ");
            b.append(pair.getKey());
            b.append("=\"");
            b.append(pair.getValue());
            b.append("\"");
        }
        
        b.append(">\n");
        
        for(XmlElement el : children)
            b.append(el.toString());
        
        for(String el : content){
            b.append(el.toString());
            b.append("\n");
        }
        
        b.append("</");
        b.append(nodeType);
        b.append(">\n");
        
        return b.toString();
    }
    
}
