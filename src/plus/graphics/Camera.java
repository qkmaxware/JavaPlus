/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.graphics;

import plus.math.Vector3;
import plus.math.Matrix;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;

/**
 *
 * @author Colin Halseth
 */
public class Camera extends Transform{
    
    //Colors to sample when drawing
    protected Bitmap Img;
    //Colors to sample when editing
    protected int[] Buffer;
    //Depth buffer to mask upfront pixels
    protected double[] DepthBuffer;
    //Background color to draw when no color exists in maps;
    protected Color background = Color.black;
    
    //CAMERA SPECIFICS --------------------------------------------------------
    //Size of the render image
    protected Dimension dimentions;
    //How to project coordinates onto the screen
    protected RenderMode mode = RenderMode.Orthographic;
    //Lens focal length
    protected int focallength = 35; 
    float n = 0.1f;                                         //Near clip plane
    float f = 1000;                                         //Far clip plane
    
    private Matrix ortho;
    public Matrix persp;
    
    public enum RenderMode{
        Orthographic, Perspective
    }
    
    public Camera(int width, int height){
        this.SetSize(width, height);
    }
    
    /**
     * Gets the distance to the far clip plane
     * @return 
     */
    public float GetFarclipDistance(){
        return this.f;
    }
    
    /**
     * Gets the distance to the near clip plane
     * @return 
     */
    public float GetNearclipDistance(){
        return this.n;
    }
    
    /**
     * Set the distances to the near and far clip planes
     * @param near
     * @param far 
     */
    public void SetClipPlanes(float near, float far){
        near = Math.abs(near);
        far = Math.abs(far);
        this.n = Math.min(near, far);
        this.f = Math.max(near,far);
        this.RebuildMatrices();
    }
    
    /**
     * Get the render height of this camera
     * @return 
     */
    public int GetWidth(){
        return this.dimentions.width;
    }
    /**
     * Get the render width of this camera;
     * @return 
     */
    public int GetHeight(){
        return this.dimentions.height;
    }
    
    /**
     * Sets the render height
     * @param height 
     */
    public void SetHeight(int height){
        int width = this.dimentions.width;
        Buffer = new int[width*height];
        Img = new Bitmap(width,height);
        dimentions = new Dimension(width,height);
        DepthBuffer = new double[this.dimentions.height*this.dimentions.width];
        RebuildMatrices();
    }
    
    /**
     * Sets the render width
     * @param width 
     */
    public void SetWidth(int width){
        int height = this.dimentions.width;
        Buffer = new int[width*height];
        Img = new Bitmap(width,height);
        dimentions = new Dimension(width,height);
        DepthBuffer = new double[this.dimentions.height*this.dimentions.width];
        RebuildMatrices();
    }
    
    /**
     * Sets the render dimentions
     * @param width
     * @param height 
     */
    public void SetSize(int width, int height){
        Buffer = new int[width*height];
        Img = new Bitmap(width,height);
        dimentions = new Dimension(width,height);
        DepthBuffer = new double[this.dimentions.height*this.dimentions.width];
        RebuildMatrices();
    }
    
    /**
     * Gets a raster pixel array
     * @return 
     */
    public int[] GetRaster(){
        return this.Img.GetRaster();
    }
    
    /**
     * Get the bitmap this camera renders to
     * @return 
     */
    public Bitmap GetBitmap(){
        return this.Img;
    }
    
    /**
     * Set the focal length of this camera. Used in perspective mode.
     * @param i 
     */
    public void SetFocalLength(int i){
        focallength = i;
        RebuildMatrices();
    }
    
    /**
     * Gets the focal length of this camera.
     * @return 
     */
    public int GetFocalLength(){
        return focallength;
    }
    
