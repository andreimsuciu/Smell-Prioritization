package frontend.controllers;

import backend.model.smells.Smell;
import backend.model.smells.SmellIntensity;
import frontend.model.SmellVersionComparison;
import frontend.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewTwoProjectsController implements Initializable {
    private final List<SmellIntensity> godClassListOld;
    private final List<SmellIntensity> dataClassListOld;
    private final List<SmellIntensity> brainMethodListOld;
    private final List<SmellIntensity> dispersedCouplingListOld;

    private final List<SmellIntensity> godClassListNew;
    private final List<SmellIntensity> dataClassListNew;
    private final List<SmellIntensity> brainMethodListNew;
    private final List<SmellIntensity> dispersedCouplingListNew;

    @FXML
    TableView<SmellVersionComparison> tableViewNew;
    @FXML
    TableColumn<SmellVersionComparison, String> smellTableColumnNew;
    @FXML
    TableColumn<SmellVersionComparison, String> entityTableColumnNew;
    @FXML
    TableColumn<SmellVersionComparison, String> priorityTableColumnNew;
    @FXML
    TableView<SmellVersionComparison> tableViewPersisted;
    @FXML
    TableColumn<SmellVersionComparison, String> smellTableColumnPersisted;
    @FXML
    TableColumn<SmellVersionComparison, String> entityTableColumnPersisted;
    @FXML
    TableColumn<SmellVersionComparison, String> priorityTableColumnPersisted;
    @FXML
    TableView<SmellVersionComparison> tableViewResolved;
    @FXML
    TableColumn<SmellVersionComparison, String> smellTableColumnResolved;
    @FXML
    TableColumn<SmellVersionComparison, String> entityTableColumnResolved;
    @FXML
    TableColumn<SmellVersionComparison, String> priorityTableColumnResolved;
    @FXML
    TableView<SmellVersionComparison> tableViewWorse;
    @FXML
    TableColumn<SmellVersionComparison, String> smellTableColumnWorse;
    @FXML
    TableColumn<SmellVersionComparison, String> entityTableColumnWorse;
    @FXML
    TableColumn<SmellVersionComparison, String> oldPriorityTableColumnWorse;
    @FXML
    TableColumn<SmellVersionComparison, String> newPriorityTableColumnWorse;
    @FXML
    TableView<SmellVersionComparison> tableViewBetter;
    @FXML
    TableColumn<SmellVersionComparison, String> smellTableColumnBetter;
    @FXML
    TableColumn<SmellVersionComparison, String> entityTableColumnBetter;
    @FXML
    TableColumn<SmellVersionComparison, String> oldPriorityTableColumnBetter;
    @FXML
    TableColumn<SmellVersionComparison, String> newPriorityTableColumnBetter;

    public ViewTwoProjectsController(
            List<SmellIntensity> godClassListOld, List<SmellIntensity> dataClassListOld, List<SmellIntensity> brainMethodListOld, List<SmellIntensity> dispersedCouplingListOld,
            List<SmellIntensity> godClassListNew, List<SmellIntensity> dataClassListNew, List<SmellIntensity> brainMethodListNew, List<SmellIntensity> dispersedCouplingListNew) {
        this.godClassListOld = godClassListOld;
        this.dataClassListOld = dataClassListOld;
        this.brainMethodListOld = brainMethodListOld;
        this.dispersedCouplingListOld = dispersedCouplingListOld;

        this.godClassListNew = godClassListNew;
        this.dataClassListNew = dataClassListNew;
        this.brainMethodListNew = brainMethodListNew;
        this.dispersedCouplingListNew = dispersedCouplingListNew;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Util.initMatchingTable(tableViewNew, entityTableColumnNew, priorityTableColumnNew, smellTableColumnNew,
                getAggregatedListNew("NEW").stream().sorted().collect(Collectors.toList()));
        Util.initMatchingTable(tableViewResolved, entityTableColumnResolved, priorityTableColumnResolved, smellTableColumnResolved,
                getAggregatedListMatching("RESOLVED").stream().sorted().collect(Collectors.toList()));
        Util.initMatchingTable(tableViewPersisted, entityTableColumnPersisted, priorityTableColumnPersisted, smellTableColumnPersisted,
                getAggregatedListCompared("PERSISTED").stream().sorted().collect(Collectors.toList()));
        Util.initComparedTable(tableViewWorse, entityTableColumnWorse, oldPriorityTableColumnWorse, newPriorityTableColumnWorse, smellTableColumnWorse,
                getAggregatedListCompared("WORSE").stream().sorted().collect(Collectors.toList()));
        Util.initComparedTable(tableViewBetter, entityTableColumnBetter, oldPriorityTableColumnBetter, newPriorityTableColumnBetter, smellTableColumnBetter,
                getAggregatedListCompared("BETTER").stream().sorted().collect(Collectors.toList()));
    }

    private List<SmellVersionComparison> getAggregatedListMatching(String tab) {
        List<SmellVersionComparison> merge = new ArrayList<>();
        List<SmellVersionComparison> god = getMatchingSmells(godClassListOld, godClassListNew, tab, Smell.GODCLASS);
        List<SmellVersionComparison> data = getMatchingSmells(dataClassListOld, dataClassListNew, tab, Smell.DATACLASS);
        List<SmellVersionComparison> brain = getMatchingSmells(brainMethodListOld, brainMethodListNew, tab, Smell.BRAINMETHOD);
        List<SmellVersionComparison> coupling = getMatchingSmells(dispersedCouplingListOld, dispersedCouplingListNew, tab, Smell.DISPERSEDCOUPLING);
        merge.addAll(god);
        merge.addAll(data);
        merge.addAll(brain);
        merge.addAll(coupling);
        return merge;
    }

    private List<SmellVersionComparison> getAggregatedListCompared(String tab) {
        List<SmellVersionComparison> merge = new ArrayList<>();
        List<SmellVersionComparison> god = getComparedSmells(godClassListOld, godClassListNew, tab, Smell.GODCLASS);
        List<SmellVersionComparison> data = getComparedSmells(dataClassListOld, dataClassListNew, tab, Smell.DATACLASS);
        List<SmellVersionComparison> brain = getComparedSmells(brainMethodListOld, brainMethodListNew, tab, Smell.BRAINMETHOD);
        List<SmellVersionComparison> coupling = getComparedSmells(dispersedCouplingListOld, dispersedCouplingListNew, tab, Smell.DISPERSEDCOUPLING);
        merge.addAll(god);
        merge.addAll(data);
        merge.addAll(brain);
        merge.addAll(coupling);
        return merge;
    }

    private List<SmellVersionComparison> getAggregatedListNew(String tab) {
        List<SmellVersionComparison> merge = new ArrayList<>();
        List<SmellVersionComparison> god = getMatchingSmells(godClassListNew, godClassListOld, tab, Smell.GODCLASS);
        List<SmellVersionComparison> data = getMatchingSmells(dataClassListNew, dataClassListOld, tab, Smell.DATACLASS);
        List<SmellVersionComparison> brain = getMatchingSmells(brainMethodListNew, brainMethodListOld, tab, Smell.BRAINMETHOD);
        List<SmellVersionComparison> coupling = getMatchingSmells(dispersedCouplingListNew, dispersedCouplingListOld, tab, Smell.DISPERSEDCOUPLING);
        merge.addAll(god);
        merge.addAll(data);
        merge.addAll(brain);
        merge.addAll(coupling);
        return merge;
    }

    private List<SmellVersionComparison> getMatchingSmells(List<SmellIntensity> oldSmells, List<SmellIntensity> newSmells, String tab, Smell smell) {
        ArrayList<SmellVersionComparison> resolvedSmells = new ArrayList<>();
        for (SmellIntensity oldSmell : oldSmells) {
            boolean found = false;
            for (SmellIntensity newSmell : newSmells) {
                found = compare(tab, oldSmell, newSmell);
                if (found) break;
            }
            if (!found) {
                resolvedSmells.add(new SmellVersionComparison(oldSmell.getEntityName(), oldSmell.getPriority(), smell, oldSmell.getMetrics()));
            }
        }
        return resolvedSmells;
    }

    private List<SmellVersionComparison> getComparedSmells(List<SmellIntensity> oldSmells, List<SmellIntensity> newSmells, String tab, Smell smell) {
        ArrayList<SmellVersionComparison> resolvedSmells = new ArrayList<>();
        for (SmellIntensity oldSmell : oldSmells) {
            for (SmellIntensity newSmell : newSmells) {
                if (compare(tab, oldSmell, newSmell)) {
                    SmellVersionComparison smellInt = new SmellVersionComparison(newSmell.getEntityName(), oldSmell.getPriority(), smell, oldSmell.getMetrics());
                    smellInt.setMetricsNew(newSmell.getMetrics());
                    smellInt.setPriorityNew(newSmell.getPriority());
                    resolvedSmells.add(smellInt);
                    break;
                }
            }
        }
        return resolvedSmells;
    }

    private boolean compare(String tab, SmellIntensity oldSmell, SmellIntensity newSmell) {
        return switch (tab) {
            case "RESOLVED", "NEW" -> oldSmell.getEntityName().equals(newSmell.getEntityName());
            case "PERSISTED" -> oldSmell.getEntityName().equals(newSmell.getEntityName()) &&
                    oldSmell.compareTo(newSmell) == 0;
            case "WORSE" -> oldSmell.getEntityName().equals(newSmell.getEntityName()) &&
                    oldSmell.compareTo(newSmell) > 0;
            case "BETTER" -> oldSmell.getEntityName().equals(newSmell.getEntityName()) &&
                    oldSmell.compareTo(newSmell) < 0;
            default -> false;
        };
    }
}
