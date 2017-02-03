/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.math.Matrix;
import plus.math.Quaternion;
import plus.math.Vector3;
import plus.system.functional.*;
import java.util.LinkedList;

/**
 * Class to replace transform, just as/more efficient and allows for hiearchies
 * @author Colin Halseth
 */
public class Transform {
    
    //Local transfomation data
    private Vector3 position = Vector3.zero;
    private Vector3 scale = Vector3.one;
    private Quaternion rotation = Quaternion.identity;
    
    private Matrix localToWorldMatrix;
    private Matrix worldToLocalMatrix;
    private boolean updateMatrix = true;
    private boolean updateBoundingbox = true;
    private AABB bounds = new AABB(Vector3.zero,Vector3.zero);
    
    private LinkedList<Transform> children = new LinkedList<Transform>();
    private Transform parent;
    
    //UTILITY METHODS ------------------
    protected double cos(double i){
        return Math.cos(i);
    }
    protected double sin(double i){
        return Math.sin(i);
    }
    protected double acos(double i){
        return Math.acos(i);
    }
    protected double asin(double i){
        return Math.asin(i);
    }
    //----------------------------------
    
    public Transform(){}
    public Transform(Transform transform){
        this.SetPosition(transform.GetPosition());
        this.SetRotation(transform.GetRotation());
        this.SetLocalScale(transform.GetLocalScale());
    }
    
    /**
     * Sets the position of this transform in local space
     * @param pos 
     */
    public void SetLocalPosition(Vector3 pos){
        this.position = pos;
        updateMatrix = true;
        updateBoundingbox = true;
    }
    
    /**
     * Sets the scale of this transform in local space
     * @param scale 
     */
    public void SetLocalScale(Vector3 scale){
        this.scale = scale;
        updateMatrix = true;
        updateBoundingbox = true;
    }
    
    /**
     * Sets the rotation of this transform in local space
     * @param rot 
     */
    public void SetLocalRotation(Quaternion rot){
        this.rotation = rot;
        //Also rotate direction vectors
        updateMatrix = true;
        updateBoundingbox = true;
    }
    
    /**
     * Sets the rotation of this transform using euler angles
     * @param angle 
     */
    public void SetLocalEulerAngles(Vector3 angle){
        this.SetLocalRotation(Quaternion.Euler(angle));
    }
    
    /**
     * Sets the position of this transform in world space. If this transform has no parent, this will be slower than SetLocalPosition() which will have the same results.
     * @param pos 
     */
    public void SetPosition(Vector3 pos){
	//This might be wrong
        if(this.parent == null){
            this.SetLocalPosition(pos);
        }
        else{
            Matrix n = this.WorldToLocalMatrix().mul(pos.GetMatrix());
            this.SetLocalPosition(new Vector3(n.Get(0, 3), n.Get(1,3), n.Get(2, 3)));
        }
    }
    
    /**
     * Sets the rotation of this transform in the world.
     * @param rot 
     */
    public void SetRotation(Quaternion rot){
        if(this.parent == null){
            this.rotation = rot;
        }
        else{
            this.rotation = this.parent.GetRotation().Conjugate().mul(rot);
        }
        updateMatrix = true;
        updateBoundingbox = true;
    }
    
    /**
     * Set the rotation of this transform in the world using euler angles
     * @param angle 
     */
    public void SetEulerAngles(Vector3 angle){
        SetRotation(Quaternion.Euler(angle));
    }
    
    /**
     * Get the local rotation in euler angles
     * @return 
     */
    public Vector3 GetLocalEulerAngles(){
        return this.rotation.Euler();
    }
    
    /**
     * Gets the scale of this transform in local space
     * @return 
     */
    public Vector3 GetLocalScale(){
        return this.scale;
    }
    
    /**
     * Gets the position of this transform in local space
     * @return 
     */
    public Vector3 GetLocalPosition(){
        return this.position;
    }
    
