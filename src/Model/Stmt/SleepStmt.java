package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDic;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;

public class SleepStmt implements IStmt{
    private final Integer number;

    public SleepStmt(Integer number) {
        this.number =  number;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyStack<IStmt> exeStack = state.getExeStack();
        if (number > 0) {
            exeStack.push(new SleepStmt(number - 1));
            state.setExeStack(exeStack);
        }
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("sleep(%d)", number);
    }
}
