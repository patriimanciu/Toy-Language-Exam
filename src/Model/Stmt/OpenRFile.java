package Model.Stmt;

import Model.Exp.Exp;
import Model.ProgramState.PrgState;
import Model.Types.StringType;
import Model.Types.Type;
import Model.Values.StringValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFile implements IStmt {
    private Exp expression;
    private StringType str;
    public OpenRFile(Exp exp) {
        this.expression = exp;
        this.str = new StringType();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value value = expression.eval(state.getSymTable(), state.getMyHeapTable());
        if (value.getType().equals(str)) {
            StringValue file = (StringValue) value;
//            System.out.println(file);
//            System.out.println(file.getValue());
//            System.out.println(file.getType());
//            System.out.println("test.in".equals(file.toString()));
            if (state.getFileTable().contains(file.getValue()))
                throw new MyException("File already opened");
            try {
                String path = file.getValue();
                BufferedReader reader = new BufferedReader(new FileReader(path));
                state.getFileTable().put(file.getValue(), reader);
            } catch (IOException e) {
                throw new MyException("Error opening file: File not found.");
            }
        }
        else {
            throw new MyException("Expression is not string.");
        }
        return null;
    }

    @Override
    public MyIDic<String, Type> typecheck(MyIDic<String, Type> typeEnv) throws MyException {
        if (expression.typecheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("OpenReadFile expression is not string.");
    }

    public String toString() {
        return "OpenRFile("+expression.toString()+")";
    }
}
