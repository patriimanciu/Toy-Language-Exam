package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;

public class ReleaseSemaphore implements IStmt{
    private String variableName;

    public ReleaseSemaphore(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        try {
            Value variableValue = state.getSymTable().lookUp(variableName);
            if (variableValue == null)
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            if (!variableValue.getType().equals(new Int()))
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));

            int semaphoreLocation = ((IntValue) variableValue).getValue();
            PrgState.semaphoreLock.lock();
            Pair<Integer, Pair<List<Integer>, Integer>> semaphoreEntry = state.getSemaphoreTable().lookUp(semaphoreLocation);
            if (semaphoreEntry == null) {
                PrgState.semaphoreLock.unlock();
                throw new MyException("Invalid semaphore location!");
            }
            Pair<List<Integer>, Integer> acquiredPrograms = semaphoreEntry.getValue();
            Integer programId = state.getID();
            if (acquiredPrograms.getKey().contains(programId))
                acquiredPrograms.getKey().remove(acquiredPrograms.getKey().indexOf(programId));
            PrgState.semaphoreLock.unlock();
            return null;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        try {
            Type var_type = typeEnv.lookUp(variableName);
            if (var_type == null)
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            if (!var_type.equals(new Int()))
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
        return typeEnv;
    }
    
    @Override
    public String toString() {
        return String.format("releaseSemaphore(%s)", variableName);
    }
}
