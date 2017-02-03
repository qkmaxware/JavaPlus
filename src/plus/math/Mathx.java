/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.math;

/**
 *
 * @author Colin Halseth
 */
public class Mathx {
    
    public static final double Deg2Rad = Math.PI / 180;
    public static final double Rad2Deg = 180 / Math.PI;
    
    /**
     * Convert degrees to radians
     * @param deg
     * @return float
     */
    public static double DegreesToRadians(double deg){
        return deg * Deg2Rad;
    }
    
    /**
     * Convert radians to degrees
     * @param rad
     * @return float
     */
    public static double RadiansToDegrees(double rad){
        return rad * Rad2Deg;
    }
    
    /**
     * Clamp a value between a max and min
     * @param value
     * @param min
     * @param max
     * @return float
     */
    public static double Clamp(double value, double min, double max){
        if(value < min){
            return min;
        }else if(value > max){
            return max;
        }
        else{
            return value;
        }
    }
    
    /**
     * Clamp a value between a max and min
     * @param value
     * @param min
     * @param max
     * @return 
     */
    public static int Clamp(int value, int min, int max){
        if(value < min){
            return min;
        }else if(value > max){
            return max;
        }
        else{
            return value;
        }
    }
    
    /**
     * Clamp a value between 0 and 1
     * @param value
     * @return float
     */
    public static double Clamp01(double value){
        return Clamp(value,0,1);
    }
    
    /**
     * Linearly interpolate between two values
     * @param to
     * @param from
     * @param t
     * @return float
     */
    public static double Lerp(double from, double to, double t){
        double dist = to-from;
        return from + dist*t;
    }
    
    /**
     * Check if a value is even
     * @param f
     * @return boolean
     */
    public static boolean IsEven(double f){
        return (f%2)==0;
    }
    
    /**
     * Compute the greatest common divisor of two values
     * @param x
     * @param y
     * @return int
     */
    public static int Gcd(int x, int y){
        if(y == 0){
            return x;
        }
        else{
            while (y != 0){
                int l = x;
                x = y;
                y = l%y;
            }
            return x;
        }
    }
    
    /**
     * Calculate the base 'x' log of a value
     * @param value
     * @param base
     * @return 
     */
    public static double Logb(double value, double base){
        return Math.log(value) / Math.log(base);
    }
    
    /**
     * Calculate the nth root of a value
     * @param value
     * @param n
     * @return 
     */
    public static Complex NRoot(double value, double n){
        //Perform (-1) ^ 1/n  * abs(x) ^ 1/n = (-x) ^ 1/n
        boolean img = value < 0;
        double v = Math.abs(value);
        double result = Math.pow(v, 1.0 / n);
        
        Complex c = new Complex(result, 0);
        
        //root n of -1, and root n of result
        //-1 ^ 1/n = cos(pi/n) + i * sin(pi/n)
        if(img){
            double real_root_n1 = Math.cos(Math.PI / n);
            double img_root_n1 = Math.sin(Math.PI / n);
            
            Complex c2 = new Complex(real_root_n1, img_root_n1);
            return c2.mul(c);
        }else{
            return c;
        }  
    }
    
    /**
     * Kronecker delta function. 1 if parameters are equal, 0 otherwise
     * @param x
     * @param y
     * @return 
     */
    public static double Delta(double x, double y){
        return x == y ? 1 : 0;
    }
    
    /**
     * Absolute difference between two values
     * @param x
     * @param y
     * @return 
     */
    public static double Difference(double x, double y){
        return Math.abs(y - x);
    }
    
    /**
     * Wrap a degree angels to between -180 and 180
     * @param degrees
     * @return 
     */
    public static double WrapAngle(double degrees){
        double newAngle = degrees;
        while(newAngle > 180) //-180 -> 179 if newAngle if -181
            newAngle -= 360;
        while(newAngle < -180) //180 -> -179 if newAngle is 181
            newAngle += 360;
        return newAngle;
    }
    
