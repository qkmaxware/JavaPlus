/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.particle;

import plus.graphics.Camera;
import plus.graphics.RenderObject;
import plus.system.Time;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Colin Halseth
 */
public class Emitter extends RenderObject{
    
    private double EmitRate = 1;        //1 particle per second
    private int MaxParticles = 1000;    //Max particles to display on the screen
    private Particle prefab;            //Prefab particles are made from, all particles are in world space
    
    private double newAt;
    private double count = 0;
    
    private boolean canemit = false;
    private LinkedList<Particle> active = new LinkedList<Particle>();
    private LinkedList<Particle> dead = new LinkedList<Particle>();
    private Time timer = new Time();
    
    public Emitter(Particle prefab){
        this.prefab = new Particle(prefab);
        
        newAt = 1/EmitRate;
    }
    
    /**
     * Set the particle emission rate
     * @param pps the number of particles per second
     */
    public void SetEmissionRate(int pps){
        this.EmitRate = pps;
        newAt = 1/EmitRate;
    }
    
    /**
     * Start the particle system
     */
    public void Start(){
        canemit = true;
        timer.Reset();
    }
    
    /**
     * Stop the particle system
     */
    public void Stop(){
        canemit = false;
    }
    
    /**
     * Stop the particle system, if true is given then all active particles are immediately destroyed as well.
     * @param immediate 
     */
    public void Stop(boolean immediate){
        if(immediate){
            for(Particle part : active){
                dead.add(part);
            }
            active.clear();
        }
        Stop();
    }

    /**
     * Perform one step in the particle simulation
     */
    public void Step(){
        
        //Emit new particles
        double deltatime = timer.DeltaTime();
        if(canemit){
            count += deltatime;
            int numToSpawn = (int)(count/newAt);
            for(int i = 0; i < numToSpawn; i++){
                if(active.size() + 1 < MaxParticles){
                    Particle p;
                    if(dead.size() > 0){
                        p = dead.getFirst();
                        dead.removeFirst();
                    }
                    else{
                        p = new Particle(prefab);
                    }
                    p.ResetToPrefab(this.prefab);
                    p.SetPosition(this.GetPosition());
                    active.add(p);
                }
            }
            count -= numToSpawn*newAt;
        }
        
        //Update all particles
        for (Iterator<Particle> iterator = active.iterator(); iterator.hasNext();) {
            Particle part = iterator.next();
            part.SetLocalPosition(part.GetLocalPosition().add(part.GetVelocity().scale(deltatime)));
            part.Age(deltatime);
            if(part.HasDied()){
                this.dead.add(part);
                iterator.remove();
            }
        }
    }
    
    /**
     * Get the number of active particles 
     * @return 
     */
    public int ActiveCount(){
        return this.active.size();
    }
    
    /**
     * Get the number of particles waiting to be spawned
     * @return 
     */
    public int DeadCount(){
        return this.dead.size();
    }
    
    @Override
    public void Render(Camera cam) {
        //Step the simulation
        Step();
        
        //Render the objects
        for(Particle part : active){
            part.Render(cam);
        }
    }
}
