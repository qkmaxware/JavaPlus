/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

/**
 *
 * @author Colin Halseth
 */
public abstract class RenderObject extends Transform{
    
    /**
     * Identifying tag
     */
    public String Name = "";
    
    /**
     * Function to set the identifying tag of this object
     * @param tag 
     */
    public void SetName(String tag){
        this.Name = tag;
    }
    
    /**
     * Tag if the object will be rendered or interacted with
     */
    public boolean IsActive = true;
    
    /**
     * Function to set the IsActive tag
     * @param active 
     */
    public void SetActive(boolean active){
        this.IsActive = active;
    }
    
    /**
     * Tag to identify if this object never changes its orientation or position. Can be post-processed to improve speed.
     */
    public boolean IsStatic = false;
    
    /**
     * Function to set the IsStatic flag
     * @param stat 
     */
    public void SetStatic(boolean stat){
        this.IsStatic = stat;
    }
    
    /**
     * Render this object to the camera
     * @param cam 
     */
    public abstract void Render(Camera cam);
}
