/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import plus.system.functional.Action;
import plus.system.functional.Action1;
import plus.system.functional.Func1;

/**
 *
 * @author Colin Halseth
 */
public class AsyncPool {
    public static final int DefaultThreadCount = 4;
    
    private static class Promise {
        public Action action;
        public Action1<Exception> error;
        public Action success;
        
        public Promise(Action a){
            this.action = a;
        }
        
        public Promise(Action a, Action1 e){
            this.action = a;
            this.error = e;
        }
        
        public Promise(Action a, Action1 e, Action s){
            this.action = a;
            this.error = e;
            this.success = s;
        }
    }
    
    public static class Worker extends Thread{
        
        private AsyncPool myPool;
        private boolean stopped = false;
        
        protected Worker(AsyncPool pool){
            this.myPool = pool;
        }
        
        public void run(){
            while(!stopped){
                Promise p = myPool.queue.poll();
                if(p == null)
                    continue;
                try{
                    Action a = p.action;
                    if(a != null)
                        a.Invoke();
                }catch(Exception ex){
                    //Something has occured, report it
                    Action1 e = p.error;
                    if(e != null)
                        e.Invoke(ex);
                }
            }
        }
        
        public void Start(){
            this.stopped = false;
            this.start();
        }
        
        public void Stop(){
            this.stopped = true;
            this.interrupt();
        }
        
        public synchronized boolean Active(){
            return !stopped;
        }
        
    }
    
    private int threadCount = 0;
    private Worker[] workers;
    private BlockingQueue<Promise> queue;
    
    public AsyncPool(){
        this(DefaultThreadCount);
    }
    
    public AsyncPool(int threadCount){
        this.threadCount = threadCount;
        workers = new Worker[this.threadCount];
        queue = new LinkedBlockingQueue<Promise>();
        for(int i = 0; i < workers.length; i++){
            workers[i] = new Worker(this);
            workers[i].Start();
        }
    }
    
    public synchronized void Stop(){
        for(int i = 0; i < this.workers.length; i++){
            workers[i].Stop();
        }
    }
    
    public synchronized void Enqueue(Action action){
        queue.offer(new Promise(action));
    }
    
    public synchronized void Enqueue(Action action, Action1 onError){
        queue.offer(new Promise(action, onError));
    }
}
