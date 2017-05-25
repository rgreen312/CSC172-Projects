// Author: Ryan Green
//CSC 172 Project 3

package project3;

public class Line {
	public Point point1;
	public Point point2;
	
	public Line (Point p1, Point p2) {
		point1=p1;
		point2=p2;
	}
	public String toString() {
		return point1.x  + " " + point1.y + "   " + point2.x + " "+ point2.y;
	}
}
