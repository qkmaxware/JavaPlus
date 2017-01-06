/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

/**
 * Notes * Removal of generic types to make it cleaner to use the code. However use may be "unsafe" as no typing is checked.
 * @author Colin Halseth
 */
public abstract class Function{
    
    public abstract Object Call(Object... args);
    
}
