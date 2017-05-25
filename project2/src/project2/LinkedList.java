package project2;

public class LinkedList<AnyType> implements SimpleLinkedList<AnyType> {
   public MyNode<AnyType> head;
   
   //set head node equal to null
   public LinkedList() {
      head = null;
   }
   //Return true if there is no data in the list, false otherwise
   public boolean isEmpty() {
      if (head == null) {
         return true;
      }
      return false;
   }
   //Inserts a value into the list, at the end
   public void insert(AnyType x) {
      //if list is empty, set head node equal to this value
      if (this.isEmpty()) {
         this.head = new MyNode<AnyType>();
         this.head.data = x;
      }
      else {
         //if list already contains this value, don't insert it again
        MyNode<AnyType> curNode = this.head;
        //loop through list to get to the end
        while (curNode.next != null) {
           curNode = curNode.next;
        }
        //once at end, insert new node and set pointers
        curNode.next = new MyNode<AnyType>();
        curNode.next.data = x;
      }
   }
   //print each element in list
   public void printList() {
      MyNode<AnyType> curNode = this.head;
      //loop through list, starting at head, to the end and print each value
      while (curNode.next != null) {
         System.out.println(curNode.data);
         curNode = curNode.next;
      }
      System.out.println(curNode.data); //print last value
   }
   //check if the list contains a given value
   public boolean contains(AnyType x) {
      MyNode<AnyType> curNode = this.head;
      //loop through list, check if the data in each node is equal to x
      while (curNode.next != null) {
         if (curNode.data == x)
            return true;
         curNode = curNode.next;
      }
      return (curNode.data == x); //check last value
   }
   //looks up a value in the list
   public AnyType lookup(AnyType x) {
      if (this.contains(x))
         return x;
      return null;
   }
   //returns the data in the head node
   public AnyType lookupFirst() {
	   if (this.head != null)
		   return this.head.data; 
	   return null;
   }
   //deletes the head node of this list
   public void deleteFirst() {
	   if (!this.isEmpty())
	   		this.head = this.head.next; //set the head equal to the next head, the previous head will no longer be referenced
   }
   //deletes given value from the list
   public void delete(AnyType x) {
      if (this.contains(x)) {
         MyNode<AnyType> curNode = this.head;
         MyNode<AnyType> prevNode = this.head;
         //loop through list until we find the node with the data to be deleted
         while (curNode.next != null) {
            if (curNode.data == x) {
               prevNode.next = curNode.next; //set next pointer to a different node
               break;
            }
            else {
               prevNode = curNode;
               curNode = curNode.next;
            }
         }
         if (curNode.data == x) {
            prevNode.next = null;
         }
      }
   }
   //inserts a value into the start of the list
   public void insertFirst(AnyType x) {
	   //create new node
	   MyNode<AnyType> newNode = new MyNode<AnyType>();
	   newNode.data = x;
	   //set pointers for new node
	   newNode.next = this.head;
	   this.head = newNode;
   }
}