    public void RebuildMatrices(){
        
        //compute focal length specific projection variables #WIP ext.math.Util.atanf
        double fov = Math.tan(plus.math.Util.DegreesToRadians(focallength)*0.5f);
        double zoom = 1/fov;
        double aspect = this.GetWidth()/this.GetHeight();
        double z1 = (f + n)/(n - f);
        double z2 = (2*f*n)/(n - f);
        persp = new Matrix(new double[][]{
            {zoom/aspect, 0 ,0 ,0},
            {0, zoom, 0, 0},
            {0, 0, z1, z2},
            {0, 0, 1, 0}
        });
        float FOVx = (float)(2*Math.atan(0.5f*this.GetWidth()/focallength));
        float FOVy = (float)(2*Math.atan(0.5f*this.GetHeight()/focallength));
        ortho = new Matrix(new double[][]{
            {Math.atan(FOVx/2),0,0,0},
            {0,Math.atan(FOVy/2),0,0},
            {0,0,-(f+n)/(f-n), (-2.0*(n*f))/(f-n)},
            {0,0,-1,0}
        });
        
        /*
        ANOTHER WAY THAT NIGHT WORK BETTER
            S = 1 / tan((fov/2))
            [S 0    0       0]
            [0 S    0       0]
            [0 0 -f/(f-n)  -1]
            [0 0 -f*n/(f-n) 0]
        
            [2/w 0 0 0]
            [0 2/h 0 0]
            [0 0 -2/(f-n) (f+n)/(f-n)]
            [0 0 0 1]
        */
    }
    
    /**
     * Get the 1D index from 2D coordinates
     * @param x
     * @param y
     * @return index
     */
    protected int GetIndex(int x, int y){
        return (y*dimentions.width + x);
    }
    
    /**
     * Forcefully set the pixel's color without using the draw or z buffers
     * @param x
     * @param y
     * @param c 
     */
    public void PostProcessPixel(int x, int y, Color c){
        Img.GetRaster()[GetIndex(x,y)] = c.getRGB();
    }
    
    /**
     * Sets the pixel at position c,y to a color, depth value is 0
     * @param x
     * @param y
     * @param c 
     */
    public void SetPixel(int x, int y, Color c){
        SetPixel(x,y,this.n,c);
    }
    
    /**
     * Sets the pixel at coordinates x,y with a distance from the camera z using the depth mask if need-be
     * @param x
     * @param c 
     */
    public void SetPixel(Vector3 x, Color c){
        SetPixel((int)x.x(),(int)x.y(),x.z(),c);
    }
    
    /**
     * Sets the pixel at coordinates x,y with a distance from the camera +z using the depth mask if need-be
     * @param x
     * @param y
     * @param z
     * @param c 
     */
    public void SetPixel(int x, int y, double z, Color c){
        //pixel has to be visible, and has to be within the draw bounds
        int i = GetIndex(x,y);
        if(c.getAlpha() > 0 && x>=0&&y>=0&&x<dimentions.width&&y<dimentions.height && z>=n&&z<=this.DepthBuffer[i]){
            Buffer[i] = c.getRGB(); //BlendColor(new Color(Buffer[i], true),c,c.getAlpha()/255).getRGB();
            DepthBuffer[i] = z;
        }
    }
    
    /**
     * Linearly interpolate two colors
     * @param a
     * @param b
     * @param t
     * @return 
     */
    protected Color BlendColor(Color a, Color b, float t){
        if(a == null && b == null){
            return Color.white;
        }else if(a == null){
            return b;
        }else if(b == null){
            return a;
        }else{ 
            float r = t*b.getRed() + (1-t)*a.getRed();
            float g = t*b.getGreen() + (1-t)*a.getGreen();
            float bb= t*b.getBlue() + (1-t)*a.getBlue();
            
            return new Color((int)r, (int)g, (int)bb);
        }
    }
    
    /**
     * Gets the pixel at a certain position
     * @param x
     * @param y
     * @return 
     */
    public Color GetPixel(int x, int y){
        Color c = new Color(Img.GetRaster()[GetIndex(x,y)],true);
        return (c == null)?this.background:c;
    }
    
