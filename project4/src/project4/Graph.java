//Author: Ryan Green
//CSC 172 Project 4
//Collaborated with: Tyler Wilson and Sam Triest

package project4;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

import javax.swing.JPanel;

public class Graph {
	private HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();
	private LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
	private LinkedList<Edge> edgeList = new LinkedList<Edge>();
	private LinkedList<Vertex> shortestPath = new LinkedList<Vertex>();
	private LinkedList<Edge> minSpanningTree = new LinkedList<Edge>();
	Tuple[] tuples;
	public int counter = 0;
	
	public Graph () {
	
	}
	/**
	 * Constructor method for graph takes an array of vertices
	 * @param vertices The vertices to be added to the graph
	 */
	public Graph(Vertex[] vertices) {
		for (int i = 0; i < vertices.length; i++)
			this.vertices.put(vertices[i].id, vertices[i]);
	}
	/**
	 * Adds an edge to the graph between two nodes
	 * @param roadId The id of the edge
	 * @param v1 The first node to by connected
	 * @param v2 The second node to be connected
	 */
	public void addEdge(String roadId, Vertex v1, Vertex v2) {
		double distance = measure(v1.latitude, v1.longitude, v2.latitude, v2.longitude); //get distance between two nodes
		//Create edges between nodes
		Edge e1 = new Edge(v1, v2, distance); 
		e1.id = roadId;
		e1.used = false;
		Edge e2 = new Edge(v2, v1, distance);
		e2.id = roadId;
		e2.used = false;
		//Add edge to adjacency lists of both nodes
		v1.connectTo(e1);
		v2.connectTo(e2);
		this.edgeList.add(e1); //add to list of edges
	}
	/**
	 * Add a vertex to the graph
	 * @param v2 The vertex to be added to the graph
	 */
	public void addVertex(Vertex v2) {
		v2.visited = false;
		v2.number = counter;
		this.vertices.put(v2.id, v2);
		this.vertexList.add(v2);
		counter++;
	}
	/**
	 * Get a vertex from the graph given an id
	 * @param id The id of the vertex
	 * @return The vertex with the given id
	 */
	public Vertex getVertex(String id) {
		return this.vertices.get(id); //get vertex from the hashtable
	}
	/**
	 * Check whether two vertices are connected
	 * @param v1 The first vertex
	 * @param v2 The second vertex
	 * @return True if the two vertices are connected, false otherwise
	 */
	public boolean connected(Vertex v1, Vertex v2) {
		ListIterator<Edge> paths = v1.adjList.listIterator(); //get the list of edges that the first vertex has
		while (paths.hasNext()) { //for each edge
			if (paths.next().to.id.equals(v2.id)) //check if the other end of that edge is the second vertex
				return true;
		}
		return false;
	}
	/**
	 * Gets the max and min latitude and longitude in the graph
	 * This can be used to scale the graph to fit the window
	 * @return An array with the max and min latitude and longitude
	 */
	public double[] getScaling() {
		double[] values = {Double.MAX_VALUE, 0, Double.MAX_VALUE, -Double.MAX_VALUE};
		ListIterator<Vertex> l = vertexList.listIterator(); //Iterator over all vertices
		//for each vertex check if its coordinates are min or max
		while (l.hasNext()) {
			Vertex v = l.next();
			if (v.latitude < values[0])
				values[0] = v.latitude;
			if (v.longitude < values[2])
				values[2] = v.longitude;
			if (v.latitude > values[1])
				values[1] = v.latitude;
			if (v.longitude > values[3])
				values[3] = v.longitude;
		}
		return values;
	}
	/**
	 * Prints out each vertex in the graph
	 */
	public void print() {
		//Iterate over each vertex in the graph and print it out
		ListIterator<Vertex> l = vertexList.listIterator();
		while (l.hasNext()) {
			System.out.println(l.next());
		}
	}
	/**
	 * Draw the graph
	 * @return A JPanel containing a graphic representation of the graph
	 */
	public JPanel draw() {
		return new View();
	}

