package Model.Values;

import Model.Types.Int;
import Model.Types.Type;

public class IntValue implements Value {
    int value;
    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Type getType() {
        return new Int();
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof IntValue;
    }
}
