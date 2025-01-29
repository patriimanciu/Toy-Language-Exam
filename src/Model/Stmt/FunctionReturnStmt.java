package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class FunctionReturnStmt implements IStmt{
    @Override
    public PrgState execute(PrgState state) throws MyException {
        try {
            state.getAllSymTables().pop();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "return";
    }
}
