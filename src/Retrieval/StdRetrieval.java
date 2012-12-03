package Retrieval;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StdRetrieval {

    String URL = "";

    public StdRetrieval(String url) {
        this.URL = url;
    }

    public String getContent() {
        try {
            StringBuilder sb = new StringBuilder();
            URL u = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(600);
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            //BufferedReader br = new BufferedReader(new strea)
            return sb.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public TreeMap<String, String> getValideContent() {
        TreeMap<String, String> output = new TreeMap<String, String>();
        Pattern p = Pattern.compile("<title>(.*)</title>.*<body[^>]*>(.*)");
        Matcher m = p.matcher(getContent());
        while (m.find()) {
            output.put("title", m.group(1));
            output.put("body", getCleanData(m.group(2)));
        }

        return output;
    }

    public static String getCleanData(String data) {
        return data.replaceAll("<script[^>]*?>.*?</script>", " ").replaceAll("<[/!]*?[^<>]*?>", " ").replaceAll("&(quot|#34);", "\"").replaceAll("&(amp|#38);", "&").replaceAll("&(lt|#60);", "<").replaceAll("&(gt|#62);", ">").replaceAll("&(nbsp|#160);", " ");
    }
}
