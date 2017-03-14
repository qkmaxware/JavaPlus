/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.structures;

/**
 *
 * @author Colin Halseth
 */
public class Tuple {
    
    private int size;
    private Class[] types;
    private Object[] values;
    
    public Tuple(Object... values){
        this.size = values.length;
        this.values = values;
        this.types = new Class[values.length];
        for(int i = 0; i < values.length; i++){
            this.types[i] = values[i].getClass();
        }
    }
    
    public Tuple(Tuple t){
        this.size = t.size;
        this.types = t.types.clone();
        this.values = t.values.clone();
    }
    
    /**
     * Get the number of elements in this tuple
     * @return 
     */
    public int Count(){
        return size;
    }
    
    /**
     * Get a value in the tuple at a given index
     * @param i
     * @return 
     */
    public Object GetValue(int i){
        return values[i];
    }
    
    /**
     * Get the type of the value in this tuple at a given index
     * @param i
     * @return 
     */
    public Class GetType(int i){
        return this.types[i];
    }
    
    /**
     * Get the types of all values in this tuple
     * @return 
     */
    public Class[] GetTypes(){
        return this.types;
    }
    
    /**
     * Test if two tuples have matching types
     * @param t
     * @return 
     */
    public boolean MatchesType(Tuple t){
        if(this.size != t.size)
            return false;
        for(int i = 0; i < this.types.length; i++){
            if(!this.types[i].equals(t.values[i]))
                return false; 
        }
        return true;
    }
    
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        boolean comma = false;
        for(Object o : this.values){
            if(comma){
                buffer.append(",");
            }
            buffer.append(o.toString());
            comma = true;
        }
        return buffer.toString();
    }
    
}
