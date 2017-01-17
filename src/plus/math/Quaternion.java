/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.math;

/**
 * https://gist.github.com/aeroson/043001ca12fe29ee911e
 * @author Colin Halseth
 */
public class Quaternion {
    
    public static final Quaternion identity = new Quaternion(0,0,0,1);
    
    private double x;
    private double y;
    private double z;
    private double w;
    
    /**
     * Quaternion identical to the identity quaternion
     */
    public Quaternion(){
        x = 0; y = 0; z = 0; w = 1;
    }
    
    /**
     * Create a quaternion with the desired x,y,z,w components
     * @param x
     * @param y
     * @param z
     * @param w 
     */
    public Quaternion(double x, double y, double z, double w){
        this.x = x; this.y = y; this.z = z; this.w = w;
    }
    
    /**
     * Create a quaternion with the desired x,y,z,w components
     * @param xyz
     * @param w 
     */
    public Quaternion(Vector3 xyz, double w){
        this.x = xyz.x();
        this.y = xyz.y();
        this.z = xyz.z();
        this.w = w;
    }
    
    /**
     * Copy the components of another quaternion
     * @param q 
     */
    public Quaternion(Quaternion q){
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }
    
    /**
     * X component
     * @return 
     */
    public double x(){
        return this.x;
    }
    
    /**
     * Y component
     * @return 
     */
    public double y(){
        return this.y;
    }
    
    /**
     * Z component
     * @return 
     */
    public double z(){
        return this.z;
    }
    
    /**
     * W component
     * @return 
     */
    public double w(){
        return this.w;
    }
    
    /**
     * The conjugate of this quaternion
     * @return 
     */
    public Quaternion Conjugate(){
        return new Quaternion(-x,-y,-z,w);
    }
    
    /**
     * The magnitude of this quaternion
     * @return 
     */
    public double Magnitude(){
        return Math.sqrt(x*x + y*y + z*z + w*w);
    }
    
    /**
     * A normalized quaternion
     * @return 
     */
    public Quaternion Normalize(){
        double m = this.Magnitude();
        return new Quaternion(x/m,y/m,z/m,w/m);
    }
    
    /**
     * The square of the magnitude of this quaternion
     * @return 
     */
    public double SquareMagnitude(){
        return x*x + y*y + z*z + w*w;
    }
    
    /**
     * Add a quaternion to this one
     * @param q
     * @return 
     */
    public Quaternion add(Quaternion q){
        return new Quaternion(x+q.x, y+q.y, z+q.z,w+q.w);
    }
    
    /**
     * Subtract a quaternion to this one
     * @param q
     * @return 
     */
    public Quaternion sub(Quaternion q){
        return new Quaternion(x-q.x, y-q.y, z-q.z,w-q.w);
    }
    
    /**
     * Multiply this quaternion by a scalar value
     * @param a
     * @return 
     */
    public Quaternion scale(double a){
        return new Quaternion(a*x,a*y,a*z,a*w);
    }
    
    /**
     * Multiply a quaternion by another in the form (this * q)
     * @param q
     * @return 
     */
    public Quaternion mul(Quaternion q){
        double newX =  this.x * q.w + this.y * q.z - this.z * q.y + this.w * q.x;
        double newY = -this.x * q.z + this.y * q.w + this.z * q.x + this.w * q.y;    
        double newZ =  this.x * q.y - this.y * q.x + this.z * q.w + this.w * q.z;
        double newW = -this.x * q.x - this.y * q.y - this.z * q.z + this.w * q.w;

        return new Quaternion(newX,newY,newZ,newW);
    }
    
    /**
     * Divide a quaternion by another this/q
     * @param q
     * @return 
     */
    public Quaternion div(Quaternion q){
        return this.mul(q.Conjugate());
    }
    
    /**
     * Dot product of this and another quaternion
     * @param q
     * @return 
     */
    public double Dot(Quaternion q){
        return x*q.x + y*q.y + z*q.z + w*q.w;
    }
    
    /**
     * Dor product between two quaternions
     * @param a
     * @param b 
     * @return  
     */
    public static double Dot(Quaternion a, Quaternion b){
        return a.Dot(b);
    }
    
    /**
     * Spherical linear interpolation from one quaternion to another
     * @param from
     * @param to
     * @param alpha
     * @return 
     */
    public static Quaternion Slerp(Quaternion from, Quaternion to, double alpha){
        double d = from.x * to.x + from.y * to.y + from.z * to.z + from.w * to.w;
        double absDot = d < 0.f ? -d : d;

        // Set the first and second scale for the interpolation
        alpha = plus.math.Mathx.Clamp01(alpha);
        double scale0 = 1f - alpha;
        double scale1 = alpha;

        // Check if the angle between the 2 quaternions was big enough to
        // warrant such calculations
        if ((1 - absDot) > 0.1) {// Get the angle between the 2 quaternions,
                // and then store the sin() of that angle
                final double angle = Math.acos(absDot);
                final double invSinTheta = 1f / Math.sin(angle);

                // Calculate the scale for q1 and q2, according to the angle and
                // it's sine value
                scale0 = (Math.sin((1f - alpha) * angle) * invSinTheta);
                scale1 = (Math.sin((alpha * angle)) * invSinTheta);
        }

        if (d < 0.f) scale1 = -scale1;

        // Calculate the x, y, z and w values for the quaternion by using a
        // special form of linear interpolation for quaternions.
        double x = (scale0 * from.x) + (scale1 * to.x);
        double y = (scale0 * from.y) + (scale1 * to.y);
        double z = (scale0 * from.z) + (scale1 * to.z);
        double w = (scale0 * from.w) + (scale1 * to.w);
        
        return new Quaternion(x,y,z,w);
    }
    
