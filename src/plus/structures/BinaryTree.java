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
public class BinaryTree<T> extends Tree<T>{
    
    public static int left = 0;
    public static int right = 1;
    
    public BinaryTree(T value){
        super(new TreeNode<T>(2,value));
    }
    
}
