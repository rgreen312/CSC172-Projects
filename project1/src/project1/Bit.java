//Author: Ryan Green
//CSC 172 Project 1

package project1;

public class Bit {
	public int data; //the data contained in this bit, either a 1 or 0
	//Takes in a data of either 0 or 1 and assigns it to the data variable
	public Bit(int data) {
		this.data = data;
	}
	//Returns the data as a string
	public String toString() {
		return ""+data;
	}
	//Flips this bits value
	//if 1, it sets the data to 0
	//if 0, it sets the data to 1
	public void flip() {
		if (this.data == 1)
			this.data = 0;
		else 
			this.data = 1;
	}
}
