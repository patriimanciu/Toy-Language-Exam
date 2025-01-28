package Utils.Collections;

import Model.Stmt.IStmt;
import Utils.Exceptions.MyException;

import java.util.List;

public interface MyIStack<T> {
    void push(T t);
    T pop() throws MyException;
    boolean isEmpty();
    List<T> toListS();
    T toArray();
    public List<IStmt> getReversed();
}
