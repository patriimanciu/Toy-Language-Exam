package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyDic;
import Utils.Collections.MyIDic;
import Utils.Collections.MyList;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;
import java.util.Vector;

public class FunctionCallStmt implements IStmt{
    private final String functionName;
    private final MyList<Exp> parameters;

    public FunctionCallStmt(String functionName, List<Exp> parameters) {
        this.functionName = functionName;
        this.parameters = new MyList<Exp>();

        for (Exp parameter : parameters) {
            this.parameters.add(parameter);
        }
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        try {
            Pair<List<String>, IStmt> functionEntry = state.getProcedureTable().lookUp(functionName);
            if (functionEntry == null)
                throw new MyException(String.format("Function '%s' does not exist!", functionName));

            List<String> paramNames = functionEntry.getKey();
            IStmt functionBody = functionEntry.getValue();

            List<Value> paramValues = new Vector<Value>();
            for (int i = 0; i < parameters.size(); ++i)
                paramValues.add(parameters.get(i).eval(state.getSymTable(), state.getMyHeapTable()));

            MyIDic<String, Value> newSymbolsTable = new MyDic<>();
            int size = paramNames.size();
            for (int i = 0; i < size; ++i)
                newSymbolsTable.put(paramNames.get(i), paramValues.get(i));

            state.getAllSymTables().push(newSymbolsTable);
            state.getExeStack().push(new FunctionReturnStmt());
            state.getExeStack().push(functionBody);
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("call " + functionName + "(");
        for (int i = 0; i < parameters.size() - 1; ++i) {
            try {
                result.append(parameters.get(i).toString()).append(", ");
            } catch (MyException e) {
                return null;
            }
        }

        if (!parameters.isEmpty()) {
            try {
                result.append(parameters.get(parameters.size() - 1).toString());
                result.append(")");
            } catch (MyException e) {
                return null;
            }
        }

        return result.toString();
    }
}
