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
public class Triple <T,K,V>{
    
    private T left;
    private K middle;
    private V right;
    
    public Triple(T left,K mid, V right){
        this.left = left;
        this.right = right;
        this.middle = mid;
    }
    
    public T GetLeft(){
        return this.left;
    }
    
    public K GetMiddle(){
        return this.middle;
    }
    
    public V GetRight(){
        return this.right;
    }
 
    @Override
    public int hashCode(){
        return this.left.hashCode() * this.middle.hashCode() * this.right.hashCode();
    }
    
    public String toString(){
        return "<"+left.toString()+","+middle.toString()+","+right.toString()+">";
    }
    
}
