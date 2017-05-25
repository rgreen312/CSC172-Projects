//Author: Ryan Green
//CSC 172 Project 4
//Collaborated with: Tyler Wilson and Sam Triest
package project4;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StreetMap {
	public static final int WINDOW_WIDTH = 1000;
	public static int WINDOW_HEIGHT = 700;
	public static void main(String[] args) {
		String inFile = args[0];
		boolean show = false;
		boolean directions = false;
		boolean meridian = false;	
		String start = "";
		String end = "";
		//Read in arguments and set variables accordingly
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-show"))
				show = true;
			if (args[i].equals("-directions")) {
				directions = true;
				start = args[i+1];
				end = args[i+2];
			}
			if (args[i].equals("-meridianmap"))
				meridian = true;
		}
		Graph g = new Graph(); //create graph
		File f = new File("src/project4");
		String path = f.getAbsolutePath();
		File in = new File(path+"/"+inFile);
		Scanner s;
		//Read in all lines from file
		try {
			s = new Scanner(in);
			
			while (s.hasNextLine()) {
				String line = s.nextLine();
				Scanner lineScan = new Scanner(line);
				String type = lineScan.next();
				if (type.equals("i")) {
					g.addVertex(new Vertex(lineScan.next(), Double.parseDouble(lineScan.next()), Double.parseDouble(lineScan.next())));
				}
				else {
					g.addEdge(lineScan.next(), g.getVertex(lineScan.next()), g.getVertex(lineScan.next()));
				}
			}
			//initialize variables and set them if the flags were used from the command line
			JPanel shortestPath = null;
			JPanel spanningTree = null;
			if (directions) {
				g.getShortestPath(start, end);
				shortestPath = g.drawShortestPath();
			}	
			else if (meridian) {
				g.MinSpanningTree();
				spanningTree = g.drawSpanningTree();
			}
			//Display the graph if the user entered that flag, also display the min spanning tree or shortest path if those flags were used
			if (show) {
				JFrame frame = new JFrame();
				frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
				frame.setTitle("Street Map");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (meridian)
					frame.add(spanningTree);
				else if (directions)
					frame.add(shortestPath);
				else {
					JPanel panel = g.draw();
					frame.add(panel);
				}
				frame.setVisible(true);

			}
		} catch (FileNotFoundException e){
			System.out.println("File not found");
		}
	}
}