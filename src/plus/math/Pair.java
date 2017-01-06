/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.math;

/**
 *
 * @author Colin Halseth
 */
public class Pair <T,V>{
    
    private T left;
    private V right;
    
    public Pair(T left, V right){
        this.left = left;
        this.right = right;
    }
    
    public T GetLeft(){
        return this.left;
    }
    
    public V GetRight(){
        return this.right;
    }
    
    @Override
    public int hashCode(){
        return this.left.hashCode() * this.right.hashCode();
    }
    
    public String toString(){
        return "<"+left.toString()+","+right.toString()+">";
    }
    
}
