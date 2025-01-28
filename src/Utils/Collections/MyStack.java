package Utils.Collections;

import Model.Stmt.IStmt;
import Utils.Exceptions.MyException;

import java.util.*;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;
    public MyStack() {
        stack = new Stack<>();
    }
    @Override
    public void push(T t) {
        stack.push(t);
    }

    @Override
    public T pop() throws MyException {
        if (stack.isEmpty())
            throw new MyException("Nothing to pop. The stack is empty.");
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<T> toListS() {
        return (List<T>) Arrays.asList(stack.toArray());
    }

    @Override
    public T toArray() {
        return (T) stack.toArray();
    }

    public String toString() {
        return stack.toString();
    }

    public MyStack<T> deepCopy() {
        MyStack<T> newStack = new MyStack<T>();
        for(T elem : stack) {
            newStack.push(elem);
        }
        return newStack;
    }

    public List<IStmt> getReversed() {
        List<IStmt> reversedList = new ArrayList<>();
        for (Object obj : stack) {
            reversedList.add((IStmt) obj); // Ensure 'obj' is truly of type IStmt
        }
        Collections.reverse(reversedList);
        return reversedList;
    }
}
