/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.game;

import plus.graphics.RenderObject;
import plus.graphics.Transform;
import java.util.LinkedList;
import plus.system.functional.Action2;

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
    
    protected LinkedList<Action2<GameObject, Double>> update_events = new LinkedList<Action2<GameObject, Double>>();
    protected LinkedList<Action2<GameObject, Double>> early_events = new LinkedList<Action2<GameObject, Double>>();
    protected LinkedList<Action2<GameObject, Double>> late_events = new LinkedList<Action2<GameObject, Double>>();
    
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
    public void OnUpdate(Action2<GameObject, Double> listener){
        update_events.add(listener);
    }
    
    /**
     * Remove an update listener
     * @param listener 
     */
    public void RemoveUpdate(Action2<GameObject, Double> listener){
        update_events.remove(listener);
    }
    
    /**
     * Add an early update listener
     * @param lisntener 
     */
    public void OnEarlyUpdate(Action2<GameObject, Double> listener){
        early_events.add(listener);
    }
    
    /**
     * Remove an early update listener
     * @param listener 
     */
    public void RemoveEarlyUpdate(Action2<GameObject, Double> listener){
        early_events.remove(listener);
    }
    
    /**
     * Add a late update listener
     * @param lisntener 
     */
    public void OnLateUpdate(Action2<GameObject, Double> listener){
        late_events.add(listener);
    }
    
    /**
     * Remove a late update listener
     * @param listener 
     */
    public void RemoveLateUpdate(Action2<GameObject, Double> listener){
        late_events.remove(listener);
    }
}
