package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Bool;
import Model.Types.Type;
import Model.Values.BoolValue;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class WhileStmt implements IStmt{
    Exp condition;
    IStmt statement;

    public WhileStmt(Exp condition, IStmt statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (condition.eval(state.getSymTable(), state.getMyHeapTable()) instanceof BoolValue boolVal) {
            if (boolVal.getValue()) {
                state.getExeStack().push(this);
                state.getExeStack().push(statement);
            }
            return null;
        }
        else
            throw new MyException("While condition should evaluate to a BooleanType");
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type type = condition.typecheck(typeEnv);
        if (type.equals(new Bool())) {
            statement.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new MyException("While condition should evaluate to a BooleanType");
    }

    @Override
    public String toString() {
        return "While(" + condition.toString() + "){" + statement.toString() + "};";
    }
}