    /**
     * Clears the buffer
     */
    public void Clear(){
        Arrays.fill(Buffer, this.background.getRGB());
    }
    
    /**
     * Commits all changes done to the image
     */
    public void Flush(){
        Img.SetRaster(Buffer);
        Arrays.fill(DepthBuffer, this.f);   //Fill up to the far clip 
        Arrays.fill(Buffer, this.background.getRGB());
    }
    
    /**
     * Sets the render mode for this camera
     * @param mode 
     */
    public void SetRenderMode(RenderMode mode){
        this.mode = mode;
    }
    
    /**
     * Get the background color
     * @return color
     */
    public Color GetBackgroundColor(){
        return this.background;
    }
    
    /**
     * Sets the background color 
     * @param c 
     */
    public void SetbackgroundColor(Color c){
        this.background = c;
    }
    
    /**
     * Fill a 2d triangle on the screen by splitting it into smaller triangles, also allows for texturing
     * @param a
     * @param b
     * @param c
     * @param uva
     * @param uvb
     * @param uvc
     * @param img 
     */
    public void FillTriangle2D(Vector3 a, Vector3 b, Vector3 c, Vector3 uva, Vector3 uvb, Vector3 uvc, Bitmap img){
        //Sort vertices by ascending 'y' into a,b,c, keep uvs with it
        if(a.y() > b.y()){
            //swap a,b
            Vector3 old = a; Vector3 olduv = uva;
            a = b;  uva = uvb;
            b = old; uvb = olduv;
        }
        if(b.y() > c.y()){
            //swap b,c
            Vector3 old = b; Vector3 olduv = uvb;
            b = c;  uvb = uvc;
            c = old; uvc = olduv;
        }
        if(a.y() > b.y()){
            //swap a,b
            Vector3 old = a; Vector3 olduv = uva;
            a = b;  uva = uvb;
            b = old; uvb = olduv;
        }

        //Try the three rasterization cases
        if(b.y() == c.y()){
            //Fill Bottom Flat Triangle
            DrawLine2D(a,b,uva,uvb,img);
            DrawLine2D(b,c,uvb,uvc,img);
            DrawLine2D(a,c,uva,uvc,img);
            fillBottomFlat(a,b,c,uva,uvb,uvc,img);
        }
        else if(a.y() == b.y()){
            //Fill top flat triangle
            DrawLine2D(a,b,uva,uvb,img);
            DrawLine2D(b,c,uvb,uvc,img);
            DrawLine2D(a,c,uva,uvc,img);
            fillTopFlat(a,b,c,uva,uvb,uvc,img);
        }
        else{
            //General case
            //Split into a top and bottom flat triangles
            double t = (b.y() - c.y())/(a.y() - c.y());
            Vector3 d = new Vector3(
                    (a.x()) + ((b.y() - a.y()) / (c.y() - a.y())) * (c.x() - a.x()),
                    b.y(),
                    plus.math.Util.Lerp(c.z(), a.z(), t) //matters for z buffering not implemented yet, still think this is slightly wrong
            );
            Vector3 uvd = new Vector3( //im thinking this is wrong...especially since uvs can be any orientation
                    plus.math.Util.Lerp(uvc.x(), uva.x(), t),
                    plus.math.Util.Lerp(uvc.y(), uva.y(), t)
            );
            //Fill 2 new triangles, and sketch the edges, need new draw line 2d function for uv mapping
            DrawLine2D(a,b,uva,uvb,img);
            DrawLine2D(b,c,uvb,uvc,img);
            DrawLine2D(a,c,uva,uvc,img);
            DrawLine2D(b,d,uvb,uvd,img);
            fillBottomFlat(a,b,d, uva, uvb, uvd, img);
            fillTopFlat(b,d,c, uvb, uvd, uvc, img);
        }
    }
    
    
    protected void fillBottomFlat(Vector3 a, Vector3 b, Vector3 c, Vector3 uva, Vector3 uvb, Vector3 uvc, Bitmap img){
        double invslope1 = (b.x() - a.x())/(b.y() - a.y());
        double invslope2 = (c.x() - a.x())/(c.y() - a.y());
        
        float leftX = (float)Math.floor(a.x());
        float rightX = (float)Math.floor(a.x());
        
        int startH = (int)Math.floor(a.y());
        int endH = (int)Math.floor(b.y());
        
        //Give the distance values to the uv coordinates
        uva = new Vector3(uva.x(), uva.y(), a.z());
        uvb = new Vector3(uvb.x(), uvb.y(), b.z());
        uvc = new Vector3(uvc.x(), uvc.y(), c.z());
        
        for(int scan = startH; scan < endH; scan++){
            
            //Texturing only
            float t = ((float)scan - startH)/(endH - startH);

            Vector3 left = Vector3.Lerp(uva, uvb, t);
            Vector3 right = Vector3.Lerp(uva, uvc, t);
            
            double zL = plus.math.Util.Lerp(a.z(), b.z(), t);
            double zR = plus.math.Util.Lerp(a.z(), c.z(), t);
            DrawLine2D(new Vector3(leftX,scan,zL),new Vector3(rightX,scan,zR),left,right,img);
            
            leftX += invslope1;
            rightX += invslope2;
        }
    }
    protected void fillTopFlat(Vector3 a, Vector3 b, Vector3 c, Vector3 uva, Vector3 uvb, Vector3 uvc, Bitmap img){
        double invslope1 = (c.x() - a.x()) / (c.y() - a.y());
        double invslope2 = (c.x() - b.x()) / (c.y() - b.y());
        
        float leftX = (float)Math.floor(c.x());
        float rightX = (float)Math.floor(c.x());

        int startH = (int)Math.floor(c.y());
        int endH = (int)Math.floor(a.y());

        //Give the distance values to the uv coordinates
        uva = new Vector3(uva.x(), uva.y(), a.z());
        uvb = new Vector3(uvb.x(), uvb.y(), b.z());
        uvc = new Vector3(uvc.x(), uvc.y(), c.z());
        
        for(int scan = startH; scan > endH; scan--){
            float t = ((float)scan - startH)/(endH - startH);

            Vector3 left = Vector3.Lerp(uvc, uva, t);
            Vector3 right = Vector3.Lerp(uvc, uvb, t);

            double zL = plus.math.Util.Lerp(c.z(), a.z(), t);
            double zR = plus.math.Util.Lerp(c.z(), b.z(), t);
            DrawLine2D(new Vector3(leftX,scan, zL),new Vector3(rightX,scan, zR),left,right,img);
            
            leftX -= invslope1;
            rightX -= invslope2;
        }
    }
    
