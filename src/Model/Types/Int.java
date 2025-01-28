package Model.Types;

import Model.Values.IntValue;
import Model.Values.Value;

public class Int implements Type{
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Int;
    }

    public String toString(){
        return "int";
    }

    @Override
    public Value defaultValue() {
        return new IntValue(0);
    }
}
