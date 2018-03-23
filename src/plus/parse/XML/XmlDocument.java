/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse.XML;

import java.util.ArrayList;
import java.util.LinkedList;
import plus.system.functional.Action1;

/**
 *
 * @author Colin Halseth
 */
public class XmlDocument {
    
    private ArrayList<XmlElement> elements = new ArrayList<XmlElement>();
    
    public void AddElement(XmlElement el){
        elements.add(el);
    }
    
    public boolean HasElements(){
        return !elements.isEmpty();
    }
    
    public void ForEachElement(Action1<XmlElement> fn){
        for(XmlElement el : elements){
            fn.Invoke(el);
        }
    }
    
    public XmlElement GetRootElement(){
        if(this.elements.isEmpty())
            return null;
        return this.elements.get(0);
    }
    
    public XmlElement FindElement(String str){
        for(XmlElement el : this.elements){
            if(el.nodeType.equals(str))
                return el;
        }
        return null;
    }
    
    public XmlElement[] FindAllElements(String name){
        LinkedList<XmlElement> list = new LinkedList<XmlElement>();
        for(XmlElement el : this.elements){
            if(el.nodeType.equals(name))
               list.add(el);
        }
        return list.toArray(new XmlElement[0]);
    }
    
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("<?XmlDocument?>\n");
        
        for(XmlElement el : elements)
            b.append(el.toString());
        
        return b.toString();
    }
    
}
