/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Colin Halseth
 */
public class Debug {
    
    private final static Logger logger = Logger.getLogger(Debug.class.getName()); 
    private static Level defaultLevel = Level.INFO;
    private static boolean IsEnabled = true;
    
    private static class DebugFormatter extends Formatter{
        public DebugFormatter(){super();}
        @Override
        public String format(LogRecord record){
            return record.getMessage()+"\n";
        }
    }
    
    static{
        logger.setUseParentHandlers(false);
        DebugFormatter format = new DebugFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(format);
        logger.addHandler(handler);
    }
    
    /**
     * Set the default level of the logging system
     * @param l 
     */
    public static void SetLevel(Level l){
        defaultLevel = l;
    }
    
    /**
     * Enable or disable logging messages
     * @param enable 
     */
    public static void Enable(boolean enable){
        IsEnabled = enable;
    }
    
    /**
     * Get the line number and class name of the line 3 calls ago
     * @return 
     */
    private static String GetPrefix(){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        //String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        
        return className + "("+lineNumber+") ";
    }
    
    /**
     * Log a message with the default log level
     * @param ob 
     */
    public static void Log(Object ob){
        if(IsEnabled)
            logger.log(defaultLevel, GetPrefix()+" "+ob.toString());
    }
    
    /**
     * Log a message with the specified log level
     * @param ob
     * @param level 
     */
    public static void Log(Object ob, Level level){
        if(IsEnabled)
            logger.log(level, GetPrefix()+" "+ob.toString());
    }
}
