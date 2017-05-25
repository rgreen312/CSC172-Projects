//Author: Ryan Green
//CSC 172 Project 4
//Collaborated with: Tyler Wilson and Sam Triest
package project4;

public class Edge implements Comparable<Edge> {
	public final Vertex from;
	public final Vertex to; // an edge from v to w
	public double weight;
	public String id;
	public boolean used = false;
	
    public Edge(Vertex from, Vertex to, double weight) { 
    	this.to = to;
    	this.from = from;
    	this.weight = weight;
    }
	public Edge() {
		this.to = null;
		this.from = null;
		this.weight = Double.MAX_VALUE;
	}
    public int compareTo(Edge e) {
    	if (this.weight == e.weight)
    		return 0;
    	else if (this.weight < e.weight)
    		return -1;
    	else 
    		return 1;
    }
}
