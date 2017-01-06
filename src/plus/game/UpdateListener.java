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
public abstract  class UpdateListener {
    
    public abstract void Update(GameObject obj, double deltatime);
    
    public UpdateListener(){}
    
}