    /**
     * Draws a line on a camera between two points
     * @param a
     * @param b
     * @param c
     */
    private void DrawLine2D(Vector3 a, Vector3 b, Color c){ 
        float distance = (float)Math.sqrt((b.x() - a.x())*(b.x() - a.x()) + (b.y() - a.y())*(b.y() - a.y()));
        for(int i = 0; i < distance; i++){
            Vector3 pix = Vector3.Lerp(a, b, i/distance);
            this.SetPixel((int)pix.x(),(int)pix.y(), pix.z(), c);
        }
    }
    
    /**
     * Draw a line on camera textured with a uv map, defaults to red line if not provided with an image
     * Note to self...could be more optimized this is a very naive approach.
     * Note 2... This is by far the slowest part of the code. Optimize asap
     * @param a
     * @param b
     * @param uva
     * @param uvb
     * @param map 
     */
    public void DrawLine2D(Vector3 a, Vector3 b, Vector3 uva, Vector3 uvb, Bitmap map){
        //Clip lines to be within the bound of the camera (prevent huge draw attempts)
        float[] ne = new float[]{0,1};
        //float[] ne = GetIntersection(a,b); //Temporary removal of line trimming
        if(ne == null){return;}
        
        a = Vector3.Lerp(a, b, ne[0]); b = Vector3.Lerp(a, b, ne[1]);
        uva = Vector3.Lerp(uva, uvb, ne[0]); uvb = Vector3.Lerp(uva, uvb, ne[1]);
        
        //If no texture exits draw a basic line
        if(map == null){
            //DrawLine2D(a,b,Color.red);
            DrawLine((int)a.x(),(int)a.y(),(int)a.z(), (int)uva.x(), (int)uva.y(), (int)b.x(), (int)b.y(), (int)b.z(), (int)uvb.x(), (int)uvb.y());
            return;
        }
        
        //Else draw the line with a texture
        float dist2d = (float)Math.sqrt((b.x() - a.x())*(b.x() - a.x()) + (b.y() - a.y())*(b.y() - a.y()));
        float invdistance = 1/dist2d;
        //Draw line
        for(float i = ne[0]; i < ne[1]; i+=invdistance){
            Vector3 pix = Vector3.Lerp(a, b, i);
            Vector3 uv = Vector3.Lerp(uva, uvb, i);
            double u = uv.x();
            double v = uv.y();
            Color color = map.GetColor((int)(u*(map.GetWidth()-1)), (int)(v*(map.GetHeight()-1)));
            this.SetPixel(pix, color);
        }
    }
    
