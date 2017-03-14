/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.structures;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import plus.system.functional.Action1;
import plus.system.functional.Func1;

/**
 *
 * @author Colin Halseth
 */
public class Table implements Iterable<Tuple>{
    
    private Tuple types;
    
    private HashMap<Integer, Tuple> rows = new HashMap<Integer, Tuple>();
    
    public Table(Class... types){
        this.types = new Tuple((Object[])types);
    }
    
    public Table(Tuple types){
        this.types = types;
    }
    
    /**
     * Number of elements in this table
     * @return 
     */
    public int Count(){
        return rows.size();
    }
    
    /**
     * Test if a tuple is contained in this table
     * @param t
     * @return 
     */
    public boolean Contains(Tuple t){
        return this.rows.containsValue(t);
    }
    
    /**
     * Insert a tuple into this table (only if it matches the table's schema)
     * @param t 
     * @return true if added successfully
     */
    public boolean Insert(Tuple t){
        if(t.MatchesType(this.types)){
            rows.put(Count(),t);
            return true;
        }
        return false;
    }
    
    /**
     * Insert a tuple into this table (only if it matches the table's schema)
     * @param values 
     * @return true if added successfully 
     */
    public boolean Insert(Object... values){
        return this.Insert(new Tuple(values));
    }
    
    
    /**
     * Remove a tuple from the table
     * @param t 
     */
    public void Remove(Tuple t){
        this.rows.values().remove(t);
    }
    
    /**
     * Remove a tuple from the table by a unique id.
     * @param i
     * @return 
     */
    public Tuple Remove(int i){
        return this.rows.remove(i);
    }

    /**
     * Get a tuple from this table by its unique id
     * @param i
     * @return 
     */
    public Tuple Get(int i){
        return rows.get(i);
    }

    /**
     * Iterate over all tuples in this table
     * @param fn 
     */
    public void ForEach(Action1<Tuple> fn){
        for(Tuple row : this){
            fn.Invoke(row);
        }
    }
    
    /**
     * Create a new table filtered from this table
     * @param filter
     * @return 
     */
    public Table Select(Func1<Tuple, Boolean> filter){
        Table t = new Table(this.types);
        for(Tuple row : this){
            if(filter.Invoke(row))
                t.Insert(row);
        }
        return t;
    }
    
    /**
     * Create a new table who's elements are sorted by a custom comparator
     * @param c
     * @return 
     */
    public Table OrderBy(Comparator<Tuple> c){
        LinkedList<Tuple> list = new LinkedList<Tuple>(this.rows.values());
        Collections.sort(list, c);
        Table t = new Table(this.types);
        for(Tuple row : list){
            t.Insert(row);
        }
        return t;
    }
    
    @Override
    public Iterator<Tuple> iterator() {
        return rows.values().iterator();
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer();
        for(Tuple t : this){
            buffer.append(t.toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }
    
}
