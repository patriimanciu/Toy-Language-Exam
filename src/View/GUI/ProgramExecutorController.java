package View.GUI;

import Controller.Controller;
import Model.ProgramState.PrgState;
import Model.Values.Value;
import Utils.Collections.MyIDic;
import Utils.Exceptions.MyException;
import Utils.State.IHeap;
import Utils.State.ISemaphoreTable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}

public class ProgramExecutorController {
    private Controller controller;

    @FXML
    private TextField numberOfProgramStatesTextField;

    @FXML
    private TableView<Pair<Integer, Value>> heapTableView;

    @FXML
    private TableColumn<Pair<Integer, Value>, Integer> addressColumn;

    @FXML
    private TableColumn<Pair<Integer, Value>, String> valueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> programStateIdentifiersListView;

    @FXML
    private TableView<Pair<String, Value>> symbolTableView;

    @FXML
    private TableColumn<Pair<String, Value>, String> variableNameColumn;

    @FXML
    private TableColumn<Pair<String, Value>, String> variableValueColumn;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private TableView<Pair<Integer, Pair<Integer, List<Integer>>>> semaphoreTableView;

    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Integer> semaphoreIndexColumn;

    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Integer> semaphoreValueColumn;

    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, String> semaphoreListColumn;

    @FXML
    private Button runOneStepButton;

    public void setController(Controller controller) {
        this.controller = controller;
        populate();
    }

    @FXML
    public void initialize() {
        programStateIdentifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        semaphoreIndexColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        semaphoreValueColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().second.first).asObject());
        semaphoreListColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.second.toString()));
    }

    private PrgState getCurrentProgramState() {
        if (controller.getProgramStates().isEmpty())
            return null;
        else {
            int currentId = programStateIdentifiersListView.getSelectionModel().getSelectedIndex();
            if (currentId == -1)
                return controller.getProgramStates().getFirst();
            else
                return controller.getProgramStates().get(currentId);
        }
    }

    private void populate() {
        populateHeapTableView();
        populateOutputListView();
        populateFileTableListView();
        populateProgramStateIdentifiersListView();
        populateSymbolTableView();
        populateExecutionStackListView();
        populateSemaphoreTableView();
    }

    @FXML
    private void changeProgramState(MouseEvent event) {
        populateExecutionStackListView();
        populateSymbolTableView();
    }

    private void populateNumberOfProgramStatesTextField() {
        List<PrgState> programStates = controller.getProgramStates();
        numberOfProgramStatesTextField.setText(String.valueOf(programStates.size()));
    }

    private void populateHeapTableView() {
        PrgState programState = getCurrentProgramState();
        IHeap<Value> heap = Objects.requireNonNull(programState).getMyHeapTable();
        ArrayList<Pair<Integer, Value>> heapEntries = new ArrayList<>();
        for(Map.Entry<Integer, Value> entry: heap.getContent().entrySet()) {
            heapEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateSemaphoreTableView() {
        PrgState programState = getCurrentProgramState();
        ISemaphoreTable semaphoreTable = Objects.requireNonNull(programState).getSemaphoreTable();
        ArrayList<Pair<Integer, Pair<Integer, List<Integer>>>> semaphoreTableEntries = new ArrayList<>();

        for (Map.Entry<Integer, javafx.util.Pair<Integer, javafx.util.Pair<List<Integer>, Integer>>> entry : semaphoreTable.getMap().entrySet()) {
            Integer key = entry.getKey();
            Integer count = entry.getValue().getKey(); // First Integer (count)
            List<Integer> waitingList = entry.getValue().getValue().getKey(); // Extract List<Integer>

            semaphoreTableEntries.add(new Pair<>(key, new Pair<>(count, waitingList)));
        }
        semaphoreTableView.setItems(FXCollections.observableArrayList(semaphoreTableEntries));
    }

    private void populateOutputListView() {
        PrgState programState = getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<Value> outputList = Objects.requireNonNull(programState).getOut().getList();
        int index;
        for (index = 0; index < outputList.size(); index++){
            output.add(outputList.get(index).toString());
        }
        outputListView.setItems(FXCollections.observableArrayList(output));
    }

    private void populateFileTableListView() {
        PrgState programState = getCurrentProgramState();
        List<String> files = new ArrayList<>(Objects.requireNonNull(programState).getFileTable().getMap().keySet());
        fileTableListView.setItems(FXCollections.observableList(files));
    }

    private void populateProgramStateIdentifiersListView() {
        List<PrgState> programStates = controller.getProgramStates();
        List<Integer> idList = programStates.stream().map(PrgState::getID).collect(Collectors.toList());
        programStateIdentifiersListView.setItems(FXCollections.observableList(idList));
        populateNumberOfProgramStatesTextField();
    }

    private void populateSymbolTableView() {
        PrgState programState = getCurrentProgramState();
        MyIDic<String, Value> symbolTable = Objects.requireNonNull(programState).getSymTable();
        ArrayList<Pair<String, Value>> symbolTableEntries = new ArrayList<>();
        for (Map.Entry<String, Value> entry: symbolTable.getMap().entrySet()) {
            symbolTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        symbolTableView.setItems(FXCollections.observableArrayList(symbolTableEntries));
    }

    private void populateExecutionStackListView() {
        PrgState programState = getCurrentProgramState();
        List<String> executionStackToString = new ArrayList<>();
        if (programState != null)
            for (Object statement: programState.getExeStack().getReversed()) {
                executionStackToString.add(statement.toString());
            }
        executionStackListView.setItems(FXCollections.observableList(executionStackToString));
    }

    @FXML
    private void runOneStep(MouseEvent mouseEvent) {
        if (controller != null) {
            try {
                List<PrgState> programStates = Objects.requireNonNull(controller.getProgramStates());
                if (!programStates.isEmpty()) {
                    controller.oneStep();
                    populate();
                    programStates = controller.removeCompletedPrg(controller.getProgramStates());
                    controller.setProgramStates(programStates);
                    populateProgramStateIdentifiersListView();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("An error has occured!");
                    alert.setContentText("There is nothing left to execute!");
                    alert.showAndWait();
                }
            } catch (MyException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Execution error!");
                alert.setHeaderText("An execution error has occured!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("An error has occured!");
            alert.setContentText("No program selected!");
            alert.showAndWait();
        }
    }
}