//Author: Ryan Green
//Lab Partner: Tyler Wilson
package project2;

public interface Queue<AnyType> {
    public boolean isEmpty();
    public void enqueue(AnyType x);
    public AnyType dequeue();
    public AnyType peek();
}
