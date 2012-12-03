package edu.pitt.sis.ImageEx;

import java.util.ArrayList;

public class Compare {
	private float pr = 1f;
	private double area = 0;
	public Compare() {

	}

	/**
	 * input particle between 0-1 0 for use all squares 1 for use largest square
	 * Ӣ�ıȽϲ�������õ���ȡ�����������ٷ�֮��������ϵľ��Σ�����1����ȡ�������0��ȫȡ��������п����ȣ�Ӧ�����ǰɣ�Ĭ��50%
	 * 
	 * @param particle
	 */
	public Compare(float particle) {
		if (particle <= 1 && particle >= 0)
			this.pr = particle;
	}

	public double toCompare(ArrayList<Square> s1, ArrayList<Square> s2, int area) {
		this.area = area;
		double fainllyratio = 0;
		s1 = Sort(s1); // ����ȡǰ����
		s2 = Sort(s2);
		ArrayList<Square> small = s1.size() < s2.size() ? s1 : s2;
		ArrayList<Square> large = s1.size() >= s2.size() ? s1 : s2;

		int tsize = 0;
		for (int i = 0; i < small.size(); i++) {
			tsize = tsize + small.get(i).getArea();
		}

		for (int i = 0; i < small.size(); i++) {
			fainllyratio = fainllyratio + (ratioFrom(small.get(i), large) * small.get(i).getArea() / tsize); // ������Ȳ�ͬ�����
																												// ���ƶ�ռ��Ȩ�ز�ͬ��
		}

		return fainllyratio;
	}
	// �㵥�����εı��ʣ����ƶȰɣ����������Ӣ�ĺ���ƴ���ˡ�
	private double ratioFrom(Square s1, ArrayList<Square> large) {

		ArrayList<Square> temp = new ArrayList<Square>();
		int xend = s1.x + s1.l;
		int yend = s1.y + s1.l;
		int xm = s1.getCenterX();
		int ym = s1.getCenterX();

		Square square = new Square(-1, -1);
		square.l = 999999999;
		for (Square s2 : large) {
			int x1 = s2.getCenterX();
			int y1 = s2.getCenterX();
			if (x1 <= xend && x1 >= s1.x && y1 <= yend && y1 >= s1.y) {
				// in the square
				temp.add(s2);
			}
			if ((getAbsolute(x1 - xm) + getAbsolute(y1 - ym)) < square.l)
				square = s2;
		}
		double l1 = 0, l2 = 0, sizeratio = 0, size1 = 0;
		if (temp.isEmpty()) {
			temp.add(square);
		}
		for (Square s : temp) {
			size1 = size1 + s.l * s.l;

			l1 = l1 + s.x + s.l / 2;
			l2 = l2 + s.y + s.l / 2;

		}
		double size2 = s1.l * s1.l;
		sizeratio = getAbsolute(size1 - size2) / ((size1 + size2) / 2);
		double l = (l1 / temp.size() - xm) * (l1 / temp.size() - xm) + (l2 / temp.size() - ym) * (l2 / temp.size() - ym);
		double ratio = Math.sqrt(l) / Math.sqrt(area);
		ratio = (ratio + sizeratio) / 2;
		return 1 - ratio;

	}

	private double getAbsolute(double i) {
		return i > 0 ? i : -i;
	}

	private ArrayList<Square> Sort(ArrayList<Square> squares) {
		ArrayList<Square> newsquares = new ArrayList<Square>();
		if (squares.size() == 0)
			return newsquares;

		while (true) {
			Square temp = new Square(-1, -1);
			for (Square s : squares) {
				if (s.l > temp.l) {
					temp = s;
				}
			}

			newsquares.add(temp);

			if (temp.l <= newsquares.get(0).l * pr)
				break;

			squares.remove(temp);
			if (squares.isEmpty())
				break;
		}

		return newsquares;
	}
}