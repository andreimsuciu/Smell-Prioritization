package backend.model.smells;

import backend.utils.Comparator;
import backend.utils.Config;
import backend.model.metrics.ClassWithMetrics;
import backend.model.metrics.MethodWithMetrics;
import backend.model.metrics.Metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmellChecker {
    private final HashMap<String, FormulaForMetric> formula;
    private final Map<String, MetricThreshold> metricThresholds;
    private final Smell smell;

    public SmellChecker(Smell smell) {
        this.formula = Config.getFormula(smell);
        this.metricThresholds = Config.getThresholds(smell);
        this.smell = smell;
    }

    public boolean checkClass(ClassWithMetrics classWM) {
        return switch (this.smell) {
            case GODCLASS -> checkGodClass(classWM);
            case DATACLASS -> checkDataClass(classWM);
            default -> throw new IllegalStateException("Unexpected value: " + this.smell);
        };
    }

    public boolean checkMethod(MethodWithMetrics methodWM) {
        ArrayList<Metric> methodMetrics = methodWM.getMethodMetrics();
        for (Metric metric : methodMetrics) {
            String metricName = metric.getName();
            //metric not part of smell
            if (!metricThresholds.containsKey(metricName)) {
            } else if (!checkMetric(metric)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDataClass(ClassWithMetrics classWM) {
        ArrayList<Metric> classMetrics = classWM.getClassMetrics();
        Metric NOPA = new Metric("", "");
        Metric NOAC = new Metric("", "");
        Metric WMC = new Metric("", "");
        for (Metric metric : classMetrics) {
            switch (metric.getName()) {
                case "NOPA" -> NOPA = metric;
                case "NOAC" -> NOAC = metric;
                case "WMC" -> WMC = metric;
            }
        }
        if (WMC.getValue().equals("N/A")) {
            return false;
        }
        int valueNOPA = Integer.parseInt(NOPA.getValue());
        int valueNOAC = Integer.parseInt(NOAC.getValue());
        int NOPANOAM = valueNOPA + valueNOAC;
        Metric NOPANOAC = new Metric("NOPA+NOAC", String.valueOf(NOPANOAM));
        return checkMetric(NOPANOAC) && checkMetric(WMC);
    }

    private boolean checkGodClass(ClassWithMetrics classWM) {
        ArrayList<Metric> classMetrics = classWM.getClassMetrics();
        for (Metric metric : classMetrics) {
            String metricName = metric.getName();
            //is interface
            if (metric.getValue().equals("N/A")) {
                return false;
            }
            //metric not part of smell
            else if (!metricThresholds.containsKey(metricName)) {
            } else if (!checkMetric(metric)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkMetric(Metric metric) {
        String metricName = metric.getName();
        FormulaForMetric formulaForMetric = formula.get(metricName);

        Double metricValue = Double.valueOf(metric.getValue());
        Double thresholdValue = Double.valueOf(metricThresholds.get(metricName)
                .get(formulaForMetric.getThreshold()));
        String comparator = formulaForMetric.getComparison();

        return Comparator.compare(metricValue, thresholdValue, comparator);
    }
}
