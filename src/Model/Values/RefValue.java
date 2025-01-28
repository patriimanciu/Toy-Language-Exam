package Model.Values;

import Model.Types.RefType;
import Model.Types.Type;

public class RefValue implements Value {
    int addr;
    Type locationType;
    public RefValue(int addr, Type locationType) {
        this.addr = addr;
        this.locationType = locationType;
    }

    public int getAddr() {
        return addr;
    }
    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    public Type getLocationType() {
        return locationType;
    }

    public String toString() {
        return "(" + addr + ", " + locationType + ")";
    }
}
