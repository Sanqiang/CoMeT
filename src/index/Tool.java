/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class Tool {
    public static String getlocalText(String path) throws FileNotFoundException, IOException
    {
        File f=new File(path);
        BufferedReader br=new BufferedReader(new FileReader(f));
        StringBuilder sb=new StringBuilder();
        String line;
        while ((line=br.readLine())!=null) {
            sb.append(line).append(" ");
        }
        br.close();
        return sb.toString();
    }
    public static String getUrlText(String path, boolean Html, String encode) throws FileNotFoundException, IOException
    {
        URL u=new URL(path);
        InputStreamReader isr=new InputStreamReader(u.openStream(),encode);
        BufferedReader br=new BufferedReader(isr);
        StringBuilder sb=new StringBuilder();
        String line;
        while ((line=br.readLine())!=null) {
            sb.append(line).append(" ");
        }
        br.close();isr.close();
        if (Html) {
            return sb.toString();
        }else
        {
            return sb.toString().replaceAll("</?[^>]+>"," ").replace("&nbsp;","");
        }
    }
}
