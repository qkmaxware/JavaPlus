/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.JSON;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Colin Halseth
 */
public class JSONarray implements JSONproperty{

    private ArrayList<JSONproperty> list = new ArrayList<JSONproperty>();
    
    public JSONarray(){}
    public JSONarray(Collection<JSONproperty> collection){
        list.addAll(collection);
    }
    
    public int Count(){
        return this.list.size();
    }
    
    public void Add(JSONproperty prop){
        list.add(prop);
    }
    
    public void Remove(JSONproperty prop){
        list.remove(prop);
    }
    
    public void Remove(int i){
        list.remove(i);
    }
    
    public void Clear(){
        list.clear();
    }
    
    @Override
    public String ToJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        
        boolean isFirst = true;
        for(JSONproperty prop : list){
            if(!isFirst){
                builder.append(", ");
            }
            isFirst = false;
            builder.append(prop.ToJSON());
        }
        
        builder.append("]");
        return builder.toString();
    }
    
}