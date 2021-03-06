/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.system;

import plus.parse.model.ObjParser;
import plus.graphics.Geometry;
import plus.math.Vector3;
import plus.graphics.Bitmap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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
     * @param file 
     */
    public static void LoadImage(String file){
        LoadImage(file.replaceAll(".*\\/|\\..*$","").trim(), file);
    }
    
    /**
     * Load an image from file saved with a specific name
     * @param name
     * @param file 
     */
    public static void LoadImage(String name, String file){
        try {
            File f = new File(file);
            BufferedImage img = ImageIO.read(f);
            String filename = name;
            bitmaps.put(filename, new Bitmap(img)); //Bitmaps will replace buffered images for speed purposes
        } catch (Exception e) {
            System.out.println("Resource: [\"" + file + "\"] doesn't exist");
        }
    }
    
    /**
     * Load an image from url
     * @param url 
     */
    public static void LoadImage(URL url){
        LoadImage(url.getPath().replaceAll(".*\\/|\\..*$","").trim(), url);
    }
    
    /**
     * Load an image from a url saved a specific name
     * @param url 
     * @param name
     */
    public static void LoadImage(String name, URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");
            
            connection.connect();
            
            InputStream is = connection.getInputStream();

            BufferedImage img = ImageIO.read(is);
            String filename = name;
            bitmaps.put(filename, new Bitmap(img)); //Bitmaps will replace buffered images for speed purposes
        } catch (Exception e) {
            System.out.println("Resource: [\"" + url.toString() + "\"] can not be accessed");
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
            List<String> lines = Files.readAllLines(Paths.get(fname));
            String[] lines_array = new String[lines.size()];
            lines.toArray(lines_array);
            for(Pair<String, Geometry> obj : ObjParser.Parse(lines_array)){
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
