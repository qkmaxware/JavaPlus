/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.particle;

import plus.math.Vector3;
import plus.graphics.Bitmap;
import plus.graphics.Sprite;

/**
 *
 * @author Colin Halseth
 */
public class Particle extends Sprite{
    
    private Vector3 velocity;
    private double LifeTime;
    private double MyLife;
    
    public Particle(double life, Bitmap image){
        super(image);
        this.velocity = Vector3.zero;
        this.LifeTime = life;
        MyLife = LifeTime;
    }
    
    public Particle(Particle p){
        super(p);
        this.velocity = p.velocity;
        this.LifeTime = p.LifeTime;
        MyLife = LifeTime;
    }
    
    /**
     * Copy the values of a prefab particle to this particle
     * @param p 
     */
    protected void ResetToPrefab(Particle p){
        this.LifeTime = p.LifeTime;
        this.MyLife = p.MyLife;
        this.sprite = p.sprite;
        this.velocity = p.velocity;
    }
    
    /**
     * Get the velocity of the particle
     * @return 
     */
    public Vector3 GetVelocity(){
        return this.velocity;
    }
    
    /**
     * Set the velocity of this particle
     * @param velocity 
     */
    public void SetVelocity(Vector3 velocity){
        this.velocity = velocity;
    }
    
    /**
     * Cause the particle to age by a given number of seconds. This only effects lifetime not the particle's transformation
     * @param seconds 
     */
    public void Age(double seconds){
        MyLife -= seconds;
    }
    
    /**
     * Test if this particle has died
     * @return 
     */
    public boolean HasDied(){
        return MyLife < 0;
    }
    
    /**
     * Get the age of the particle
     * @return 
     */
    public double GetAge(){
        return LifeTime - MyLife;
    }
    
    /**
     * Get how long this particle will live
     * @return 
     */
    public double GetLifetime(){
        return this.LifeTime;
    }
    
    /**
     * Set how long this particle will live. Will scale the age of the particle appropriately.
     * @param life
     * @return 
     */
    public void SetLifetime(double life){
        double difference = life - this.LifeTime;
        this.LifeTime = life;
        if(!HasDied())
            this.MyLife += difference;
    }
}
