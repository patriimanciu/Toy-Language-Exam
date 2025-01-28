package Utils.Collections;

import java.util.List;

public interface MyIList<T> {
    void add(T t);
    void clear();
    T[] toList();

    List<T> getList();
}
