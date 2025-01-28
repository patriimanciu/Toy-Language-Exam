package Model.Types;

import Model.Values.RefValue;
import Model.Values.Value;

public class RefType implements Type{
    Type innerType;
    public RefType(Type innerType){
        this.innerType = innerType;
    }

    public Type getInnerType() {
        return innerType;
    }
    public boolean equals(Object another) {
        if (another instanceof RefType) {
            return innerType.equals(((RefType) another).getInnerType());
        }
        else
            return false;
    }
    public String toString() {
        return "Ref(" + innerType.toString() + ")";
    }
    @Override
    public Value defaultValue() {
        return new RefValue(0, innerType);
    }
}
