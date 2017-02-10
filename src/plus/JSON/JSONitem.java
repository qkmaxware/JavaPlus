/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.JSON;

/**
 *
 * @author Colin Halseth
 */
public class JSONitem implements JSONproperty {

    private Object object;
    private Class claz;
    
    public JSONitem(Object item){
        this.object = item;
        this.claz = (Class)item.getClass();
    }
    
    @Override
    public String ToJSON() {
        //Null is just null
        if(object == null)
            return "null";
        //Numberical values are not quoted
        if(object instanceof Double || object instanceof Float || object instanceof Long || object instanceof Integer || object instanceof Boolean)
            return object.toString();
        //Else its going to be saved as a string
        return "\""+object.toString()+"\"";
    }
    
}