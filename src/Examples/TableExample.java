/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import java.util.Comparator;
import plus.structures.Table;
import plus.structures.Tuple;
import plus.system.Debug;

/**
 *
 * @author Colin Halseth
 */
public class TableExample {
 
    public static void main(String[] args){
        
        //Basic Table use
                              //Name        , Id #
        Table table = new Table(String.class, Integer.class);
        
        table.Insert("Alison Tester",2);
        table.Insert("Joe Example",1);
        
        Debug.Log(table);
        
        //Table ordering
        Comparator<Tuple> idSorter = new Comparator<Tuple>() {
            @Override
            public int compare(Tuple t, Tuple t1) {
                int id1 = (Integer)t.GetValue(1);
                int id2 = (Integer)t1.GetValue(1);
                return Integer.compare(id1, id2);
            }
        };
        
        Table sorted = table.OrderBy(idSorter);
        Debug.Log(sorted);
        
        //Table selection
        Table joe = table.Select((row) -> {
            return (Boolean)((String)row.GetValue(0)).contains("Joe");
        });
        
        Debug.Log(joe);
    }
    
}
