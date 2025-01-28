package Model.Stmt;

import Model.Exp.Exp;
import Model.Exp.RelationalExp;
import Model.Exp.VariableExpr;
import Model.ProgramState.PrgState;
import Model.Types.Int;
import Model.Types.Type;
import Utils.Collections.MyIDic;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;
import Utils.State.MyExeStack;

public class ForStmt implements IStmt {
    private final String variable;
    private final Exp expression1;
    private final Exp expression2;
    private final Exp expression3;
    private final IStmt statement;

    public ForStmt(String variable, Exp expression1, Exp expression2, Exp expression3, IStmt statement) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyStack<IStmt> stack = state.getExeStack();
        IStmt toWhile = new CompStmt(new AssignStmt(variable, expression1),
                new WhileStmt(new RelationalExp("<", new VariableExpr(variable), expression2),
                        new CompStmt(statement, new AssignStmt(variable, expression3))));
        stack.push(toWhile);
        state.setExeStack(stack);
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type t1 = expression1.typecheck(typeEnv);
        Type t2 = expression2.typecheck(typeEnv);
        Type t3 = expression3.typecheck(typeEnv);

        if (t1 == null || t2 == null || t3 == null) {
            throw new MyException("One of the expressions returned null during typecheck: t1=" + t1 + ", t2=" + t2 + ", t3=" + t3);
        }

        if (t1.equals(new Int()) && t2.equals(new Int()) && t3.equals(new Int())) {
            return typeEnv;
        } else {
            throw new MyException("For statement typecheck failed: t1=" + t1 + ", t2=" + t2 + ", t3=" + t3);
        }
    }

    @Override
    public String toString() {
        return "for(" + variable + "=" + expression1 + ";" + expression2 + ";" + expression3 + "){" + statement + "}";
    }
}