    /**
     * Gets the word space position of this transform. If this transformation has no parent, GetLocalPosition() is faster with identical results.
     * @return 
     */
    public Vector3 GetPosition(){
	//This might be wrong
        if(this.parent == null){
            return this.GetLocalPosition();
        }
        else{
            Matrix n = this.LocalToWorldMatrix().mul(this.GetLocalPosition().GetMatrix());
            Vector3 res = new Vector3(n.Get(0, 3), n.Get(1,3), n.Get(2, 3));
            return res;
        }
    }
    
    /**
     * Gets the rotation of this transform in the world.
     * @return 
     */
    public Quaternion GetRotation(){
        if(this.parent == null){
            return this.rotation;
        }
        else{
            return this.parent.GetRotation().mul(this.rotation);
        }
    }
    
    /**
     * Gets the rotation of this transform in the world using euler angles.
     * @return 
     */
    public Vector3 GetEulerAngles(){
        return GetRotation().Euler();
    }
    
    /**
     * Gets the rotation of this transform in local space
     * @return 
     */
    public Quaternion GetLocalRotation(){
        return this.rotation;
    }
    
    /**
     * Gets a vector in the forward direction
     * @return 
     */
    public Vector3 Forward(){
        Matrix m = this.LocalToWorldMatrix();
        return new Vector3(m.Get(0, 2), m.Get(1,2), m.Get(2, 2));
    }
    
    /**
     * Gets a vector in the right direction
     * @return 
     */
    public Vector3 Right(){
        Matrix m = this.LocalToWorldMatrix();
        return new Vector3(m.Get(0, 0), m.Get(1,0), m.Get(2, 0));
    }
    
    /**
     * Gets a vector in the up direction
     * @return 
     */
    public Vector3 Up(){
        Matrix m = this.LocalToWorldMatrix();
        return (new Vector3(-m.Get(0, 1), -m.Get(1,1),- m.Get(2, 1))); //down vector inverted
    }
    
    /**
     * Gets the parent transform
     * @return 
     */
    public Transform Parent(){
        return this.parent;
    }
    
    /**
     * Sets the parent of this transform
     * @param parent 
     */
    public void SetParent(Transform parent){
        if(this.parent != null)
            this.parent.children.remove(this);
        if(parent != null)
            parent.children.add(this);
        this.parent = parent;
    }
    
    /**
     * Gets the number of direct children of this transform
     * @return 
     */
    public int ChildCount(){
        return this.children.size();
    }
    
    /**
     * Gets the child of this transform at an index
     * @param index
     * @return 
     */
    public Transform GetChild(int index){
        return this.children.get(index);
    }

    /**
     * Gets all children transformations
     * @return 
     */
    public LinkedList<Transform> GetChildren(){
        return this.children;
    }
    
    /**
     * Create a 4x4 transformation matrix with the given values for position, rotation, and scale
     * @param translation
     * @param rotation
     * @param scale
     * @return 
     */
    public static Matrix TRS(Vector3 translation, Quaternion rotation, Vector3 scale){
        double x = translation.x(); double y = translation.y(); double z = translation.z();
        double l = scale.x(); double j = scale.y(); double k = scale.z();
        double a = rotation.x(); double b = rotation.y(); double c = rotation.z(); double d = rotation.w();
        
        
        Matrix m = new Matrix(new double[][]{
            {l*(1-2*c*c - 2*b*b), l*(2*a*b - 2*c*d), l*(2*a*c + 2*b*d), x},
            {j*(2*a*b + 2*c*d), j*(1-2*c*c - 2*a*a), j*(2*b*c - 2*a*d), y},
            {k*(2*a*c - 2*b*d), k*(2*a*d + 2*b*c), k*(1-2*b*b -2*a*a), z},
            {0,0,0,1}
        });
        
        return m;
    }
    
