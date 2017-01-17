/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import java.awt.Color;
import plus.math.Vector3;
import plus.graphics.Camera;
import plus.graphics.Geometry;
import plus.game.Game;
import plus.game.GameObject;
import plus.game.GameScene;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import plus.graphics.gui.Ui;
import plus.graphics.gui.UiText;
import plus.math.Quaternion;
import plus.system.Debug;

/**
 *
 * @author Colin Halseth
 */
public class Rendering {
    
    public static void main(String[] args){
        Game game = new Game();                                     //Create the game object
        
        //Create GameObjects. GameObjects recieve games events
        Geometry floor = new Geometry(Geometry.plane);
        floor.SetLocalScale(Vector3.one.scale(2));
        floor.SetLocalPosition(new Vector3(0,1,8));
        floor.SetLocalEulerAngles(new Vector3(0,0,45));
        floor.SetRenderMode(Geometry.RenderMode.Solid);

        //Should be
        // 0, 45, 0
        // 0, 0.4, 0, 0.9
        // 0, 45, 0
        //https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles look here maybe
        
        Quaternion q = Quaternion.identity;
        Quaternion m = Quaternion.Euler(new Vector3(0,45,0));
        Debug.Log(new Vector3(0,45,0));
        Debug.Log(m);
        Debug.Log(m.Euler());
        
        
        GameObject floorObj = new GameObject("Floor", floor);
        floorObj.OnUpdate((go, deltatime) -> {
            //go.transform.SetLocalRotation(go.transform.GetLocalRotation().mul(Quaternion.Euler(new Vector3(0,12*deltatime,0))));
        });
        
        Camera cam = new Camera(640,480);                           //Create a new camera who's render size is 640x480 pixels
        cam.SetRenderMode(Camera.RenderMode.Perspective);           //Use perspective rendering
       
        GameObject cameraObj = new GameObject("Camera");            //Create camera gameobject
        cam.SetParent(cameraObj.GetTransform());                    //Parent te camer's position to the gaemobject
        
        cam.SetLocalEulerAngles(new Vector3(0,0,0));                //Rotate the camera
        cam.SetLocalPosition(new Vector3(0,0,0));                   //Move the camera (note -y is up). I accidentaly inverted my axis

        UiText text = new UiText("RENDERING DEMO", Color.yellow);   //Create some text to draw on the camera
        text.origin = Ui.TopLeft;                                   //Anchor the text to the top left corner of the scene
        
        GameScene scene = new GameScene("Render",                   //Create 3d scene  
                new GameObject[]{floorObj, cameraObj},
                new Ui[]{text}
        );  
        scene.camera = cam;
        game.GetSceneManager().LoadScene(scene);                    //Set the active scene
        
        cameraObj.OnUpdate((obj, deltatime) -> {                    //Add an event listener
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
                            Vector3 nrot = (new Vector3(-dif.y(),dif.x(),0)).scale(12*deltatime);
                            Vector3 rotation = obj.transform.GetLocalEulerAngles().add(nrot);
                            obj.transform.SetLocalRotation(Quaternion.Euler(rotation));
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
