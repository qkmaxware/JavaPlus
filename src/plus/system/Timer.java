/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import java.util.LinkedList;

/**
 *
 * @author Colin Halseth
 */
public class Timer extends Thread{
 
    protected Time mytime;
    protected int tick;
    protected boolean isrun;
    
    private LinkedList<TimerListener> listeners = new LinkedList<TimerListener>();
    
    /**
     * Create a new timer with the given delay after ticks
     * @param tick 
     */
    public Timer(int tick){
        this.tick = tick;
    }
    
    /**
     * Adds an event listener to fire on every tick of the timer
     * @param tl 
     */
    public void AddListener(TimerListener tl){
        listeners.add(tl);
    }
    
    /**
     * Remove an event listener listening to tick evets
     * @param tl 
     */
    public void RemoveListener(TimerListener tl){
        listeners.remove(tl);
    }
    
    /**
     * Stop the timer
     */
    public void Stop(){
        this.isrun = false;
    }
    
    /**
     * Start the timer
     */
    @Override
    public void run(){
        mytime = new Time();
        isrun = true;
        while(isrun){
            try {
                if(tick > 0)
                    Thread.sleep(tick); //this is in an if statement because there might be overhead even though its sleep for 0 time
            } catch (InterruptedException ex) {
                System.out.println(Timer.class.getName()+": "+ex.toString());
                break;
            }
            
            double t = mytime.DeltaTime();
            for(TimerListener listener : listeners){
                listener.OnTimerTick(t);
            }
        }
    }
    
}
