package Model.Values;

import Model.Types.StringType;
import Model.Types.Type;

public class StringValue implements Value {
    private String value;
    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    public String toString() {
        return value;
    }

    public boolean equals(Object another) {
        return another instanceof StringValue;
    }
}
