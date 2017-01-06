/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import plus.math.Vector3;

/**
 *
 * @author Colin Halseth
 */
public class Random {

    private static java.util.Random rng = new java.util.Random();
    
    /**
     * Set the seed of the random generator
     * @param i 
     */
    public static void SetSeed(long i){
        rng = new java.util.Random(i);
    }
    
    /**
     * Get a random number between 0 and 1.
     * @return float
     */
    public static float Next(){
        return rng.nextFloat();
    }
    
    /**
     * Get a random boolean value
     * @return 
     */
    public static boolean Bool(){
        float v = Random.Next();
        return (v > 0.5f)? true: false;
    }
    
    /**
     * Get a random vector on a unit circle.
     * @return vector
     */
    public static Vector3 OnUnitCircle(){
        return new Vector3(Range(-1.0f,1.0f),Range(-1.0f,1.0f),0).Normalize();
    }
    
    /**
     * Get a random vector inside a unit circle.
     * @return 
     */
    public static Vector3 InsideUnitCircle(){
        return new Vector3(Range(-1.0f,1.0f),Range(-1.0f,1.0f),0).Normalize().scale(Next());
    }
    
    /**
     * Get a random vector on a unit sphere.
     * @return vector
     */
    public static Vector3 OnUnitSphere(){
        return new Vector3(Range(-1.0f,1.0f),Range(-1.0f,1.0f),Range(-1.0f,1.0f)).Normalize();
    }
    
    /**
     * Get a random vector inside a unit sphere.
     * @return vector
     */
    public static Vector3 InsideUnitSphere(){
        return new Vector3(Range(-1.0f,1.0f),Range(-1.0f,1.0f),Range(-1.0f,1.0f)).Normalize().scale(Next());
    }
    
    /**
     * Get a random integer between two values.
     * @param i
     * @param j
     * @return int
     */
    public static int Range(int i, int j){
        float v = Random.Next();
        float lerp = (1-v)*i + v*j;
        return Math.round(lerp);
    }
    
    /**
     * Get a random float between two values.
     * @param i
     * @param j
     * @return float
     */
    public static float Range(float i, float j){
        float v = Random.Next();
        float lerp = (1-v)*i + v*j;
        return lerp;
    }
    
}
