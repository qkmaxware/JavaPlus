/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics.animation;

import plus.graphics.Bitmap;
import plus.system.Time;

/**
 *
 * @author Colin
 */
public class SpriteAnimation {
    
    private Bitmap[] frames;
    private int currentframe;
    private float runtime;
            
    private boolean stopped = true;
    private long starttime;
    
    public SpriteAnimation(Bitmap[] anim, float length){
        frames = anim;
        currentframe = 0;
        this.runtime = length;
    }
    
    /**
     * Play the animation
     */
    public void Play(){
        if(!stopped){
            return;
        }
        if(frames.length <=0 ){
            return;
        }
        Reset();
        this.stopped = false;
    }
    
    /**
     * Stop the animation
     */
    public void Stop(){
        this.stopped = true;
    }
    
    /**
     * Reset and stop the animation
     */
    public void Reset(){
        this.currentframe = 0;
        this.stopped = true;
        this.starttime = Time.Time();
    }
    
    /**
     * Get the total length of the animation in seconds
     * @return 
     */
    public float GetLength(){
        return this.runtime;
    }
    
    /**
     * Get the current animation frame
     * @return 
     */
    public Bitmap GetFrame(){
        return this.frames[currentframe];
    }
    
    /**
     * Get a frame in the animation
     * @param i
     * @return 
     */
    public Bitmap GetFrame(int i){
        return this.frames[i];
    }
    
    /**
     * Pick the next frame
     */
    public void Step(){
        if(!stopped){
            long time = Time.Time();
            
            double percent = Time.ToSeconds(time - starttime)/runtime;

            int index = (int)(percent*(this.frames.length-1));
            while(index > this.frames.length-1){
                index -= this.frames.length-1;
                starttime += Time.ToMilliseconds(runtime);
            }
            this.currentframe = index;
        }
    }
    
}
