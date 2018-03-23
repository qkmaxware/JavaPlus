/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.parse;

import java.util.Arrays;
import java.util.Collection;

/**
 * Restore Queue 
 * @author Colin Halseth
 */
public class RQueue<T> {
    
    private Object[] values;
    private int index = 0;
    
    public RQueue(Collection<T> values){
        this.values = new Object[values.size()];
        this.values = values.toArray(this.values);
    }
    
    /**
     * Get the number of elements remaining in the queue
     * @return 
     */
    public int size(){
        return Math.max(values.length - index, 0);
    }
    
    /**
     * Test if we reached the end of the queue
     * @return 
     */
    public boolean isEmpty(){
        return size() == 0;
    }
    
    /**
     * Get a point to restore this queue to
     * @return 
     */
    public int RestorePoint(){
        return index;
    }
    
    /**
     * Restore removed elements
     * @param point 
     */
    public void RestoreTo(int point){
        point = ((point > values.length - 1) ? (values.length - 1) : ((point < 0)? 0 : point));
        index = point;
    }
    
    /**
     * Look at but do not remove first element
     * @return 
     */
    public T peek(){
        if(isEmpty())
            return null;
        else
            return (T)values[index];
    }
    
    /**
     * Retrieve and remove the first element
     * @return 
     */
    public T pollFirst(){
        if(isEmpty())
            return null;
        else{
            T v = (T)values[index];
            index++;
            return v;
        }
    }
    
    /**
     * Move the head of the queue to the next element
     */
    public void next(){
       index++; 
    }
    
    public String toString(){
        return Arrays.toString(values);
    }
    
}