    /**
     * Draw a line between 2 points. Uses a modified Bresenham's line algorithm with 3 coordinates and uv mappings
     * Original algorithm found at https://gist.github.com/yamamushi/5823518
     * @param x1
     * @param y1
     * @param z1
     * @param u1
     * @param v1
     * @param x2
     * @param y2
     * @param z2
     * @param u2
     * @param v2 
     */
    public void DrawLine(int x1, int y1, int z1, int u1, int v1, int x2, int y2, int z2, int u2, int v2){
        int i, dx, dy, dz, l, m, n, x_inc, y_inc, z_inc, err_1, err_2, dx2, dy2, dz2;

        dx = x2 - x1;
        dy = y2 - y1;
        dz = z2 - z1;
        x_inc = (dx < 0) ? -1 : 1;
        l = Math.abs(dx);
        y_inc = (dy < 0) ? -1 : 1;
        m = Math.abs(dy);
        z_inc = (dz < 0) ? -1 : 1;
        n = Math.abs(dz);
        dx2 = l << 1;
        dy2 = m << 1;
        dz2 = n << 1;

        if ((l >= m) && (l >= n)) {
            err_1 = dy2 - l;
            err_2 = dz2 - l;
            for (i = 0; i < l; i++) {
                this.SetPixel(x1, y1, z1, Color.red);
                if (err_1 > 0) {
                    y1 += y_inc;
                    err_1 -= dx2;
                }
                if (err_2 > 0) {
                    z1 += z_inc;
                    err_2 -= dx2;
                }
                err_1 += dy2;
                err_2 += dz2;
                x1 += x_inc;
            }
        } else if ((m >= l) && (m >= n)) {
            err_1 = dx2 - m;
            err_2 = dz2 - m;
            for (i = 0; i < m; i++) {
                this.SetPixel(x1, y1, z1, Color.red);
                if (err_1 > 0) {
                    x1 += x_inc;
                    err_1 -= dy2;
                }
                if (err_2 > 0) {
                    z1 += z_inc;
                    err_2 -= dy2;
                }
                err_1 += dx2;
                err_2 += dz2;
                y1 += y_inc;
            }
        } else {
            err_1 = dy2 - n;
            err_2 = dx2 - n;
            for (i = 0; i < n; i++) {
                this.SetPixel(x1, y1, z1, Color.red);
                if (err_1 > 0) {
                    y1 += y_inc;
                    err_1 -= dz2;
                }
                if (err_2 > 0) {
                    x1 += x_inc;
                    err_2 -= dz2;
                }
                err_1 += dy2;
                err_2 += dx2;
                z1 += z_inc;
            }
        }
        this.SetPixel(x1, y1, z1, Color.red);
    }
    
