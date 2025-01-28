package Model.Exp;

import Model.Types.Bool;
import Model.Types.Int;
import Model.Types.Type;
import Model.Values.BoolValue;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

import java.util.Objects;

public class RelationalExp implements Exp{
    private Exp leftExp;
    private Exp rightExp;
    private int operator; // 1 - <, 2 - <=, 3 - ==, 4 - !=, 5 - >, 6 - >=

    private String operatorString(int op) {
        if (op == 1)
            return "<";
        if (op == 2)
            return "<=";
        if (op == 3)
            return "==";
        if (op == 4)
            return "!=";
        if (op == 5)
            return ">";
        if (op == 6)
            return ">=";
        return "";
    }

    public RelationalExp(String c, Exp leftExp, Exp rightExp) {
        this.leftExp = leftExp;
        this.rightExp = rightExp;
        if (Objects.equals(c, "<"))
            operator = 1;
        if (Objects.equals(c, "<="))
            operator = 2;
        if (Objects.equals(c, "=="))
            operator = 3;
        if (Objects.equals(c, "!="))
            operator = 4;
        if (Objects.equals(c, ">"))
            operator = 5;
        if (Objects.equals(c, ">="))
            operator = 6;
    }
    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        Value leftVal = leftExp.eval(tbl, heap);
        Value rightVal = rightExp.eval(tbl, heap);

        if (leftVal instanceof IntValue && rightVal instanceof IntValue) {
            IntValue leftIntVal = (IntValue) leftVal;
            IntValue rightIntVal = (IntValue) rightVal;
            int n1 = leftIntVal.getValue();
            int n2 = rightIntVal.getValue();

            switch (operator) {
                case 1:
                    return new BoolValue(n1 - n2 < 0);
                case 2:
                    return new BoolValue(n1 - n2 <= 0);
                case 3:
                    return new BoolValue(n1 - n2 == 0);
                case 4:
                    return new BoolValue(n1 - n2 != 0);
                case 5:
                    return new BoolValue(n1 - n2 > 0);
                case 6:
                    return new BoolValue(n1 - n2 >= 0);
                default:
                    throw new MyException("Invalid operator");
            }
        }
        throw new MyException("Invalid expression. Operands are not of type IntValue.");
    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        Type t1, t2;
        t1 = leftExp.typecheck(typeTbl);
        t2 = rightExp.typecheck(typeTbl);
        if (t1.equals(new Int())) {
            if (t2.equals(new Int()))
                return new Bool();
            else
                throw new MyException("Error: Second operand must be Int");
        }
        else
            throw new MyException("Error: First operand must be Int");
    }

    public String toString() {
        return leftExp.toString() + " " + operatorString(operator) + " " + rightExp.toString();
    }
}
