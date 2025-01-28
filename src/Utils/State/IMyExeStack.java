package Utils.State;

import Model.Stmt.IStmt;
import Utils.Exceptions.MyException;

import java.util.List;

public interface IMyExeStack {
    IStmt pop() throws MyException;
    void push(IStmt i);
    boolean isEmpty();
    int size();
    String toString();
    List<IStmt> toList();
}
