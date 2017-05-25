//Author: Ryan Green
//Lab Partner: Tyler Wilson
package project2;

public class MyStack<AnyType> implements Stack<AnyType> {
	LinkedList<AnyType> stack; //this class uses the LinkedList data type we created in lab 4 to implement a stack
	public MyStack() {
		stack = new LinkedList<AnyType>(); //create new linked list
	}
	//Returns if the stack is empty
	public boolean isEmpty() {
		return this.stack.isEmpty(); //check if the linked list is empty
	}
	//Pushes a value to the top of the stack
	public void push(AnyType x) {
		this.stack.insertFirst(x); //to push to the stack, insert the value as the head of the list
	}
	//Returns the last value added to the stack, and removes that value from the stack
	public AnyType pop() {
		AnyType x = stack.lookupFirst(); //get value
		stack.deleteFirst(); //delete value from stack
		return x; //return value
	}
	//Returns the last value added to the stack without deleting that value
	public AnyType peek() {
		if (this.stack.head != null)
			return this.stack.lookupFirst(); //return first value in stack
		return null;
	}
	//Prints out each element of the stack
	public void printList(){
		this.stack.printList();
	}
}