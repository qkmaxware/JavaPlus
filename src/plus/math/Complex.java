package plus.math;

public class Complex{

    private double real;
    private double img;
    
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
     * Create a complex number from a string representation
     * @param str 
     */
    public Complex(String str){
        String s[] = str.split("[\\Q+-\\Ei]");
        this.real = 0; this.img = 0;
        
        if(s.length > 0)
            this.real = Float.parseFloat(s[0]);
        if(s.length > 1)
            this.img = Float.parseFloat(s[1]);
        
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