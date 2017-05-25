// Author: Ryan Green
//CSC 172 Project 3

package project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PointLocation {
	public static void main(String args[]) {
		//create scanner to get user input from the console
		Scanner s = new Scanner(System.in); 
		//Get name of input file from the user
		System.out.println("Enter the input file name: ");
		File in = new File("src/project3/");
		String loc = in.getAbsolutePath();
		String inFileName = loc+"/"+s.nextLine(); 
     // String inFileName = s.nextLine();
		s.close(); //close scanner	
		BinaryTree lines = new BinaryTree(); //create new tree
		try {
			File f = new File(inFileName); //find file user gave
			Scanner inFile = new Scanner(f); //create scanner on file
			String firstLine = inFile.nextLine(); //get first line of file, which contains number of lines to be entered into tree
			if (!Character.isDigit(firstLine.charAt(0))) { //if the first line is not a number, print error and exit
				System.out.println("Invalid input");
				System.exit(0);
			}
			int numLines = Integer.parseInt(firstLine); //get the number of lines to be inserted into the tree
			for (int j = 0; j < numLines; j++) { //for that many lines
				String line = inFile.nextLine(); //get the line containing the data for the line
				String[] points = line.split(" "); //split the data on spaces
				Point p1 = new Point(Double.parseDouble(points[0]), Double.parseDouble(points[1])); //get the first point of the line
				Point p2 = new Point(Double.parseDouble(points[2]), Double.parseDouble(points[3])); //get the second point of the line
				lines.insert(new Line(p1, p2)); //insert line into tree
			}
			System.out.println("Printing tree in order");
			lines.printInOrder(); //print out the tree in order after adding all lines
			System.out.println("External nodes: " + lines.externalNodes()); //print out the number of external nodes
			System.out.println("External path length: " + lines.externalPathLength()); //print out the external path length
			System.out.println("Average path length: " + lines.averagePathLength()); //print the average path length
			while (inFile.hasNextLine()) { //for each line in the file
				String nextPoints = inFile.nextLine(); //get the next line
				if (nextPoints.equals("")) //if the line is blank
					continue; //continue to next line
				String[] compPoints = nextPoints.split(" "); //otherwise get the points to be compared against the lines in the tree
				Point p1 = new Point(Double.parseDouble(compPoints[0]), Double.parseDouble(compPoints[1])); //get first point
				Point p2 = new Point(Double.parseDouble(compPoints[2]), Double.parseDouble(compPoints[3])); //get the second point
				lines.sameRegion(p1, p2); //check if the points are in the same region
			}
			inFile.close();
		} catch (FileNotFoundException e) { //Catch exception of the file can't be foudn
			System.out.println("Could not find file"+e);
		}
	}
}
