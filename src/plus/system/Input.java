/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import plus.math.Vector3;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

/**
 *
 * @author Colin Halseth
 */
public class Input {

    private java.awt.Component panel;

    private HashMap<Integer, Boolean> currentValues = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> pressEvent = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> releaseEvent = new HashMap<Integer, Boolean>();

    private HashMap<Integer, Boolean> mousevalues = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> mousepressEvent = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> mousereleaseEvent = new HashMap<Integer, Boolean>();

    private double mouseX = 0; private double mx;
    private double mouseY = 0; private double my;
    
    public Input(java.awt.Component listeningpanel) {
        this.panel = listeningpanel;
        mx = this.MouseLocation().x();
        my = this.MouseLocation().y();
        
        panel.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                currentValues.put(e.getKeyCode(), true);
                pressEvent.put(e.getKeyCode(), true);
                releaseEvent.put(e.getKeyCode(), false);
            }

            public void keyReleased(KeyEvent e) {
                currentValues.put(e.getKeyCode(), false);
                pressEvent.put(e.getKeyCode(), false);
                releaseEvent.put(e.getKeyCode(), true);
            }

            public void keyTyped(KeyEvent e) {

            }
        });

        panel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent me) {
            }

            public void mousePressed(MouseEvent me) {
                int btn = me.getButton();
                mousevalues.put(btn,true);
                mousepressEvent.put(btn, true);
                mousereleaseEvent.put(btn,false);
            }

            public void mouseReleased(MouseEvent me) {
                int btn = me.getButton();
                mousevalues.put(btn,false);
                mousepressEvent.put(btn, false);
                mousereleaseEvent.put(btn,true);
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }
        });
        
        panel.addMouseMotionListener(new MouseMotionListener(){

            @Override
            public void mouseDragged(MouseEvent me) {}

            @Override
            public void mouseMoved(MouseEvent me) {
                mouseX = me.getXOnScreen() - mx;
                mx = me.getXOnScreen();
                mouseY = me.getYOnScreen() - my;
                my = me.getYOnScreen();
            }
        });
    }

    /**
     * Checks if a key is being held down
     *
     * @param keycode
     * @return
     */
    public boolean KeyDown(int keycode) {
        boolean b = (currentValues.containsKey(keycode)) ? currentValues.get(keycode) : false;
        return b;
    }

    /**
     * Checks if a key has been pressed since the last check
     *
     * @param keycode
     * @return
     */
    public boolean KeyPressed(int keycode) {
        if(this.pressEvent.containsKey(keycode) && this.pressEvent.get(keycode) == true){
            this.pressEvent.put(keycode, false);
            return true;
        }
        this.pressEvent.put(keycode, false);
        return false;
    }

    /**
     * Checks if a key has been released since the last check
     *
     * @param keycode
     * @return
     */
    public boolean KeyReleased(int keycode) {
        if(this.releaseEvent.containsKey(keycode) && this.releaseEvent.get(keycode) == true){
            this.releaseEvent.put(keycode,false);
            return true;
        }
        this.releaseEvent.put(keycode,false);
        return false;
    }

    /**
     * Checks if the mouse button is compressed
     *
     * @param mousebutton
     * @return
     */
    public boolean MouseDown(int mousebutton) {
        if(this.mousevalues.containsKey(mousebutton) && this.mousevalues.containsKey(mousebutton) == true){
            return true;
        }
        return false;
    }

    /**
     * Checks if the mouse button has been pressed since the last check
     * @param mousebutton
     * @return
     */
    public boolean MousePressed(int mousebutton) {
        boolean result = false;
        if(this.mousepressEvent.containsKey(mousebutton) && this.mousepressEvent.get(mousebutton) == true){
            result = true;
        }
        this.mousepressEvent.put(mousebutton, false);
        return result;
    }

    /**
     * Checks if the mouse button has been released since the last check
     * @param mousebutton
     * @return
     */
    public boolean MouseReleased(int mousebutton) {
        boolean result = false;
        if(this.mousereleaseEvent.containsKey(mousebutton) && this.mousereleaseEvent.get(mousebutton) == true){
            result = true;
        }
        this.mousepressEvent.put(mousebutton, false);
        return result;
    }

    /**
     * Distance the mouse has moved
     * @return 
     */
    public double MouseX(){
        return this.mouseX;
    }
    
    /**
     * Distance the mouse has moved
     * @return 
     */
    public double MouseY(){
        return this.mouseY;
    }
    
    /**
     * Get the mouse position on the listening component component
     * @return 
     */
    public Vector3 MouseLocation() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        return new Vector3(p.x, p.y);
    }
    
    /**
     * Get the mouse position on the panel this input object is listening to
     * @return 
     */
    public Vector3 MouseLocationOnPanel(){
        if(this.panel.isVisible()){
            Point p = this.panel.getMousePosition();
            if(p!=null)
            return new Vector3(p.x, p.y);
        }
        return null;
    }
}
