package edu.pitt.sis.ImageEx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class API {
public static void main(String[] args) throws Exception {
	Binarization b = new Binarization();  
	FileInputStream is = new FileInputStream(new File("D:/Web/Image/group_collapse.gif"));  
	int a[][] = b.toBinarization(is);  
	Distribution d = new Distribution();  
	ArrayList<Square> s = d.toDistribution(a);  
	System.out.println(s.size());  
	  
	FileInputStream is2 = new FileInputStream(new File("D:/Web/Image/group_expand.gif"));  
	int a2[][] = b.toBinarization(is2);  
	d = new Distribution();  
	ArrayList<Square> s2 = d.toDistribution(a2);  
	System.out.println(s.size());  
	Compare c= new Compare();  
	double m1 = c.toCompare(s, s2, b.getArea());  
	double m2 = c.toCompare(s2,s, b.getArea());  
	System.out.println("'''''" +m1 + "," +m2 +"," );  
}
}
