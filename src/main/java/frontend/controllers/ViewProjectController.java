package frontend.controllers;

import backend.io.read.csv.AnalysisReader;
import backend.model.smells.SmellIntensity;
import backend.model.smells.SmellPriority;
import frontend.MultipleProjectsTask;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ViewProjectController implements Initializable {
    @FXML
    public ProgressBar progressBar;
    @FXML
    public ListView<String> versionsListView;
    @FXML
    public Button viewVersionsBtn;
    public String projectName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> versions = AnalysisReader.getProjectVersions(projectName);

        versionsListView.getItems().addAll(versions);
        versionsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Code Smell Prioritization");
        stage.show();
    }

    public void view(ActionEvent event) throws IOException {
        ObservableList<String> selectedVersions = versionsListView.getSelectionModel().getSelectedItems();
        if (selectedVersions.size() == 1) {
            openSingleProjectVersionView(selectedVersions.get(0), event);
        } else if (selectedVersions.size() == 2) {
            openTwoProjectVersionView(selectedVersions.get(0), selectedVersions.get(1), event);
        } else {
            openMultipleProjects(selectedVersions, event);
        }
    }

    private void openMultipleProjects(ObservableList<String> selectedVersions, ActionEvent event) throws IOException {
        Task<HashMap<String, List<SmellPriority>>> task = new MultipleProjectsTask(selectedVersions, this.projectName);

        //ini progress bar
        this.progressBar.progressProperty().bind(task.progressProperty());
        this.progressBar.setVisible(true);

        //start analysis task in thread
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        task.setOnSucceeded(t -> {
            HashMap<String, List<SmellPriority>> result = task.getValue();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewMultipleVersions.fxml"));

            ViewMultipleVersionsController controller = new ViewMultipleVersionsController(this.projectName, result);
            loader.setController(controller);

            try {
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("View Multiple Versions of " + this.projectName);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void openTwoProjectVersionView(String projectVersionOld, String projectVersionNew, ActionEvent event) throws IOException {
        List<SmellIntensity> godClassListOld = AnalysisReader.getSmellIntensity(projectName, projectVersionOld, "godClass.csv");
        List<SmellIntensity> dataClassListOld = AnalysisReader.getSmellIntensity(projectName, projectVersionOld, "dataClass.csv");
        List<SmellIntensity> brainMethodListOld = AnalysisReader.getSmellIntensity(projectName, projectVersionOld, "brainMethod.csv");
        List<SmellIntensity> dispersedCouplingListOld = AnalysisReader.getSmellIntensity(projectName, projectVersionOld, "dispersedCoupling.csv");

        List<SmellIntensity> godClassListNew = AnalysisReader.getSmellIntensity(projectName, projectVersionNew, "godClass.csv");
        List<SmellIntensity> dataClassListNew = AnalysisReader.getSmellIntensity(projectName, projectVersionNew, "dataClass.csv");
        List<SmellIntensity> brainMethodListNew = AnalysisReader.getSmellIntensity(projectName, projectVersionNew, "brainMethod.csv");
        List<SmellIntensity> dispersedCouplingListNew = AnalysisReader.getSmellIntensity(projectName, projectVersionNew, "dispersedCoupling.csv");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewTwoProjects.fxml"));

        ViewTwoProjectsController controller = new ViewTwoProjectsController(
                godClassListOld, dataClassListOld, brainMethodListOld, dispersedCouplingListOld,
                godClassListNew, dataClassListNew, brainMethodListNew, dispersedCouplingListNew);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(projectName + " " + projectVersionOld + " to " + projectVersionNew);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void openSingleProjectVersionView(String projectVersion, ActionEvent event) throws IOException {
        List<SmellIntensity> godClassList = AnalysisReader.getSmellIntensity(projectName, projectVersion, "godClass.csv");
        List<SmellIntensity> dataClassList = AnalysisReader.getSmellIntensity(projectName, projectVersion, "dataClass.csv");
        List<SmellIntensity> brainMethodList = AnalysisReader.getSmellIntensity(projectName, projectVersion, "brainMethod.csv");
        List<SmellIntensity> dispersedCouplingList = AnalysisReader.getSmellIntensity(projectName, projectVersion, "dispersedCoupling.csv");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewSingleProject.fxml"));

        ViewSingleProjectController controller = new ViewSingleProjectController(godClassList, dataClassList, brainMethodList, dispersedCouplingList);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(projectName + " " + projectVersion);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
