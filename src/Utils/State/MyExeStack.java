package Utils.State;

import Model.Stmt.IStmt;
import Utils.Collections.MyIStack;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;

import java.util.List;

public class MyExeStack implements IMyExeStack{
    MyIStack<IStmt> stack;
    public MyExeStack(){
        this.stack=new MyStack<>();
    }
    @Override
    public IStmt pop() throws MyException {
        return stack.pop();
    }

    @Override
    public void push(IStmt i) {
        stack.push(i);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public List<IStmt> toList() {
        return stack.toListS();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
