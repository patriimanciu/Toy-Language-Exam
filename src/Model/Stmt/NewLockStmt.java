package Model.Stmt;


import Model.ProgramState.PrgState;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.ILockTable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLockStmt implements IStmt {
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public NewLockStmt(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        ILockTable lockTable = state.getLockTable();
        MyIDic<String, Value> symTable = state.getSymTable();
        int freeAddress = lockTable.getFreeValue();
        lockTable.put(freeAddress, -1);
        if (symTable.contains(var) && symTable.lookUp(var).getType().equals(new Int())) {
            symTable.update(var, new IntValue(freeAddress));
        }
        else
            throw new MyException("Variable not declared!");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new Int()))
            return typeEnv;
        else
            throw new MyException("Var is not of int Types!");
    }

    @Override
    public String toString() {
        return String.format("newLock(%s)", var);
    }
}