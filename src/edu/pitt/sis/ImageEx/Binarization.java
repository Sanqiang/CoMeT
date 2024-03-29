package edu.pitt.sis.ImageEx;  
  
import java.awt.image.BufferedImage;  
import java.io.IOException;  
import java.io.InputStream;  
  
import javax.imageio.ImageIO;  
  
public class Binarization {  
    private static float RP = 0.333f;  
    private static float GP = 0.334f;  
    private static float BP = 0.333f;  
    private static int Threshold = 0;  
    private int h;  
    private int w;  
    public Binarization(){  
          
    }  
    /** 
     * set your threshold such as 160  �����ֹ����жϺڰ׵ı�׼��Ĭ����ͼ��ȫ�ֵ�ƽ��ֵ�� 
     * @param threshold 
     */  
    public Binarization(int threshold){  
        Threshold = threshold;  
    }  
    /** 
     * init with r,g,b Proportion  ����R G B��Ȩ�رȣ�Ĭ��ƽ�� 
     * @param r 
     * @param g 
     * @param b 
     */  
    public Binarization(float r,float g,float b,int threshold){  
        RP = r;  
        GP = g;  
        BP = b;  
        Threshold = threshold;  
    }  
    public int getH(){  
        return h;  
    }  
    public int getW() {  
        return w;  
    }  
    public int getArea(){  
        return w*h;  
    }  
      
    public int[][] toBinarization(InputStream stream) throws IOException {  
        BufferedImage bi=ImageIO.read(stream);//input image  
        h=bi.getHeight();//get height  
        w=bi.getWidth();//get width  
        int sumRGB = 0;  
        int[][] gray=new int[w][h];  
        for (int x = 0; x < w; x++) {  
            for (int y = 0; y < h; y++) {  
                gray[x][y]=getGray(bi.getRGB(x, y));  
                sumRGB = sumRGB + gray[x][y];  
            }  
        }  
        if(Threshold ==0){  
            Threshold = sumRGB/(h*w); //threshold by avager  
        }  
        int[][] binary=new int[w][h];  
          
        for (int x = 0; x < w; x++) {  
            for (int y = 0; y < h; y++) {  
                if(getAverageColor(gray, x, y, w, h)>Threshold){  
                    binary[x][y] = 1 ;    /// 1 for White  
                }else{  
                    binary[x][y] = 0 ;    /// 0 for black      
                }  
            }  
        }  
        //System.out.print("black " +test1 + " white "+test2);  
        return binary;  
    }  
  
    private int getGray(int rgb){  
          
        int r = (rgb & 16711680) >> 16;  
        int g = (rgb & 65280) >> 8;  
        int b = (rgb & 255);  
        int top=(int) (r*RP+g*GP+b*BP);  
        return (int)(top);  
    }  
      
    private int getAverageColor(int[][] gray, int x, int y, int w, int h)  
    {  
        int rs = gray[x][y]  
                        + (x == 0 ? 255 : gray[x - 1][y])  
                        + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])  
                        + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])  
                        + (y == 0 ? 255 : gray[x][y - 1])  
                        + (y == h - 1 ? 255 : gray[x][y + 1])  
                        + (x == w - 1 ? 255 : gray[x + 1][ y])  
                        + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])  
                        + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);  
        return rs / 9;  
    }  
  
}  