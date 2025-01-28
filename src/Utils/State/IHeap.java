package Utils.State;
import Utils.Exceptions.MyException;

import java.util.Map;

public interface IHeap<V> {
    int put(V v) throws MyException;
    void setContent(Map<Integer, V> map);
    Map<Integer, V> getContent();
    V lookUp(int a);
    void update(int addr, V value);
}