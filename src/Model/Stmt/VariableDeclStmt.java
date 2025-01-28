package Model.Stmt;

import Model.ProgramState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class VariableDeclStmt implements IStmt{
    String name;
    Type type;

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public VariableDeclStmt(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return type.toString() + " " + name;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDic<String, Value> symTable = state.getSymTable();
        if (symTable.contains(name))
            throw new MyException("Variable " + name + " already exists");
        symTable.put(name, type.defaultValue());
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
