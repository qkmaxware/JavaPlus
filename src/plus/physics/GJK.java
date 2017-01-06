/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.physics;

import plus.math.Pair;
import plus.math.Triple;
import plus.math.Vector3;
import java.util.ArrayList;

/**
 * Based upon the implementation at http://programyourfaceoff.blogspot.ca/2012/01/gjk-algorithm.html
 * @author Colin Halseth
 */
public class GJK {
    
    private static int MaxIterations = 25;
/*
    public static Triple<Boolean, Vector, Float> Intersect(Collider c, Collider g){
        //Inital direction, some are more optimal.
        Vector initialAxis = Vector.one;
        
        Vector A = c.Support(initialAxis).sub(g.Support(initialAxis.scale(-1)));
        ArrayList<Vector> simplex = new ArrayList<Vector>();
        simplex.add(A);
        Vector D = A.scale(-1);
        
        for(int i = 0; i < MaxIterations; i++){
            A = c.Support(D).sub(g.Support(D.scale(-1)));
            
            if(A.Dot(D) < 0)
                return new Triple<Boolean, Vector, Float>(false, Vector.zero, 0.0f);
                
            
            simplex.add(A);
            
            Pair<Vector, Boolean> eval = Evaluate(simplex,D);
            
            if(eval.GetRight()){
                return new Triple<Boolean, Vector, Float>(true, eval.GetLeft().Normalize(), eval.GetLeft().Magnitude());
            }
        }
        
        return new Triple<Boolean, Vector, Float>(false, Vector.zero, 0.0f);
    }
    
    public static void SetMaxIterations(int i){
        if(i > 0)
            MaxIterations = i;
    }
    
    private static boolean SameDirection(Vector a, Vector b){
        return a.Dot(b) > 0;
    } 
    
    private static Vector CreateVector(Vector a, Vector b){
        return new Vector(b.x() - a.x(), b.y() - a.y(), b.z() - a.z());
    }
    
    private static  Pair<Vector, Boolean> Evaluate(ArrayList<Vector> simplex, Vector dir){
        switch(simplex.size()){
            case 2:{    //Line
                return ProcessLine(simplex, dir);
            }
            case 3:{    //Triangle
                return ProcessTriangle(simplex,dir);
            }
            case 4:{    //Tetrahedron
                return ProcessTetrehedron(simplex,dir);
            }
        }
        
        //Default case should never be hit
        return new Pair<Vector,Boolean>(Vector.one, false);
    }
    
    private static Pair<Vector, Boolean> ProcessLine(ArrayList<Vector> simplex, Vector dir){
        Vector a = simplex.get(1);
        Vector b = simplex.get(0);
        
        Vector ab = b.sub(a);
        Vector a0 = a.scale(-1);
        
        if(SameDirection(ab,a0)){
            float dot = Vector.Dot(ab, a0);
            float angle = (float)Math.acos(dot / (ab.Magnitude() * a0.Magnitude()));
            dir = Vector.Cross(Vector.Cross(ab, a0), ab);
        }
        else{
            simplex.remove(b);
            dir = a0;
        }
        
        return new Pair<Vector, Boolean>(dir,false);
    }
    
    private static Pair<Vector, Boolean> ProcessTriangle(ArrayList<Vector> simplex, Vector dir){
        Vector a = simplex.get(2);
        Vector b = simplex.get(1);
        Vector c = simplex.get(0);
        
        Vector ab = b.sub(a);
        Vector ac = c.sub(a);
        Vector abc = Vector.Cross(ab, ac);
        Vector a0 = a.scale(-1);
        Vector acNormal = Vector.Cross(abc, ac);
        Vector abNormal = Vector.Cross(a, abc);
        
        if(SameDirection(acNormal,a0)){
            if(SameDirection(ac,a0)){
                simplex.remove(b);
                dir = Vector.Cross(Vector.Cross(ac, a0), ac);
            }else{
                if(SameDirection(ab,a0)){
                    simplex.remove(c);
                    dir = Vector.Cross(Vector.Cross(ab,a0), ab);
                }else{
                    simplex.remove(b);
                    simplex.remove(c);
                    dir = a0;
                }
            }
        }
        else{
            if(SameDirection(abNormal,a0)){
                if(SameDirection(ab,a0)){
                    simplex.remove(c);
                    dir = Vector.Cross(Vector.Cross(ab, a0), ab);
                }
                else{
                    simplex.remove(b);
                    simplex.remove(c);
                    dir = a0;
                }
            }else{
                if(SameDirection(abc,a0)){
                    dir = Vector.Cross(Vector.Cross(abc, a0), abc);
                }
                else{
                    Vector n_abc = abc.scale(-1);
                    dir = Vector.Cross(Vector.Cross(n_abc, a0), n_abc);
                }
            }
        }
        
        return new Pair<Vector,Boolean>(dir,false);
    }
    
    private static Pair<Vector, Boolean> ProcessTetrehedron(ArrayList<Vector> simplex, Vector dir){
        Vector a = simplex.get(3);
        Vector b = simplex.get(2);
        Vector c = simplex.get(1);
        Vector d = simplex.get(0);
        
        Vector ac = c.sub(a);
        Vector ad = d.sub(a);
        Vector ab = b.sub(a);
        Vector bc = c.sub(b);
        Vector bd = d.sub(b);
        
        Vector acd = Vector.Cross(ad, ac);
        Vector abd = Vector.Cross(ab, ad);
        Vector abc = Vector.Cross(ac, ab);
        
        Vector a0 = a.scale(-1);
        
        if(SameDirection(abc,a0)){
            if(SameDirection(Vector.Cross(abc, ac),a0)){
                simplex.remove(b);
                simplex.remove(d);
                dir = Vector.Cross(Vector.Cross(ac, a0), ac);
            }
            else if(SameDirection(Vector.Cross(ab, abc), a0)){
                simplex.remove(c);
                simplex.remove(d);
                dir = Vector.Cross(Vector.Cross(ab, a0), ab);
            }else{
                simplex.remove(d);
                dir = abc;
            }
        }
        else if(SameDirection(acd, a0)){
            if(SameDirection(Vector.Cross(acd, ad), a0)){
                simplex.remove(b);
                simplex.remove(c);
                dir = Vector.Cross(Vector.Cross(ad, a0), ad);
            }
            else if(SameDirection(Vector.Cross(ac, acd), a0)){
                simplex.remove(b);
                simplex.remove(d);
                dir = Vector.Cross(Vector.Cross(ac, a0), ac);
            }
            else{
                simplex.remove(b);
                dir = acd;
            }
        }
        else if(SameDirection(abd, a0)){
            if(SameDirection(Vector.Cross(abd, ab),a0)){
                simplex.remove(c);
                simplex.remove(d);
                dir = Vector.Cross(Vector.Cross(ab, a0), ab);
            }
            else if(SameDirection(Vector.Cross(ad, abd), a0)){
                simplex.remove(b);
                simplex.remove(c);
            }
            else{
                simplex.remove(c);
                dir = abd;
            }
        }
        else{
            return new Pair<Vector,Boolean>(Vector.one, true);
        }
        
        return new Pair<Vector,Boolean>(dir, false);
    }
    */
}