    /**
     * Gets a linearly interpolated coordinates for line clipping
     * @param a
     * @param b
     * @return 
     */
    public double[] GetIntersection(Vector3 a, Vector3 b){
        double t0 = 0;double t1 = 1;
        if((a.x() >= 0 && a.x() <= this.dimentions.width && a.y() >=0 && a.y() <= this.dimentions.height)&&
           (b.x() >= 0 && b.x() <= this.dimentions.width && b.y() >=0 && b.y() <= this.dimentions.height)){
            //A and B are inside the view, this is fine
        }
        else{
            double x0 = a.x(); double y0 = a.y();
            double x1 = b.x(); double y1 = b.y();
            
            double xMin = 0; double yMin = 0;
            double xMax = this.dimentions.width; double yMax = this.dimentions.height;
            
            double dx = x1 - x0, dy = y1 - y0;
            double p[] = {-dx, dx, -dy, dy};
            double q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};
            
            for (int i = 0; i < 4; i++) {
                if (p[i] == 0) {
                    if (q[i] < 0) {
                        return null;
                    }
                } else {
                    double u = (double) q[i] / p[i];
                    if (p[i] < 0) {
                        t0 = Math.max(u, t0);
                    } else {
                        t1 = Math.min(u, t1);
                    }
                }
            }
            
            if (t0 > t1) {
                return null;
            }
        }
        return new double[]{t0,t1};
    }
    
    /**
     * Projects a triangle from localspace to screenspace, will return null if not within view frustrum
     * @param transform
     * @param a
     * @param b
     * @param c 
     */
    public Vector3[] ProjectTriangle(Matrix transform, Vector3 a, Vector3 b ,Vector3 c){
        Matrix res;
        Vector3[] points = new Vector3[]{a,b,c};
        Vector3[] projnorm = new Vector3[3];
        Vector3[] proj = new Vector3[3];
        boolean allHidden = true;
        
        //TODO make affine matrix inverse which is faster than cofactor expansion (my current method)
        //http://stackoverflow.com/questions/2624422/efficient-4x4-matrix-inverse-affine-transform
        //http://math.stackexchange.com/questions/21533/shortcut-for-finding-a-inverse-of-matrix/21537
        
        switch(mode){
            case Orthographic: //this.GetMatrix().Invert -> Affine inverse
                res = this.ortho.mul(this.AffineInverse()).mul(transform);
                for(int i = 0; i < points.length; i++){
                    Matrix sPosM = res.mul(points[i].GetMatrix());
                    double w = sPosM.Get(3, 3);
                    Vector3 sPos = new Vector3(
                            sPosM.Get(0, 3),
                            sPosM.Get(1, 3),
                            -sPosM.Get(2, 3) 
                    );
                    Vector3 tPos = new Vector3(
                            (sPosM.Get(0, 3) + this.GetWidth()/2) ,
                            (sPosM.Get(1, 3) + this.GetHeight()/2) ,
                            -sPosM.Get(2, 3) 
                    );
                    //Is within frustrum and clip planes?
                    boolean isNotNull = (sPos.x() <= w && sPos.x() >= -w && sPos.y() <= w && sPos.y() >= -w && sPos.z() >=n && sPos.z() <= f);
                    allHidden &= !isNotNull;
                    projnorm[i] = new Vector3(sPos.x(),sPos.y());
                    proj[i] = tPos;
                }
                break;
            case Perspective:
                res = this.persp.mul(this.AffineInverse()).mul(transform);
                for(int i = 0; i < points.length; i++){
                    Matrix sPosM = res.mul(points[i].GetMatrix());
                    double w = sPosM.Get(3, 3);
                    if(w == 0)
                        w = 1;
                    Vector3 sPos = new Vector3(
                            (sPosM.Get(0, 3)),
                            (sPosM.Get(1, 3)),
                            -sPosM.Get(2, 3) 
                    );
                    Vector3 tPos = new Vector3(
                            ((sPosM.Get(0, 3)/w)*this.GetWidth()/2 + this.GetWidth()/2) ,
                            ((sPosM.Get(1, 3)/w)*this.GetWidth()/2 + this.GetHeight()/2) ,
                            -sPosM.Get(2, 3) 
                    );
                    //Is within frustrum and clip planes?
                    boolean isNotNull = (sPos.x() <= w && sPos.x() >= -w && sPos.y() <= w && sPos.y() >= -w && sPos.z() >=n && sPos.z() <= f);
                    allHidden &= !isNotNull;
                    projnorm[i] = sPos;
                    proj[i] = tPos;
                }
                break;
        }
       
        if(!allHidden){
            return proj;
        }
        else{
            //Top left
            //boolean test1 = Util.InTriangle(proj[0], proj[1], proj[2], Vector.zero);
            //boolean test2 = Util.InTriangle(proj[0], proj[1], proj[2], new Vector(0,this.GetWidth()));
            //boolean test3 = Util.InTriangle(proj[0], proj[1], proj[2], new Vector(this.GetHeight(),this.GetWidth()));
            //boolean test4 = Util.InTriangle(proj[0], proj[1], proj[2], new Vector(this.GetHeight(),0));
            //boolean test5 = Util.LineRectangleCollision(proj[0], proj[1], 0, this.GetWidth(), 0, this.GetHeight());
            //boolean test6 = Util.LineRectangleCollision(proj[0], proj[2], 0, this.GetWidth(), 0, this.GetHeight());
            //boolean test7 = Util.LineRectangleCollision(proj[1], proj[2], 0, this.GetWidth(), 0, this.GetHeight());
            
            //if(test1 || test2 || test3 || test4 || test5 || test6 || test7){
               //return proj; 
            //}
            //else{
                return null;  
            //}
        }
    }
    
    /**
     * Project a series of local space coordinates to screen space.
     * @param transform
     * @param points
     * @return 
     */
    public Vector3[] Project(Matrix transform, Vector3 ... points){
        Matrix res;
        Vector3[] proj = new Vector3[points.length]; //maybe not a vector since i need to keep the W value for now
        
        //WIP Things within -w -> w are within view frustrum, divide these by w and continue
        //Will speed things up tremendously when complete
        
        switch(mode){
            case Orthographic:
                res = this.ortho.mul(this.AffineInverse()).mul(transform);
                for(int i = 0; i < points.length; i++){
                    Matrix sPosM = res.mul(points[i].GetMatrix());
                    double w = sPosM.Get(3, 3);
                    Vector3 sPos = new Vector3(
                            (sPosM.Get(0, 3) + this.GetWidth()/2) ,
                            (sPosM.Get(1, 3) + this.GetHeight()/2) ,
                            -sPosM.Get(2, 3) 
                    );
                    proj[i] = sPos;
                }
                break;
            case Perspective:
                res = this.persp.mul(this.AffineInverse()).mul(transform);
                for(int i = 0; i < points.length; i++){
                    Matrix sPosM = res.mul(points[i].GetMatrix());
                    double w = sPosM.Get(3, 3);
                    if(w == 0)
                        w = 1;
                    Vector3 sPos = new Vector3(
                            ((sPosM.Get(0, 3)/w)*this.GetWidth()/2 + this.GetWidth()/2) ,
                            ((sPosM.Get(1, 3)/w)*this.GetWidth()/2 + this.GetHeight()/2) ,
                            -sPosM.Get(2, 3) 
                    );
                    proj[i] = sPos;
                }
                break;
        }
        return proj;
    }
    
}
