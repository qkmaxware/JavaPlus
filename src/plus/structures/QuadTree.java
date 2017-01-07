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
public class QuadTree<T> extends Tree<T>{
    
    public static int NW = 0;
    public static int NE = 1;
    public static int SW = 2;
    public static int SE = 3;
    
    public QuadTree(T value){
        super(new TreeNode<T>(4, value));
    }
    
}
