package Utils.State;

import Model.Stmt.IStmt;
import Utils.Collections.MyDic;
import javafx.util.Pair;

import java.util.List;

public class MyProcedureTable extends MyDic<String, Pair<List<String>, IStmt>> implements MyIProcedureTable {
}
