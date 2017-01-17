/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import plus.math.*;
import plus.system.Debug;
import plus.system.Perlin;
import plus.system.Random;

/**
 *
 * @author Colin Halseth
 */
public class MathExample {
    
    public static void main(String[] args){
        
        Debug.Log(ConversionTools.Convert(300, ConversionTools.Temperature.kelvin, ConversionTools.Temperature.celsius));
        Debug.Log(ConversionTools.Convert(2, ConversionTools.Area.km, ConversionTools.Area.hectare));
        
        Quaternion q = Quaternion.Euler(new Vector3(60,0,0));

        Vector3 rot = q.mul(Vector3.forward);
        Debug.Log(q);
        Debug.Log(rot);
        
        Complex a = Complex.Parse("12 + 5i");   //new Complex(12, 5);
        Complex b = Complex.Parse("2 - 12i");   //new Complex(2, -12);
        
        Debug.Log(a);
        Debug.Log(a.add(b));
        
        Matrix m = new Matrix(new double[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        });
        Debug.Log(m);
        
        Debug.Log(plus.math.Mathx.Factorial(5));
        
        Debug.Log(Random.Range(0.0f, 120f));
        
        Debug.Log(Perlin.Noise(0.3, 0.6));
        
    }
    
}
