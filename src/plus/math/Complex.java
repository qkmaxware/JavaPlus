package plus.math;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plus.system.Debug;

public class Complex{

    private double real;
    private double img;
    private static Pattern regex = Pattern.compile("(\\d*(?:\\.\\d*)?)?\\s*([+-]?\\s*\\d*(?:\\.\\d*)?[iIjJ])?");
    
    /**
     * Creates a complex number with real and imaginary parts
     * @param real
     * @param imaginary 
     */
    public Complex(double real, double imaginary){
        this.real = real;
        this.img = imaginary;
    }
    
    /**
     * Create a complex number with a real value
     * @param real 
     */
    public Complex(double real){
        this.real = real;
        this.img = 0;
    }
    
    /**
     * Create a complex number who's value is 0
     */
    public Complex(){
        this.real = 0;
        this.img = 0;
    }

    /**
     * Parse a string into a complex value
     * @param value
     * @return 
     */
    public static Complex Parse(String value){
        Complex c = new Complex();
        
        Matcher matches = regex.matcher(value);
        while(matches.find()){
            //Starting at group 1 (0 is full match)
            for(int i = 1; i <= matches.groupCount(); i++){
                if(matches.group(i) == null)
                    continue;
                
                String match = matches.group(i).toLowerCase();
                if(match.isEmpty()){
                    continue;
                }
                
                boolean negative = false;
                if(match.startsWith("-")){
                    negative = true;
                    match = match.substring(1).trim();
                }
                else if (match.startsWith("+")){
                    negative = false;
                    match = match.substring(1).trim();
                }

                boolean img = false;
                if(match.matches(".*[ij]$")){
                    img = true;
                    match = match.substring(0, match.length() - 1).trim();
                }

                double dvalue = Double.parseDouble(match);
                if(img){
                    c.img = (negative ? -1: 1) * dvalue; 
                }else{
                    c.real = (negative ? -1: 1) * dvalue; 
                }
            }
        }
        
        return c;
    }
    
    
    /**
     * The complex conjugate of this number
     * @return 
     */
    public Complex Conjugate(){
        return new Complex(this.real, this.img*-1);
    }
    
    /**
     * Get the real part of this number
     * @return 
     */
    public double Real(){
        return this.real;
    }
    
    /**
     * Get the imaginary part of this number
     * @return 
     */
    public double Imaginary(){
        return this.img;
    }
    
    /**
     * Add a complex number to this one
     * @param c
     * @return 
     */
    public Complex add(Complex c){
        return new Complex(this.real + c.real, this.img + c.img);
    }
    
    /**
     * Subtract a complex number from this one
     * @param c
     * @return 
     */
    public Complex sub(Complex c){
        return new Complex(this.real - c.real, this.img - c.img);
    }
    
    /**
     * Multiply this complex number by another
     * @param c
     * @return 
     */
    public Complex mul(Complex c){
        return new Complex(this.real * c.real - this.img * c.img, this.real * c.img + this.img * c.real);
    }
    
    /**
     * Divide this complex number by another
     * @param c
     * @return 
     */
    public Complex div(Complex c){
        double d = (c.real*c.real + c.img*c.img);
        double r = (this.real*c.real + this.img*c.img)/d;
        double i = (this.img*c.real - this.real*c.img)/d;
        return new Complex(r,i);
    }
    
    /**
     * The reciprocal of this complex number
     * @return 
     */
    public Complex Reciprocal(){
        Complex den = this.mul(this.Conjugate());
        return this.Conjugate().div(den);
    }
    
    public String toString(){
        return this.real + " + (" + this.img + "i)";
    }
    
}