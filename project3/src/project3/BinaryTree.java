// Author: Ryan Green
//CSC 172 Project 3

package project3;

public class BinaryTree {
	public TreeNode root; //The root of the tree
	//The values indicating clockwise, counterclockwise, and colinear
	public static final int CLOCKWISE = 1;
	public static final int COUNTERCLOCKWISE = -1;
	public static final int COLINEAR = 0;
	
	/**
	 * Constructor method for the binary tree
	 * Set the root equal to null
	 */
	public BinaryTree() {
		this.root = null;
	}
	/**
	 * Inserts a line into the tree
	 * @param l
	 */
	public void insert(Line l) {
		//if the root is null, set it equal to a node with the new line
		if (this.root == null) { 
			root = new TreeNode(l);
		}
		else {
			root.insert(l, 0); //otherwise insert it on the root node
		}		
	}
	/**
	 * Checks if two points are in the same region or intersected by a line
	 * @param p1 The first point to be checked
	 * @param p2 The second point to be checked
	 */
	public void sameRegion(Point p1, Point p2) {
		TreeNode result = null; //set result to null
		if (this.root != null) { //if root isn't null, search for a line intersecting the two points
			result = this.root.search(p1, p2);
		}
		if (result == null) //if ther is no line intersecting them, then they are in the same region
			System.out.println("Points in same region");
		else //if there is a line separating them, print it to the console
			System.out.println("Separated by line: " +result.data.toString());
	}
	/**
	 * Gets the number of external nodes in the tree
	 * External nodes represent the number of regions created by the lines in the tree
	 * @return The number of external nodes
	 */
	public int externalNodes() {
		if (this.root == null) //if root is null, 1 external node
			return 1;
		return TreeNode.countExternalNodes(this.root); //otherwise count the external nodes below root
	}
	/**
	 * Gets the external path length of the tree
	 * @return The external path length
	 */
	public int externalPathLength() {
		if (this.root != null) //if root is null, external path length is 0
			return this.root.externalPathLength(0);
		return 0;
	}
	/**
	 * Gets the average path length of the tree
	 * Average path length is external path length divided by external nodes
	 * @return The average path length
	 */
	public double averagePathLength() {
		if (this.root != null) //if root isn't null, calculate average
			return (double)this.root.externalPathLength(0) / (double)TreeNode.countExternalNodes(this.root);
		return 0;
	}
	/**
	 * Prints the tree in order
	 */
	public void printInOrder() {
		if (this.root != null)
			this.root.printInOrder();
	}
}
