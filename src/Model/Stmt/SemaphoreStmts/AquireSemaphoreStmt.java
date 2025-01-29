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

public class AquireSemaphoreStmt implements IStmt {
    private String variableName;
    public AquireSemaphoreStmt(String variableName) {
        this.variableName = variableName;
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
    public PrgState execute(PrgState state) throws MyException {
        try {
            Value variableValue = state.getSymTable().lookUp(variableName);
            if (variableValue == null)
                throw new MyException(String.format("Variable '%s' has not been declared!", variableName));
            if (!variableValue.getType().equals(new Int()))
                throw new MyException(String.format("Variable '%s' should have integer type!", variableName));

            int semaphoreLocation = ((IntValue) variableValue).getValue();
            state.semaphoreLock.lock();

            Pair<Integer, List<Integer>> semaphore_entry = state.getSemaphoreTable().lookUp(semaphoreLocation);
            if (semaphore_entry == null) {
                PrgState.semaphoreLock.unlock();
                throw new MyException("Invalid semaphore location!");
            }

            Integer semaphore_n = semaphore_entry.getKey();
            List<Integer> acquired_programs = semaphore_entry.getValue();
            if (semaphore_n > acquired_programs.size()) {
                if (!acquired_programs.contains(state.getID()))
                    acquired_programs.add(state.getID());
            }
            else
                state.getExeStack().push(this);
            PrgState.semaphoreLock.unlock();
            return null;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("acquireSemaphore(%s)", variableName);
    }
}
