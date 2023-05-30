package frontend.controllers;

import frontend.AnalyzeTask;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AnalyzeController {

    @FXML
    public ProgressBar progressBar;
    @FXML
    private Label xmlLocationsLabel;
    @FXML
    private TextField projectNameInput;
    private List<File> files;
    private String analysisFolder;

    public void selectXmls(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Multiple XMLs");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Only", "*.xml"));
        fileChooser.setInitialDirectory(new File("src"));
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        StringBuilder stringBuilder = new StringBuilder();
        for (File file : files) {
            stringBuilder.append(file.getName());
            stringBuilder.append(",");
        }
        xmlLocationsLabel.setText(stringBuilder.toString());
        this.files = files;
    }

    public void analyzeXmls(ActionEvent event) {
        Task<Void> task = new AnalyzeTask(this.files,projectNameInput.getText());

        //initialize progress bar
        this.progressBar.progressProperty().bind(task.progressProperty());
        this.progressBar.setVisible(true);

        //start analysis task in thread
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        //open View window on complete
        task.setOnSucceeded(t -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewProject.fxml"));

                ViewProjectController viewProjectController = new ViewProjectController();
                viewProjectController.setProjectName(projectNameInput.getText());
                loader.setController(viewProjectController);

                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("View Project " + projectNameInput.getText());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Code Smell Prioritization");
        stage.show();
    }
}
