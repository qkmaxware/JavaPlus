/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.JSON;

/**
 *
 * @author Colin Halseth
 */
public class JSONformatter {
    public static String Format(String json){
        json = json.replaceAll("\\{", "\\{\n");
        json = json.replaceAll("\\,", "\\,\n");
        
        return json;
    }
}
