package Utils.State;

import Model.Stmt.IStmt;
import Utils.Collections.MyIDic;
import javafx.util.Pair;

import java.util.List;

public interface MyIProcedureTable extends MyIDic<String, Pair<List<String>, IStmt>> {
}