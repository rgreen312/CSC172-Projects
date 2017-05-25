//Author: Ryan Green
//CSC 172 Project 4
//Collaborated with: Tyler Wilson and Sam Triest
package project4;

import java.util.LinkedList;

public class Vertex implements Comparable<Vertex>{
	public LinkedList<Edge> adjList = new LinkedList<Edge>();
	public String id;
	public double latitude;
	public double longitude;
	public boolean visited = false;
	double distance;
	public boolean known;
	public Vertex last;
	public int number;
	
	public Vertex(String id, double latitude, double longitude) {
		this.id=id;
		this.latitude=latitude;
		this.longitude=longitude;
		visited=false;
	}
	public String toString() {
		return id+" "+latitude+" "+longitude;
	}
	public void connectTo(Edge e) {
		this.adjList.add(e);
	}
	public int compareTo(Vertex v2) {
		if (this.distance == v2.distance)
			return 0;
		else if (this.distance < v2.distance)
			return -1;
		else 
			return 1;
	}
}