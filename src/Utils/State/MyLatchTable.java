package Utils.State;

import Utils.Collections.MyDic;
import Utils.Exceptions.MyException;

public class MyLatchTable extends MyDic<Integer, Integer> implements ILatchTable{
    private int nextFreeLocation;

    public MyLatchTable() {
        super();
        this.nextFreeLocation = 1;
    }

    @Override
    public void put(Integer key, Integer value) throws MyException {
        if (!key.equals(nextFreeLocation))
            throw new MyException("Invalid lock table location!");
        super.put(key, value);
        synchronized (this) {
            nextFreeLocation++;
        }
    }

    @Override
    public int put(Integer value) throws MyException {
        super.put(nextFreeLocation, value);
        synchronized (this) {
            nextFreeLocation++;
        }
        return nextFreeLocation-1;
    }

    @Override
    public int get(int position) throws MyException {
        synchronized (this) {
            if (!this.getMap().containsKey(position))
                throw new MyException(String.format("%d is not present in the table!", position));
            return this.getMap().get(position);
        }
    }

    @Override
    public int getFirstFreeLocation(){
        int locationAddress = 1;
        while (this.getMap().get(locationAddress) != null)
            locationAddress++;
        return locationAddress;
    }
}
