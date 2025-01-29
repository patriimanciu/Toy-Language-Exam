package Utils.State;

import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;

public interface ISemaphoreTable extends MyIDic<Integer, Pair<Integer, Pair<List<Integer>, Integer>>> {
    int put(Pair<Integer, Pair<List<Integer>, Integer>> value) throws MyException;
    int getFirstFreeLocation();
}