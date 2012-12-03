/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.sis.ImageEx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageRank {

    public String UploadPicUrl;
    public String PicURL;
    InputStream UploadPicIS;
    public ArrayList<String> PicURLs = new ArrayList<String>();
    Binarization b = new Binarization();
    Distribution d = new Distribution();
    Compare c = new Compare();

    public ImageRank(String uploadPicUrl, String picURL) throws Exception {
        UploadPicUrl = uploadPicUrl;
        PicURL = picURL;
        getPicURLs();
    }

    public ImageRank(String uploadPicUrl, ArrayList<String> picURLs) throws Exception {
        UploadPicUrl = uploadPicUrl;
        PicURLs = picURLs;
    }

    public ImageRank(InputStream uploadPicS, ArrayList<String> picURLs) throws Exception {
        UploadPicIS = uploadPicS;
        PicURLs = picURLs;
    }

    public ArrayList<ImageItem> getRankedPicUrl() throws Exception {
        ArrayList<ImageItem> list = new ArrayList<ImageItem>();
        if (UploadPicIS == null) {
            UploadPicIS = getInputStream(UploadPicUrl);
        }
        int[][] std = b.toBinarization(UploadPicIS);
        ArrayList<Square> stdl = d.toDistribution(std);

        for (String PicUrl : PicURLs) {
            try {
                int[][] PicMatrix = b.toBinarization(getInputStream(PicUrl));
                ArrayList<Square> comparel = d.toDistribution(PicMatrix);
                double m1 = c.toCompare(stdl, comparel, b.getArea());
                double m2 = c.toCompare(comparel, stdl, b.getArea());
                list.add(new ImageItem(PicUrl, Math.max(m1, m2)));
            } catch (Exception e) {
                //System.out.println(PicUrl);
                //throw e;
            }
        }
        Collections.sort(list, new Comparator<ImageItem>() {

            @Override
            public int compare(ImageItem o1, ImageItem o2) {
                return Double.compare(o1.score, o2.score) * (-1);
            }
        });
        return list;
    }

    private void getPicURLs() throws Exception {
        URL u = new URL(PicURL);
        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        Pattern p = Pattern.compile("<img\\s+[^>]*\\s*src\\s*=\\s*([']?)(\\S+)'?[^>]*>");
        Matcher m = p.matcher(sb.toString());
        while (m.find()) {
            String url = m.group(2);
            if (url.length() > 1) {
                if (url.charAt(0) == '\"') {
                    url = url.substring(1);
                }
                if (url.charAt(url.length() - 1) == '\"') {
                    url = url.substring(0, url.length() - 1);
                }
                if (url.charAt(0) == '/') {
                    if (url.charAt(1) == '/') {
                        String suffix = PicURL.substring(0, PicURL.indexOf("/"));
                        url = suffix + url;
                    } else {
                        //http:// there are 7 chars and so start at 8th char
                        String suffix = PicURL.substring(0, PicURL.substring(8).indexOf("/") + 8);
                        url = suffix + url;
                    }

                }
                PicURLs.add(url);
            }
        }
    }

    public InputStream getInputStream(String path) throws Exception {
        URL url = new URL(path);
        return url.openStream();
    }

    public static void main(String[] args) throws Exception {
        ImageRank ir = new ImageRank("http://www.igo.cn/2010/images/userimages/2012/03/20120329000005.jpg", "http://renren.com/");
        for (ImageItem it : ir.getRankedPicUrl()) {
            System.out.println("<img src='" + it.URL + "' />" + it.score + "<br />");
        }

    }
}
