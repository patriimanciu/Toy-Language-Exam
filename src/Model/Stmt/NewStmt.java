package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class NewStmt implements IStmt {
    String name;
    Exp exp;

    public NewStmt(String name, Exp expression) {
        this.name = name;
        this.exp = expression;
    }

    @Override
    public PrgState execute(PrgState prg) throws MyException {
        Value v = prg.getSymTable().lookUp(name);
        if (v == null)
            throw new MyException("Variable not found");
        if (v instanceof RefValue refVal) {
            Value value= exp.eval(prg.getSymTable(), prg.getMyHeapTable());
            if (value.getType().equals(refVal.getLocationType())) {
                int address= prg.getMyHeapTable().put(value);
                prg.getSymTable().put(name, new RefValue(address, value.getType()));
                return null;
            }
            else
                throw new MyException("Value does not match the inner type");
        }
        else
            throw new MyException("Value is not a RefValue");
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type typeExp = exp.typecheck(typeEnv);
        Type typeVar = typeEnv.lookUp(name);
        if (typeVar.equals(new RefType(typeExp)))
            return typeEnv;
        else
            throw new MyException("Error: NEW Stmt right hand side and left hand side have different types");
    }

    @Override
    public String toString() {
        return "new("+ name+","+exp+")";
    }

}