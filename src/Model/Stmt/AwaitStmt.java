package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class AwaitStmt implements IStmt {
    private String variableName;

    public AwaitStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        try {
            Type variableType = typeEnv.lookUp(variableName);
            if (variableType == null)
                throw new MyException(String.format("Variable %s has not been declared!", variableName));
            if (!variableType.equals(new Int()))
                throw new MyException(String.format("Variable %s should be of integer type!", variableName));

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        try {
            Value variableValue = state.getSymTable().lookUp(variableName);
            if (variableValue == null)
                throw new MyException(String.format("Variable %s has not been declared!", variableName));
            if (!variableValue.getType().equals(new Int()))
                throw new MyException(String.format("Variable %s should be of integer type!", variableName));

            int latchLocation = ((IntValue) variableValue).getValue();
            Integer latchValue = state.getLatchTable().get(latchLocation);

            if (latchValue == null)
                throw new MyException("Invalid latch table location!");
            if (latchValue != 0)
                state.getExeStack().push(this);

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

        return null;
    }

    @Override
    public String toString() {
        return String.format("await(%s)", variableName);
    }
}
