/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.audio;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Colin
 */
public class Audioclip {
 
    protected Clip clip;
    
    public Audioclip(Audioclip c){
        this.clip = c.clip;
    }
    
    public Audioclip(Clip c){
        this.clip = c;
    }
    
    public Audioclip(){
        this.clip = null;
    }
    
    public Audioclip(File f){
        this.clip = null;
        
        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
            Clip in = AudioSystem.getClip();
            in.open( audioIn );
            this.clip = in;
        }catch(Exception e){
            throw new RuntimeException("Audio file: "+f+" cannot be found.");
        }
    }
    
}
