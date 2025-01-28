package Utils.Collections;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    private final List<T> list;
    public MyList() {
        list = new ArrayList<>();
    }
    @Override
    public void add(T o) {
        list.add(o);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T[] toList() {
        return (T[]) list.toArray();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    public String toString() {
        return list.toString();
    }
}
