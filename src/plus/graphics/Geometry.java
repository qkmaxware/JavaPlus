/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.math.Matrix;
import plus.math.Vector3;

/**
 *
 * @author Colin Halseth
 */
public class Geometry extends RenderObject{
    
    public enum RenderMode{
        Wireframe, Solid
    }
    
    protected RenderMode rendermode = RenderMode.Solid;
    
    public static final Geometry cube = new Geometry(new Vector3[]
                                                    {
                                                        new Vector3(-1,-1,1),
                                                        new Vector3(-1,1,1),
                                                        new Vector3(-1,-1,-1),
                                                        new Vector3(-1,1,-1),
                                                        new Vector3(1,-1,1),
                                                        new Vector3(1,1,1),
                                                        new Vector3(1,-1,-1),
                                                        new Vector3(1,1,-1),
                                                    },
                                                    new int[]
                                                    {
                                                        3,2,0,
                                                        7,6,2,
                                                        
                                                        5,4,6,
                                                        1,0,4,
                                                        
                                                        2,6,4,
                                                        7,3,1,
                                                        
                                                        1,3,0,
                                                        3,7,2,
                                                        
                                                        7,5,6,
                                                        5,1,4,
                                                        
                                                        0,2,4,
                                                        5,7,1 
                                                    },
                                                    new Vector3[]{
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                        new Vector3(0,0,0), new Vector3(1,0,0), new Vector3(0,1,0),
                                                    }
                                                    );
    public static final Geometry point = new Geometry(new Vector3[]{Vector3.zero});
    
    public static final Geometry plane = new Geometry(
                                                    new Vector3[]{
                                                        new Vector3(-1,-1,0),
                                                        new Vector3( 1,-1,0),
                                                        new Vector3(-1, 1,0),
                                                        new Vector3( 1, 1,0),
                                                    },
                                                    new int[]{
                                                        0,1,2,
                                                        1,2,3
                                                    },
                                                    new Vector3[]{
                                                        new Vector3(0,1,0), new Vector3(1,1,0), new Vector3(0,0,0),
                                                        new Vector3(1,1,0), new Vector3(0,0,0), new Vector3(1,0,0)
                                                    }
    );
    
    //Spacial data
    protected Vector3[] vertices;
    protected int[] tris;
    protected Vector3[] uvs;

    //Textrue data
    protected Bitmap texture;
    
    public Geometry(){
        this.vertices = new Vector3[0];
        this.tris = new int[0];
        this.uvs = new Vector3[0];
        
        AABB newBounds = new AABB(this);
        this.SetBounds(newBounds);
        newBounds.Rebuild(this);
    }
    public Geometry(Vector3[] verts){
        this.vertices = verts;
        this.tris = new int[0];
        this.uvs = new Vector3[0];
        
        AABB newBounds = new AABB(this);
        this.SetBounds(newBounds);
        newBounds.Rebuild(this);
    }
    public Geometry(Vector3[] verts, int[] edges){
        this.vertices = verts;
        this.tris = edges;
        this.uvs = new Vector3[0];
        
        AABB newBounds = new AABB(this);
        this.SetBounds(newBounds);
        newBounds.Rebuild(this);
    }
    public Geometry(Vector3[] verts, int[] edges, Vector3[] uvs){
        this.vertices = verts;
        this.tris = edges;
        this.uvs = uvs;
        
        AABB newBounds = new AABB(this);
        this.SetBounds(newBounds);
        newBounds.Rebuild(this);
    }
    public Geometry(Geometry geom){
        this.vertices = new Vector3[geom.vertices.length];
        this.tris = new int[geom.tris.length];
        this.uvs = new Vector3[geom.uvs.length];
        
        System.arraycopy(geom.vertices, 0, this.vertices, 0, geom.vertices.length);
        System.arraycopy(geom.tris, 0, this.tris, 0, geom.tris.length);
        System.arraycopy(geom.uvs, 0, this.uvs, 0, geom.uvs.length);
        
        this.SetPosition(geom.GetPosition());
        this.SetRotation(geom.GetRotation());
        this.SetLocalScale(geom.GetLocalScale());
        
        AABB newBounds = new AABB(this);
        this.SetBounds(newBounds);
        newBounds.Rebuild(this);
    }
    
    /**
     * Sets the render mode of this object
     * @param mode 
     */
    public void SetRenderMode(RenderMode mode){
        rendermode = mode;
    }
   
    /**
     * Gets the render mode of this object (wireframe, solid)
     * @return 
     */
    public RenderMode GetRenderMode(){
        return this.rendermode;
    }
    
    /**
     * Assigns a texture to this geometry
     * @param i 
     */
    public void SetBitmap(Bitmap i){
        this.texture = i;
    }
    
    /**
     * Array of all vertices for this geometry, in local coordinates
     * @return 
     */
    public Vector3[] GetVertices(){
        return this.vertices;
    }
    
    /**
     * Get a list of vertices in world coordinates, projects vertices using this transform
     * @return 
     */
    public Vector3[] GetWorldVertices(){
        Matrix mat = this.GetMatrix();
        Vector3[] proj = new Vector3[this.vertices.length];
        for(int i = 0; i < this.vertices.length; i++){
            Matrix p = mat.mul(this.vertices[i].GetMatrix());
            Vector3 v = new Vector3(
                    p.Get(0, 3),
                    p.Get(1, 3),
                    p.Get(2, 3)
            );
            proj[i] = v;
        }
        return proj;
    }
    
    /**
     * Returns a list of indexes that make up a triangle
     * @return 
     */
    public int[] GetTriangles(){
        return this.tris;
    }
    
    /**
     * Returns a list of uv coordinates related to each vertex
     * @return 
     */
    public Vector3[] GetUvs(){
        return this.uvs;
    }

    /**
     * Render this object to a camera
     * @param cam 
     */
    @Override
    public void Render(Camera cam) {
        for(int i = 0; i < this.tris.length; i+=3){
            //Get the vertex indexes for this edge
            int edge1 = this.tris[i];
            int edge2 = this.tris[i+1];
            int edge3 = this.tris[i+2];
            
            //Project vetices. If not inside view frustrum skip drawing.
            Vector3[] args = cam.ProjectTriangle(this.GetMatrix(), this.vertices[edge1], this.vertices[edge2], this.vertices[edge3]);
            if(args == null){
                continue;
            }

            //Get the vertices for this edge from the indexes
            Vector3 vertex = args[0];
            Vector3 vertex2 = args[1];
            Vector3 vertex3 = args[2];
            
            //Get uvs ...
            Vector3 uv1 = (this.uvs.length > i)?this.uvs[i]:Vector3.zero;
            Vector3 uv2 = (this.uvs.length > i+1)?this.uvs[i+1]:Vector3.zero;
            Vector3 uv3 = (this.uvs.length > i+2)?this.uvs[i+2]:Vector3.zero;
            
            //Draw a line between the points
            switch(rendermode){
                case Wireframe:
                    cam.DrawLine2D(vertex, vertex2, uv1, uv2, texture);
                    cam.DrawLine2D(vertex, vertex3, uv1, uv3, texture);
                    cam.DrawLine2D(vertex2, vertex3, uv2, uv3, texture);
                    break;
                case Solid:
                    cam.FillTriangle2D(vertex,vertex2,vertex3,uv1,uv2,uv3,texture);
                    break;
            }
        }
    }
}
