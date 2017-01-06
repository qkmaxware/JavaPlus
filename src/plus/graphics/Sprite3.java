/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.graphics.animation.SpriteAnimation;
import plus.math.Vector3;

/**
 *
 * @author Colin
 */
public class Sprite3 extends Geometry{
    
    private SpriteAnimation anim;
    
    public Sprite3(Bitmap image){
        
        super(new Vector3[]{
            new Vector3(-1,-1,0),
            new Vector3( 1,-1,0),
            new Vector3(-1, 1,0),
            new Vector3( 1, 1,0),
        },
        new int[]{
            0,1,2,
            1,2,3
        },
        new Vector3[]{
            new Vector3(0,1,0), new Vector3(1,1,0), new Vector3(0,0,0),
            new Vector3(1,1,0), new Vector3(0,0,0), new Vector3(1,0,0)
        });
        
        this.SetBitmap(image);
    }
    
    public Sprite3(Sprite3 spr){
        
        super(new Vector3[]{
            new Vector3(-1,-1,0),
            new Vector3( 1,-1,0),
            new Vector3(-1, 1,0),
            new Vector3( 1, 1,0),
        },
        new int[]{
            0,1,2,
            1,2,3
        },
        new Vector3[]{
            new Vector3(0,1,0), new Vector3(1,1,0), new Vector3(0,0,0),
            new Vector3(1,1,0), new Vector3(0,0,0), new Vector3(1,0,0)
        });
        
        this.SetBitmap(spr.texture);
        this.anim = spr.anim;
        
        this.SetLocalPosition(spr.GetLocalPosition());
        this.SetLocalRotation(spr.GetLocalRotation());
        this.SetLocalScale(spr.GetLocalScale());
    }
    
    /**
     * Give this sprite an animation sequence to play
     * @param anim 
     */
    public void SetAnimation(SpriteAnimation anim){
        this.anim = anim;
    }
    
    /**
     * Get the animation sequence of this sprite
     * @return 
     */
    public SpriteAnimation GetAnimation(){
        return this.anim;
    }
    
    /**
     * Render the sprite to a camera
     * @param cam 
     */
    @Override
    public void Render(Camera cam) {
        //if animation, animation.step; animation.GetFrame();
        if(this.anim != null){
            this.anim.Step();
            this.SetBitmap(this.anim.GetFrame());
        }
        super.Render(cam);
    }
}