    /**
     * Create a quaternion from a euler angle (yaw,pitch,roll)
     * @param axis
     * @return 
     */
    public static Quaternion Euler(Vector3 axis){
        
        //NOTE* swapped x,z attitude, bank because it gave me correct rotations this way. 
        //Not sure why since x axis def is not bank axis - also changed on fromEuler function
        //No problems observed with doing this right now.
        double heading = Math.toRadians(axis.y());
        double attitude = Math.toRadians(axis.z());
        double bank = Math.toRadians(axis.x());
        
        double c1 = Math.cos(heading/2);
        double c2 = Math.cos(attitude/2);
        double c3 = Math.cos(bank/2);
        double s1 = Math.sin(heading/2);
        double s2 = Math.sin(attitude/2);
        double s3 = Math.sin(bank/2);
        
        double w = c1 * c2 * c3 - s1 * s2 * s3;
        double x = s1 * s2 * c3 + c1 * c2 * s3;
        double y = s1 * c2 * c3 + c1 * s2 * s3;
        double z = c1 * s2 * c3 - s1 * c2 * s3;
        
        return new Quaternion(
                x,y,z,w
        );
        
    }
    
    /**
     * Create a euler angle (yaw,pitch,roll) representation from a quaternion
     * @return 
     */
    public Vector3 Euler(){
       
        double sqw = this.w * this.w;
        double sqx = this.x * this.x;
        double sqy = this.y * this.y;
        double sqz = this.z * this.z;
        
        double unit = sqx + sqy + sqz + sqw;
        double test = this.x*this.y + this.z * this.w;
        
        double heading;
        double attitude;
        double bank;
        
        if(test > 0.499 * unit){
            //North poll singularity
            heading = 2 * Math.atan2(this.x, this.w);
            attitude = Math.PI * (0.5);
            bank = 0;
        }
        else if(test < -0.499 * unit){
            //South poll singularity
            heading = -2 * Math.atan2(this.x, this.w);
            attitude = - Math.PI * 0.5;
            bank = 0;
        }else{
            heading = Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw);
            attitude = Math.asin(2 * test / unit);
            bank = Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw);
        }
       
        return new Vector3(Math.toDegrees(bank), Math.toDegrees(heading), Math.toDegrees(attitude));
    }
    
    /**
     * Create a quaternion from an Angle-Axis representation
     * @param angle
     * @param axis
     * @return 
     */
    public static Quaternion AngleAxis(float angle, Vector3 axis){
        if(axis.SquareMagnitude() == 0)
            return Quaternion.identity;
        
        double rad = Math.toRadians(angle) * 0.5;
        Vector3 normal = axis.Normalize();
        normal = normal.scale(Math.sin(rad));
        return new Quaternion(
                normal.x(),
                normal.y(),
                normal.z(),
                Math.cos(rad)
        ).Normalize();
    }
    
    /**
     * Angle between two quaternions
     * @param a
     * @param b
     * @return 
     */
    public static double Angle(Quaternion a, Quaternion b){
        double d = a.Dot(b);
        return Math.toDegrees(Math.acos(Math.min(Math.abs(d), 1)) * 2);
    }
    
    /**
     * Treat this quaternion as a rotation matrix and post-multiply by a column vector
     * @param vec
     * @return 
     */
    public Vector3 mul(Vector3 vec){
        double num = x * 2;
        double num2 = y * 2;
        double num3 = z * 2;
        double num4 = x * num;
        double num5 = y * num2;
        double num6 = z * num3;
        double num7 = x * num2;
        double num8 = x * num3;
        double num9 = y * num3;
        double num10 = w * num;
        double num11 = w * num2;
        double num12 = w * num3;
        
        return new Vector3(
            (1f - (num5 + num6)) * vec.x() + (num7 - num12) * vec.y() + (num8 + num11) * vec.z(),
            (num7 + num12) * vec.x() + (1f - (num4 + num6)) * vec.y() + (num9 - num10) * vec.z(),
            (num8 - num11) * vec.x() + (num9 + num10) * vec.y() + (1f - (num4 + num5)) * vec.z()    
        );
    }
    
    /**
     * Convert the quaternion into the equivalent rotation matrix
     * @return 
     */
    public Matrix ToMatrix3(){
        Matrix r = new Matrix(new double[][]{
            {1 - 2*y*y - 2*z*z,  2*x*y - 2*z*w,      2*x*z + 2*y*w},
            {2*x*y + 2*z*w,      1 - 2*x*x - 2*z*z,  2*y*z - 2*x*w},
            {2*x*z - 2*y*w,      2*y*z + 2*x*w,      1 - 2*x*x -2*y*y}
        });
        return r;
    }
    
    @Override
    public boolean equals(Object b){
        if(b instanceof Quaternion){
            Quaternion vv = (Quaternion)b;
            return this.x() == vv.x() && this.y() == vv.y() && this.z() == vv.z() && this.w() == vv.w();
        }
        return false;
    }
    
    public String toString(){
        return "("+w+","+x+","+y+","+z+")";
    }
}