    /**
     * Recompute the transformation matrices
     */
    public void RecomputeMatrices(){
        //localToWorld --> trans * scale * rotY * rotX * rotZ
        this.localToWorldMatrix = Transform.TRS(position, rotation, scale);
        /*
        double x = position.x(); double y = position.y(); double z = position.z();
        double l = scale.x(); double j = scale.y(); double k = scale.z();
        //TODO use quaterionons only, don't convert to EulerAngles
        Vector3 rotation = this.rotation.ToEulerAngle();
        double a = plus.math.Mathx.DegreesToRadians(rotation.x()); double b = plus.math.Mathx.DegreesToRadians(rotation.y()); double g = plus.math.Mathx.DegreesToRadians(rotation.z());
        
        this.localToWorldMatrix = new Matrix(
                new double[][]
                {
                    {l*sin(a)*sin(b)*sin(g) + l*cos(b)*cos(g), l*sin(a)*sin(b)*cos(g) - l*sin(g)*cos(b), l*sin(b)*cos(a), x},
                    {j*sin(g)*cos(a), j*cos(a)*cos(g), -j*sin(a), y},
                    {k*sin(a)*sin(g)*cos(b)-k*sin(b)*cos(g), k*sin(a)*cos(b)*cos(g)+k*sin(b)*sin(g), k*cos(a)*cos(b), z},
                    {0,0,0,1}
                }
        );*/
		
        //world to local as inverse of localToWorld --> affine inverse of localToWorld
        /*
        a b c b.x
        d e f b.y
        g h i b.z
        0 0 0 1
        http://stackoverflow.com/questions/2624422/efficient-4x4-matrix-inverse-affine-transform
        http://math.stackexchange.com/questions/21533/shortcut-for-finding-a-inverse-of-matrix/21537
        http://stackoverflow.com/questions/9455298/3x3-matrix-determinant-function-making-it-faster
        */
        Matrix A = this.localToWorldMatrix;
        //3x3 submatrix values 'M'
        double a = A.Get(0, 0); double b = A.Get(0, 1); double c = A.Get(0, 2);
        double d = A.Get(1, 0);double e = A.Get(1, 1);double f = A.Get(1, 2);
        double g = A.Get(2, 0);double h = A.Get(2, 1);double i = A.Get(2, 2);
        double bx = A.Get(0,3); double by = A.Get(1,3); double bz = A.Get(2, 3);
        double det = -a*f*h + e*a*i - b*d*i + b*f*g + c*d*h - e*c*g;
        //Inverse of 'M'
        double invMa = (e*i - f*h)/det; double invMb = -(b*i-c*h)/det; double invMc = (b*f - c*e)/det;
        double invMd = -(d*i - f*g)/det; double invMe = (a*i - c*g)/det;double invMf = -(a*f - c*d)/det;
        double invMg = (d*h-e*g)/det; double invMh =  -(a*h-b*g)/det; double invMi = (a*e-b*d)/det;
        /*
        Matrix invM = new Matrix(3,3, 
                new float[]{
                    (e*i - f*h)/det, -(b*i-c*h)/det, (b*f - c*e)/det,
                    -(d*i - f*g)/det, (a*i - c*g)/det, -(a*f - c*d)/det,
                    (d*h-e*g)/det, -(a*h-b*g)/det, (a*e-b*d)/det
                }
        );*/
        double nInvMbx = -(invMa*bx + invMb*by + invMc*bz);
        double nInvMby = -(invMd*bx + invMe*by + invMf*bz);
        double nInvMbz = -(invMg*bx + invMh*by + invMi*bz);
        Matrix in = new Matrix(new double[][]{
            {invMa, invMb,  invMc,  nInvMbx},
            {invMd, invMe,  invMf,  nInvMby},
            {invMg, invMh,  invMi,  nInvMbz},
            {0,     0,      0,      1}
        });
        
        this.worldToLocalMatrix = in;
        
        this.updateMatrix = false;
    }
    
    /**
     * Gets the matrix to transform from local coordinates to world coordinates
     * @return 
     */
    public Matrix LocalToWorldMatrix(){
        if(updateMatrix)
            RecomputeMatrices();
        if(this.parent != null)
            return this.parent.LocalToWorldMatrix().mul(this.localToWorldMatrix);
        else
            return this.localToWorldMatrix;
    }
    
