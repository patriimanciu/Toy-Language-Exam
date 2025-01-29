package Utils.Collections;

import Utils.Exceptions.MyException;
import javafx.util.Pair;
import java.util.List;

public interface MyISemaphoreTable extends MyIDic<Integer, Pair<Integer, List<Integer>>> {
    int put(Pair<Integer, List<Integer>> value) throws MyException;
    int getFirstFreeLocation();
}
