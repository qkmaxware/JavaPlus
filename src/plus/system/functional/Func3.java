/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system.functional;

/**
 *
 * @author Colin Halseth
 */
public interface Func3<T1, T2, T3, Out> {
    public Out Invoke(T1 var1, T2 var2, T3 var3);
}
