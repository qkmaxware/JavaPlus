/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics.gui;

import plus.graphics.Bitmap;
import plus.graphics.Camera;
import plus.math.Vector3;

/**
 *
 * @author Colin Halseth
 */
public class UiImage extends Ui{

    public Bitmap image;
    
    public UiImage(Bitmap image){
        this.image = image;
    }
    
    @Override
    public void Render(Camera camera) {
        if(image == null)
            return;
        
        int ox = (int)(this.anchor.x() * camera.GetWidth() - this.origin.x() * image.GetWidth());
        int oy = (int)(this.anchor.y() * camera.GetHeight() - this.origin.y() * image.GetHeight());
        
        Vector3 pos = this.GetPosition();
        int x = (int)pos.x();
        int y = (int)pos.y();
        
        for(int i = 0; i < image.GetWidth(); i++){
            for(int j = 0; j < image.GetHeight(); j++){
                camera.SetPixel(ox+x+i, oy+y+j, image.GetColor(i, j));
            }
        }
    }
    
}
