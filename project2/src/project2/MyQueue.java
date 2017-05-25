//Author: Ryan Green
//Lab Partner: Tyler Wilson
package project2;

public class MyQueue<AnyType> implements Queue<AnyType> {
	//uses a DoublyLinkedList as created in lab 5 to act as a queue
	public MyDoubleLinkedList<AnyType> queue;
	public MyQueue() {
		queue = new MyDoubleLinkedList<AnyType>(); //initialize linked list
	}
	public boolean isEmpty() {
		return this.queue.isEmpty(); //return if the linked list is empty
	}
	public void enqueue(AnyType x) {
		this.queue.insert(x); //insert value into linked list normally, it becomes the tail
	}
	//to dequeue we get the data in the head element and then remove the node that contains that element (i.e. the head)
	public AnyType dequeue() {
      AnyType x = null;
      if (this.queue.head.data != null) {
   		x = this.queue.head.data;
   		this.queue.delete(x); //delete head node
      }
      else if (this.queue.tail.data != null) {
   		x = this.queue.tail.data;
   		this.queue.tail.data = null; //delete head node
      }
      if (x != null)    
		   return x;
      return null;
	}
	// returns the first value in the queue, but doesn't remove it
	public AnyType peek() {
      if (this.queue.head.data != null) 
		   return this.queue.head.data; //return the value in the head
      else if (this.queue.tail.data != null) {
         AnyType d = this.queue.tail.data;
         return d;
      }
      return null;
	}
	//prints out each value in the queue
	public void printList() {
		this.queue.printList();
	}
}