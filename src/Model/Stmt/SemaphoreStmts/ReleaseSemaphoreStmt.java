package Model.Stmt.SemaphoreStmts;

import Model.ProgramState.PrgState;
import Model.Stmt.IStmt;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;

public class ReleaseSemaphoreStmt  implements IStmt {
    private String variableName;

    public ReleaseSemaphoreStmt(String variableName) {
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
            Pair<Integer, List<Integer>> semaphoreEntry = state.getSemaphoreTable().lookUp(semaphoreLocation);
            if (semaphoreEntry == null) {
                PrgState.semaphoreLock.unlock();
                throw new MyException("Invalid semaphore location!");
            }

            Integer semaphore = semaphoreEntry.getKey();
            List<Integer> acquiredPrograms = semaphoreEntry.getValue();

            Integer programId = state.getID();
            if (acquiredPrograms.contains(programId))
                acquiredPrograms.remove(programId);

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

