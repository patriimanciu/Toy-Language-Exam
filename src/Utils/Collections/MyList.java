package Utils.Collections;

import Utils.Exceptions.MyException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    private final List<T> list;
    public MyList() {
        this.list = new ArrayList<>();
    }

    public T get(int index) throws MyException {
        if (index < 0 || index >= list.size())
            throw new MyException("Index out of bounds!");
        try {
            return list.get(index);
        } catch (Exception exception) {
            throw new MyException(exception.getMessage());
        }
    }

    public int size() {
        return list.size();
    }

    @Override
    public void add(T elem) {
        this.list.add(elem);
    }

    @Override
    public T pop() throws MyException {
        if (list.isEmpty())
            throw new MyException("The list is empty!");
        return this.list.remove(0);
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        return this.list.toString();
    }
}
