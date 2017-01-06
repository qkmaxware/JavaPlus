/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.game;

/**
 *
 * @author Colin Halseth
 */
public class SceneManager {

    private GameScene active = null;
    private GameScene nextScene = null;
    
    private Game game;
    
    protected SceneManager(Game game){
        this.game = game;
    }
    
    public void LoadScene(GameScene scene){
        if(!game.IsRunning()){
            this.active = scene;
        }else{
            this.nextScene = scene;
        }
    }
    
    protected void SwapScene(){
        if(this.nextScene == null)
            return;
            
        //Cleanup current scene
        
        //Load new scene
        this.active = nextScene;
        this.nextScene =  null;
    }
    
    public GameScene GetActive(){
        return this.active;
    }
    
}
