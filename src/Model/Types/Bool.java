package Model.Types;

import Model.Values.BoolValue;
import Model.Values.Value;

public class Bool implements Type{
    public boolean equals(Object obj) {
        return obj instanceof Bool;
    }

    public String toString() {
        return "bool";
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }
}
