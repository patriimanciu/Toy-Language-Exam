package Utils.State;

import Utils.Collections.MyDic;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;

public class SemaphoreTable extends MyDic<Integer, Pair<Integer, Pair<List<Integer>, Integer>>> implements ISemaphoreTable {
    private int nextFreeLocation;

    public SemaphoreTable() {
        super();
        this.nextFreeLocation = 1;
    }
    public void put(Integer key, Pair<Integer, Pair<List<Integer>, Integer>> value) throws MyException {
        if (!key.equals(nextFreeLocation))
            throw new MyException("Invalid semaphore table location!");
        super.put(key, value);
        synchronized (this) {
            nextFreeLocation++;
        }
    }

    @Override
    public int put(Pair<Integer, Pair<List<Integer>, Integer>> value) throws MyException {
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
