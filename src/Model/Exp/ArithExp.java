package Model.Exp;

import Model.Types.Int;
import Model.Types.Type;
import Model.Values.IntValue;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;

public class ArithExp implements Exp{
    Exp exp1;
    Exp exp2;
    int op; // 1 - plus, 2 - minus, 3 - star, 4 - divide

    public ArithExp( char c,Exp exp1, Exp exp2){
        this.exp1 = exp1;
        this.exp2 = exp2;
        if (c == '+')
            op = 1;
        if (c == '-')
            op = 2;
        if (c == '*')
            op = 3;
        if (c == '/')
            op = 4;
    }

    public Exp getExp1() {
        return exp1;
    }

    public Exp getExp2() {
        return exp2;
    }

    public int getOp() {
        return op;
    }

    public String getStringOp() {
        return switch (op) {
            case 1 -> "+";
            case 2 -> "-";
            case 3 -> "*";
            case 4 -> "/";
            default -> "";
        };
    }

    public String toString() {
        return exp1.toString() + " " + getStringOp() + " " + exp2.toString();
    }

    @Override
    public Value eval(MyIDic<String, Value> tbl, IHeap<Value> heap) throws MyException {
        Value v1, v2;
        v1 = exp1.eval(tbl, heap);
        if (v1.getType().equals(new Int())) {
            v2 = exp2.eval(tbl, heap);
            if (v2.getType().equals(new Int())) {
                IntValue iv1 = (IntValue) v1;
                IntValue iv2 = (IntValue) v2;
                int n1, n2;
                n1 = iv1.getValue();
                n2 = iv2.getValue();
                if (op == 1)
                    return new IntValue(n1 + n2);
                if (op == 2)
                    return new IntValue(n1 - n2);
                if (op == 3)
                    return new IntValue(n1 * n2);
                if (op == 4)
                    if (n2 != 0)
                        return new IntValue(n1 / n2);
                    else
                        throw new MyException("Invalid operation: Division by zero");

            }
            else {
                throw new MyException("Error: Second operand is not an integer");
            }
        }
        else {
            throw new MyException("Error: First operand is not an integer");
        }
        return null;
    }

    @Override
    public Type typecheck(MyIDic<String, Type> typeTbl) throws MyException {
        Type t1, t2;
        t1 = exp1.typecheck(typeTbl);
        t2 = exp2.typecheck(typeTbl);
        if (t1.equals(new Int())) {
            if (t2.equals(new Int()))
                return new Int();
            else
                throw new MyException("Error: Second operand is not an integer");
        }
        else
            throw new MyException("Error: First operand is not an integer");
    }
}
