/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package o;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author lily
 */
public class EasyCUtilities{
    
    /**
     * @param args the command line arguments
     */
    public static String compile() throws Exception {
        String result = "0";
        try {
            final ExecuteCmd ec = new ExecuteCmd();
            result = ec.exec("gcc.exe EasyC.c -o EasyC.exe", "")[1];
        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(null, "编译错误！" + result);
            return "编译错误!";
        }
        return "编译成功。\n" + result;
    }

    static void saveFile(final String path, final String code)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"))) {
            writer.write(code);
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null, "保存失败！");
        }
    }

    static String run(final String parameter) throws InterruptedException {
        String result = "";
        try {
            final ExecuteCmd ec = new ExecuteCmd();
            result = ec.exec("EasyC.exe", parameter)[0];
        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(null, "运行错误！");
        }
        return result;
    }

    static String open(final String path) throws FileNotFoundException, IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String result = "";
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (final Exception e) {
            // do nothing...
        }
        br.close();
        return result;
    }

    static void deleteExe() throws IOException, InterruptedException {
        final File file = new File("EasyC.exe");
        file.delete();
    }

    public static void setClipboardText(final String str) {
        // 获取系统剪贴板
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        final Transferable t = new StringSelection(str);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(t, null);
    }

    public static String getClipboardText() throws ClassNotFoundException, UnsupportedFlavorException, IOException {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable trans = new StringSelection(null);
        clipboard.getContents(trans);
        System.out.println((String) trans.getTransferData(new DataFlavor("PLAIN_TEXT")));
        return (String) trans.getTransferData(new DataFlavor("PLAIN_TEXT"));
    }

    public static String doGet(final String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            final URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 Safari/536.11");
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                final StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    static void downloadRule(final String addr) throws IOException {
        saveFile("r.lily", doGet(addr));
    }

    static String getContent(final String host, final String left, final String right) {
        final String str = doGet(host);
        return str.substring(str.indexOf("[lilyRule]")+"[lilyRule]".length(),str.indexOf("[/lilyRule]"));
    }

    
}
