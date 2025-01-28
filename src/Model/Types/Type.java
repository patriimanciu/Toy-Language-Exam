package Model.Types;

import Model.Values.Value;

public interface Type {
    boolean equals(Object obj);
    String toString();
    Value defaultValue();
}
