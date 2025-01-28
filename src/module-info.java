module gui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports View.GUI;
    opens View.GUI to javafx.fxml;

    exports Controller;
    opens Controller to javafx.fxml;

    exports Utils.Exceptions;
    opens Utils.Exceptions to javafx.fxml;

    exports Utils.Collections;
    opens Utils.Collections to javafx.fxml;

    exports  Model.Exp;
    opens Model.Exp to javafx.fxml;

    exports Model.ProgramState;
    opens Model.ProgramState to javafx.fxml;

    exports Model.Stmt;
    opens Model.Stmt to javafx.fxml;

    exports Model.Types;
    opens Model.Types to javafx.fxml;

    exports Utils.State;
    opens Utils.State to javafx.fxml;

    exports Model.Values;
    opens Model.Values to javafx.fxml;

    exports Repository;
    opens Repository to javafx.fxml;

    exports View.CLI;
    opens View.CLI to javafx.fxml;

    exports View.Command;
    opens View.Command to javafx.fxml;
}