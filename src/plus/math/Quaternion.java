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
public class Quaternion {
    
    public static final Quaternion identity = new Quaternion(0,0,0,1);
    
    private double x;
    private double y;
    private double z;
    private double w;
    
    /**
     * Quaternion with all 0 values.
     */
    public Quaternion(){
        x = 0; y = 0; z = 0; w = 0;
    }
    
    /**
     * Quaternion with desired x,y,z,w components
     * @param x
     * @param y
     * @param z
     * @param w 
     */
    public Quaternion(double x, double y, double z, double w){
        this.x = x; this.y = y; this.z = z; this.w = w;
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
        return (double)Math.sqrt(x*x + y*y + z*z + w*w);
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
        double newX = this.w * x + this.x * w + this.y * z - this.z * y;
        double newY = this.w * y + this.y * w + this.z * x - this.x * z;
        double newZ = this.w * z + this.z * w + this.x * y - this.y * x;
        double newW = this.w * w - this.x * x - this.y * y - this.z * z;

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
        alpha = plus.math.Util.Clamp01(alpha);
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
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/index.htm
     * @param axis
     * @return 
     */
    public static Quaternion FromEulerAngle(Vector3 axis){
        double heading = Math.toRadians(axis.y());
        double attitude = Math.toRadians(axis.x());
        double bank = Math.toRadians(axis.z());
        
        double c1 = Math.cos(heading/2);
        double c2 = Math.cos(attitude/2);
        double c3 = Math.cos(bank/2);
        double s1 = Math.sin(heading/2);
        double s2 = Math.sin(attitude/2);
        double s3 = Math.sin(bank/2);
        
        double x = s1 * s2 * c3 + c1 * c2 * s3;
        double y = s1 * c2 * c3 + c1 * s2 * s3;
        double z = c1 * s2 * c3 - s1 * c2 * s3;
        double w = c1 * c2 * c3 - s1 * s2 * s3;
        
        return new Quaternion(
                x,y,z,w
        );
    }
    
    /**
     * Create a euler angle (yaw,pitch,roll) representation from a quaternion
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/
     * @return 
     */
    public Vector3 ToEulerAngle(){
        Quaternion q1 = this;
        double test = q1.x*q1.y + q1.z*q1.w;
        double heading = 0, attitude = 0, bank = 0;
        
	if (test > 0.499) { // singularity at north pole
		heading = 2 * Math.atan2(q1.x,q1.w);
		attitude = Math.PI/2;
		bank = 0;
	}
        else if (test < -0.499) { // singularity at south pole
		heading = -2 * Math.atan2(q1.x,q1.w);
		attitude = - Math.PI/2;
		bank = 0;
	}else{
            double sqx = q1.x*q1.x;
            double sqy = q1.y*q1.y;
            double sqz = q1.z*q1.z;
            heading = Math.atan2(2*q1.y*q1.w-2*q1.x*q1.z , 1 - 2*sqy - 2*sqz);
            attitude = Math.asin(2*test);
            bank = Math.atan2(2*q1.x*q1.w-2*q1.y*q1.z , 1 - 2*sqx - 2*sqz);
        }
        
        double yaw = Math.toDegrees(heading);
        double roll = Math.toDegrees(bank);
        double pitch = Math.toDegrees(attitude);
        
        return new Vector3(pitch,yaw,roll);
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
        return "("+x+","+y+","+z+","+w+")";
    }
}
