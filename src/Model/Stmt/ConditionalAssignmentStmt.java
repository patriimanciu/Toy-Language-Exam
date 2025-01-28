package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Bool;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class ConditionalAssignmentStmt implements IStmt{
    private final String key;
    private final Exp exp1;
    private final Exp exp2;
    private final Exp exp3;


    public ConditionalAssignmentStmt(String key, Exp exp1, Exp exp2, Exp exp3) {
        this.key = key;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDic<String, Value> symbolTable = state.getSymTable();
        if (symbolTable.contains(key)) {
            Value value1 = exp1.eval(symbolTable, state.getMyHeapTable());
            Value value2 = exp2.eval(symbolTable, state.getMyHeapTable());
            Value value3 = exp3.eval(symbolTable, state.getMyHeapTable());
            Type typeId = (symbolTable.lookUp(key)).getType();
            if (value2.getType().equals(typeId) && value3.getType().equals(typeId)) {
                if (value1.getType().equals(new Bool())) {
                    boolean condition = ((BoolValue) value1).getValue();
                    if (condition)
                        symbolTable.update(key, value2);
                    else
                        symbolTable.update(key, value3);
                } else {
                    throw new MyException("The Expression of Conditional Assignment Statement has to be of bool type!");
                }
            } else {
                throw new MyException("Declared Types of variable " + key + " and Types of the assigned Expressions do not match.");
            }
        } else {
            throw new MyException("The used variable " + key + " was not declared before.");
        }
        state.setSymTable(symbolTable);
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookUp(key);
        Type typeExpr2 = exp2.typecheck(typeEnv);
        Type typeExpr3 = exp3.typecheck(typeEnv);
        if (typeVar.equals(typeExpr2) && typeVar.equals(typeExpr3)){
            Type typeExpr1 = exp1.typecheck(typeEnv);
            if (typeExpr1.equals(new Bool())) {
                return typeEnv;
            }
            else
                throw new MyException("The Expression of Conditional Assignment Statement has to be of bool type!");
        }
        else
            throw new MyException("Conditional Assignment Statement: right hand side and left hand side have different types.");
    }

    @Override
    public String toString() {
        return String.format("%s = %s ? %s : %s", key, exp1, exp2, exp3);
    }
}
