/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import plus.graphics.Geometry;
import plus.math.Vector3;
import plus.graphics.Bitmap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import plus.math.Pair;

/**
 *
 * @author Colin Halseth
 */
public class Resources {
 
    protected static HashMap<String,Bitmap> bitmaps = new HashMap<String,Bitmap>();
    protected static HashMap<String,Geometry> geoms = new HashMap<String,Geometry>();
    
    /**
     * Load an image from file 
     * @param fname 
     */
    public static void LoadImage(String fname){
        try {
            File f = new File(fname);
            BufferedImage img = ImageIO.read(f);
            String filename = fname.replaceAll(".*\\/|\\..*$","").trim();
            bitmaps.put(filename, new Bitmap(img)); //Bitmaps will replace buffered images for speed purposes
        } catch (Exception e) {
            System.out.println("Resource: [\"" + fname + "\"] doesn't exist");
        }
    }
    
    /**
     * Get a loaded image by name
     * @param name
     * @return 
     */
    public static Bitmap GetImage(String name){
        return bitmaps.get(name);
    }
    
    /**
     * Get all loaded images.
     * @return 
     */
    public static Bitmap[] GetAllImages(){
        Bitmap[] img = new Bitmap[bitmaps.keySet().size()];
        int i = 0;
        for(String key : bitmaps.keySet()){
            img[i++] = bitmaps.get(key);
        }
        return img;
    }
    
    /**
     * Load a 3d .obj file. This is very finicky
     * Note uv loading still needs some work, seems to miss 1/2 of the coodinates
     * @param fname 
     */
    public static void LoadObject(String fname){ 
       try {
            for(Pair<String, Geometry> obj : ObjParser.Parse((String[])Files.readAllLines(Paths.get(fname)).toArray())){
                geoms.put(obj.GetLeft(), obj.GetRight());
            } 
        } catch (Exception e) {
            System.out.println("Resource: [\"" + fname + "\"] failed to load because");
            e.printStackTrace();
        } 
    }
    
    /**
     * Get a loaded 3d .obj file by name
     * @param name
     * @return 
     */
    public static Geometry GetObject(String name){
        return geoms.get(name);
    }
    
    /**
     * Get all loaded .obj files
     * @return 
     */
    public static Geometry[] GetAllObjects(){
        Geometry[] img = new Geometry[geoms.keySet().size()];
        int i = 0;
        for(String key : geoms.keySet()){
            img[i++] = geoms.get(key);
        }
        return img;
    }
    
}
