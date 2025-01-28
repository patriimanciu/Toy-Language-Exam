package Controller;

import Model.ProgramState.PrgState;
import Model.Values.RefValue;
import Model.Values.Value;
import Repository.IRepo;
import Utils.Exceptions.MyException;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Pair {
    final PrgState first;
    final MyException second;

    public Pair(PrgState first, MyException second) {
        this.first = first;
        this.second = second;
    }
}

public class Controller {
    private IRepo repository;
    private boolean flag = true;
    ExecutorService executor;
    public Controller(IRepo repository) {
        this.repository = repository;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    private void display(PrgState programState) {
        System.out.println(programState.toString());
    }

    public void oneStepForAllPrograms(List<PrgState> programStates) throws MyException, InterruptedException {
        programStates.forEach(programState -> {
            try {
                repository.logPrgStateExec(programState);
//                display(programState);
            } catch (MyException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            }
        });
        List<Callable<PrgState>> callList = programStates.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());

        List<Pair> newProgramList;
        newProgramList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return new Pair(future.get(), null);
                    } catch (ExecutionException | InterruptedException e) {
                        if (e.getCause() instanceof MyException)
                            return new Pair(null, (MyException) e.getCause());
                        System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
                        return null;
                    }
                }).filter(Objects::nonNull)
                .filter(pair -> pair.first != null || pair.second != null)
                .collect(Collectors.toList());

        for (Pair error: newProgramList)
            if (error.second != null)
                throw error.second;
        programStates.addAll(newProgramList.stream().map(pair -> pair.first).toList());

        programStates.forEach(programState -> {
            try {
                repository.logPrgStateExec(programState);
//                display(programState);
            } catch (MyException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            }
        });
        repository.setPrgList(programStates);
    }

    public void oneStep() throws MyException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> programStates = removeCompletedPrg(repository.getPrgList());
        oneStepForAllPrograms(programStates);
        conservativeGarbageCollector(programStates);
        //programStates = removeCompletedPrg(repository.getProgramList());
        executor.shutdownNow();
        //repository.setProgramStates(programStates);
    }

    public void allStep() throws MyException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> programStates = removeCompletedPrg(repository.getPrgList());
        while (!programStates.isEmpty()) {
            oneStepForAllPrograms(programStates);
            conservativeGarbageCollector(programStates);
            programStates = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        repository.setPrgList(programStates);
    }

    public void conservativeGarbageCollector(List<PrgState> programStates) {
        List<Integer> symTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddrFromSymTable(p.getSymTable().getMap().values()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());
        programStates.forEach(p -> p.getMyHeapTable().setContent((HashMap<Integer, Value>) safeGarbageCollector(symTableAddresses, getAddressesFromHeap(p.getMyHeapTable().getContent().values()), p.getMyHeapTable().getContent())));
    }


    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(p -> p.isNotCompleted()).collect(Collectors.toList());
    }

    List<Integer> getAddrFromSymTable(Collection<Value> symTableValues){
        return symTableValues.stream()
                .filter(v-> v instanceof RefValue) //gets all the refValues
                .map(v-> {RefValue v1 = (RefValue)v; return v1.getAddr();}) //each refValue gets replace by it s adress
                .collect(Collectors.toList());
    }

    public List<Integer> getAddressesFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddr();})
                .collect(Collectors.toList());
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddresses, List<Integer> heapAddresses, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> ( symTableAddresses.contains(e.getKey()) || heapAddresses.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setProgramStates(List<PrgState> programStates) {
        this.repository.setPrgList(programStates);
    }

    public List<PrgState> getProgramStates() {
        return this.repository.getPrgList();
    }
}
