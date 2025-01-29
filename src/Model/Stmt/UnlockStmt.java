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

public class UnlockStmt implements IStmt{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public UnlockStmt(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDic<String, Value> symTable = state.getSymTable();
        ILockTable lockTable = state.getLockTable();
        if (symTable.contains(var)) {
            if (symTable.lookUp(var).getType().equals(new Int())) {
                IntValue fi = (IntValue) symTable.lookUp(var);
                int foundIndex = fi.getValue();
                if (lockTable.containsKey(foundIndex)) {
                    if (lockTable.get(foundIndex) == state.getID())
                        lockTable.update(foundIndex, -1);
                } else {
                    throw new MyException("Index not in the lock table!");
                }
            } else {
                throw new MyException("Var is not of int Types!");
            }
        } else {
            throw new MyException("Variable is not defined!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        if (typeEnv.lookUp(var).equals(new Int()))
            return typeEnv;
        else
            throw new MyException("Var is not of Types int!");
    }

    @Override
    public String toString() {
        return String.format("unlock(%s)", var);
    }
}
