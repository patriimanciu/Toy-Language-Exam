package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyDic;
import Utils.Collections.MyIDic;
import Utils.Collections.MySemaphoreTable;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;
import Utils.State.MyHeap;
import Utils.State.MyLockTable;

import java.util.Map;

public class ForkStmt implements IStmt{
    private final IStmt statement;

    public ForkStmt(IStmt statement) {
        this.statement = statement;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyStack newStack = new MyStack();
//        newStack.push(statement);
        MyIDic<String, Value> newSymTable = new MyDic<>();
        for (Map.Entry<String, Value> entry: state.getSymTable().getMap().entrySet()) {
            newSymTable.put(entry.getKey(), entry.getValue());
        }
        return new PrgState(newStack, newSymTable, state.getFileTable(), (MyHeap<Value>) state.getMyHeapTable(), (MyLockTable) state.getLockTable(), (MySemaphoreTable) state.getSemaphoreTable(),state.getOut(), statement);
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("fork(%s)", statement.toString());
    }
}
