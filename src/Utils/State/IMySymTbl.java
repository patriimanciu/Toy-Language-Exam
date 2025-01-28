package Utils.State;

import Model.Values.Value;

public interface IMySymTbl {
    void put(String s, Value v);
    Value lookUp(String key);
    boolean contains(String key);
    void update(String key, Value value);
    void remove(String key);
    Iterable<String> getKeys();
}
