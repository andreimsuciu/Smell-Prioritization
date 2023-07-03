package frontend;

import backend.model.metrics.Metric;
import backend.model.smells.SmellIntensity;
import frontend.model.SmellVersionComparison;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class Util {
    public static void initSmellIntensityTable(TableView<SmellIntensity> table, TableColumn<SmellIntensity, String> column, TableColumn<SmellIntensity, String> priority, List<SmellIntensity> list) {
        table.setItems(FXCollections.observableList(list));

        table.setRowFactory(tv -> new TableRow<>() {
            private final Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(SmellIntensity smellIntensity, boolean empty) {
                super.updateItem(smellIntensity, empty);
                if (smellIntensity == null) {
                    setTooltip(null);
                } else {
                    StringBuilder tooltipSb = new StringBuilder();
                    for(Metric metric: smellIntensity.getMetrics()){
                        tooltipSb.append(metric.getName()).append(":").append(metric.getValue()).append(" ");
                    }
                    tooltipSb.deleteCharAt(tooltipSb.length()-1);
                    tooltip.setText(tooltipSb.toString());
                    setTooltip(tooltip);
                }
            }
        });

        column.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        priority.setCellValueFactory(new PropertyValueFactory<>("priority"));
    }

    public static void initMatchingTable(TableView<SmellVersionComparison> table, TableColumn<SmellVersionComparison, String> entity, TableColumn<SmellVersionComparison, String> priority,
                                         TableColumn<SmellVersionComparison, String> smell, List<SmellVersionComparison> list) {
        table.setItems(FXCollections.observableList(list));

        table.setRowFactory(tv -> new TableRow<>() {
            private final Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(SmellVersionComparison smell, boolean empty) {
                super.updateItem(smell, empty);
                if (smell == null) {
                    setTooltip(null);
                } else {
                    StringBuilder tooltipSb = new StringBuilder();
                    for(Metric metric: smell.getMetricOld()){
                        tooltipSb.append(metric.getName()).append(":").append(metric.getValue()).append(" ");
                    }
                    tooltipSb.deleteCharAt(tooltipSb.length()-1);
                    tooltip.setText(tooltipSb.toString());
                    setTooltip(tooltip);
                }
            }
        });

        entity.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        priority.setCellValueFactory(new PropertyValueFactory<>("priorityOld"));
        smell.setCellValueFactory(new PropertyValueFactory<>("smell"));
    }

    public static void initComparedTable(TableView<SmellVersionComparison> table, TableColumn<SmellVersionComparison, String> entity,
                                         TableColumn<SmellVersionComparison, String> priorityOld, TableColumn<SmellVersionComparison, String> priorityNew,
                                         TableColumn<SmellVersionComparison, String> smell, List<SmellVersionComparison> list) {
        table.setItems(FXCollections.observableList(list));

        table.setRowFactory(tv -> new TableRow<>() {
            private final Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(SmellVersionComparison smell, boolean empty) {
                super.updateItem(smell, empty);
                if (smell == null) {
                    setTooltip(null);
                } else {
                    StringBuilder tooltipSb = new StringBuilder();
                    tooltipSb.append("OLD: ");
                    for(Metric metric: smell.getMetricOld()){
                        tooltipSb.append(metric.getName()).append(":").append(metric.getValue()).append(" ");
                    }
                    tooltipSb.deleteCharAt(tooltipSb.length()-1);
                    tooltipSb.append("|");
                    tooltipSb.append("NEW: ");
                    for(Metric metric: smell.getMetricsNew()){
                        tooltipSb.append(metric.getName()).append(":").append(metric.getValue()).append(" ");
                    }
                    tooltipSb.deleteCharAt(tooltipSb.length()-1);
                    tooltip.setText(tooltipSb.toString());
                    setTooltip(tooltip);
                }
            }
        });

        entity.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        smell.setCellValueFactory(new PropertyValueFactory<>("smell"));
        priorityOld.setCellValueFactory(new PropertyValueFactory<>("priorityOld"));
        priorityNew.setCellValueFactory(new PropertyValueFactory<>("priorityNew"));
    }

    public static void setSeries(XYChart.Series<String, Integer> series, String string, Integer integer, String name){
        series.getData().add(new XYChart.Data<>(string, integer));
        series.setName(name);
    }

}
