package Model.Stmt.SemaphoreStmts;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.Vector;

public class NewSemaphoreStmt implements IStmt {
    private String variableName;
    private Exp expression;

    public NewSemaphoreStmt(String variableName, Exp expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        try {
            Type varType = typeEnv.lookUp(variableName);
            if (varType == null)
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            if (!varType.equals(new Int()))
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));
            if (!expression.typecheck(typeEnv).equals(new Int()))
                throw new MyException(String.format("Expression '%s' should evaluate to an integer!", expression.toString()));
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        try {
            Value expressionValue = expression.eval(state.getSymTable(), state.getMyHeapTable());
            if (!expressionValue.getType().equals(new Int()))
                throw new MyException(String.format("Expression '%s' should evaluate to an integer!", expression.toString()));

            int expression_int = ((IntValue) expressionValue).getValue();
            PrgState.semaphoreLock.lock();

            int new_location = state.getSemaphoreTable().put(new Pair<>(expression_int, new Vector<>()));
            Value variable_value = state.getSymTable().lookUp(variableName);
            if (variable_value == null)
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            if (!variable_value.getType().equals(new Int()))
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));

            state.getSymTable().put(variableName, new IntValue(new_location));
            PrgState.semaphoreLock.unlock();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("newSemaphore(%s, %s)", variableName, expression.toString());
    }
}