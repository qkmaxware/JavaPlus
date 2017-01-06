/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.game;

import plus.graphics.Bitmap;
import plus.graphics.Camera;
import plus.graphics.RenderPanel;
import plus.system.Input;
import plus.system.TimerListener;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
/**
 *
 * @author Colin Halseth
 */
public class Game {
    
    private SceneManager manager = new SceneManager(this);
    private plus.system.Timer timer = new plus.system.Timer(0);
    private RenderPanel viewport;
    public final Input input;
    private Robot robot;
    
    public Game(){
        this.viewport = new RenderPanel(new Bitmap(640,480));
        this.input = new Input(this.viewport);
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            this.robot = null;
        }

        timer.AddListener(new TimerListener(){
            @Override
            public void OnTimerTick(double deltatime) {
                GameScene scene = manager.GetActive();
                
                if(scene != null){
                    //Early update
                    scene.EarlyUpdate(deltatime);

                    //Update
                    scene.Update(deltatime);

                    //Do physics
                    scene.PhysicsStep();

                    //Render output 
                    if(viewport != null && scene.camera != null && viewport.GetBitmap() != scene.camera.GetBitmap()){
                        viewport.SetBitmap(scene.camera.GetBitmap());
                    }
                    scene.Render();

                    //Late update
                    scene.LateUpdate(deltatime);

                    //Cleanup objects we want to destroy since we shouldn't remove objects while rendering is being done                
                    scene.ResolveQueue();
                }
                
                //Swap buffers from the draw buffer to the display buffer, change scenes if acked to
                viewport.SwapBuffers();
                manager.SwapScene();
                
            }
        });
    }
    
    /**
     * Get the scene manager
     * @return 
     */
    public SceneManager GetSceneManager(){
        return this.manager;
    }
 
    /**
     * Get the input reader for the game
     * @return 
     */
    public Input Input(){
        return this.input;
    }
    
    /**
     * Get the swing component this game will render it's output to
     * @return 
     */
    public RenderPanel GetViewport(){
        return this.viewport;
    }
    
    /**
     * Get the camera that is considered the active camera
     * @return 
     */
    public Camera GetActiveCamera(){
        return this.manager.GetActive()!=null?this.manager.GetActive().camera:null;
    }
    
    /**
     * Set the active camera that the active scene will render to
     * @param c 
     */
    public void SetActiveCamera(Camera c){
        if(this.manager.GetActive() != null){
            this.manager.GetActive().camera = c;
        }
    }
    
    /**
     * Center's the mouse in the viewport
     */
    public void CenterMouse(){
        if(!this.viewport.isVisible())
            return;
        
        Point point = this.viewport.getLocationOnScreen();
        this.MoveMouse((point.x + this.viewport.getWidth()*0.5f), (point.y + this.viewport.getHeight()*0.5f));
    }
    
    /**
     * Move the mouse to a desired coordinate onscreen
     * @param x
     * @param y 
     */
    public void MoveMouse(float x, float y){
        if(robot != null){
            this.robot.mouseMove((int)x, (int)y);
        }
    }
    
    /**
     * Safely add an object to the active scene. No effect if a scene is not yet loaded
     * @param obj 
     */
    public void Instanciate(GameObject obj){
        if(this.manager.GetActive() != null){
            this.manager.GetActive().Instanciate(obj);
        }
    }
    
    /**
     * Safely remove an object from the scene. no effect if a scene is not yet loaded
     * @param obj 
     */
    public void Destroy(GameObject obj){
        if(this.manager.GetActive() != null){
            this.manager.GetActive().Destroy(obj);
        }
    }
    
    /**
     * Start the game
     */
    public void Start(){
        timer.start();
    }
    
    /**
     * Stop the game
     */
    public void Stop(){
        timer.Stop();
    }
    
    /**
     * Is the game loop running
     * @return 
     */
    public boolean IsRunning(){
        return timer.isAlive();
    }
}
