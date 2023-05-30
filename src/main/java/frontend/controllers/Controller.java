package frontend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    @FXML

    public void analyze(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/analyze.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Start Analysis");
        stage.show();
    }

    public void view(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Project Directory");
        directoryChooser.setInitialDirectory(new File("src"));
        File selectedDirectory = directoryChooser.showDialog(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewProject.fxml"));

        ViewProjectController viewProjectController = new ViewProjectController();
        viewProjectController.setProjectName(selectedDirectory.getName());
        loader.setController(viewProjectController);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("View Project " + selectedDirectory.getName());
        stage.show();
    }

    private void switchWindow(Parent root, ActionEvent event) {

    }
}
