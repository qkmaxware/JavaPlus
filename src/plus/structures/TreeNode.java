/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.structures;

import java.util.ArrayList;
import plus.system.functional.*;

/**
 *
 * @author Colin Halseth
 */
public class TreeNode<T> {
    
    private T value;
    private ArrayList<TreeNode<T>> children;
    private int maxSize = -1;
    private boolean noLimit = false;
    
    protected TreeNode(T value){
        noLimit = true;
        this.children = new ArrayList<TreeNode<T>>();
    }
    protected TreeNode(int children, T value){
        this.maxSize = children;
        this.children = new ArrayList<TreeNode<T>>(children);
        this.value = value;
    }
    protected TreeNode(int children, T value, boolean limit){
        this.maxSize = children;
        this.children = new ArrayList<TreeNode<T>>(children);
        this.value = value;
        noLimit = !limit;
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
        return this.children.get(i);
    }
    
    /**
     * Set the child at 'i'
     * @param i
     * @param child 
     */
    public void SetChild(int i, TreeNode<T> child){
        if(child.maxSize == this.maxSize || noLimit)
            children.set(i, child);
    }
    
    /**
     * Insert a child into this node if able to
     * @param childData 
     */
    public void Add(T childData){
        TreeNode<T> child = new TreeNode<T>(this.children.size(), childData, !this.noLimit);
        this.SetChild(this.children.size(), value);
    }
    
    /**
     * Set the child at 'i'
     * @param i
     * @param child 
     */
    public void SetChild(int i, T value){
        TreeNode<T> child = new TreeNode<T>(this.children.size(), value, !this.noLimit);
        SetChild(i, child);
    }
    
    /**
     * Use a function to insert a value into this node or it's children. 
     * @param fn takes the current node and the inserted value as its parameters 
     * @param Value 
     */
    public void InsertChild(Func2<TreeNode<T>, T, Integer> fn, T Value){
        Integer child = (Integer)fn.Invoke(this,Value);
        if(this.children.get(child) != null){
            this.children.get(child).InsertChild(fn, Value);
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
        for(int i = 0; i < this.children.size(); i++){
            if(this.children.get(i) != null)
                tree += this.children.get(i).toString()+",";
        }
        return tree + ")";
    }
}
