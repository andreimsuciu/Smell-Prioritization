package backend.utils;

import backend.model.smells.FormulaForMetric;
import backend.model.smells.MetricThreshold;
import backend.model.smells.Smell;
import frontend.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class Config {
    public static String analysisFolderPath;
    //metrics
    public static List<String> classMetrics;
    public static List<String> methodMetrics;
    //Smell string
    public static String godClassThresholdsString;
    public static String godClassFormulaString;
    public static String dataClassThresholdsString;
    public static String dataClassFormulaString;
    public static String brainMethodThresholdsString;
    public static String brainMethodFormulaString;
    public static String dispersedCouplingThresholdsString;
    public static String dispersedCouplingFormulaString;

    public static void readConfig() throws IOException {
        URL configFile = Main.class.getResource("/configuration.cnf");
        InputStream configFileStream = configFile.openStream();
        Properties properties = new Properties();
        properties.load(configFileStream);
        configFileStream.close();

        analysisFolderPath = (String) properties.get("analysisFolderPath");

        //read metric configs
        classMetrics = getMetrics((String) properties.get("classMetrics"));
        methodMetrics = getMetrics((String) properties.get("methodMetrics"));

        //read smell configs
        godClassThresholdsString = (String) properties.get("godClassThresholds");
        godClassFormulaString = (String) properties.get("godClassFormula");
        dataClassThresholdsString = (String) properties.get("dataClassThresholds");
        dataClassFormulaString = (String) properties.get("dataClassFormula");
        brainMethodThresholdsString = (String) properties.get("brainMethodThresholds");
        brainMethodFormulaString = (String) properties.get("brainMethodFormula");
        dispersedCouplingThresholdsString = (String) properties.get("dispersedCouplingThresholds");
        dispersedCouplingFormulaString = (String) properties.get("dispersedCouplingFormula");
    }

    public static HashMap<String, FormulaForMetric> getFormula(Smell smell) {
        return switch (smell) {
            case GODCLASS -> getFormulaOfType(godClassFormulaString);
            case DATACLASS -> getFormulaOfType(dataClassFormulaString);
            case BRAINMETHOD -> getFormulaOfType(brainMethodFormulaString);
            case DISPERSEDCOUPLING -> getFormulaOfType(dispersedCouplingFormulaString);
        };
    }

    public static Map<String, MetricThreshold> getThresholds(Smell smell) {
        return switch (smell) {
            case GODCLASS -> getThresholdOfType(godClassThresholdsString);
            case DATACLASS -> getThresholdOfType(dataClassThresholdsString);
            case BRAINMETHOD -> getThresholdOfType(brainMethodThresholdsString);
            case DISPERSEDCOUPLING -> getThresholdOfType(dispersedCouplingThresholdsString);
        };
    }

    private static HashMap<String, FormulaForMetric> getFormulaOfType(String smell) {
        String[] formulaParts = smell.split("&");
        HashMap<String, FormulaForMetric> formulaElements = new HashMap<>();
        for (String formulas : formulaParts) {
            String[] formula = formulas.split(",");
            formulaElements.put(formula[0], new FormulaForMetric(formula[1], formula[2]));
        }
        return formulaElements;
    }

    private static Map<String, MetricThreshold> getThresholdOfType(String smell) {
        Map<String, MetricThreshold> thresholds = new HashMap<>();
        String[] metricAndNames = smell.split("&");
        for (String metricAndName : metricAndNames) {
            String[] metrics = metricAndName.split(":");
            String name = metrics[0];
            String[] valuesString = metrics[1].split(",");
            thresholds.put(name, new MetricThreshold(valuesString[0], valuesString[1], valuesString[2], valuesString[3], valuesString[4]));
        }
        return thresholds;
    }

    private static List<String> getMetrics(String metricsString) {
        String[] metricAndNames = metricsString.split(",");
        return Arrays.asList(metricAndNames);
    }
}
