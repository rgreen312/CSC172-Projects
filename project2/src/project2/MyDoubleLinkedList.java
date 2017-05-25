//Author: Ryan Green
//Lab Partner: Tyler Wilson
package project2;

public class MyDoubleLinkedList<AnyType> implements DoublyLinkedList<AnyType> {
   public MyDoubleNode<AnyType> head; //first element in list
   public MyDoubleNode<AnyType> tail; //last element in list
   
   public MyDoubleLinkedList() {
      //create nodes for head and tail and set next and previous values
      this.head = new MyDoubleNode<AnyType>();
      this.tail = new MyDoubleNode<AnyType>();
      this.head.next = this.tail;
      this.head.prev = this.tail;
      this.tail.next = this.head;
      this.tail.prev = this.head;
   }
   //check if the head node contains data
   public boolean isEmpty() {
      return (this.head.data == null);
   }
   //inserts a new value into the linked list
   public void insert(AnyType x) {
      //if empty set the head value
      if (this.isEmpty()) {;
         this.head.data = x;
      }
      //if tail is empty set the tail value
      else if (this.tail.data == null) {
         this.tail.data = x;
      }
      //if the list does not contain the value
      else if (!this.contains(x)) {
         MyDoubleNode<AnyType> newNode = new MyDoubleNode<AnyType>();
         newNode.data = x;
         //new nodes next value is head, previous is the old tail
         newNode.next = this.head;    
         this.head.prev = newNode;
         this.tail.next = newNode;
         newNode.prev = this.tail;
         //set tail equal to the new node
         this.tail = newNode; 
      }
   }
   //prints the list
   public void printList() {
      MyDoubleNode<AnyType> curNode = this.head;
      while (curNode != this.tail) {
         System.out.println(curNode.data);
         curNode = curNode.next;
      }
      System.out.println(curNode.data);
   }
   //prints the list in reverse
   public void printListRev() {
      MyDoubleNode<AnyType> curNode = this.tail;
      //loop through the list backwards
      while (curNode != this.head) {
         System.out.println(curNode.data);
         curNode = curNode.prev;
      }
      System.out.println(curNode.data);
   }
   //returns whether or not the list contains a given value
   public boolean contains(AnyType x) {
      MyDoubleNode<AnyType> curNode = this.head;
      //loop through list, check each value
      while (curNode != this.tail) {
         if (curNode.data == x) 
            return true;
         curNode = curNode.next;
      }
      return (curNode.data == x);
   }
   //checks if the list contains a value, if it does return that value
   public AnyType lookup(AnyType x) {
      if (this.contains(x))
         return x;
      return null;
   }  
   //deletes a node from the list
   public void delete(AnyType x) {
      MyDoubleNode<AnyType> curNode = this.head;
      if (this.tail.data == x && this.tail.prev == this.head)
         this.tail.data = null;
      //if the tail is the node to be deleted
      else if (this.tail.data == x) {
      //change the pointers of head and tail, set tail value again
         this.tail.prev.next = this.head;
         this.head.prev = this.tail.prev;
         this.tail = this.tail.prev;
      }
      else if (this.head.data == x && this.head.next == tail) {
         this.head.data = null;
      }
      else {
         //loop through whole list
         while (curNode != this.tail) {
            if (curNode.data == x) {
            //if value is found, change the pointers of the previous and next nodes to point to each other
               MyDoubleNode<AnyType> nextNode = curNode.next;
               MyDoubleNode<AnyType> prevNode = curNode.prev;
               nextNode.prev = prevNode;
               prevNode.next = nextNode;
               if (curNode == this.head)
                  head = curNode.next;
               break;
            }
            curNode = curNode.next;
         }
      }
   }
}