	public class View extends JPanel {
		/**
		 * Creates a JPanel with all the lines between vertices drawn
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			double[] ranges = getScaling(); //get scaling values
			//Iterate over each point
			ListIterator<Vertex> points = vertexList.listIterator();
			g.setColor(Color.BLACK);
			while (points.hasNext()) { //for each vertex
				Vertex v = points.next();
				double latRange = ranges[1]-ranges[0];
				double longRange = Math.abs(ranges[3]-ranges[2]);
				//get scaled x and y values
				int x1 = (int)((v.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y1 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(v.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
				ListIterator<Edge> edges = v.adjList.listIterator();
				//for each edge connected to this vertex
				while (edges.hasNext()) {
					//get scaled x and y values for the other vertex and draw line
					Edge e = edges.next();
					Vertex v2 = e.to;
					int x2 = (int)((v2.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
					int y2 = StreetMap.WINDOW_HEIGHT-(int)((v2.latitude-ranges[0])*(StreetMap.WINDOW_HEIGHT)/latRange);
					g.drawLine(x1,y1,x2,y2);
				}
			}
		}
	}
	/**
	 * Gets the minimum weight spanning tree of the graph using Kruskal's algorithm
	 */
	public void MinSpanningTree() {
		tuples = new Tuple[vertexList.size()]; //initialize tuples
		Edge[] edges = edgeList.toArray(new Edge[edgeList.size()]); 
		Arrays.sort(edges); //sort edges
		for (int i = 0; i < vertexList.size(); i++)
			tuples[i] = new Tuple(i, vertices.get(i)); //create tuples for each vertex
		for (int j = 0; j < edges.length; j++) {
			if (minSpanningTree.size() > vertexList.size() - 1) //if the max num of edges has been reached, break
				break;
			if (root(edges[j].from.number) != (root(edges[j].to.number))) { 
				//if this segment should be added to the min spanning tree, add it
				minSpanningTree.add(edges[j]);
				union(edges[j].from.number, edges[j].to.number); //join the disjoint sets
			}
		}
		//Print out all roads traveled 
		ListIterator<Edge> e = minSpanningTree.listIterator();
		System.out.println("Roads Traveled:");
		while (e.hasNext()) {
			System.out.println(e.next().id);
		}
	}
	//Get the root of a tuple
	public int root(int i) {
		if (i == tuples[i].i)
			return tuples[i].i;
		else 
			return root(tuples[i].i);
	}
	//Get the depth of a tuple
	public int depth(int i) {
		if (i == tuples[i].i)
			return 1;
		else 
			return 1+depth(tuples[i].i);
	}
	//Join two tuples 
	public void union(int a, int b) {
		if (root(a) == root(b))
				return;
		if (depth(a) < depth(b))
			tuples[root(a)].i = root(b);
		else 
			tuples[root(b)].i = root(a);
	}
	public JPanel drawSpanningTree() {
		return new Tree();
	}
	public class Tree extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			double[] ranges = getScaling();
			double latRange = ranges[1]-ranges[0];
			double longRange = Math.abs(ranges[3]-ranges[2]);
			//Iterate over each point
			ListIterator<Vertex> points = vertexList.listIterator();
			g.setColor(Color.BLACK);
			while (points.hasNext()) { //for each vertex
				Vertex v = points.next();
				//get scaled x and y values
				int x1 = (int)((v.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y1 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(v.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
				ListIterator<Edge> edges = v.adjList.listIterator();
				//for each edge connected to this vertex
				while (edges.hasNext()) {
					//get scaled x and y values for the other vertex and draw line
					Edge e = edges.next();
					Vertex v2 = e.to;
					int x2 = (int)((v2.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
					int y2 = StreetMap.WINDOW_HEIGHT-(int)((v2.latitude-ranges[0])*(StreetMap.WINDOW_HEIGHT)/latRange);
					g.drawLine(x1,y1,x2,y2);
				}
			}
			g.setColor(Color.CYAN);
			ListIterator<Edge> e = minSpanningTree.listIterator();
			Edge current;
			while (e.hasNext()) {
				current = e.next();
				int x1 = (int)((current.from.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y1 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(current.from.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
				int x2 = (int)((current.to.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y2 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(current.to.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
				g.drawLine(x1,y1,x2,y2);
			}
		}
	}
	public JPanel drawShortestPath() {
		return new Path();
	}
	public class Path extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ListIterator<Vertex> l = shortestPath.listIterator();
			//System.out.println(shortestPath.toString());
			double[] ranges = getScaling();
			double latRange = ranges[1]-ranges[0];
			double longRange = Math.abs(ranges[3]-ranges[2]);
			//Iterate over each point
			ListIterator<Vertex> points = vertexList.listIterator();
			g.setColor(Color.BLACK);
			while (points.hasNext()) { //for each vertex
				Vertex v = points.next();
				//get scaled x and y values
				int x1 = (int)((v.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y1 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(v.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
				ListIterator<Edge> edges = v.adjList.listIterator();
				//for each edge connected to this vertex
				while (edges.hasNext()) {
					//get scaled x and y values for the other vertex and draw line
					Edge e = edges.next();
					Vertex v2 = e.to;
					int x2 = (int)((v2.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
					int y2 = StreetMap.WINDOW_HEIGHT-(int)((v2.latitude-ranges[0])*(StreetMap.WINDOW_HEIGHT)/latRange);
					g.drawLine(x1,y1,x2,y2);
				}
			}
			Vertex current = l.next();
			Vertex next = l.next();
			g.setColor(Color.RED);
			while (l.hasNext()) {
				int x1 = (int)((current.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y1 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(current.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
				int x2 = (int)((next.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
				int y2 = StreetMap.WINDOW_HEIGHT-(int)((next.latitude-ranges[0])*(StreetMap.WINDOW_HEIGHT)/latRange);
				g.drawLine(x1,y1,x2,y2);
				current = next;
				next = l.next();
			}
			int x1 = (int)((current.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
			int y1 = StreetMap.WINDOW_HEIGHT-(int)((Math.abs(current.latitude-ranges[0]))*(StreetMap.WINDOW_HEIGHT/latRange));
			int x2 = (int)((next.longitude-ranges[2])*(StreetMap.WINDOW_WIDTH/longRange));
			int y2 = StreetMap.WINDOW_HEIGHT-(int)((next.latitude-ranges[0])*(StreetMap.WINDOW_HEIGHT)/latRange);
			g.drawLine(x1,y1,x2,y2);
		}
	}
	/**
	 * Implements Djikstra's algorithm to find the shortest path length from one vertex to all other vertices
	 * @param v The vertex to find all the shortest path lengths from
	 */
	public void djikstra(Vertex v) {
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>(); //heap to store all known vertices
		Vertex start = vertices.get(v.id);
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		vertexList.addAll(vertices.values());
		int unknowns = 0;
		
		//for each vertex
		for (Vertex i : vertexList) {
			if (i.id == v.id) 
				i.distance = 0;
			else 
				i.distance = Double.MAX_VALUE;
			i.known = false;
			unknowns++;
			i.last=null;
		}
		ListIterator<Edge> edges = v.adjList.listIterator();
		while (edges.hasNext()) {
			Edge curEdge = edges.next();
			Vertex to = curEdge.to;
			to.known = true;
			to.distance = curEdge.weight;
			to.last = v;
			vertexQueue.add(to);
		}
		//while there are still nodes left to be explored
		while (!vertexQueue.isEmpty()) {
			Vertex closest = vertexQueue.poll(); 
			ListIterator<Edge> newKnowns = closest.adjList.listIterator(); 
			//run through all new known nodes
			while (newKnowns.hasNext()) {
				Edge curEdge = newKnowns.next();
				boolean wasKnown = curEdge.to.known;
				curEdge.to.known = true;
				//if this distance is less than the current distance stored in the edge, change the last node pointer in the vertex
				if (curEdge.to.distance > closest.distance + curEdge.weight) {
					curEdge.to.last = closest;
					curEdge.to.distance = closest.distance + curEdge.weight;
				}
				if (!wasKnown) //if the node wasn't previously known, add it to the vertex
					vertexQueue.add(curEdge.to);
			}
		}
	}
	/**
	 * Gets the shortest path between two vertices and prints out the list of intersections on this path and the length of that path
	 * @param v1 The first vertex
	 * @param v2 The second vertex
	 */
	public void getShortestPath(String v1, String v2) {
		//get the two vertices from the graph
		Vertex from = vertices.get(v1);
		Vertex to = vertices.get(v2);
		double distance = 0;
		shortestPath.add(to);
		djikstra(from); //run djikstra's algorithm on the first node
		//Print out the path between the two points
		System.out.println("Path");
		System.out.print(to.id + " -> ");
		Vertex last = to.last;
		while (last != from) {
			if (last.last == null) {
				System.out.println("No Path");
				return;
			}
			shortestPath.add(last);
			System.out.print(last.id+ " -> ");
			last = last.last;
		}
		//Print out distance between points
		distance = to.distance;
		shortestPath.add(from);
		System.out.print(from.id);
		System.out.println();
		System.out.println("Distance: " + distance + " miles");
	}
	/**
	 * Gets the distance between two points based on their latitude and longitude
	 * Taken from: http://stackoverflow.com/questions/639695/how-to-convert-latitude-or-longitude-to-meters
	 * @param lat1 First latitude
	 * @param lon1 First longitude
	 * @param lat2 Second latitude
	 * @param lon2Second longitude
	 * @return The distance between the two points
	 */
	public double measure(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
	    double R = 6378.137; // Radius of earth in KM
	    double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
	    double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = R * c;
	    return (d * 1000)/1609.34;  //miles
	}
}