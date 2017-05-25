//Author: Ryan Green
//Lab Partner: Tyler Wilson
package project2;

public interface Stack<AnyType> {
    public boolean isEmpty();
    public void push(AnyType x);
    public AnyType pop();
    public AnyType peek();
}
