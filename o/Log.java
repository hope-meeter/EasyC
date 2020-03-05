/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package o;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author lily
 */
public class Log {
     
    public static void log(String str) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("l.lily"), "utf-8"));
            bw.newLine();
            bw.write(str);
            bw.close();
        }catch(Exception e){
            //do nothing..
        }
        
    }
    
}