    /**
     * Wrap a value as if it loops from a minimum value to a maximum
     * @param value
     * @param max
     * @param min
     * @return 
     */
    public static double Wrap(double value, double max, double min){
        while(value < min){
            value -= min;
        }
        while(value > max){
            value -= max;
        }
        return value;
    }
    
    /**
     * Sum of a list of numbers
     * @param value
     * @return float
     */
    public static double Sum(double ... value){
        double sum = 0;
        for(int i = 0; i < value.length; i++){
            sum += value[i];
        }
        return sum;
    }
    
    /**
     * Product of a list of numbers
     * @param value
     * @return float
     */
    public static double Product(double ... value){
        double sum = 1;
        for(int i = 0; i < value.length; i++){
            sum *= value[i];
        }
        return sum;
    }
    
    /**
     * Factorial of a number. For n > 12 integer overflow can occur.
     * @param n
     * @return int
     */
    public static int Factorial(int n){
        int ans = 1;
        for(int i = 1; i<=n; i++){
            ans *= i;
        }
        return ans;
    }
    
    /**
     * Fast inverse tangent approximation
     * @param val
     * @return 
     */
    public static double atanf(double val){
        if(val >= -1 && val <=1)
            //Different types of approximations
            //(float)(Math.PI*0.25f*val + 0.273f*val*(1- Math.abs(val)));
            //val/(1 + 0.28125f*val*val);
            return  val/(1 + 0.28125f*val*val);
        else
            return Math.atan(val);
    }
    
    /**
     * Checks if a point is inside a triangle defined by 3 corners
     * @param v1
     * @param v2
     * @param v3
     * @param p1
     * @return 
     */
    public static boolean InTriangle(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 p1){
        boolean b1,b2,b3;
        
        Vector3 p3 = v2; Vector3 p2 = v1;
        b1 = (p1.x() - p3.x()) * (p2.y() - p3.y()) - (p2.x() - p3.x()) * (p1.y() - p3.y()) < 0.0f;
        p3 = v3; p2 = v2;
        b2 = (p1.x() - p3.x()) * (p2.y() - p3.y()) - (p2.x() - p3.x()) * (p1.y() - p3.y()) < 0.0f;
        p3 = v1; p2 = v3;
        b3 = (p1.x() - p3.x()) * (p2.y() - p3.y()) - (p2.x() - p3.x()) * (p1.y() - p3.y()) < 0.0f;
        
        return ((b1 == b2) && (b2 == b3));
    }
    
    public static boolean LineRectangleCollision(Vector3 p1, Vector3 p2,  double l, double r, double t, double b){
        // Calculate m and c for the equation for the line (y = mx+c)
        double m = (p2.y()-p1.y()) / (p2.x()-p1.x());
        double c = p1.y() - (m*p1.x());
        
        double top_intersection,bottom_intersection,toptrianglepoint,bottomtrianglepoint;
        
        // if the line is going up from right to left then the top intersect point is on the left
        if(m>0)
        {
           top_intersection = (m*l  + c);
           bottom_intersection = (m*r  + c);
        }
        // otherwise it's on the right
        else
        {
           top_intersection = (m*r  + c);
           bottom_intersection = (m*l  + c);
        }

        // work out the top and bottom extents for the triangle
        if(p1.y()<p2.y())
        {
           toptrianglepoint = p1.y();
           bottomtrianglepoint = p2.y();
        }
        else
        {
           toptrianglepoint = p2.y();
           bottomtrianglepoint = p1.y();
        }
        
        double topOverlap = top_intersection>toptrianglepoint ? top_intersection : toptrianglepoint;
        double btnOverlap = bottom_intersection<bottomtrianglepoint ? bottom_intersection : bottomtrianglepoint;
     
         return (topOverlap<btnOverlap) && (!((btnOverlap<t) || (topOverlap>b)));
    }
}
