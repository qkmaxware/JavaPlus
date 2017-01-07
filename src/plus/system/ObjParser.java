/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import java.util.LinkedList;
import plus.graphics.Geometry;
import plus.math.Pair;
import plus.math.Vector3;

/**
 *
 * @author Colin Halseth
 */
public class ObjParser {

    public static LinkedList<Pair<String, Geometry>> Parse(String[] lines) {

        String geom = null;
        LinkedList<Vector3> vertices = new LinkedList<Vector3>();
        LinkedList<Integer> faces = new LinkedList<Integer>();
        LinkedList<Vector3> uvs = new LinkedList<Vector3>();
        LinkedList<Vector3> loadeduvs = new LinkedList<Vector3>();

        LinkedList<Pair<String, Geometry>> geoms = new LinkedList<Pair<String, Geometry>>();

        for (String line : lines) {
            if (line.startsWith("o ")) {
                String[] values = line.split(" ");
                if (geom != null) {
                    //Push and reset lists;
                    Vector3[] verts = new Vector3[vertices.size()];
                    Integer[] tris = new Integer[faces.size()];
                    int[] trisI = new int[faces.size()];
                    Vector3[] uv = new Vector3[uvs.size()];
                    vertices.toArray(verts);
                    faces.toArray(tris);
                    uvs.toArray(uv);

                    vertices = new LinkedList<Vector3>();
                    faces = new LinkedList<Integer>();
                    uvs = new LinkedList<Vector3>();
                    loadeduvs = new LinkedList<Vector3>();

                    for (int i = 0; i < trisI.length; i++) {
                        trisI[i] = (int) tris[i];
                    }
                    geoms.add(
                            new Pair<String, Geometry>(
                                    geom,
                                    new Geometry(
                                            verts,
                                            trisI,
                                            uv
                                    )
                            )
                    );
                    System.out.println(geom);
                    geom = null;
                }
                geom = line.substring(2).trim();
            } else if (line.startsWith("v ")) {
                String[] values = line.split(" ");
                vertices.add(new Vector3(
                        Float.parseFloat(values[1]),
                        Float.parseFloat(values[2]),
                        Float.parseFloat(values[3])
                ));
            } else if (line.startsWith("f ")) {
                String[] values = line.split(" ");

                String[] first = values[1].split("/");
                String[] sec = values[2].split("/");
                String[] third = values[3].split("/");

                    //Blender exports .obj as 1 indexed, convert to 0 indexed
                // [vertex coordinate, texture coordinate index, vertex normal index]
                faces.add(Integer.parseInt(first[0]) - 1);
                faces.add(Integer.parseInt(sec[0]) - 1);
                faces.add(Integer.parseInt(third[0]) - 1);
                try { //Try uvs...may not have any
                    uvs.add(loadeduvs.get(Integer.parseInt(first[1]) - 1));
                    uvs.add(loadeduvs.get(Integer.parseInt(sec[1]) - 1));
                    uvs.add(loadeduvs.get(Integer.parseInt(third[1]) - 1));
                } catch (Exception e) {
                }
            } else if (line.startsWith("vt ")) {
                String[] values = line.split(" ");
                loadeduvs.add(new Vector3(
                        Float.parseFloat(values[1]),
                        Float.parseFloat(values[2])
                ));
            }
        }

        if (geom != null) {
            //Push and reset lists;
            Vector3[] verts = new Vector3[vertices.size()];
            Integer[] tris = new Integer[faces.size()];
            int[] trisI = new int[faces.size()];
            Vector3[] uv = new Vector3[uvs.size()];
            vertices.toArray(verts);
            faces.toArray(tris);
            uvs.toArray(uv);

            vertices = new LinkedList<Vector3>();
            faces = new LinkedList<Integer>();
            uvs = new LinkedList<Vector3>();
            loadeduvs = new LinkedList<Vector3>();

            for (int i = 0; i < trisI.length; i++) {
                trisI[i] = (int) tris[i];
            }
            geoms.add(
                    new Pair<String, Geometry>(
                            geom,
                            new Geometry(
                                    verts,
                                    trisI,
                                    uv
                            )
                    )
            );
        }
        
        return geoms;
    }

}
