package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.Vector;

public class NewSemaphore implements IStmt{
    private String variableName;
    private Exp expression1;
    private Exp expression2;

    public NewSemaphore(String variableName, Exp expression1, Exp expression2) {
        this.variableName = variableName;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        try {
            Type varType = typeEnv.lookUp(variableName);
            if (varType == null)
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            if (!varType.equals(new Int()))
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));
            if (!expression1.typecheck(typeEnv).equals(new Int()))
                throw new MyException(String.format("Expression '%s' should evaluate to an integer!", expression1.toString()));
            if (!expression2.typecheck(typeEnv).equals(new Int()))
                throw new MyException(String.format("Expression '%s' should evaluate to an integer!", expression2.toString()));
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        try {
            Value expression1Value = expression1.eval(state.getSymTable(), state.getMyHeapTable());
            Value expression2Value = expression2.eval(state.getSymTable(), state.getMyHeapTable());

            if (!expression1Value.getType().equals(new Int()))
                throw new MyException(String.format("Expression '%s' should evaluate to an integer!", expression1.toString()));
            if (!expression2Value.getType().equals(new Int()))
                throw new MyException(String.format("Expression '%s' should evaluate to an integer!", expression2.toString()));

            int value1 = ((IntValue) expression1Value).getValue();
            int value2 = ((IntValue) expression2Value).getValue();

            int newLocation = state.getSemaphoreTable().getFirstFreeLocation();
            state.getSemaphoreTable().put(newLocation, new Pair<>(value1, new Pair<>(new Vector<>(), value2)));
            PrgState.semaphoreLock.lock();

            Value variableValue = state.getSymTable().lookUp(variableName);
            if (variableValue == null) {
                PrgState.semaphoreLock.unlock();
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            }
            if (!variableValue.getType().equals(new Int())) {
                PrgState.semaphoreLock.unlock();
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));
            }
            state.getSymTable().put(variableName, new IntValue(newLocation));
            PrgState.semaphoreLock.unlock();
            return null;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("newSemaphore(%s, %s, %s)", variableName, expression1.toString(), expression2.toString());
    }
}
