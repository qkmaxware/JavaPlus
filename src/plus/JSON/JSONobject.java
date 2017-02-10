/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Colin Halseth
 */
public class JSONobject implements JSONproperty{
    
    private HashMap<String, JSONproperty> properties = new HashMap<String, JSONproperty>();
    
    public void Add(String var, JSONproperty prop){
        if(properties.containsKey(var))
            properties.remove(var);
        properties.put(var, prop);
    }
    
    public JSONproperty Get(String var){
        return properties.get(var);
    }
    
    private void Clear(){
        properties.clear();
    }
    
    private void Clear(String var){
        properties.remove(var);
    }

    @Override
    public String ToJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("{"); boolean isFirst = true;
        for(Map.Entry<String, JSONproperty> prop : this.properties.entrySet()){
            if(!isFirst){
                builder.append(", ");
            }
            builder.append("\"");
            builder.append(prop.getKey());
            builder.append("\":");
            builder.append(prop.getValue().ToJSON());
            isFirst = false;
        }
        builder.append("}");
        return builder.toString();
    }
    
}