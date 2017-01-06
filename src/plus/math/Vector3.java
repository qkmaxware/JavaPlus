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
public class Vector3 {
    
    private double x;
    private double y;
    private double z;
    
    public static final Vector3 zero = new Vector3();
    public static final Vector3 one = new Vector3(1,1,1);
    public static final Vector3 up = new Vector3(0,-1,0);
    public static final Vector3 right = new Vector3(1,0,0);
    public static final Vector3 forward = new Vector3(0,0,1);
    
    /**
     * Create a vector with all components set to 0
     */
    public Vector3(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    /**
     * Creates a vector with only an x component
     * @param a 
     */
    public Vector3(double a){
        this.x = a;
        this.y = 0;
        this.z = 0;
    }
    
    /**
     * Creates a vector with only x,y components
     * @param x
     * @param y 
     */
    public Vector3(double x, double y){
        this.x = x;
        this.y = y;
        this.z = 0;
    }
    
    /**
     * Copy a vector
     * @param b 
     */
    public Vector3(Vector3 b){
        this.x = b.x;
        this.y = b.y;
        this.z = b.z;
    }
    
    /**
     * Create a vector with components
     * @param x
     * @param y
     * @param z 
     */
    public Vector3 (double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Create a transformation matrix from this vector
     * @return 
     */
    public Matrix GetMatrix(){
        return new Matrix(new double[][]{{1,0,0,x},{0,1,0,y},{0,0,1,z},{0,0,0,1}});
    }
    
    /**
     * The x component
     * @return double
     */
    public double x(){
        return this.x;
    }
    
    /**
     * The y component
     * @return double
     */
    public double y(){
        return this.y;
    }
    
    /**
     * The z component
     * @return double
     */
    public double z(){
        return this.z;
    }
    
    /**
     * Flip the direction of this vector
     * @return 
     */
    public Vector3 Invert(){
        return this.scale(-1);
    }
    
    /**
     * a normalized version of this vector
     * @return vector
     */
    public Vector3 Normalize(){
        double f = this.Magnitude();
        return new Vector3(
                this.x/f,
                this.y/f,
                this.z/f
        );
    }
    
    /**
     * The magnitude of this vector
     * @return double
     */
    public double Magnitude(){
        return (double)Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    
    /**
     * The magnitude of this vector squared. Faster than Magnitude()
     * @return 
     */
    public double SquareMagnitude(){
        return (this.x*this.x + this.y*this.y + this.z*this.z);
    }
    
    /**
     * Dot product of this vector
     * @param b
     * @return double
     */
    public double Dot(Vector3 b){
        return this.x*b.x + this.y*b.y + this.z*b.z;
    }
    
    /**
     * Dot product between two vectors
     * @param a
     * @param b
     * @return 
     */
    public static double Dot(Vector3 a, Vector3 b){
        return a.Dot(b);
    }
    
    /**
     * The cross product of this vector
     * @param b
     * @return vector
     */
    public Vector3 Cross(Vector3 b){
        return new Vector3(
                this.y*b.z - this.z*b.y,
                this.z*b.x - this.x*b.z,
                this.x*b.y - this.y*b.x
        );
    }
 
    /**
     * Cross product of two vectors
     * @param a
     * @param b
     * @return 
     */
    public static Vector3 Cross(Vector3 a, Vector3 b){
        return a.Cross(b);
    }
    
    /**
     * Add a vector to this one
     * @param b
     * @return vector
     */
    public Vector3 add(Vector3 b){
        return new Vector3(
                this.x + b.x,
                this.y + b.y,
                this.z + b.z
        );
    }
    
    /**
     * Subtract a vector from this one
     * @param b
     * @return 
     */
    public Vector3 sub(Vector3 b){
        return new Vector3(
                this.x - b.x,
                this.y - b.y,
                this.z - b.z
        );
    }
    
    /**
     * Scale this vector by a scalar amount
     * @param a
     * @return 
     */
    public Vector3 scale(double a){
        return new Vector3(
                this.x * a,
                this.y * a,
                this.z * a
        );
    }
    /**
     * Linearly interpolate between two vectors
     * @param From
     * @param To
     * @param t
     * @return vector
     */
    public static Vector3 Lerp(Vector3 From, Vector3 To, double t){
        Vector3 Dir = To.sub(From);
            double val = (t > 1) ? 1 : t; 
            val = (val < 0) ? 0 : val;
            return From.add(Dir.scale(val));
    }
    
    /**
     * Angle between two vectors
     * @param From
     * @param To
     * @return double
     */
    public static double Angle(Vector3 From, Vector3 To) {
        return (double)Math.acos(From.Normalize().Dot(To.Normalize()));
    }
    
    /**
     * Distance between two vectors
     * @param From
     * @param To
     * @return double
     */
    public static double Distance(Vector3 From, Vector3 To) {
         return (To.sub(From)).Magnitude();
    }
    
    /**
     * Computes a maximum vector of two input vectors
     * @param a
     * @param b
     * @return 
     */
    public static Vector3 Max(Vector3 a, Vector3 b){
        double x= Math.max(a.x(), b.x()); 
        double y= Math.max(a.y(), b.y()); 
        double z= Math.max(a.z(), b.z());
        return new Vector3(x,y,z);
    }
    
    /**
     * Computes a maximum vector from a list of vectors
     * @param list
     * @return 
     */
    public static Vector3 Max(Vector3... list){
        double x = list[0].x;
        double y = list[0].y;
        double z = list[0].z;
        for(int i = 0; i < list.length; i++){
            x = Math.max(x, list[i].x);
            y = Math.max(y, list[i].y);
            z = Math.max(z, list[i].z);
        }
        return new Vector3(x,y,z);
    }
    
    /**
     * Computes a minimum vector of two input vectors
     * @param a
     * @param b
     * @return 
     */
    public static Vector3 Min(Vector3 a, Vector3 b){
        double x= Math.min(a.x(), b.x()); 
        double y= Math.min(a.y(), b.y()); 
        double z= Math.min(a.z(), b.z());
        return new Vector3(x,y,z);
    }
    
    /**
     * Computes a minimum vector from a list of vectors
     * @param list
     * @return 
     */
    public static Vector3 Min(Vector3... list){
        double x = list[0].x;
        double y = list[0].y;
        double z = list[0].z;
        for(int i = 0; i < list.length; i++){
            x = Math.min(x, list[i].x);
            y = Math.min(y, list[i].y);
            z = Math.min(z, list[i].z);
        }
        return new Vector3(x,y,z);
    }
    
    /**
     * Reflect a vector around a normal
     * @param normal
     * @return 
     */
    public Vector3 Reflect(Vector3 normal){
        return normal.scale(-2*(this.Dot(normal))).add(this);
    }
    
    /**
     * Project vector a onto b
     * @param a
     * @param b
     * @return 
     */
    public static Vector3 Project(Vector3 a, Vector3 b){
        double magB = b.Magnitude();
        return b.scale((a.Dot(b))/(magB*magB));
    }
    
    /**
     * Rotate a vector by a set of angles
     * @param a
     * @return 
     */
    public Vector3 Rotate(Vector3 a){
        Matrix rotX = new Matrix(new double[][]{{1,0,0,0},{0,Math.cos(a.x()),-Math.sin(a.x()),0},{0,Math.sin(a.x()),Math.cos(a.x()),0},{0,0,0,1}}); 
        Matrix rotY = new Matrix(new double[][]{{Math.cos(a.y()),0,Math.sin(a.y()),0},{0,1,0,0},{-Math.sin(a.y()),0,Math.cos(a.y()),0},{0,0,0,1}});
        Matrix rotZ = new Matrix(new double[][]{{Math.cos(a.z()),-Math.sin(a.z()),0,0},{Math.sin(a.z()),Math.cos(a.z()),0,0},{0,0,1,0},{0,0,0,1}});
        Matrix rot = (rotY).mul(rotX).mul(rotZ);
        Matrix ths = this.GetMatrix();
        Matrix nw = rot.mul(ths);
        return new Vector3(
                    nw.Get(0, 3),
                    nw.Get(1, 3),
                    nw.Get(2, 3)
        );
    }
    
    @Override
    public String toString(){
        return "("+x+","+y+","+z+")";
    }
    
    @Override
    public boolean equals(Object b){
        if(b instanceof Vector3){
            Vector3 vv = (Vector3)b;
            return this.x() == vv.x() && this.y() == vv.y() && this.z() == vv.z();
        }
        return false;
    }
}
