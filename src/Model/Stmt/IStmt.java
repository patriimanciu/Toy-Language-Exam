package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException;
}