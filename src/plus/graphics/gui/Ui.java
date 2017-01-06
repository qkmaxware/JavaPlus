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
    public static final Vector3 BottomLeft = new Vector3(0,1);
    public static final Vector3 BottomRight = new Vector3(1,1);
    public static final Vector3 BottomMiddle = new Vector3(0.5f, 1);
    public Vector3 anchor = TopLeft;
    
    public Vector3 origin = TopLeft;
    
    public void SetPosition(Vector3 pos){
        this.position = pos;
    }
    
    public Vector3 GetPosition(){
        return this.position;
    }
    
    public abstract void Render(Camera camera);
    
}
