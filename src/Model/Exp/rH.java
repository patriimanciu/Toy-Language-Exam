package Model.Exp;

import Model.Types.RefType;
import Model.Types.Type;
import Model.Values.RefValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class rH implements Exp{
    Exp expr;

    public rH(Exp expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "readHeap(" + expr.toString() + ")";
    }

    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        Value value = expr.eval(tbl, heap);
        if (!(value.getType() instanceof RefType))
            throw new MyException("Heap should only be accessed through references");
        return heap.getContent().get(((RefValue) value).getAddr());

    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        Type type = expr.typecheck(typeTbl);
        if (!(type instanceof RefType))
            throw new MyException("Heap should only be accessed through references");
        return ((RefType) type).getInnerType();

    }
}
