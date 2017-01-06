package plus.math;

/**
 * A class for storing numbers as fractions
 * @author Colin Halseth
 */
public class Rational {

    public static final Rational zero = new Rational(0);
    public static final Rational one = new Rational(1);
    //Rational approximations for E and PI
    public static final Rational E = new Rational(28245729,10391023);
    public static final Rational PI = new Rational(355,113);
    
    private long top;
    private long bottom;

    /**
     * Create a new integer valued rational number
     * @param i 
     */
    public Rational(long i){
        top = i;
        bottom = 1;
    }
    
    public Rational(Rational rt){
        top = rt.top;
        bottom = rt.bottom;
    }
    
    /**
     * Create a ration who's value is 0
     */
    public Rational (){
        top = 0;
        bottom = 1;
    }
    
    /**
     * Creates a new rational from a fraction
     * @param top
     * @param bottom 
     */
    public Rational(long top, long bottom){
        this.top = top;
        this.bottom = bottom;
    }
    
    /**
     * Create a new rational with a decimal value
     * @param ft 
     */
    public Rational(float ft){
        //Initial stage
        float t = ft;
        float b = 1;
        
        String fts = (""+ft);
        String[] splits = fts.split(".");
        double pow = 1;
        if(splits.length > 1){
            pow = Math.pow(10, splits[1].length());
        }
        top = (int)(t*pow);
        bottom = (int)(b*pow);
    }
    
    /**
     * Add this rational to another
     * @param rt
     * @return rational
     */
    public Rational add(Rational rt){
        Rational r = new Rational();
        
        r.top = (this.top*rt.bottom) + (rt.top*this.bottom);
        r.bottom = (this.bottom*rt.bottom);
        r.simplify();
        
        return r;
    }
    
    /**
     * Subtract this rational by another
     * @param rt
     * @return rational
     */
    public Rational sub(Rational rt){
        Rational r = new Rational();
        
        r.top = (this.top*rt.bottom) - (rt.top*this.bottom);
        r.bottom = (this.bottom*rt.bottom);
        r.simplify();
        
        return r;
    }
    
    /**
     * Multiply this rational by another
     * @param rt
     * @return rational
     */
    public Rational mul(Rational rt){
        Rational r = new Rational();
        
        r.top = this.top * rt.top;
        r.bottom = this.bottom * rt.bottom;
        r.simplify();
        
        return r;
    }
    
    /**
     * Multiply this rational by an integer
     * @param rt
     * @return 
     */
    public Rational mul(int rt){
        Rational r = new Rational();
        
        r.top = this.top * rt;
        r.bottom = this.bottom;
        r.simplify();
        
        return r;
    }
    
    /**
     * Divides this rational by another
     * @param rt
     * @return rational
     */
    public Rational div(Rational rt){
        Rational r = new Rational();
        
        r.top = this.top * rt.bottom;
        r.bottom = this.bottom * rt.top;
        r.simplify();
        
        return r;
    }
    
    /**
     * Divides this number by an integer
     * @param rt
     * @return 
     */
    public Rational div(int rt){
        Rational r = new Rational();
        
        r.top = this.top;
        r.bottom = this.bottom * rt;
        r.simplify();
        
        return r;
    }
    
    /**
     * Computes a rational number to the power of a whole number
     * @param i
     * @return rational
     */
    public Rational pow(int i){
        Rational r = new Rational();
        
        r.top = (int)Math.pow(this.top, i);
        r.bottom = (int)Math.pow(this.bottom, i);
        
        return r;
    }
    /**
     * Computes a rational number to a decimal power. Loss of precision can occur here
     * @param i
     * @return rational
     */
    public Rational pow(float i){
        Rational r = new Rational();
        
        r.top = (int)Math.pow(this.top, i);
        r.bottom = (int)Math.pow(this.bottom, i);
        
        return r;
    }
    
    /**
     * Computes a rational number to a rational power. Loss of precision can occur here
     * @param i
     * @return rational
     */
    public Rational pow(Rational i){
        Rational r = new Rational();
        
        r.top = (int)Math.pow(this.top, i.getDecimalValue());
        r.bottom = (int)Math.pow(this.bottom, i.getDecimalValue());
        
        return r;
    }
    
    /**
     * Computes the inverse of this rational
     * @return 
     */
    public Rational inverse(){
        Rational r = new Rational();
        
        r.top = this.bottom;
        r.bottom = this.top;
        
        return r;
    }
    
    /**
     * Attempt to simplify this rational if a common factor exists 
     */
    public void simplify(){
        long gcd = gcd(Math.abs(top),Math.abs(bottom));
        top = (top < 0 && bottom < 0)?-top/gcd:top/gcd;
        bottom = (top < 0 && bottom < 0)?-bottom/gcd:bottom/gcd;
    }
    
    public long gcd(long x, long y){
        if(y == 0){
            return x;
        }
        else{
            while (y != 0){
                long l = x;
                x = y;
                y = l%y;
            }
            return x;
        }
    }
    
    /**
     * Gets the decimal value of this rational
     * @return 
     */
    public double getDecimalValue(){
        return (double)top/(double)bottom;
    }
    
    /**
     * Gets the integer value of this rational
     * @return 
     */
    public long getWholeValue(){
        return top/bottom;
    }
    
    /**
     * This rational represented as a fraction
     * @return 
     */
    public String realString(){
        return top + "/" + bottom;
    }
    
    @Override
    public boolean equals(Object b){
        if(b instanceof Rational){
            Rational rt = (Rational)b;
            return (this.getDecimalValue() == rt.getDecimalValue());
        }
        return false;
    }
    
    /**
     * This rational represented as a decimal
     * @return 
     */
    public String toString(){
        return ""+getDecimalValue();
    }
    
}
