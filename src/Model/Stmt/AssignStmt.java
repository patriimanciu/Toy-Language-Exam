package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class AssignStmt implements IStmt{
    String variableName;
    Exp expression;

    public AssignStmt(String variableName, Exp expression){
        this.variableName = variableName;
        this.expression = expression;
    }

    public String toString(){
        return variableName + " = " + expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyStack stack = state.getExeStack();
        MyIDic<String, Value> symTable = state.getSymTable();
        IHeap<Value> heap = state.getMyHeapTable();
        if (symTable.contains(variableName)){
            Value var = expression.eval(symTable, heap);
            Type typeId = symTable.lookUp(variableName).getType();
            if (var.getType().equals(typeId)){
                symTable.update(variableName, var);
            }
            else
                throw new MyException("Variable '" + variableName + "' is not assignable to type '" + typeId + "'");
        }
        else
            throw new MyException("Variable '" + variableName + "' not found");
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookUp(variableName);
        Type typeEx = expression.typecheck(typeEnv);
        if (typeVar.equals(typeEx))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types ");
    }
}
