/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.physics;

import plus.graphics.Transform;
import plus.math.Quaternion;
import plus.math.Vector3;

/**
 *
 * @author Colin Halseth
 */
public class Body {
 
    //Body Properties
    private float mass; private float invMass;
    
    //Body Attributes
    private Vector3 momentum;
    private Vector3 angmomentum;
    
    private Quaternion spin;
    private Vector3 angvelocity;
    private Vector3 velocity;
    
    //Linked information
    private Transform transform;
    public boolean IsSleeping = false;
    public boolean UseGravity = true;
    
    public Body(float mass, Transform transform){
        this.mass = mass; this.invMass = 1/mass;
        this.transform = transform;
        
        this.angmomentum = Vector3.zero; this.angvelocity = Vector3.zero;
        this.momentum = Vector3.zero; this.velocity = Vector3.zero;
        
        //compute inertia tensor  ->  required for rotational motion
        
    }
   
    /**
     * Get the transformation this rigidbody applies to
     * @return 
     */
    public Transform GetTransform(){
        return this.transform;
    }
    
    /**
     * Apply a force to the center of mass 
     * @param force 
     */
    public void AddForce(Vector3 force){
        //dp/dt = F   ->   dp = Fdt
        this.momentum = this.momentum.add(force);
        this.velocity = this.momentum.scale(invMass);
        
        if(this.IsSleeping)
            this.IsSleeping = false;
    }
    
    /**
     * Apply a force at a point
     * @param point
     * @param force 
     */
    public void AddForceAt(Vector3 point, Vector3 force){
        this.AddForce(force);
        this.AddTorque(force.Cross(point.sub(this.transform.GetPosition())));
    }
    
    /**
     * Get the velocity of this rigidbody
     * @return 
     */
    public Vector3 GetVelocity(){
        return this.velocity;
    }
    
    /**
     * Get the angular velocity of this rigidbody
     * @return 
     */
    public Vector3 GetAngularVelocity(){
        return this.angvelocity;
    }
    
    /**
     * Add torque to this rigidbody
     * @param torque 
     */
    public void AddTorque(Vector3 torque){
        //angvelocity = this.angmomentum * invInertiaTensor;
        Quaternion orient = this.transform.GetRotation().Normalize();
        Quaternion q = new Quaternion(angvelocity.x(), angvelocity.y(), angvelocity.z(), 0);
        spin = (q.mul(orient)).scale(0.5f);
        
        if(this.IsSleeping)
            this.IsSleeping = false;
    }
    
    /**
     * Update the position and rotation of the body
     * @param deltatime 
     */
    protected void Update(double deltatime){
        //D = V*T
        this.transform.SetPosition(this.transform.GetPosition().add(this.velocity.scale(deltatime)));
        //R = Spin*T
        this.transform.SetRotation(this.transform.GetRotation().add(spin.scale(deltatime)));
    }
    
}
