// Author: Ryan Green
//CSC 172 Project 3

package project3;

public class TreeNode {
	public static final int CLOCKWISE = 1;
	public static final int COUNTERCLOCKWISE = -1;
	public static final int COLINEAR = 0;
	public Line data; //the line stored in this node
	public TreeNode leftChild;
	public TreeNode rightChild;
	
	public TreeNode() {}
	/**
	 * Constructor method for TreeNode class
	 * Sets the data of this node to the line passed in
	 * @param l The line to be stored in this node
	 */
	public TreeNode(Line l) {
		this.data = l;
		this.leftChild = null;
		this.rightChild = null;
	}
	/**
	 * Method to insert a line into a node
	 * Checks if the line intersects the node and then inserts it on the left, right, or both sides accordingly
	 * @param l The line to be inserted into the tree
	 * @param linesIntersected The number of lines the line to be inserted has intersected so far
	 */
	public void insert(Line l, int linesIntersected) {
		//if lines don't intersect and the number of lines intersected is less than 2
		if (intersection(data, l) == false && linesIntersected < 2) {
			//if the new line is counterclockwise to this node's line, insert it on the left
			if (ccw(l.point1, data.point1, data.point2) == COUNTERCLOCKWISE)
				if (this.leftChild == null) //if node does not exist
					this.leftChild =  new TreeNode(l); //create new node if one does not exist
				else
					this.leftChild.insert(l, linesIntersected); //recursively inser
			//if the new line is clockwise to this node's line, insert it on the right
			else if (ccw(l.point1, data.point1, data.point2) == CLOCKWISE) 
				if (this.rightChild == null) //if node does not exist
					this.rightChild = new TreeNode(l); //create new node
				else
					this.rightChild.insert(l, linesIntersected); //insert recursively
		}
		//if lines intersect, insert line on both sides of the tree
		else if (intersection(data, l)) {
			if (this.leftChild != null) //if left child does not exist
				this.leftChild.insert(l, linesIntersected+1); //insert recursively, incrementing lines intersected variable
			else 
				this.leftChild = new TreeNode(l); //otherwise create new node
			if (this.rightChild != null) //if right child does not exist
				this.rightChild.insert(l, linesIntersected+1); //insert recursively, incrementing lines intersected variable
			else 
				this.rightChild = new TreeNode(l); //otherwise create new node
		}
	}
	/**
	 * Checks whether two lines intersect within the box
	 * Help taken from:
	 * http://www.ahristov.com/tutorial/geometry-games/intersection-segments.html
	 * @param l1 One of the lines to be checked
	 * @param l2 The second line to be checked
	 * @return True if the lines intersect within the box, false otherwise
	 */
	public boolean intersection (Line l1, Line l2) {
		//Get all the coordinates
		double x1 = l1.point1.x;
		double y1 = l1.point1.y;
		double x2 = l1.point2.x;
		double y2 = l1.point2.y;
		
		double x3 = l2.point1.x;
		double y3 = l2.point1.y;
		double x4 = l2.point2.x;
		double y4 = l2.point2.y;
		
	    double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
	    if (d == 0) return false;
	    
	    double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
	    double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
	    
	    if (xi < Math.min(x1,x2) || xi > Math.max(x1,x2)) return false;
	    if (xi < Math.min(x3,x4) || xi > Math.max(x3,x4)) return false;
		if (xi >= 0 && xi <= 1 && yi <= 1 && yi >= 0) //check if the intersection point is within the box
	    	return true; //if it is return true
		return false;
	}
	/**
	 * Checks whether a point is counterclockwise or clockwise from a line
	 * Taken from the Project 3 description
	 * @param p0 
	 * @param p1
	 * @param p2
	 * @return Returns the direction of the point from the line
	 */
	int ccw(Point p0, Point p1, Point p2) {
		double dx1 = p1.x-p0.x;
		double dy1 = p1.y-p0.y;
		double dx2 = p2.x-p0.x;
		double dy2 = p2.y-p0.y;
		if (dx1*dy2 > dy1*dx2) return COUNTERCLOCKWISE;
	    else if (dx1*dy2 < dy1*dx2) return CLOCKWISE;
	    else if ((dx1*dx2 < 0) || (dy1*dy2 < 0)) return CLOCKWISE;
	    else if ((dx1*dx1+dy1*dy1) < (dx2*dx2+dy2*dy2)) return COUNTERCLOCKWISE;
	    else return COLINEAR;	
	}
	/**
	 * Searches the tree for a line that separates two points, if one is found it returns that line
	 * @param p1 The first point
	 * @param p2 The second point
	 * @return The node that contains the line separating the two points. If no such line exists, returns null
	 */
	public TreeNode search(Point p1, Point p2) {
		//get the two regions the points are in
		int region1 = ccw(p1, this.data.point1, this.data.point2); 
		int region2 = ccw(p2, this.data.point1, this.data.point2);
		
		if (region1 != region2){ //if they are separated by this line
			return this;
		}
		//if the region of the points is clockwise, search the right subtree
		if (region1 == CLOCKWISE) {
			if (this.rightChild == null)
				return null;
			else
				return this.rightChild.search(p1, p2);
		}
		//if the region of the points is counterclockwise, search the left subtree
		if (region1 == COUNTERCLOCKWISE) {
			if (this.leftChild == null)
				return null;
			else 
				return this.leftChild.search(p1, p2);
		}
		return null;
	}
	/**
	 * Counts the number of external nodes in the tree
	 * @param n The current node being counted
	 * @return The number of external nodes within this tree
	 */
	public static int countExternalNodes(TreeNode n){
		  if( n == null) 
		    return 0;
		  if( n.leftChild == null && n.rightChild == null )  //if this is a leaf node
		    return 2; //return 2, one for each child
		// if one child is null but not the other, check the number of external nodes in that tree and add one to it for the null value at this node
        else if (n.leftChild == null) 
          return 1 + countExternalNodes(n.rightChild);
        else if (n.rightChild == null) 
          return 1 + countExternalNodes(n.leftChild);
        else //if neither of the children are null, call this function on both children
		    return countExternalNodes(n.rightChild) + countExternalNodes(n.leftChild);
	}
	/**
	 * Gets the external path length of the tree
	 * @param depth The current depth of the tree
	 * @return returns the total external path length of this node's subtree
	 */
	public int externalPathLength(int depth) {
		if (this.rightChild == null && this.leftChild == null)  //if this is a leaf node
			return 2*(depth+1); //return 2 times the depth + 1 because there are two regions beneath this node, so the depth is one greater and then doubled
		else if (this.rightChild != null && this.leftChild != null)  // if both of the children are not null
			return this.rightChild.externalPathLength(depth + 1) + this.leftChild.externalPathLength(depth + 1); //get the path length of both nodes, incrementing the depth
		// if one of the children is null but the other isn't, search the one that is not, adding the current depth plus 1 to that because the other child is null
		else if (this.rightChild != null)
			return 1+depth+this.rightChild.externalPathLength(depth+1);
		else if (this.leftChild != null)
			return 1+depth+this.leftChild.externalPathLength(depth+1);
		return depth;	
	}
	/**
	 * Prints the tree in order
	 */
	public void printInOrder() {
		//if left isn't null print it
		if (this.leftChild != null)
			this.leftChild.printInOrder();
		//next print the data in this element
		System.out.println(this.data.toString());
		//finally print the right subtree if not null
		if (this.rightChild != null)
			this.rightChild.printInOrder();
	}
}
