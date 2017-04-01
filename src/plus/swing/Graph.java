/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import plus.math.Mathx;
import plus.system.functional.Func1;

/**
 *
 * @author Colin Halseth
 */
public class Graph extends JPanel{
    
    private ArrayList<Func1<Double,Double>> fns = new ArrayList<Func1<Double,Double>>();
    
    private Color lineColor = Color.red;
    private Color backColor = Color.white;
    private Color axisColor = Color.black;
    
    private double startX = -10;
    private double endX = 10;
    
    private double startY = -10;
    private double endY = 10;
    
    public void SetFunction(int f, Func1<Double, Double> function){
        fns.set(f, function);  
    }
    public void AddFunction(Func1<Double, Double> function){
        fns.add(function);  
    }
    
    public void SetRange(int a, int b){
        if(a < b) {
            startX = a;
            endX = b;
        }else{
            startX = b;
            endX = a;
        }
    }
    
    public void SetDomain(int a, int b){
        if(a < b) {
            startY = a;
            endY = b;
        }else{
            startY = b;
            endY = a;
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        if(fns.isEmpty())
            return;
        
        Graphics2D g2 = (Graphics2D)g;
        
        int pixelHeight = this.getHeight();
        int pixelWidth = this.getWidth();
        
        double width = this.getWidth();
        double height = this.getHeight();
        double YDifference = endY - startY;
        double XDifference = endX - startX;
        
        //Background Fill
        g2.setColor(backColor);
        g2.fillRect(0, 0, pixelWidth, pixelHeight);
        
        //Draw axis (x,y)
        g2.setColor(axisColor);
        double locX = + (endX - startX)/2 * (pixelWidth / XDifference);  //midpoint
        double locY = + (endY - startY)/2 * (pixelHeight / YDifference);
        g2.draw(new Line2D.Double(locX, 0, locX, pixelHeight));                 //Y
        g2.draw(new Line2D.Double(0, locY, pixelWidth, locY));                  //X
        
        //Draw lines
        g2.setColor(lineColor);
        for(Func1<Double,Double> fn : this.fns){
            if(fn != null)
            for(int i = 1; i < width; i++){
                double pointX1 = Mathx.Lerp(startX, endX, (i-1)/(width-1));
                double pointX2 = Mathx.Lerp(startX, endX, i/(width-1));

                double pos1 = fn.Invoke(pointX1);
                double pos2 = fn.Invoke(pointX2);

                double screenH1 = (endY-pos1)*pixelHeight/YDifference;
                double screenH2 = (endY-pos2)*pixelHeight/YDifference;

                g2.draw(new Line2D.Double(i-1, screenH1, i, screenH2));
            }
        }
    }
    
}
