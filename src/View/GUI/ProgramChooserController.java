package View.GUI;

import Controller.Controller;
import Model.Exp.*;
import Model.ProgramState.PrgState;
import Model.Stmt.*;
import Model.Stmt.CountdownLatchStmts.AwaitStmt;
import Model.Stmt.CountdownLatchStmts.CountDownStmt;
import Model.Stmt.CountdownLatchStmts.NewLatchStmt;
import Model.Types.Bool;
import Model.Types.Int;
import Model.Types.RefType;
import Model.Types.StringType;
import Model.Values.BoolValue;
import Model.Values.IntValue;
import Model.Values.StringValue;
import Repository.IRepo;
import Repository.MyRepo;
import Utils.Collections.MyDic;
import Utils.Collections.MyList;
import Utils.Collections.MyStack;
import Utils.Exceptions.MyException;
import Utils.State.MyHeap;
import Utils.State.MyLatchTable;
import Utils.State.MyLockTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }
    @FXML
    private ListView<IStmt> programsListView;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() {
        programsListView.setItems(getAllStatements());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent actionEvent) {
        IStmt selectedIStmt= programsListView.getSelectionModel().getSelectedItem();
        if (selectedIStmt== null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered!");
            alert.setContentText("No Statements selected!");
            alert.showAndWait();
        } else {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try {
                selectedIStmt.typecheck(new MyDic<>());
                PrgState programState = new PrgState(new MyStack(), new MyDic<>(), new MyDic<>(), new MyHeap<>(), new MyLockTable(), new MyLatchTable(),
                new MyList<>(),  selectedIStmt);
                IRepo repository = new MyRepo(programState, "log" + (id + 1) + ".txt");
                Controller controller = new Controller(repository);
                programExecutorController.setController(controller);
            } catch (MyException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error encountered!");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private ObservableList<IStmt> getAllStatements() {
        java.util.List<IStmt> allStatements = new ArrayList<>();

//        int v; v=2; Print(v)
        IStmt p1 = new CompStmt(new VariableDeclStmt("v", new Int()),
                new CompStmt(new AssignStmt("v", new ValueExp(new StringValue("2"))),
                        new PrintStmt(new VariableExpr("v"))));
        allStatements.add(p1);

//        int a; int b; a=2+3*5; b=a+1; Print(b)
        IStmt p2 = new CompStmt( new VariableDeclStmt("a",new Int()),
                new CompStmt(new VariableDeclStmt("b",new Int()),
                        new CompStmt(new AssignStmt("a",
                                new ArithExp('+',new ValueExp(new IntValue(2)),
                                        new ArithExp('*',new ValueExp(new IntValue(3)),
                                                new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",
                                        new ArithExp('+',new VariableExpr("a"), new ValueExp(new IntValue(1)))),
                                        new PrintStmt(new VariableExpr("b"))))));
        allStatements.add(p2);

//        bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
        IStmt p3 = new CompStmt(new VariableDeclStmt("a",new Bool()),
                new CompStmt(new VariableDeclStmt("v", new Int()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VariableExpr("a"),
                                        new AssignStmt("v",new ValueExp(new IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VariableExpr("v"))))));
        allStatements.add(p3);

//        Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
        IStmt p4 = new CompStmt(
                new VariableDeclStmt("v", new RefType(new Int())),
                new CompStmt(new NewStmt("v", new ConstExpr(new IntValue(20))),
                        new CompStmt(new VariableDeclStmt("a", new RefType(new RefType(new Int()))),
                                new CompStmt(new NewStmt("a", new VariableExpr("v")),
                                        new CompStmt(new PrintStmt(new VariableExpr("v")),
                                                new PrintStmt(new VariableExpr("a")))))));
        allStatements.add(p4);

        // Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStmt p5 = new CompStmt(new VariableDeclStmt("v", new RefType(new Int())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VariableDeclStmt("a", new RefType(new RefType(new Int()))),
                                new CompStmt(new NewStmt("a", new VariableExpr("v")),
                                        new CompStmt( new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new CompStmt(new PrintStmt(new rH(new VariableExpr("v"))),
                                                        new PrintStmt(new rH(new rH(new VariableExpr("a"))))))))));
        allStatements.add(p5);

        // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);
        IStmt p6 = new CompStmt(new VariableDeclStmt("v", new RefType(new Int())), new CompStmt(new NewStmt("v",
                new ValueExp(new IntValue(20))),new CompStmt( new PrintStmt(new rH(new VariableExpr("v"))),
                new CompStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(30))), new PrintStmt(new ArithExp(
                        '+',new rH(new VariableExpr("v")),new ValueExp(new IntValue(5))))))));
        allStatements.add(p6);

        // int v; v=4; (while (v>0) print(v);v=v-1);print(v)
        IStmt p7=  new CompStmt(
                new VariableDeclStmt("v", new Int()), // int v;
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(4))), // v = 4;
                        new CompStmt(
                                new WhileStmt(
                                        new RelationalExp(">", new VariableExpr("v"), new ValueExp(new IntValue(0))), // while (v > 0)
                                        new CompStmt(
                                                new PrintStmt(new VariableExpr("v")), // print(v);
                                                new AssignStmt("v", new ArithExp('-', new VariableExpr("v"), new ValueExp(new IntValue(1)))) // v = v - 1;
                                        )
                                ),
                                new PrintStmt(new VariableExpr("v")) // print(v);
                        )
                )
        );
        allStatements.add(p7);

        // int v; Ref int a; v=10; new(a,22); fork(wH(a,30);v=32;print(v);print(rH(a))); print(v);print(rH(a))
        IStmt p8 = new CompStmt(
                new VariableDeclStmt("v", new Int()),  // int v
                new CompStmt(
                        new VariableDeclStmt("a", new RefType(new Int())),  // Ref int a
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),  // v = 10
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(22))),  // new(a, 22)
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new WriteHeapStmt("a", new ValueExp(new IntValue(30))),  // wH(a, 30)
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),  // v = 32
                                                                        new CompStmt(
                                                                                new PrintStmt(new VariableExpr("v")),  // print v
                                                                                new PrintStmt(new rH(new VariableExpr("a")))  // print rH(a)
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompStmt(
                                                        new PrintStmt(new VariableExpr("v")),  // print v
                                                        new PrintStmt(new rH(new VariableExpr("a")))  // print rH(a)
                                                )
                                        )
                                )
                        )
                )
        );
        allStatements.add(p8);

