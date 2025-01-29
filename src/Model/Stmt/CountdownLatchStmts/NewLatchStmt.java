package Model.Stmt.CountdownLatchStmts;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class NewLatchStmt implements IStmt {
    private String variableName;
    private Exp expression;

    public NewLatchStmt(String variableName, Exp expression){
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value expressionValue = expression.eval(state.getSymTable(), state.getMyHeapTable());
        if (!expressionValue.getType().equals(new Int()))
            throw new MyException(String.format("Expression '%s' should have evaluate to an integer", expression.toString()));

        int latch = ((IntValue) expressionValue).getValue();
        int latchLocation = state.getLatchTable().put(latch);

        Value variableValue = state.getSymTable().lookUp(variableName);
        if (variableValue == null)
            throw new MyException(String.format("Variable '%s' has not been declared", variableName));
        if (!variableValue.getType().equals(new Int()))
            throw new MyException(String.format("Variable '%s' should be of integer type", variableName));
        state.getSymTable().update(variableName, new IntValue(latchLocation));

        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type variableType = typeEnv.lookUp(variableName);
        if (variableName == null)
            throw new MyException(String.format("Variable '%s' has not been declared", variableName));
        if (!variableType.equals(new Int()))
            throw new MyException(String.format("Variable '%s' should be of integer type", variableName));

        Type expressionType = expression.typecheck(typeEnv);
        if (!expressionType.equals(new Int()))
            throw new MyException(String.format("Expression '%s' should be of integer type", expressionType));

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("newLatch(%s, %s)", variableName, expression.toString());
    }
}
