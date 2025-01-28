package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.StringType;
import Model.Types.Type;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class CloseRFile implements IStmt {
    private Exp expression;
    private StringType str;

    public CloseRFile(Exp expression) {
        this.expression = expression;
        this.str = new StringType();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var value = expression.eval(state.getSymTable(), state.getMyHeapTable());
        if (!value.getType().equals(str))
            throw new MyException("Invalid expression: not a string.");
        var buff = state.getFileTable().lookUp(value.toString());
        if (buff == null)
            throw new MyException("File not opened.");
        try {
            buff.close();
            state.getFileTable().remove(value.toString());
        } catch (Exception e) {
            throw new MyException("Error closing the file.");
        }
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        if (expression.typecheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Invalid expression CloseReadFile: not a string.");
    }

    public String toString() {
        return "CloseRFile(" + expression.toString() + ")";
    }
}
