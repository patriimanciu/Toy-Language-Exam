package Model.Values;

import Model.Types.Bool;
import Model.Types.Type;

public class BoolValue implements Value {
    boolean value;
    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public Type getType() {
        return new Bool();
    }

    public boolean equals(Object another) {
        return another instanceof BoolValue;
    }
}
