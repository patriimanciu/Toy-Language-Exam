package Utils.Collections;

import Utils.Exceptions.MyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDic<K, V> implements MyIDic<K, V>{
    private Map<K, V> dic;
    public MyDic() {
        dic = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        dic.put(key, value);
    }

    @Override
    public V lookUp(K key) {
        return dic.get(key);
    }

    @Override
    public boolean contains(Object key) {
        return dic.containsKey(key);
    }

    @Override
    public void update(K key, V value) {
        dic.put(key, value);
    }

    @Override
    public void remove(K key) {
        dic.remove(key);
    }

    @Override
    public List<K> getKeys() {
        return new ArrayList<>(dic.keySet());
    }

    @Override
    public Map<K, V> getMap() {
        return dic;
    }

    @Override
    public MyIDic<K, V> deepCopy() throws MyException {
        MyIDic<K, V> toReturn = new MyDic<>();
        for (K key: getKeys())
            toReturn.put(key, lookUp(key));
        return toReturn;
    }

    public String toString() {
        return dic.toString();
    }
}
