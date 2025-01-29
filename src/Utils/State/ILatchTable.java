package Utils.State;

import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;

public interface ILatchTable extends MyIDic<Integer, Integer> {
    int put(Integer value) throws MyException;
    int get(int position) throws MyException;
    int getFirstFreeLocation();
}
