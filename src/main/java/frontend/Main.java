package frontend;

import backend.analysis.Analyser;
import backend.utils.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        String cssFile = this.getClass().getResource("/styles.css").toExternalForm();
        Config.readConfig();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(cssFile);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Code Smell Prioritization");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
