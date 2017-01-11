/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import plus.graphics.Bitmap;
import plus.graphics.Camera;
import plus.math.Vector3;
import plus.system.Debug;

/**
 *
 * @author Colin Halseth
 */
public class UiText extends Ui {

    private String txt;
    private Font font;
    private Color color = Color.BLACK;
    private Bitmap generatedBitmap;

    public UiText(String text){
        this.font = new JLabel().getFont(); //System default font
        this.SetText(text);
    }
    
    public UiText(String string, Color color){    
        this.font = new JLabel().getFont(); //System default font
        this.color = color;
        this.SetText(string);
    }
    
    public UiText(String string, Font font){    
        this.font = font;
        this.SetText(string);
    }
    
    public UiText(String string, Font font, Color color){     
        this.font = font;
        this.color = color;
        this.SetText(string);
    }
    
    public void SetFont(Font font){
        this.font = font;
    }
    
    public void SetFontColor(Color c){
        this.color = c;
    }
    
    public void SetText(String text){
        this.txt = text;
        
        FontMetrics metrics = new JLabel().getFontMetrics(font);
        
        int width = metrics.stringWidth(txt);
        int height = metrics.getHeight();
        
        BufferedImage img = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)img.getGraphics();
        g2.setColor(color);
        g2.setFont(font);
        g2.drawString(text, 0, height);
        
        generatedBitmap = new Bitmap(img);
    }
    
    public void Refresh(){
        this.SetText(this.txt);
    }
    
    @Override
    public void Render(Camera camera) {
        int ox = (int)(this.anchor.x() * camera.GetWidth() - this.origin.x() * generatedBitmap.GetWidth());
        int oy = (int)(this.anchor.y() * camera.GetHeight() - this.origin.y() * generatedBitmap.GetHeight());
        
        Vector3 pos = this.GetPosition();
        int x = (int)pos.x();
        int y = (int)pos.y();
        
        for(int i = 0; i < generatedBitmap.GetWidth(); i++){
            for(int j = 0; j < generatedBitmap.GetHeight(); j++){
                camera.SetPixel(ox+x+i, oy+y+j, generatedBitmap.GetColor(i, j));
            }
        }
    }
    
}