    /**
     * Gets the matrix to transform from world coordinates to local coordinates
     * @return 
     */
    public Matrix WorldToLocalMatrix(){
        if(updateMatrix)
            RecomputeMatrices();
        if(this.parent != null)
            return this.parent.WorldToLocalMatrix().mul(this.worldToLocalMatrix);
        else
            return this.worldToLocalMatrix;
    }
    
    /**
     * Shortcut for LocalToWorldMatrix()
     * @return 
     */
    public Matrix GetMatrix(){
        return this.LocalToWorldMatrix();
    }
    
    /**
     * Shortcut for WorldToLocalMatrix()
     * @return 
     */
    public Matrix AffineInverse(){
        return this.WorldToLocalMatrix();
    }
    
    /**
     * Recalculate the bounding volume's world coordinates
     */
    public void RecalculateBounds(){
        this.bounds.Rebuild(this);
    }
    
    /**
     * Get the bounding box for this transformation
     * @return 
     */
    public AABB GetBounds(){
        if(updateBoundingbox){
            RecalculateBounds();
        }
        
        updateBoundingbox = false;
        
        return this.bounds;
    }
    
    /**
     * Set a new bounding box for this transform
     * @param bounds 
     */
    protected void SetBounds(AABB bounds){
        this.bounds = bounds;
    }
    
    /**
     * Transform a point in local coordinates into world coordinates
     * @param a
     * @return 
     */
    public Vector3 TransformPoint(Vector3 a){
        Matrix m = this.LocalToWorldMatrix();
        Matrix r = m.mul(a.GetMatrix());
        return new Vector3(r.Get(0, 3), r.Get(1, 3), r.Get(2, 3));
    }
    
    /**
     * Transforms a direction from local coordinates to world coordinates
     * @param a
     * @return 
     */
    public Vector3 TransformDirection(Vector3 a){
        Vector3 d = this.GetRotation().mul(a);
        return d;
    }
    
    /**
     * Transform a point in world coordinates into local coordinates
     * @param a
     * @return 
     */
    public Vector3 InverseTransformPoint(Vector3 a){
        Matrix m = this.WorldToLocalMatrix();
        Matrix r = m.mul(a.GetMatrix());
        return new Vector3(r.Get(0, 3), r.Get(1, 3), r.Get(2, 3));
    }
    
    /**
     * Transform a direction from world coordinates to local coordinates 
     * @param a
     * @return 
     */
    public Vector3 InverseTransformDirection(Vector3 a){
        return this.GetRotation().Conjugate().mul(a);
    }
    
    /**
     * Call a function for this object and each of it's children
     * @param fn 
     */
    public void Cascade(Action1<Transform> fn){
        fn.Invoke(this);
        for(Transform child : this.GetChildren()){
            child.Cascade(fn);
        }
    }
    
    /**
     * Rotate this transform around a point in world-space
     * @param pivot
     * @param angle 
     */
    public void RotateAround(Vector3 pivot, Quaternion angle){
        Vector3 dir = this.GetPosition().sub(pivot);
        dir = angle.mul(dir);
        Vector3 point = dir.add(pivot);
        this.SetPosition(point);
    }
    
    /**
     * Align this tranform's forward vector to be facing a point in world-space
     * @param position 
     */
    public void LookAt(Vector3 position){
        Vector3 w =(position.sub(this.GetPosition())).Normalize();
        Vector3 a = Vector3.Cross(Vector3.forward, w);
        double theta = Math.acos(Vector3.Dot(Vector3.forward, w));
        Quaternion q = new Quaternion(
                a.x() * Math.sin(theta/2),
                a.y() * Math.sin(theta/2),
                a.z() * Math.sin(theta/2),
                Math.cos(theta/2)
        );
        this.SetRotation(q);
    }
}
