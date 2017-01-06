/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.audio;

/**
 *
 * @author Colin
 */
public class Audioplayer {
    
    private Audioclip clip;
    
    public void SetClip(Audioclip c){
        Stop();
        ResetPosition();
        this.clip = c;
    }
    
    public Audioclip GetClip(){
        return clip;
    }
    
    public boolean IsPlaying(){
        return clip.clip.isRunning();
    }
    
    public void Stop(){
        if(IsPlaying())
            clip.clip.stop();
    }
    
    public void ResetPosition(){
        clip.clip.setFramePosition(0);
    }
    
    public void Play(){
        if(IsPlaying()){
            Stop();
            ResetPosition();
        }
        clip.clip.start();
    }
    
}
