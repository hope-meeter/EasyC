/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package o;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author lily
 */
public class Translator {
    private ArrayList<String> rules = new ArrayList<String>();
    public String version;
    public int updateStatus = -9;

    public Translator() {

        try {
            xmport();
            updateStatus = download();
        } catch (FileNotFoundException ex) {
            System.out.println("文件找不到。");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void xmport() throws UnsupportedEncodingException, FileNotFoundException, IOException {// 导入规则
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("r.lily"), "utf-8"));
            this.version = br.readLine().substring(5, 17);

            String line = "";
            while ((line = br.readLine()) != null) {
                rules.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("finish.");
    }

    private int download() throws IOException {
        if (updateStatus != -9) {
            return 0;
        }
        // 0表示无需更新；-1表示获取失败；1表示需要更新
        String[] hosts = {};

        for (String host : hosts) {
            String str = "";
            if ((str = EasyCUtilities.getContent(host, "[lilyRule]", "[/lilyRule]")) != null) {
                try {
                    String m = str.substring(str.indexOf("[lilyM]") + "[lilyM]".length(), str.indexOf("[/lilyM]"));
                    if (!m.equals("")) {
                        JOptionPane.showMessageDialog(null, version);
                    }
                } catch (Exception e) {

                }

                try {
                    String ver = str.substring(str.indexOf("[ver]") + "[ver]".length(), str.indexOf("[/ver]"));
                    String addr = str.substring(str.indexOf("[addr]") + "[addr]".length(), str.indexOf("[/addr]"));
                    if (Long.valueOf(ver) > Long.valueOf(this.version)) {
                        EasyCUtilities.downloadRule(addr);
                        System.out.println("status1");
                        return 1;
                    } else {
                        System.out.println("status0");
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        System.out.println("status-1");
        return -1;
    }

    public String translate(String origin) throws FileNotFoundException, IOException {
        String result = "可能出现的问题：";
        String[] messages = origin.split("\n");

        for (String message : messages) {
            boolean r = false;

            // System.out.println("------length:"+message.length());

            if (message.contains("EasyC.c:") && message.contains(": ")) {

                // System.out.println("------"+message.indexOf("EasyC.c:")+"**"+message.indexOf(":
                // "));
                if (message.indexOf("EasyC.c:") + "EasyC.c:".length() < message.indexOf(": ")) {
                    String row = message
                            .substring(message.indexOf("EasyC.c:") + "EasyC.c:".length(), message.indexOf(": "))
                            .split("[:]")[0];
                    if (row.length() < 5) {
                        result += "\n问题可能出现在第" + row + "行：";
                    }
                }
                r = true;
            }
            for (String rule : rules) {

                // System.out.println("***"+message+"***"+rule);
                String[] str = rule.split("<lilyBr>");
                // System.out.println("***"+message.replaceAll(str[0], ""));

                if (!message.replaceAll(str[0], "").equals(message)) {
                    // System.out.println(rule + "------hit");
                    if (r == true) {
                        result += "\n";
                    }
                    result += str[1];
                }
                r = false;
            }
        }
        return result;
    }
}
