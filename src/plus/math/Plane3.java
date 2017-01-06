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
public class Plane3 {
        private Vector3 point;
        private Vector3 normal;
       
        /**
         * Create a plane that intersects a point, and has a desired normal direction
         * @param point_in_plane
         * @param normal
         */
        public Plane3(Vector3 point_in_plane, Vector3 normal){
            this.point = point_in_plane;
            this.normal = normal.Normalize();
        }
       
        /**
         * Create a plane who's normal can be defined by the cross product of AB and BC and intersects one of the points
         * @param a
         * @param b
         * @param c
         */
        public Plane3(Vector3 a, Vector3 b, Vector3 c){
            this.point = a;
            this.normal = Vector3.Cross(a.sub(b), b.sub(c)).Normalize();
        }
       
        /**
         * Tests if a point is in front of the plane
         * @param position
         * @return true if position is in front of the plane as defined by the plane's normal
         */
        public boolean IsFront(Vector3 position){
            double dot = Distance(position);
            return dot > 0;
        }
       
        /**
         * Tests if two points are on the same side of the plane
         * @param a
         * @param b
         * @return true if a and b are on the same side of the plane
         */
        public boolean SameSide(Vector3 a, Vector3 b){
            return (IsFront(a) == IsFront(b));
        }
       
        /**
         * Calculates the signed distance from a point to the plane
         * @param a
         * @return distance
         */
        public double Distance(Vector3 a){
            Vector3 AB = a.sub(point);
            return Vector3.Dot(AB, this.normal); 
        }
}
