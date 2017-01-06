/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.system.TimerListener;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * A panel that can render a bitmap image, usually from a Camera object
 * @author Colin Halseth
 */
public class RenderPanel extends Canvas implements TimerListener {

    protected Bitmap bit;
    protected BufferStrategy bufStrat;
    
    //bitmap displays
    protected BufferedImage img;
    protected int[] imgdata;

    public RenderPanel(Bitmap bitmap) {
        this.SetBitmap(bitmap);
        
        //Default to the size of the bitmap
        Dimension size = new Dimension(bitmap.GetWidth(), bitmap.GetHeight());
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.setFocusable(true);
    }

    /**
     * Sets a new bitmap to render to the panel
     * @param bitmap 
     */
    public void SetBitmap(Bitmap bitmap){
        if(bitmap == null)
            return;
        
        this.bit = bitmap;
        Dimension size = new Dimension(bitmap.GetWidth(), bitmap.GetHeight());
        img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        imgdata = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }
    
    /**
     * Get a reference to this panel's output bitmap
     * @return 
     */
    public Bitmap GetBitmap(){
        return this.bit;
    }
    
    /**
     * Initializes the double buffer for this RenderPanel. Must already be
     * visible
     */
    public void InitBuffer() {
        this.createBufferStrategy(1);
        this.bufStrat = this.getBufferStrategy();
    }

    public void SwapBuffers() {
        //private float xScaleFactor, yScaleFactor = ...; 
        //private BufferedImage originalImage = ...; public void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D)g; int newW = (int)(originalImage.getWidth() * xScaleFactor); int newH = (int)(originalImage.getHeight() * yScaleFactor); g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); g2.drawImage(originalImage, 0, 0, newW, newH, null); } 
        if (this.getParent() != null) {
            if (this.bufStrat == null) {
                InitBuffer();
            }
        } else {
            return;
        }

        Graphics2D g = (Graphics2D) this.bufStrat.getDrawGraphics();
        int[] raster = bit.GetRaster();
        System.arraycopy(raster, 0, imgdata, 0, imgdata.length);
        //auto-scale image to canvas size
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        g.dispose();
        this.bufStrat.show();
    }

    //LISTENERS BELOW
    /**
     * Event called on every timer 'tick', first param is the time since last
     * tick
     *
     * @param deltatime
     */
    @Override
    public void OnTimerTick(double deltatime) {
        this.SwapBuffers();
    }
}
