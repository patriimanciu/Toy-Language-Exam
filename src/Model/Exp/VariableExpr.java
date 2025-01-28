package Model.Exp;

import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class VariableExpr implements Exp {
    private String variableName;

    public VariableExpr(String variableName) {
        this.variableName = variableName;
    }

    public String toString() {
        return variableName;
    }

    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        return tbl.lookUp(variableName);
    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        return typeTbl.lookUp(variableName);
    }
}
