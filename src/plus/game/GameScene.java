/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.game;

import plus.graphics.Camera;
import plus.graphics.RenderObject;
import plus.graphics.Scene;
import plus.physics.Simulation;
import java.util.LinkedList;
import plus.graphics.gui.Ui;
import plus.system.functional.Action2;

/**
 *
 * @author Colin Halseth
 */
public class GameScene{
        public String title;
        public Scene scene;
        public Simulation physics;
        
        //Store initial conditions
        public Camera camera;
        
        private LinkedList<GameObject> objs = new LinkedList<GameObject>();
        private LinkedList<GameObject> created = new LinkedList<GameObject>();
        private LinkedList<GameObject> destroyed = new LinkedList<GameObject>();
        
        private LinkedList<Ui> overlay_adds = new LinkedList<Ui>();
        private LinkedList<Ui> overlay_destroys = new LinkedList<Ui>();
        
        //private LinkedList<GameObject> backup_objs = new LinkedList<GameObject>();
        //private LinkedList<Transform> backup_transforms = new LinkedList<Transform>();
        
        public GameScene(String title){
            this.title = title;
            this.scene = new Scene();
            this.physics = new Simulation();
        }
        
        public GameScene(String title, GameObject[] objects, Ui[] overlays){
            this(title);
            if(objects != null)
            for(GameObject g : objects){
                this.Instanciate(g);
            }
            if(overlays != null)
            for(Ui g : overlays){
                this.AddUi(g);
            }
            this.ResolveQueue();
        }
        
        /*
        public void BackUp(){
            backup_objs.clear();
            backup_objs.addAll(objs);
            
            backup_transforms.clear();
            for(GameObject g : objs){
                backup_transforms.add(new Transform(g.GetTransform()));
            }
        }*/
        
        /**
         * Add an object to a list to be safely created next game loop
         * @param go 
         */
        public void Instanciate(GameObject go){
            created.add(go);
        }
        
        /**
         * Add an object to a list to be safely removed next game loop
         * @param go 
         */
        public void Destroy(GameObject go){
            destroyed.add(go);
        }
        
        /**
         * Add a UI overlay element
         * @param ui 
         */
        public void AddUi(Ui ui){
            overlay_adds.add(ui);
        }
        
        /**
         * Remove a UI overlay element
         * @param ui 
         */
        public void RemoveUi(Ui ui){
            this.overlay_destroys.add(ui);
        }
        
        /**
         * INTERNAL, resolve the instanciation and destruction queues
         */
        protected void ResolveQueue(){
            objs.addAll(created);
            for(GameObject obj : created){
                if(obj.GetRenderable() != null)
                this.scene.Add((RenderObject)obj.GetRenderable());
            }
            objs.removeAll(destroyed);
            for(GameObject obj : destroyed){
                if(obj.GetRenderable() != null)
                this.scene.Remove((RenderObject)obj.GetRenderable());
            }
            for(Ui obj : this.overlay_adds){
                this.scene.AddUi(obj);
            }
            for(Ui obj : this.overlay_destroys){
                this.scene.RemoveUi(obj);
            }       
            
            created.clear();
            destroyed.clear();
        }
        
        protected void EarlyUpdate(double deltatime){
            for(GameObject obj : this.objs)
            for(Action2<GameObject, Double> event : obj.early_events){
                event.Invoke(obj,deltatime);
            }
        }
        
        protected void Update(double deltatime){
            for(GameObject obj : this.objs)
            for(Action2<GameObject, Double> event : obj.update_events){
                event.Invoke(obj,deltatime);
            }
        }
        
        protected void LateUpdate(double deltatime){
            for(GameObject obj : this.objs)
            for(Action2<GameObject, Double> event : obj.late_events){
                event.Invoke(obj,deltatime);
            }
        }
        
        protected void PhysicsStep(){
            this.physics.Step();
        }
        
        public void Render(){
            if(this.camera != null){
                this.scene.RenderTo(camera);
            }
        }
    }