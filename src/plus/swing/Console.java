/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;
import plus.system.Debug;
import plus.system.functional.Action1;
 
/**
*
* @author Colin
*/
public class Console extends JTextArea{
   
    private String prefix;
   
    private int protectedUntil = 0;
  
    private LinkedList<Action1<String>> onSubmit = new LinkedList<Action1<String>>();
    
    public Console(String prefix){
        super();
        this.prefix = prefix;
    
        AbstractDocument doc = (AbstractDocument)this.getDocument();
       
        DocumentFilter filter = new DocumentFilter(){
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr){
                if(offset >= protectedUntil){
                    try{
                       fb.insertString(offset, string, attr);
                    }catch(Exception e){}
                }
            } 
            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length){
                if(offset >= protectedUntil){
                try{
                    fb.remove(offset, length);
                }catch(Exception e){}
                }
            }
            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs){
                if(offset >= protectedUntil){
                try{
                    fb.replace(offset, length, text, attrs);
                }catch(Exception e){}
                }
            }
        };
        this.addKeyListener(new KeyListener(){
 
            @Override
            public void keyTyped(KeyEvent ke) {
               
            }
 
            @Override
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER){
                    try{
                        String input = getText(protectedUntil, doc.getLength() - protectedUntil);
                        for(Action1<String> fn: onSubmit){
                            fn.Invoke(input);
                        }
                    }catch(Exception e){
                        Debug.Log(e);
                    }
                    append("\n"+prefix+" ");
                    Protect(doc.getLength());
                    ke.consume();
                }
            }
 
            @Override
            public void keyReleased(KeyEvent ke) {
         
            }
       
        });
       
        
        doc.setDocumentFilter(filter);
       
        this.append(prefix+" ");
        this.Protect(doc.getLength());
        this.setCaretPosition(doc.getLength());
    }
   
    public void Protect(int i){
        this.protectedUntil = i;
    }
   
    public void SetPrefix(String prefix){
        this.prefix = prefix;
    }
    
    public void Clear(){
        Protect(0);
        this.setText("");
    }
    
    public void AddSubmitListener(Action1<String> fn){
        this.onSubmit.add(fn);
    }
    
    public void RemoveSubmitListener(Action1<String> fn){
        this.onSubmit.remove(fn);
    }
}
