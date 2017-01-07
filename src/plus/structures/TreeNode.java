/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.structures;

import plus.system.functional.*;

/**
 *
 * @author Colin Halseth
 */
public class TreeNode<T> {
    
    private T value;
    private TreeNode<T>[] children;
    
    protected TreeNode(int children){
        this.children = new TreeNode[children];
    }
    protected TreeNode(int children, T value){
        this.children = new TreeNode[children];
        this.value = value;
    }
    
    /**
     * Get the value stored in the node
     * @return 
     */
    public T GetValue(){
        return this.value;
    }
    
    /**
     * Get the child at index 'i'
     * @param i
     * @return 
     */
    public TreeNode<T> GetChild(int i){
        return this.children[i];
    }
    
    /**
     * Set the child at 'i'
     * @param i
     * @param child 
     */
    public void SetChild(int i, TreeNode<T> child){
        if(child.children.length == this.children.length)
            children[i] = child;
    }
    
    /**
     * Set the child at 'i'
     * @param i
     * @param child 
     */
    public void SetChild(int i, T value){
        TreeNode<T> child = new TreeNode<T>(this.children.length, value);
        SetChild(i, child);
    }
    
    /**
     * Use a function to insert a value into this node or it's children. 
     * @param fn takes the current node and the inserted value as its parameters 
     * @param Value 
     */
    public void InsertChild(Func2<TreeNode<T>, T, Integer> fn, T Value){
        Integer child = (Integer)fn.Invoke(this,Value);
        if(this.children[child] != null){
            this.children[child].InsertChild(fn, Value);
        }else{
            this.SetChild(child, Value);
        }
    }
    
    /**
     * Cascade down this node and its children in calling the function at every node.
     * @param fn takes the current node a its only parameter
     */
    public void PreOrderCascade(Action1<TreeNode<T>> fn){
        fn.Invoke(this);
        for(TreeNode<T> child : this.children){
            if(child != null)
                child.PreOrderCascade(fn);
        }
    }
    
    /**
     * Cascade down this node and its children in calling the function at every node.
     * @param fn takes the current node a its only parameter
     */
    public void PostOrderCascade(Action1<TreeNode<T>> fn){
        for(TreeNode<T> child : this.children){
            if(child != null)
                child.PreOrderCascade(fn);
        }
        fn.Invoke(this);
    }
    
    public String toString(){
        String tree = this.value.toString()+"(";
        for(int i = 0; i < this.children.length; i++){
            if(this.children[i] != null)
                tree += this.children[i].toString()+",";
        }
        return tree + ")";
    }
}
