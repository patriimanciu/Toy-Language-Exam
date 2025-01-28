package Model.Stmt;

import Model.Exp.Exp;
import Model.Exp.RelationalExp;
import Model.ProgramState.PrgState;
import Model.Types.Type;
import Utils.Collections.MyIDic;
import Utils.Collections.MyIStack;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;

public class SwitchStmt implements IStmt{
    Exp exp;
    Exp  exp1;
    Exp  exp2;
    IStmt stmt;
    IStmt stmt1;
    IStmt stmt2;

    public SwitchStmt(Exp  exp, Exp  exp1, Exp  exp2, IStmt stmt, IStmt stmt1, IStmt stmt2) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt = stmt;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyStack exeStack = state.getExeStack();
        IStmt newStmt =
                new IfStmt(
                        new RelationalExp("==", exp, exp1),
                        stmt,
                        new IfStmt(
                                new RelationalExp ("==", exp, exp2),
                                stmt1,
                                stmt2));
        exeStack.push(newStmt);
        state.setExeStack(exeStack);
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        Type typexp1 = exp1.typecheck(typeEnv);
        Type typexp2 = exp2.typecheck(typeEnv);

        if (typexp.equals(typexp1) && typexp2.equals(typexp1)) {

            stmt.typecheck(typeEnv.deepCopy());
            stmt1.typecheck(typeEnv.deepCopy());
            stmt2.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The Exp s of Switch IStmt were not of the same type!");
    }

    public String toString() {
        return "\n(switch(" + exp.toString() + ")\n (case(" + exp1.toString()
                + "): " + stmt.toString() + ")\n(case (" + exp2.toString() + "): " +
                stmt1.toString() + ")\n (default: " + stmt2.toString() + "));\n";
    }
}
