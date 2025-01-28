package Model.Exp;

import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class ValueExp implements Exp {
    private Value expr;

    public ValueExp(Value expr) {
        this.expr = expr;
    }

    public String toString() {
        return expr.toString();
    }

    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        return expr;
    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        return expr.getType();
    }
}
