package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Collections.MyIList;
import Utils.Exceptions.MyException;

public class PrintStmt implements IStmt{
    private final Exp expression;

    public PrintStmt(Exp exp){
        this.expression = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value val = expression.eval(state.getSymTable(), state.getMyHeapTable());
        MyIList<Value> out = state.getOut();
        out.add(val);
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        expression.typecheck(typeEnv);
        return typeEnv;
    }

    public String toString(){
        return "print(" + expression.toString() + ")";
    }
}
