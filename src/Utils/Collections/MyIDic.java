package Utils.Collections;

import Utils.Exceptions.MyException;

import java.util.List;
import java.util.Map;

public interface MyIDic<K,V> {
    void put(K key, V value);
    V lookUp(K key);
    boolean contains(K key);
    void update(K key, V value);
    void remove(K key);
    List<K> getKeys();
    Map<K,V> getMap();
    MyIDic<K, V> deepCopy() throws MyException;
}