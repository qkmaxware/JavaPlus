/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.math.Vector3;
import java.awt.Color;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Colin Halseth
 */
public class Sprite extends RenderObject {

    protected Bitmap sprite;
    protected Vector3 ImageCenter;
    
    public Sprite(Sprite spr){
        this.ImageCenter = spr.ImageCenter;
        this.sprite = spr.sprite;
    }
    
    public Sprite(Bitmap image){
        this.sprite = image;
         ImageCenter = new Vector3(sprite.GetWidth() / 2, sprite.GetHeight() / 2, 0);
    }
    
    public Sprite(String resource) {
        try {
            sprite = new Bitmap(ImageIO.read(new File(resource)));
            ImageCenter = new Vector3(sprite.GetWidth() / 2, sprite.GetHeight() / 2, 0);
        } catch (Exception e) {
            System.out.println("Resource: [\"" + resource + "\"] doesn't exist");
        }
    }

    /**
     * The bitmap representation of this sprite
     * @return 
     */
    public Bitmap GetRawImage() {
        return sprite;
    }
    
    /**
     * The width of this sprite
     * @return 
     */
    public int GetWidth(){
        return sprite.GetWidth();
    }
    
    /**
     * The height of this sprite
     * @return 
     */
    public int GetHeight(){
        return sprite.GetHeight();
    }
    
    /**
     * Render this sprite onto a camera
     * @param cam 
     */
    @Override
    public void Render(Camera cam) {
        //Project World space to Screen space
        Vector3 pos = cam.Project(this.GetMatrix(), Vector3.zero)[0];
        
        //Limit loop to only pixels that are visible onscreen. ie crop large images
        int PX = (int)ImageCenter.x();
        int PY = (int)ImageCenter.y();
        int top = Math.max(PY - (int)pos.y(),0);
        int bottom = Math.min(PY + (int)(cam.GetHeight() - pos.y()),sprite.GetHeight());
        int left = Math.max(PX - (int)pos.x(),0);
        int right = Math.min(PX + (int)(cam.GetWidth() - pos.x()),sprite.GetWidth());
        
        //System.out.println("Drawing from ("+left+"->"+right+") in "+sprite.getWidth());
        //System.out.println("Drawing from ("+top+"->"+bottom+") in "+sprite.getHeight());
        
        //Draw Sprite, if no sprite exists draw a red square
        if (sprite != null) {
            for(int i = left; i < right; i++){
                for(int j = top; j < bottom; j++){
                    //Get relative pixel postion
                    double dx = i - ImageCenter.x();
                    double dy = j - ImageCenter.y();

                    //Get pixel of sprite
                    int color = sprite.GetARGB(i, j);

                    int  red = (color & 0x00ff0000) >> 16;
                    int  green = (color & 0x0000ff00) >> 8;
                    int  blue = color & 0x000000ff;
                    int alpha = (color>>24) & 0xff;
                    Color x = new Color(red,green,blue,alpha);
                    
                    cam.SetPixel((int) (dx + pos.x()), (int) (dy + pos.y()), x);
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    float dx = i - 5;
                    float dy = j - 5;
                    cam.SetPixel((int) (i + pos.x()), (int) (j + pos.y()), Color.red);
                }
            }
        }
    }
}
