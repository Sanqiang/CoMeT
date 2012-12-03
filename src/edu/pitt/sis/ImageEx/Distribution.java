package edu.pitt.sis.ImageEx;  
  
import java.util.ArrayList;  
  
public class Distribution {  
      
    private boolean isBlackSquare = true;  
    private int picW;  
    private int picH;  
    public Distribution(){  
          
    }  

    public Distribution(boolean isBlackSquare){  
        this.isBlackSquare = isBlackSquare;  
    }  

    public ArrayList<Square> toDistribution(int binary[][]){  
        picW = binary.length;  
        picH = binary[0].length;  
        //get picture's height width  
          
        if(!isBlackSquare){  
            anti(binary);  
        }  
          
        return  getSquareArray(binary);  
    }     
      
    public int[] getWH(){  
        int[] xy = new int[2];  
        xy[0] = picW;  
        xy[1] = picH;  
        return xy;  
    }  

    private ArrayList<Square> getSquareArray(int binary[][]){  
        ArrayList<Square> squareList = new ArrayList<Square>();  
        boolean goon = true;  
        int x=0,y=0;  
        int tags = (picW>picH?picW:picH)/100;  
        while(goon) {  
              
            //Calculate next x,y  
            for(int j= y; j<picH;j++) {  
                for(int i = 0;i <picW;i++) {  
                    if(binary[i][j] == 1) {  
                        x = i;  
                        y = j;  
                        i = picW;  
                        j = picH;  
                    }  
                }  
                      
            }  
            if(binary[x][y] != 1 || (x ==picW-1)&&(y ==picH-1)){  
                break;  
            }  
              
            Square square = getSquare(binary,x,y);  
            if(square.l>tags)  
                squareList.add(square);  
                          
            if(x+square.l>=picW&&y+square.l>=picH)  
                goon = false;  
              
        }  
        return squareList;  
    }  
      
    private Square getSquare(int binary[][],int x,int y) {  
        Square square = new Square(x,y);  
        int length = 0;  
        int black=0,write=0,writeSum=0;  
        boolean goon = true;  
        while(goon) {  
            black = getSidePoint(binary,x,y,length);  
            write = 2*length -1 -black;  
            writeSum = writeSum +write;  
            length ++;  
            //quit Standard  
            if(x+length >=picW || y+length>=picH || writeSum>=black){  
                goon = false;  
            }  
        }  
        square.l = length;  
          
        return square;    
    }  
      
    /** 
     * get black point in square side  ��þ��ε�2���ߵĺڵ���������2����ÿ�ε���x��y ����+1�� 
     * @param binary 
     * @param start_X 
     * @param start_Y 
     * @param length 
     * @return 
     */  
    private int getSidePoint(int binary[][],int start_X,int start_Y,int length){  
          
        /*if(length==0) { 
            //for test! 
            System.out.println("Distribution-->getSidePoint-->legth ==0"); 
            System.exit(0); 
        }*/  
          
        int diffPoint = 0;  
        int x ,y;  
          
        if(binary[start_X+length][start_Y+length]==1)  
            diffPoint--;  
          
        for(int i=0;i<=length;i++){  
            x = binary[start_X+i][start_Y+length];  
            y = binary[start_X+length][start_Y+i];  
            if(x==1)  
                diffPoint++;  
            if(y==1)  
                diffPoint++;  
            binary[start_X+i][start_Y+length] = -1;  
            binary[start_X+length][start_Y+i] = -1;  // Mark Read  
        }  
          
        return diffPoint;  
    }  
      
    private void anti(int binary[][]) {  
        for (int x = 0; x < picW; x++) {  
            for (int y = 0; y < picH; y++) {  
                if(binary[x][y] == 1 ){  
                    binary[x][y] = 0 ;    /// 0 for White   
                }else{  
                    binary[x][y] = 1 ;    /// 1 for black   
                }  
            }  
        }  
    }  
}  