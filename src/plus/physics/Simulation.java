/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.physics;

import plus.math.Vector3;
import plus.system.Time;
import java.util.ArrayList;

/**
 *
 * @author Colin Halseth
 */
public class Simulation {
    
    //List of all rigidbodies, uses arraylist for faster iterations
    private final ArrayList<Body> rigidbodies = new ArrayList<>(20);
    
    //Timer for discrete stepping
    private Time time;
    
    /**
     * Add a rigidbody to this simulation
     * @param body 
     */
    public void Add(Body body){
        this.rigidbodies.add(body);
    }
    
    /**
     * Remove a rigidbody from this simulation
     * @param body 
     */
    public void Remove(Body body){
        this.rigidbodies.remove(body);
    }
    
    /**
     * Perform one step in the simulation
     */
    public void Step(){
        //Difference in timestep
        if(this.time == null) 
            this.time = new Time();
        double deltatime = time.DeltaTime();
        
        //Go through all rigidbodies and move them
        for(int i = 0; i < rigidbodies.size(); i++){
            Body body = rigidbodies.get(i);
            
            //If this object is sleeping, ignore it.
            //Saves us computation cycles for objects that aren't moving at all
            //Bodies wake up automatically when forces are applied to them
            if(body.IsSleeping)
                return;
            
            //Apply forces to the object
            if(body.UseGravity)
                body.AddForce(Vector3.up.scale(-9.8f*deltatime));
            
            
            
            //Check for collisions
            
            //Update the body's position and rotation
            body.Update(deltatime);
        }
        
    }
}
