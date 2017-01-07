/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import plus.system.Resources;
import plus.math.Vector3;
import plus.graphics.Camera;
import plus.graphics.Geometry;
import plus.game.Game;
import plus.game.GameObject;
import plus.game.GameScene;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author Colin Halseth
 */
public class Rendering {
    
    public static void main(String[] args){
        //Load all system resources we are going to use
        Resources.LoadImage("resources/textures/dry.jpg");
        Resources.LoadImage("resources/textures/grass.png");
        Resources.LoadImage("CrateSheet.png");
        Resources.LoadObject("cube.obj");
        
        Game game = new Game();                                     //Create the game object
        
        GameScene scene = new GameScene("Render");                  //Create 3d scene     
        
        //Create GameObjects
        Geometry floor = new Geometry(Geometry.plane);
        floor.SetLocalScale(Vector3.one.scale(2));
        floor.SetLocalPosition(new Vector3(0,1,8));
        floor.SetLocalEulerAngles(new Vector3(90,0,0));
        floor.SetRenderMode(Geometry.RenderMode.Solid);
        floor.SetBitmap(Resources.GetImage("grass"));

        GameObject floorObj = new GameObject("Floor", floor);
        scene.Instanciate(floorObj);
        
        Camera cam = new Camera(640,480);                           //Create a new camera who's render size is 640x480 pixels
        cam.SetRenderMode(Camera.RenderMode.Perspective);           //Use perspective rendering
       
        GameObject cameraObj = new GameObject("Camera");            //Create camera gameobject
        cam.SetParent(cameraObj.GetTransform());                    //Parent te camer's position to the gaemobject
        
        cam.SetLocalEulerAngles(new Vector3(0,0,0));                //Rotate the camera
        cam.SetLocalPosition(new Vector3(0,0,0));                   //Move the camera (note -y is up). I accidentaly inverted my axis
        
        scene.Instanciate(cameraObj);
        
        game.GetSceneManager().LoadScene(scene);                    //Set the active scene
        scene.camera = cam;

        cameraObj.OnUpdate((obj, deltatime) -> {
                //Camera movement -- fly camera behavior
                Vector3 movement = Vector3.zero;
                if(game.input.KeyDown(KeyEvent.VK_W)){
                    movement = movement.add(obj.GetTransform().Forward());
                }
                if(game.input.KeyDown(KeyEvent.VK_S)){
                    movement = movement.sub(obj.GetTransform().Forward());
                }
                if(game.input.KeyDown(KeyEvent.VK_A)){
                    movement = movement.sub(obj.GetTransform().Right());
                }
                if(game.input.KeyDown(KeyEvent.VK_D)){
                    movement = movement.add(obj.GetTransform().Right());
                }
                if(game.input.KeyDown(KeyEvent.VK_E)){
                    movement = movement.add(obj.GetTransform().Up());
                }
                if(game.input.KeyDown(KeyEvent.VK_Q)){
                    movement = movement.sub(obj.GetTransform().Up());
                }
                if(game.input.KeyDown(KeyEvent.VK_SHIFT)){
                    Vector3 mouse = game.input.MouseLocationOnPanel();
                    if(mouse != null){

                    Vector3 halfwidth = new Vector3(game.GetViewport().getWidth(),game.GetViewport().getHeight()).scale(0.5f);
                    Vector3 dif = mouse.sub(halfwidth);
                    if(dif.SquareMagnitude() > 10){
                        Vector3 rot = obj.GetTransform().GetLocalEulerAngles().add((new Vector3(-dif.y(),dif.x(),0)).scale(12*deltatime));
                        obj.GetTransform().SetLocalEulerAngles(rot);
                    }
                    game.CenterMouse();
                    }
                }
                
                //Framerate independance for movement -- speed 12
                Vector3 delta = movement.scale(12*deltatime);
                obj.GetTransform().SetLocalPosition(obj.GetTransform().GetLocalPosition().add(delta));
        });
        
        //Basic SWING components to house the engine
        JFrame frame = new JFrame();                                //Create the window that houses the game
        frame.setTitle("Render Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //Exit game when window is closed
        frame.add(game.GetViewport());                              //Add the game's viewport to the window
        frame.setSize(640, 480); 
        frame.setVisible(true);
        
        game.Start();                                               //Start the game's loop
    }
    
}
