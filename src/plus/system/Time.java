/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

/**
 *
 * @author Colin Halseth
 */
public class Time {
    
    protected long time;
    protected long starttime;
    
    public Time(){
        this.time  = System.currentTimeMillis();
        this.starttime = this.time;
    }
    
    /**
     * Reset this time object to the current system time
     */
    public void Reset(){
        this.time  = System.currentTimeMillis();
        this.starttime = this.time;
    }
    
    /**
     * How much time has passed since the last check in seconds
     * @return float time
     */
    public double DeltaTime(){
        long time = this.time;
        long newTime = System.currentTimeMillis();
        this.time = newTime;
        return (double)((newTime - time)*0.001);
    }
    
    /**
     * Convert from milliseconds to seconds
     * @param milliseconds
     * @return 
     */
    public static double ToSeconds(long milliseconds){
        return (double)(milliseconds * 0.001);
    }
    
    /**
     * convert from seconds to milliseconds
     * @param seconds
     * @return 
     */
    public static long ToMilliseconds(double seconds){
        return (long)(1000 * (double)seconds);
    }
    
    /**
     * Get the current system time in milliseconds
     * @return long time in milliseconds
     */
    public static long Time(){
        return System.currentTimeMillis();
    }
    
    /**
     * Get the amount of time that has elapsed since the Time object was created
     * @return Amount of elapsed time in seconds
     */
    public double ElapsedTime(){
        long time = System.currentTimeMillis();
        return (double)((time - starttime)*0.001);
    }
    
}
