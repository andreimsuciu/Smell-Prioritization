package frontend.controllers;

import backend.model.smells.SmellIntensity;
import frontend.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViewSingleProjectController implements Initializable {
    private final List<SmellIntensity> godClassList;
    private final List<SmellIntensity> dataClassList;
    private final List<SmellIntensity> brainMethodList;
    private final List<SmellIntensity> dispersedCouplingList;
    private final List<SmellIntensity> aggregated;

    //AGG VIEW
    @FXML
    TableView<SmellIntensity> tableAgg;
    @FXML
    TableColumn<SmellIntensity, String> entityTableColumn;
    @FXML
    TableColumn<SmellIntensity, String> priorityTableColumn;
    @FXML
    BarChart<String, Integer> numOfSmellAggChart;
    @FXML
    BarChart<String, Integer> numOfPriorityAggChart;

    //GOD CLASS
    @FXML
    TableView<SmellIntensity> tableGod;
    @FXML
    TableColumn<SmellIntensity, String> entityTableColumnGod;
    @FXML
    TableColumn<SmellIntensity, String> priorityTableColumnGod;
    @FXML
    BarChart<String, Integer> numOfPriorityChartGod;

    //DATA CLASS
    @FXML
    TableView<SmellIntensity> tableData;
    @FXML
    TableColumn<SmellIntensity, String> entityTableColumnData;
    @FXML
    TableColumn<SmellIntensity, String> priorityTableColumnData;
    @FXML
    BarChart<String, Integer> numOfPriorityChartData;

    //GOD CLASS
    @FXML
    TableView<SmellIntensity> tableBrain;
    @FXML
    TableColumn<SmellIntensity, String> entityTableColumnBrain;
    @FXML
    TableColumn<SmellIntensity, String> priorityTableColumnBrain;
    @FXML
    BarChart<String, Integer> numOfPriorityChartBrain;

    //GOD CLASS
    @FXML
    TableView<SmellIntensity> tableCoupling;
    @FXML
    TableColumn<SmellIntensity, String> entityTableColumnCoupling;
    @FXML
    TableColumn<SmellIntensity, String> priorityTableColumnCoupling;
    @FXML
    BarChart<String, Integer> numOfPriorityChartCoupling;

    public ViewSingleProjectController(List<SmellIntensity> godClassList, List<SmellIntensity> dataClassList, List<SmellIntensity> brainMethodList, List<SmellIntensity> dispersedCouplingList) {
        this.godClassList = godClassList.stream().sorted().collect(Collectors.toList());
        this.dataClassList = dataClassList.stream().sorted().collect(Collectors.toList());
        this.brainMethodList = brainMethodList.stream().sorted().collect(Collectors.toList());
        this.dispersedCouplingList = dispersedCouplingList.stream().sorted().collect(Collectors.toList());

        this.aggregated = Stream.of(this.godClassList, this.dataClassList, this.brainMethodList, this.dispersedCouplingList)
                .flatMap(Collection::stream)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAggTab();
        //GOD
        Util.initSmellIntensityTable(tableGod, entityTableColumnGod, priorityTableColumnGod, godClassList);
        initNumOfPriorityChart(godClassList, numOfPriorityChartGod);
        //DATA
        Util.initSmellIntensityTable(tableData, entityTableColumnData, priorityTableColumnData, dataClassList);
        initNumOfPriorityChart(dataClassList, numOfPriorityChartData);
        //BRAIN
        Util.initSmellIntensityTable(tableBrain, entityTableColumnBrain, priorityTableColumnBrain, brainMethodList);
        initNumOfPriorityChart(brainMethodList, numOfPriorityChartBrain);
        //COUPLING
        Util.initSmellIntensityTable(tableCoupling, entityTableColumnCoupling, priorityTableColumnCoupling, dispersedCouplingList);
        initNumOfPriorityChart(dispersedCouplingList, numOfPriorityChartCoupling);
    }

    private void initAggTab() {
        Util.initSmellIntensityTable(tableAgg, entityTableColumn, priorityTableColumn, aggregated);

        //1st chart
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("God Class", godClassList.size()));
        series.getData().add(new XYChart.Data<>("Data Class", dataClassList.size()));
        series.getData().add(new XYChart.Data<>("Brain Method", brainMethodList.size()));
        series.getData().add(new XYChart.Data<>("Dispersed Coupling", dispersedCouplingList.size()));
        numOfSmellAggChart.getData().add(series);

        //2nd chart
        initNumOfPriorityChart(aggregated, numOfPriorityAggChart);
    }

    private void initNumOfPriorityChart(List<SmellIntensity> list, BarChart<String, Integer> chart) {
        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
        int verylow = 0;
        int low = 0;
        int medium = 0;
        int high = 0;
        int veryhigh = 0;
        for (SmellIntensity smellIntensity : list) {
            switch (smellIntensity.getPriority()) {
                case "VERYLOW" -> verylow += 1;
                case "LOW" -> low += 1;
                case "MEDIUM" -> medium += 1;
                case "HIGH" -> high += 1;
                case "VERYHIGH" -> veryhigh += 1;
            }
        }
        series2.getData().add(new XYChart.Data<>("VERYLOW", verylow));
        series2.getData().add(new XYChart.Data<>("LOW", low));
        series2.getData().add(new XYChart.Data<>("MEDIUM", medium));
        series2.getData().add(new XYChart.Data<>("HIGH", high));
        series2.getData().add(new XYChart.Data<>("VERYHIGH", veryhigh));

        chart.getData().add(series2);
    }
}
