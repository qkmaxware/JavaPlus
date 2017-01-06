/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.game;

import plus.graphics.RenderObject;
import plus.graphics.Transform;
import java.util.LinkedList;

/**
 *
 * @author Colin Halseth
 */
public class GameObject{

    public String name;
    public String tag;
    
    private final Transform transform = new Transform();
    private RenderObject renderable;
    private plus.physics.Body body;
    
    protected LinkedList<UpdateListener> update_events = new LinkedList<UpdateListener>();
    protected LinkedList<UpdateListener> early_events = new LinkedList<UpdateListener>();
    protected LinkedList<UpdateListener> late_events = new LinkedList<UpdateListener>();
    
    public GameObject(String name){
        this.name = name;
    }
    
    public GameObject(String name, RenderObject renderable){
        this.name = name;
        this.renderable = renderable;
        this.renderable.SetParent(transform);
    }
    
    /**
     * Get the transform of this GameObject
     * @return 
     */
    public Transform GetTransform(){
        return transform;
    }
    
    /**
     * Get the renderable component
     * @return 
     */
    public RenderObject GetRenderable(){
        return renderable;
    }
    
    /**
     * Add an update listener
     * @param lisntener 
     */
    public void OnUpdate(UpdateListener listener){
        update_events.add(listener);
    }
    
    /**
     * Remove an update listener
     * @param listener 
     */
    public void RemoveUpdate(UpdateListener listener){
        update_events.remove(listener);
    }
    
    /**
     * Add an early update listener
     * @param lisntener 
     */
    public void OnEarlyUpdate(UpdateListener listener){
        early_events.add(listener);
    }
    
    /**
     * Remove an early update listener
     * @param listener 
     */
    public void RemoveEarlyUpdate(UpdateListener listener){
        early_events.remove(listener);
    }
    
    /**
     * Add a late update listener
     * @param lisntener 
     */
    public void OnLateUpdate(UpdateListener listener){
        late_events.add(listener);
    }
    
    /**
     * Remove a late update listener
     * @param listener 
     */
    public void RemoveLateUpdate(UpdateListener listener){
        late_events.remove(listener);
    }
}
