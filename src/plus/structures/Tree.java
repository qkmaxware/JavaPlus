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
public class Tree<T> {
    
    protected TreeNode<T> root;

    public Tree(T value){
        root = new TreeNode<T>(value);
    }
    
    public Tree(TreeNode<T> node){
        root = node;
    }
    
    /**
     * Get the root of the tree
     * @return 
     */
    public TreeNode<T> GetRoot(){
        return root;
    }
    
    public String toString(){
        return this.root.toString();
    }
}