//        string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)
        IStmt p9 = new CompStmt(new VariableDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("/Users/patriimanciu/home/uni/projects/Toy-Language-Interpreter/src/View/GUI/test.in"))),
                        new CompStmt(new OpenRFile(new VariableExpr("varf")),
                                new CompStmt(new VariableDeclStmt("varc", new Int()),
                                        new CompStmt(new ReadFile(new VariableExpr("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VariableExpr("varc")),
                                                        new CompStmt(new ReadFile(new VariableExpr("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VariableExpr("varc")),
                                                                        new CloseRFile(new VariableExpr("varf"))))))))));
        allStatements.add(p9);

//        int a; int b; a=5; b=7; if a>b then print(a) else print(b)
        IStmt p10 = new CompStmt(new VariableDeclStmt("a", new Int()),
                new CompStmt(new VariableDeclStmt("b", new Int()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(5))),
                                new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(7))),
                                        new IfStmt(new RelationalExp(">", new VariableExpr("a"),
                                                new VariableExpr("b")),new PrintStmt(new VariableExpr("a")),
                                                new PrintStmt(new VariableExpr("b")))))));
        allStatements.add(p10);


        IStmt ex13 = new CompStmt(
                new VariableDeclStmt("v1", new RefType(new Int())),
                new CompStmt(
                        new VariableDeclStmt("v2", new RefType(new Int())),
                        new CompStmt(
                                new VariableDeclStmt("v3", new RefType(new Int())),
                                new CompStmt(
                                        new VariableDeclStmt("cnt", new Int()),
                                        new CompStmt(
                                                new NewStmt("v1", new ValueExp(new IntValue(2))),
                                                new CompStmt(
                                                        new NewStmt("v2", new ValueExp(new IntValue(3))),
                                                        new CompStmt(
                                                                new NewStmt("v3", new ValueExp(new IntValue(4))),
                                                                new CompStmt(
                                                                        new NewLatchStmt("cnt", new rH(new VariableExpr("v2"))),
                                                                        new CompStmt(
                                                                                new ForkStmt(
                                                                                        new CompStmt(
                                                                                                new WriteHeapStmt("v1", new ArithExp('*', new rH(new VariableExpr("v1")), new ValueExp(new IntValue(10)))),
                                                                                                new CompStmt(
                                                                                                        new PrintStmt(new rH(new VariableExpr("v1"))),
                                                                                                        new CountDownStmt("cnt")
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new CompStmt(
                                                                                        new ForkStmt(
                                                                                                new CompStmt(
                                                                                                        new WriteHeapStmt("v2", new ArithExp('*', new rH(new VariableExpr("v2")), new ValueExp(new IntValue(10)))),
                                                                                                        new CompStmt(
                                                                                                                new PrintStmt(new rH(new VariableExpr("v2"))),
                                                                                                                new CountDownStmt("cnt")
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompStmt(
                                                                                                new ForkStmt(
                                                                                                        new CompStmt(
                                                                                                                new WriteHeapStmt("v3", new ArithExp('*', new rH(new VariableExpr("v3")), new ValueExp(new IntValue(10)))),
                                                                                                                new CompStmt(
                                                                                                                        new PrintStmt(new rH(new VariableExpr("v3"))),
                                                                                                                        new CountDownStmt("cnt")
                                                                                                                )
                                                                                                        )
                                                                                                ),
                                                                                                new CompStmt(
                                                                                                        new AwaitStmt("cnt"),
                                                                                                        new CompStmt(
                                                                                                                new PrintStmt(new ValueExp(new IntValue(100))),
                                                                                                                new CompStmt(
                                                                                                                        new CountDownStmt("cnt"),
                                                                                                                        new PrintStmt(new ValueExp(new IntValue(100)))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )

                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStatements.add(ex13);
        return FXCollections.observableArrayList(allStatements);
    }
}