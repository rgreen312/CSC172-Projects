//Author: Ryan Green
//CSC 172 Project 1

package project1;

//parity bit class extends bit
public class ParityBit extends Bit {
	//takes either a 1 or 0 and stores it in the data variable from the Bit class
	public ParityBit(int info) {
		super(info);
	}
	//returns the data of this bit as a string
	public String toString() {
      return ""+data;
	}
}
