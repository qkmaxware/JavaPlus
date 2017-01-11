/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics.gui;

import plus.graphics.Camera;
import plus.math.Vector3;

/**
 *
 * @author Colin Halseth
 */
public abstract class Ui{
    
    private Vector3 position = Vector3.zero;
    
    public static final Vector3 TopLeft = new Vector3(0,0);
    public static final Vector3 TopRight = new Vector3(1,0);
    public static final Vector3 TopMiddle = new Vector3(0.5f, 0);
    
    public static final Vector3 CenterLeft = new Vector3(0, 0.5f);
    public static final Vector3 CenterRight = new Vector3(1, 0.5f);
    public static final Vector3 CenterMiddle = new Vector3(0.5f, 0.5f);
    
    public static final Vector3 BottomLeft = new Vector3(0,1);
    public static final Vector3 BottomRight = new Vector3(1,1);
    public static final Vector3 BottomMiddle = new Vector3(0.5f, 1);
    
    /**
     * The position on the screen that all this gui Element is drawn relative to.
     */
    public Vector3 anchor = TopLeft;
    
    /**
     * The position on the image that acts like the center
     */
    public Vector3 origin = CenterMiddle;
    
    public void SetPosition(Vector3 pos){
        this.position = pos;
    }
    
    public Vector3 GetPosition(){
        return this.position;
    }
    
    public abstract void Render(Camera camera);
    
}
