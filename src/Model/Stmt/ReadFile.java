package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.Int;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.IntValue;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public class ReadFile implements IStmt{
    private Exp exp;
    private String varName;
    private Int i;
    private StringType str;

    public ReadFile(Exp exp, String fileName) {
        this.exp = exp;
        this.varName = fileName;
        i = new Int();
        str = new StringType();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
       var value = exp.eval(state.getSymTable(), state.getMyHeapTable());
       if (!state.getSymTable().contains(varName) || !state.getSymTable().lookUp(varName).getType().equals(i)) {
           throw new MyException("The var is not defines or it doesn't match the type.");
       }
       var buffer = state.getFileTable().lookUp(value.toString());
       try {
           String line = buffer.readLine();
           if (line == null) {
               state.getSymTable().update(varName, new Int().defaultValue());
           } else {
               state.getSymTable().update(varName, new IntValue(Integer.parseInt(line)));
           }
       } catch (Exception e) {
           throw new MyException("Error reading from file.");
       }
       return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        if (exp.typecheck(typeEnv).equals(new StringType()))
            if (typeEnv.lookUp(varName).equals(new Int()))
                return typeEnv;
            else
                throw new MyException("ReadFile requires an int as its variable parameter.");
        else
            throw new MyException("ReadFile requires a string as es Expressions parameter.");

    }

    public String toString() {
        return "ReadFile(" + exp.toString() + ", " + varName + ")";
    }
}
