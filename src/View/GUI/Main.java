package View.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader programListLoader = new FXMLLoader();
            programListLoader.setLocation(Main.class.getResource("/GUI/ProgramChooserController.fxml"));
            Parent programListRoot = programListLoader.load();
            Scene programListScene = new Scene(programListRoot, 500, 550);
            ProgramChooserController programChooserController = programListLoader.getController();
            primaryStage.setTitle("Select a program");
            primaryStage.setScene(programListScene);
            primaryStage.show();

            FXMLLoader programExecutorLoader = new FXMLLoader();
            programExecutorLoader.setLocation(Main.class.getResource("/GUI/ProgramExecutorController.fxml"));
            Parent programExecutorRoot = programExecutorLoader.load();
            Scene programExecutorScene = new Scene(programExecutorRoot, 700, 500);
            ProgramExecutorController programExecutorController = programExecutorLoader.getController();
            programChooserController.setProgramExecutorController(programExecutorController);
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Program Executor");
            secondaryStage.setScene(programExecutorScene);
            secondaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
