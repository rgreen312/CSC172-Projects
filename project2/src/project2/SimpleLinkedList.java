//Author: Ryan Green
//Lab Partner: Tyler Wilson
package project2;

public interface SimpleLinkedList<AnyType> {
   public void insert(AnyType x);
   public void delete(AnyType x);
   public boolean contains(AnyType x);
   public AnyType lookup(AnyType x);
   public boolean isEmpty();
   public void printList();
}
