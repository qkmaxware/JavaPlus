/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import plus.structures.BinaryTree;
import plus.system.Function;
import java.util.LinkedList;
import plus.structures.TreeNode;
import plus.system.Debug;

/**
 *
 * @author Colin Halseth
 */
public class FunctionExample {
    
    static int sum = 0;
    
    public static void main(String[] args){
        
        //Create the binary tree
        BinaryTree<Integer> number = new BinaryTree<Integer>(5);
        Function insertFn = new Function(){
            public Integer Call(Object... args) {
                TreeNode<Integer> parent = (TreeNode<Integer>)args[0];
                Integer val = (Integer)args[1];
                if(val < parent.GetValue())
                    return 0;
                else return 1;
            }
        };
        
        //Use function to insert values into our trees at the desired position
        number.GetRoot().InsertChild(insertFn, 4);
        number.GetRoot().InsertChild(insertFn, 6);
        number.GetRoot().InsertChild(insertFn, 7);
        number.GetRoot().InsertChild(insertFn, 8);
        number.GetRoot().InsertChild(insertFn, 1);
        
        Debug.Log(number.toString());
        
        //Empty list of all objects who's value is < 5
        LinkedList<TreeNode<Integer>> lessThanFive = new LinkedList<>();
        
        //Create functions which we will use to filter through our tree
        Function filterFn = new Function(){
            @Override
            public Object Call(Object... args) {
                if(((TreeNode<Integer>)args[0]).GetValue() <= 5)
                    lessThanFive.add((TreeNode<Integer>)args[0]);
                return null;
            }  
        };
        
        Function sumFn = new Function(){
            public Object Call(Object... args) {
                if(args[0] != null)
                    sum += ((TreeNode<Integer>)args[0]).GetValue();
                return null;
            }
        };
        
        //Run through the tree and perform an actions on each node
        number.GetRoot().PreOrderCascade(sumFn);
        number.GetRoot().PreOrderCascade(filterFn);
        
        Debug.Log("Sum: "+sum);
        Debug.Log("There are: "+lessThanFive.size()+" numbers less than 5");
    }
}
