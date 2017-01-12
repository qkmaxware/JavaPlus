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
public class Property<T> {
    
    private T value;
    
    public Property(T val){
        this.value = val;
    }
    
    public void Set(T val){
        this.value = val;
    }
    
    public T Get(){
        return this.value;
    }
    
}
