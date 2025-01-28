package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Bool;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class IfStmt implements IStmt{
    Exp expression;
    IStmt thenStmt;
    IStmt elseStmt;

    public IfStmt(Exp expression, IStmt thenStmt, IStmt elseStmt){
        this.expression = expression;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public Exp getExpression() {
        return expression;
    }

    public IStmt getThenStmt() {
        return thenStmt;
    }

    public IStmt getElseStmt() {
        return elseStmt;
    }

    public String toString(){
        return "(IF("+ expression.toString()+") THEN(" +thenStmt.toString() +")ELSE("+elseStmt.toString()+"))";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyStack stack = state.getExeStack();
        MyIDic<String, Value> symTable = state.getSymTable();
        IHeap<Value> heap = state.getMyHeapTable();
        Value condition = expression.eval(symTable, heap);
        if (!condition.getType().equals(new Bool()))
            throw new MyException("Condition is not boolean");
        BoolValue boolCondition = (BoolValue) condition;
        if (boolCondition.getValue())
            stack.push(thenStmt);
        else
            stack.push(elseStmt);
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeExpr.equals(new Bool())) {
            thenStmt.typecheck(typeEnv.deepCopy());
            elseStmt.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new MyException("Condition is not boolean");
    }
}
