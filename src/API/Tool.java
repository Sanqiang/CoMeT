package API;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {

    public static String readFile(String Path) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(new File(Path)));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }

    public static TreeMap<String, String> getMapForDocument(String Path) {
        TreeMap<String, String> output = new TreeMap<String, String>();
        try {
            StringBuilder SbText = new StringBuilder();
            String DOCNO = "";
            BufferedReader br = new BufferedReader(new FileReader(new File(Path)));
            String line = "";
            boolean readTEXT = false, readDOCNO = false;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("<DOCNO>") && line.endsWith("</DOCNO>")) {
                    DOCNO = line.replaceAll("<[/]?DOCNO>", "").trim();
                }
                if (line.equals("</TEXT>")) {
                    readTEXT = false;
                    output.put(DOCNO, SbText.toString());
                }

                if (readTEXT) {
                    SbText.append(line);
                }

                if (line.equals("<TEXT>")) {
                    readTEXT = true;
                    SbText = new StringBuilder();
                }
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return output;
    }

    public static TreeMap<String, String> getMapForTopicStatement(String Path) {
        TreeMap<String, String> output = new TreeMap<String, String>();
        try {
            StringBuilder SbText = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(new File(Path)));
            String line = "";
            while ((line = br.readLine()) != null) {
                SbText.append(line);
            }
            br.close();

            Pattern p = Pattern.compile("<topicid>((\\S| )*)</topicid>\\s*<title>((\\S| )+)</title>\\s*<description>((\\S| |\n|\r)*)</description>");
            Matcher m = p.matcher(SbText.toString());
            while (m.find()) {
                //System.out.println(m.group(1));
                //System.out.println(m.group(3));
                //System.out.println(m.group(5));
                output.put(m.group(1), m.group(3) + m.group(5));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return output;
    }
}
