package Utils.Collections;

import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;

public class MySemaphoreTable extends  MyDic<Integer, Pair<Integer, List<Integer>>> implements MyISemaphoreTable {
    private int nextFreeLocation;

    public MySemaphoreTable() {
        super();
        this.nextFreeLocation = 1;
    }
    public void put(Integer key, Pair<Integer, List<Integer>> value) throws MyException {
        if (!key.equals(nextFreeLocation))
            throw new MyException("Invalid semaphore table location!");
        super.put(key, value);
        synchronized (this) {
            nextFreeLocation++;
        }
    }

    @Override
    public int put(Pair<Integer, List<Integer>> value) throws MyException {
        super.put(nextFreeLocation, value);
        synchronized (this) {
            nextFreeLocation++;
        }
        return nextFreeLocation - 1;
    }

    @Override
    public int getFirstFreeLocation() {
        int locationAddress = 1;
        while (this.getMap().get(locationAddress) != null)
            locationAddress++;
        return locationAddress;
    }
}
