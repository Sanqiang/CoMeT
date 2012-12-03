package edu.pitt.sis.ImageEx;

public class Square {
	int x;
	int y;
	public Square(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	int l;
	public int getCenterX() {
		return x + l / 2;
	}
	public int getArea() {
		return l * l;
	}

}
