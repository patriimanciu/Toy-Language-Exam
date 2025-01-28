package Model.Exp;

import Model.Types.Bool;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class LogicalExp implements Exp{
    private Exp first;
    private Exp second;
    int operator; // 1-and, 2-or, 3-not

    public Exp getFirstExp() {
        return first;
    }

    public Exp getSecondExp() {
        return second;
    }

    public int getOperator() {
        return operator;
    }

    public String getStringOperator() {
        return switch (operator) {
            case 1 -> "&&";
            case 2 -> "||";
            case 3 -> "^";
            default -> "";
        };
    }

    public String toString() {
        return first.toString() + " " + getStringOperator() + " " + second.toString();
    }

    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        Value firstVal, secondVal;
        firstVal = first.eval(tbl, heap);
        if (operator == 3) {
            if (firstVal.getType().equals(new Bool())) {
                BoolValue boolVal = (BoolValue) firstVal;
                return new BoolValue(boolVal.getValue());
            }
            else
                throw new MyException("Operand must be bool");
        }
        secondVal = second.eval(tbl, heap);
        if (secondVal.getType().equals(new Bool()) && firstVal.getType().equals(new Bool())) {
            BoolValue boolVal2 = (BoolValue) secondVal;
            BoolValue boolVal1 = (BoolValue) firstVal;
            if (operator == 1)
                return new BoolValue(boolVal1.getValue() && boolVal2.getValue());
            if (operator == 2)
                return new BoolValue(boolVal2.getValue() || boolVal1.getValue());
        }
        throw new MyException("Operands must be bool");
    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        Type t1, t2;
        t1 = first.typecheck(typeTbl);
        t2 = second.typecheck(typeTbl);
        if (t1.equals(new Bool())) {
            if (t2.equals(new Bool()))
                return new Bool();
            else
                throw new MyException("Error: Second operand must be bool");
        }
        else
            throw new MyException("Error: First operand must be bool");
    }
}
