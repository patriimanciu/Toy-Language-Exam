package Model.Exp;

import Model.Types.Type;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public interface Exp {
    Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException;
    Type typecheck(MyIDic<String, Type> typeTbl) throws MyException;
}
