package Model.ProgramState;

import Model.Stmt.CompStmt;
import Model.Stmt.IStmt;
import Model.Stmt.NopStmt;
import Model.Values.Value;
import Utils.Collections.*;
import Utils.Exceptions.MyException;
import Utils.State.*;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrgState {
    private int ID;
    private static int lastID = 0;
    private MyStack exeStack;
    public MyStack getExeStack() {
        return exeStack;
    }
    public void setExeStack(MyStack exeStack) {
        this.exeStack = exeStack;
    }

    private MyIDic<String, Value> symTable;
    public MyIDic<String, Value> getSymTable() {
        return (MyIDic<String, Value>) symTable;
    }
    public void setSymTable(MyIDic<String, Value> symTable) {
        this.symTable = symTable;
    }

    private MyList<Value> out;
    public MyList<Value> getOut() {
        return out;
    }
    public void setOut(MyList<Value> out) {
        this.out = out;
    }


    public IHeap<Value> myHeapTable;
    public IHeap<Value> getMyHeapTable() {
        return myHeapTable;
    }
    public void setMyHeapTable(IHeap<Value> myHeapTable) {
        this.myHeapTable = myHeapTable;
    }

    public ILockTable lockTable;
    public ILockTable getLockTable() {
        return lockTable;
    }
    public void setLockTable(ILockTable lockTable) {
        this.lockTable = lockTable;
    }
//    public String lockTableToString() throws MyException {
//        StringBuilder lockTableStringBuilder = new StringBuilder();
//        for (int key: lockTable.keySet()) {
//            lockTableStringBuilder.append(String.format("%d -> %d\n", key, lockTable.get(key)));
//        }
//        return lockTableStringBuilder.toString();
//    }

    private ISemaphoreTable semaphoreTable;
    static public Lock semaphoreLock = new ReentrantLock();
    public ISemaphoreTable getSemaphoreTable() {
        return semaphoreTable;
    }
    public void setSemaphoreTable(ISemaphoreTable semaphoreTable) {
        this.semaphoreTable = semaphoreTable;
    }

    IStmt originalProgram;
    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty())
            throw new MyException("PrgState stack is empty.");
        IStmt curr = (IStmt) exeStack.pop();
        if (curr instanceof NopStmt)
            return this;
        return curr.execute(this);
    }

    public synchronized int setID () {
        lastID++;
        return lastID;
    }

    public int getID() {
        return ID;
    }

    private MyIDic<String, BufferedReader> fileTable;
    public PrgState(MyStack stk, MyIDic<String, Value> symtbl, MyIDic<String, BufferedReader> filetbl,
                    MyHeap<Value> heapTable, MyLockTable myLockTable, SemaphoreTable mySemaphoreTbl, MyList<Value> ot, IStmt prg) {
        exeStack = stk;
        symTable = symtbl;
        fileTable = filetbl;
        myHeapTable = heapTable;
        lockTable = myLockTable;
        semaphoreTable = mySemaphoreTbl;
        out = ot;
        originalProgram = prg;
        stk.push(prg);
        ID = setID();
    }

    public String toString() {
        return "----------------------------------------------- \n ID: " + ID + "\n ExeStack: " + distinctStatamentsString() +
                "\n SymTable: " + symTable.toString() +
                "\n Out: " + out.toString() +
                "\n FileTable: " + getFileTableList() +
                "\n Heap: " + getMyHeapTable() +
                "\n Lock Table:\n" + lockTable.toString() +
                "\n Semaphore Table:\n" + semaphoreTable.toString() +
                "\n----------------------------------------------- \n";
    }

    public MyIDic<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public String getFileTableList(){
        // return a string with the list of files from the file table, each on a new line
        StringBuilder result = new StringBuilder();
        for (String key : fileTable.getKeys()) {
            result.append(key.toString()).append("\n");
        }
        return result.toString();
    }

    public String getSymTableList(){
        // return a string with the list of variables from the symbol table, each on a new line
        StringBuilder result = new StringBuilder();
        for (String key : symTable.getKeys()) {
            result.append(key).append(symTable.lookUp(key)).append("\n");
        }
        return result.toString();
    }

    public String getOutList(){
        // return a string with the list of values from the output list, each on a new line
        StringBuilder result = new StringBuilder();
        for (Value value : out.toList()) {
            result.append(value.toString()).append("\n");
        }
        return result.toString();
    }

    public String semaphoreTableToString() throws MyException {
        StringBuilder semaphoreTableStringBuilder = new StringBuilder();
        for (int key: semaphoreTable.getKeys()) {
            semaphoreTableStringBuilder.append(String.format("%d -> %d; %s; %d\n", key, semaphoreTable.lookUp(key).getKey(), semaphoreTable.lookUp(key).getValue().getKey().toString(), semaphoreTable.lookUp(key).getValue().getValue()));
        }
        return semaphoreTableStringBuilder.toString();
    }

    private Node<IStmt> toTree(IStmt stmt) {
        Node node;
        if (stmt instanceof CompStmt){
            CompStmt comptStmt = (CompStmt) stmt;
            node = new Node<>(new NopStmt());
            node.setLeft(new Node<>(comptStmt.getFirstStmt()));
            node.setRight(toTree( comptStmt.getSecondStmt()));
        }
        else {
            node = new Node<>(stmt);
        }
        return node;

    }
    public List<IStmt> distinctStataments() {
        MyTree<IStmt> tree =  new MyTree<IStmt>();
        List<IStmt> inOrderList=new LinkedList<IStmt>();
        if(!getExeStack().toListS().isEmpty()) {
            IStmt stmt = (IStmt) getExeStack().toListS().getFirst();
            tree.setRoot(toTree(stmt));
            tree.inorderTraversal(inOrderList, tree.getRoot());
        }
        return inOrderList;
    }

    public String distinctStatamentsString() {
        List<IStmt> inOrderList = distinctStataments();
        StringBuilder str = new StringBuilder();
        for (IStmt stmt : inOrderList) {
            if(!Objects.equals(stmt.toString(),";")) {
                if (!Objects.equals(stmt.toString(), "")) {
                    str.append(stmt.toString());
                    str.append("\n");
                }
            }
        }
        return str.toString();
    }


}
