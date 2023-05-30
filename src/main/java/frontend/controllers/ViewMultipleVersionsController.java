package frontend.controllers;

import backend.model.smells.SmellPriority;
import frontend.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ViewMultipleVersionsController implements Initializable {
    private final String projectName;
    private final HashMap<String, List<SmellPriority>> smellPriorities;

    @FXML
    LineChart<String, Integer> totalSmells;
    @FXML
    BarChart<String, Integer> smellCategory;
    @FXML
    BarChart<String, Integer> smellPriority;

    public ViewMultipleVersionsController(String projectName, HashMap<String, List<SmellPriority>> smellPriorities) {
        this.projectName = projectName;
        this.smellPriorities = smellPriorities;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTotalSmells();
        initSmellCategory();
        initSmellPriority();
    }

    private void initSmellPriority() {
        XYChart.Series<String, Integer> seriesVlow = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesLow = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesMedium = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesHigh = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesVhigh = new XYChart.Series<>();
        for (String key : smellPriorities.keySet()) {
            int verylow = 0;
            int low = 0;
            int medium = 0;
            int high = 0;
            int veryhigh = 0;
            for (SmellPriority smell : smellPriorities.get(key)) {
                switch (smell.getPriority()) {
                    case "VERYLOW" -> verylow += 1;
                    case "LOW" -> low += 1;
                    case "MEDIUM" -> medium += 1;
                    case "HIGH" -> high += 1;
                    case "VERYHIGH" -> veryhigh += 1;
                }
            }
            Util.setSeries(seriesVlow, key, verylow, "VERY LOW");
            Util.setSeries(seriesLow, key, low, "LOW");
            Util.setSeries(seriesMedium, key, medium, "MEDIUM");
            Util.setSeries(seriesHigh, key, high, "HIGH");
            Util.setSeries(seriesVhigh, key, veryhigh, "VERY HIGH");
        }
        smellPriority.getData().add(seriesVlow);
        smellPriority.getData().add(seriesLow);
        smellPriority.getData().add(seriesMedium);
        smellPriority.getData().add(seriesHigh);
        smellPriority.getData().add(seriesVhigh);
    }

    private void initSmellCategory() {
        XYChart.Series<String, Integer> seriesGod = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesData = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesCoupling = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesBrain = new XYChart.Series<>();
        for (String key : smellPriorities.keySet()) {
            List<SmellPriority> smells = smellPriorities.get(key);
            int god = 0;
            int data = 0;
            int coupling = 0;
            int brain = 0;

            for (SmellPriority smellPriority : smells) {
                switch (smellPriority.getSmell()) {
                    case GODCLASS -> god++;
                    case DATACLASS -> data++;
                    case BRAINMETHOD -> brain++;
                    case DISPERSEDCOUPLING -> coupling++;
                }
            }
            Util.setSeries(seriesGod, key, god, "God Class");
            Util.setSeries(seriesData, key, data, "Data Class");
            Util.setSeries(seriesCoupling, key, coupling, "Dispersed Coupling");
            Util.setSeries(seriesBrain, key, brain, "Brain Method");
        }
        smellCategory.getData().add(seriesGod);
        smellCategory.getData().add(seriesData);
        smellCategory.getData().add(seriesCoupling);
        smellCategory.getData().add(seriesBrain);
    }

    private void initTotalSmells() {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (String key : smellPriorities.keySet()) {
            Util.setSeries(series,
                    key,
                    smellPriorities.get(key).size(),
                    "");
        }
        totalSmells.getData().add(series);
    }
}
