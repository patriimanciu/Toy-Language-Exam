package Utils.State;

import Model.Values.Value;

public class MySymTbl implements IMySymTbl{

    @Override
    public void put(String s, Value v) {

    }

    @Override
    public Value lookUp(String key) {
        return null;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public void update(String key, Value value) {

    }

    @Override
    public void remove(String key) {

    }

    @Override
    public Iterable<String> getKeys() {
        return null;
    }
}
