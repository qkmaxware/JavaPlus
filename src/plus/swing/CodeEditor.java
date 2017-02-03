/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import plus.system.Debug;
import plus.system.functional.Func1;

/**
 *
 * @author Colin Halseth
 */
public class CodeEditor extends JPanel {
    
    private JTextPane editor;
    private JTextPane numbers;
    
    private LinkedList<Func1<String, AttributeSet>> styling_rules = new LinkedList<Func1<String, AttributeSet>>();
    
    public CodeEditor(){
        this.setLayout(new BorderLayout());
        
        editor = new JTextPane();
        numbers = new JTextPane();
     
        StyleContext context = StyleContext.getDefaultStyleContext();
        AttributeSet none = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        
        DefaultStyledDocument doc = new DefaultStyledDocument (){
            @Override
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                
                String text = getText(0, getLength());
                int startInd = Math.max(FindStartIndex(text, offset), 0);
                int endInd = FindEndIndex(text, offset + str.length());
                
                String word = text.substring(startInd,endInd);
                boolean styled = false;
                for(Func1<String,AttributeSet> rules : styling_rules){
                    AttributeSet set = rules.Invoke(word);
                    if(set != null) {
                        setCharacterAttributes(startInd, endInd - startInd, set, false);
                        styled = true;
                        break;
                    }
                }
                if(!styled){
                    setCharacterAttributes(startInd, endInd - startInd, none, false);
                }
            }
            
            @Override
            public void remove(int offset, int length) throws BadLocationException{
                super.remove(offset, length);
                
                String text = getText(0, getLength());
                int startInd = Math.max(FindStartIndex(text, offset), 0);
                int endInd = FindEndIndex(text, offset);
                
                String word = text.substring(startInd,endInd);
                System.out.println(word);
                boolean styled = false;
                for(Func1<String,AttributeSet> rules : styling_rules){
                    AttributeSet set = rules.Invoke(word);
                    if(set != null) {
                        setCharacterAttributes(startInd, endInd - startInd, set, false);
                        styled = true;
                        break;
                    }
                }
                if(!styled){
                    setCharacterAttributes(startInd, endInd - startInd, none, false);
                }
            }
            
            @Override
            public void replace(int offset, int length, String str, AttributeSet a) throws BadLocationException{
                super.replace(offset, length, str, a);
                
                String text = getText(0, getLength());
                int startInd = Math.max(FindStartIndex(text, offset), 0);
                int endInd = FindEndIndex(text, offset + str.length());
                
                String word = text.substring(startInd,endInd);
                boolean styled = false;
                for(Func1<String,AttributeSet> rules : styling_rules){
                    AttributeSet set = rules.Invoke(word);
                    if(set != null) {
                        setCharacterAttributes(startInd, endInd - startInd, set, false);
                        styled = true;
                        break;
                    }
                }
                if(!styled){
                    setCharacterAttributes(startInd, endInd - startInd, none, false);
                }
            }
        };
        editor.setDocument(doc);
        
        editor.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {
                UpdateNumbering();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                UpdateNumbering();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {}
        
        });
        
        numbers.setEditable(false);
        numbers.setBackground(new Color(184,184,184));
        numbers.setPreferredSize(new Dimension(30,40));
        
        this.add(editor, BorderLayout.CENTER);
        this.add(numbers, BorderLayout.WEST);
    }
    
    private int FindStartIndex(String text, int index){
        while(--index >= 0){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
        }
        return index;
    }
    
    private int FindEndIndex(String text, int index){
        while(index < text.length()){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
            index++;
        }
        return index;
    }
    
    private void UpdateNumbering(){
        int size = editor.getText().split("\n").length;
        //int size = editor.getLineCount();
        numbers.setText("");
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < size; i++){
            buffer.append(i).append("\n");
        }
        numbers.setText(buffer.toString());
    }
    
    /**
     * Set the font used by the code editor
     * @param font 
     */
    public void SetFont(Font font){
        editor.setFont(font);
        numbers.setFont(font);
    }
    
    /**
     * Add styling rules for the editor.
     * @param rules 
     */
    public void AddStylingRules(Func1<String, AttributeSet> ... rules){
        for(Func1<String,AttributeSet> rule : rules)
            this.styling_rules.addLast(rule);
    }
    
    /**
     * Add a styling rule by string pattern and color
     * @param pattern
     * @param color 
     */
    public void AddStylingRule(String pattern, Color color){
        StyleContext context = StyleContext.getDefaultStyleContext();
        AttributeSet attr = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, color);
        Pattern p = Pattern.compile("\\s*"+pattern+"\\s*");
        Func1<String,AttributeSet> fn = (text) -> {
            if(p.matcher(text).matches()){
                return attr;
            }
            return null;
        }; 
        AddStylingRules(fn);      
    }
    
    /**
     * Remove styling rules for the editor
     * @param rules 
     */
    public void RemoveStylingRules(Func1<String, AttributeSet> ... rules){
        for(Func1<String,AttributeSet> rule : rules)
            this.styling_rules.remove(rule);
    }
}
