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
     * Tests if this complex number falls on the real axis
     * @return 
     */
    public boolean IsReal(){
        return this.img == 0;
    }
    
    /**
     * Get the imaginary part of this number
     * @return 
     */
    public double Imaginary(){
        return this.img;
    }
    
    /**
     * Test is this number falls on the imaginary axis
     * @return 
     */
    public boolean IsImaginary(){
        return this.real == 0;
    }
    
    /**
     * The complex argument
     * @return 
     */
    public double Arg(){
        return Math.atan2(this.img, this.real);
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
     * Multiply this complex number by a scalar value
     * @param d
     * @return 
     */
    public Complex scale(double d){
        return new Complex(this.real * d, this.img * d);
    }
    
    /**
     * Compute z^c where z and c are both complex
     * @param c
     * @return 
     */
    public Complex pow(Complex c){
        //In exponential form 
        //(a+ib)^(c+id) = e^(ln(r)(c+id)+i theta (c+id))
        // -> ln(r)c + ln(r)id + i0c - 0d
        //e^(i theta) = cos0 + isin0
        //e^(ln(r)c - 0d) * e^(i(ln(r)*d + 0c))
        double r = Math.sqrt(this.real*this.real + this.img*this.img);
        double theta = this.Arg();
        double lnr = Math.log(r);
        
        //e^(ln(r)c - 0d)
        double scalar = Math.pow(Math.E, lnr*c.real - theta*c.img);
        
        //e^(i(ln(r)*d + 0c)) = e^(i a) = cos(a) + isin(a)
        double real = Math.cos(lnr*c.img + theta*c.real);
        double img =  Math.sin(lnr*c.img + theta*c.real);
        
        return new Complex(scalar * real, scalar * img);
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