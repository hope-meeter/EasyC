/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package o;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class ExecuteCmd {
    
    public String[] exec(String command, String parameter) throws IOException, InterruptedException {
//        String cmd = "C:\\Users\\lily\\Desktop\\EasyC\\tools\\MinGW\\bin\\gcc.exe";
        String cmd = command;
        final Process process = Runtime.getRuntime().exec(cmd);
        if(!parameter.equals("")){
            enterMessage(process.getOutputStream(), parameter);
        }
        
        String gis = printMessage(process.getInputStream());
        String ges = printMessage(process.getErrorStream());
        
        int value = process.waitFor();
//        System.out.println(value);
//        System.out.println("gis:"+gis+"\n"+"ges:"+ges);
        String[] result = {gis,ges};
        return result;
    }
    //防止输出流缓存不够导致堵塞，所以开启新的线程读取输出流。
    private String printMessage(final InputStream input) throws IOException {
        final StringBuffer result = new StringBuffer();
        Reader reader = new InputStreamReader(input, "utf-8");
        BufferedReader bf = new BufferedReader(reader);
        String line = null;
        while((line=bf.readLine())!=null) {
                result.append(line+"\n");
        }
        bf.close();
        
        return result.toString();
    }

    private void enterMessage(OutputStream output , String parameter) throws IOException {
        Writer writer = new OutputStreamWriter(output, "utf-8");
        String[] paras = parameter.split("<enter>");
        for(String para : paras){
            writer.write((para + "\n"));
            writer.flush();
        }
        
    }
}