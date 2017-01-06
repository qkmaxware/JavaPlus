package plus.graphics;

import plus.graphics.Geometry;
import plus.graphics.Transform;
import plus.math.Vector3;

public class AABB{

    //World - space extents
    private Vector3 min;
    private Vector3 max;
    
    //Local space corners of the box
    private final Vector3[] box = new Vector3[8]; //only 8 elements
    
    /**
     * Create an AABB from a max and min point n world space
     * @param min
     * @param max 
     */
    public AABB(Vector3 min, Vector3 max){
        this.min = min;
        this.max = max;
        
        //Compute box
        this.box[0] = min;
        this.box[1] = new Vector3(min.x(), min.y(), max.z());
        this.box[2] = new Vector3(max.x(), min.y(), min.z());
        this.box[3] = new Vector3(max.x(), min.y(), max.z());
        this.box[4] = new Vector3(min.x(), max.y(), max.z());
        this.box[5] = new Vector3(max.x(), max.y(), min.z());
        this.box[6] = new Vector3(max.x(), max.y(), max.z());
        this.box[7] = max;
    }
    
    /**
     * Create an AABB that encapsulates a geometry
     * @param geom 
     */
    public AABB(Geometry geom){
        Vector3[] verts = geom.GetVertices();
        //default to 0 in local coordinates aka point mass
        Vector3 max = Vector3.zero;
        Vector3 min = Vector3.zero;
        if(verts.length > 0){
            //something exists so default to this vertex instead
            max = verts[0];
            min = verts[0];

            for(int i = 0; i < verts.length; i++){
                max = Vector3.Max(verts[i], max);
                min = Vector3.Min(verts[i], min);
            }
        }
        //Compute box
        this.box[0] = min;
        this.box[1] = new Vector3(min.x(), min.y(), max.z());
        this.box[2] = new Vector3(max.x(), min.y(), min.z());
        this.box[3] = new Vector3(max.x(), min.y(), max.z());
        this.box[4] = new Vector3(min.x(), max.y(), max.z());
        this.box[5] = new Vector3(max.x(), max.y(), min.z());
        this.box[6] = new Vector3(max.x(), max.y(), max.z());
        this.box[7] = max;
        
        Rebuild(geom);
    }
    
    /**
     * Rebuild the size and position of this AABB from a transformation matrix
     * @param transform 
     */
    public void Rebuild(Transform transform){
        Vector3[] newbox = new Vector3[box.length];
        for(int i = 0; i < box.length; i++){
            newbox[i] = transform.TransformPoint(box[i]);
        }
        this.max = Vector3.Max(newbox);
        this.min = Vector3.Min(newbox);
    }
    
    /**
     * Get the length, height, depth of the bounding box
     * @return 
     */
    public Vector3 GetExtent(){
        return GetMax().sub(GetMin());
    }
    
    /**
     * Get the center of the bounding box
     * @return 
     */
    public Vector3 GetCenter(){
        return GetMin().add(GetExtent().scale(0.5f));
    }
    
    /**
     * Get the maximum corner of this bounding box
     * @return 
     */
    public Vector3 GetMax(){
        return this.max;
    }
    
    /**
     * Get the minimum corner of this bounding box
     * @return 
     */
    public Vector3 GetMin(){
        return this.min;
    }
    
    /**
     * Test if two AABB overlap
     * @param a
     * @param b
     * @return 
     */
    public static boolean Intersect(AABB a, AABB b){
        Vector3 distance1 = b.GetMin().sub(a.GetMax());
        Vector3 distance2 = a.GetMin().sub(b.GetMax());
        
        Vector3 distances = Vector3.Max(distance1, distance2);
        double dist = Math.max(Math.max(distances.x(), distances.y()), distances.z());
        
        return (dist < 0);
    }
    
    /**
     * Test if an AABB contains a point
     * @param box
     * @param point
     * @return 
     */
    public static boolean Contains(AABB box, Vector3 point){
        Vector3 max = box.GetMax();
        Vector3 min = box.GetMin();
        return (point.x() > min.x() && point.y() > min.y() && point.z() > min.z()) &&
               (point.x() < max.z() && point.y() < max.y() && point.z() < max.z());
    }
    
    /**
     * Test if this AABB intersects another
     * @param other
     * @return 
     */
    public boolean Intersect(AABB other){
        return AABB.Intersect(this, other);
    }
    
    /**
     * Test if this AABB intersects a point
     * @param point
     * @return 
     */
    public boolean Contains(Vector3 point){
        return AABB.Contains(this, point);
    }
}