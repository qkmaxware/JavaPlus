/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import plus.math.Complex;
import plus.math.Matrix;
import plus.system.Debug;
import plus.system.Perlin;
import plus.system.Random;

/**
 *
 * @author Colin Halseth
 */
public class MathExample {
    
    public static void main(String[] args){
        
        Complex a = new Complex("12 + 5i");
        Complex b = new Complex("2 - 12i");
        
        Debug.Log(a.add(b));
        
        Matrix m = new Matrix(new double[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        });
        Debug.Log(m);
        
        Debug.Log(plus.math.Util.Factorial(5));
        
        Debug.Log(Random.Range(0.0f, 120f));
        
        Debug.Log(Perlin.Noise(0.3, 0.6));
        
    }
    
}
