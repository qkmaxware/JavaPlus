/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.graphics.gui.Ui;
import plus.system.Debug;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Colin Halseth
 */
public class Scene{
    
    //Render layers containign all renderable objects in this scene
    private ArrayList<LinkedList<RenderObject>> renderlayers = new ArrayList<LinkedList<RenderObject>>(10);
    
    private LinkedList<Ui> gui = new LinkedList<Ui>();
    
    /**
     * Add an object to the scene model on render layer 'i'
     * @param i
     * @param tr 
     */
    public void Add(int i, RenderObject tr){
        //Add enough intermediate render layers to get to index i
        while(renderlayers.size()-1 < i){
            renderlayers.add(new LinkedList<RenderObject>());
        }
        renderlayers.get(i).add(tr);
    }
    
    /**
     * Add a ui element to this scene
     * @param el 
     */
    public void AddUi(Ui el){
        this.gui.add(el);
    }
    
    /**
     * Add several ui elements to this scene
     * @param els 
     */
    public void AddUi(Ui ... els){
        for(Ui ui : els){
            AddUi(ui);
        }
    }
    
    /**
     * Remove a ui element from this scene
     * @param el 
     */
    public void RemoveUi(Ui el){
        this.gui.remove(el);
    }
    
    /**
     * Remove several ui elements from this scene
     * @param els 
     */
    public void RemoveUi(Ui ... els){
        for(Ui ui : els){
            RemoveUi(ui);
        }
    }
    
    /**
     * Add an object to the scene model
     * @param tr 
     */
    public void Add(RenderObject tr){
        Add(0,tr);
    }
    
    /**
     * Add many objects to the scene model
     * @param trs 
     */
    public void Add(RenderObject ... trs){
        for(int i = 0; i< trs.length; i++){
            Add(trs[i]);
        }
    }
    
    /**
     * Add many objects to the scene model on render layer 'i'
     * @param i
     * @param trs 
     */
    public void Add(int i, RenderObject ... trs){
        for(int j = 0; j< trs.length; j++){
            Add(i,trs[j]);
        }
    }
    
    /**
     * Remove an object from the scene model
     * @param tr 
     */
    public void Remove(RenderObject tr){
        for(LinkedList<RenderObject> layer : renderlayers){
            layer.remove(tr);
        }
    }
    
    /**
     * Remove object at index 'j' from render layer 'i'
     * @param i
     * @param j 
     */
    public void Remove(int i, int j){
        renderlayers.get(i).remove(j);
    }
    
    /**
     * Remove an object from the scene model
     * @param i 
     */
    public void Remove(int i){
        Remove(0,i);
    }
    
    /**
     * Renders this scene to a camera
     * @param cam 
     */
    public void RenderTo(Camera cam){
        for(LinkedList<RenderObject> layer : renderlayers){
            if(layer.size() > 0)
            for(RenderObject obj : layer){
                if(obj != null && obj.IsActive)
                    obj.Render(cam);
            }
        }
        for(Ui ui : this.gui){
            ui.Render(cam);
        }
        cam.Flush();
    }
}
