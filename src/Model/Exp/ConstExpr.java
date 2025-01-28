package Model.Exp;

import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class ConstExpr implements Exp {
    Value value;

    public ConstExpr(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        return value;
    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        return value.getType();
    }
}
