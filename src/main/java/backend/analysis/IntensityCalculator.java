package backend.analysis;

import backend.model.interval.PriorityInterval;
import backend.model.smells.SmellIntensity;
import backend.utils.Calculator;
import backend.utils.Config;
import backend.utils.Constants;
import backend.model.interval.MetricIntervals;
import backend.model.metrics.EntityWithMetrics;
import backend.model.metrics.Metric;
import backend.model.smells.FormulaForMetric;
import backend.model.smells.MetricThreshold;
import backend.model.smells.Smell;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IntensityCalculator {
    public final Smell smell;
    //list of metrics and their respective intervals
    private final ArrayList<MetricIntervals> metricIntervals;
    private final ArrayList<String> metricsForSmell;

    public IntensityCalculator(Smell smell) {
        this.smell = smell;
        this.metricIntervals = new ArrayList<>();
        this.metricsForSmell = new ArrayList<>();
        calculateIntervalsForSmell(smell);
    }

    public SmellIntensity calculateSmellIntensity(EntityWithMetrics entityWithMetrics) {
        Double smellAverage = calculateSmellAverage(entityWithMetrics);
        for (PriorityInterval priorityInterval : Constants.priorityIntervals) {
            if (smellAverage >= priorityInterval.getInterval().left &&
                    smellAverage < priorityInterval.getInterval().right) {
                return new SmellIntensity(entityWithMetrics.getName(), priorityInterval.getName(), entityWithMetrics.getMetrics());
            }
        }
        //if over the threshold return max priority
        return new SmellIntensity(entityWithMetrics.getName(),
                Constants.priorityIntervals.get(Constants.priorityIntervals.size() - 1).getName(),
                entityWithMetrics.getMetrics());
    }

    private Double calculateSmellAverage(EntityWithMetrics entityWithMetrics) {
        Double sum = (double) 0;

        for (Metric metric : entityWithMetrics.getMetrics()) {
            if (!metricsForSmell.contains(metric.getName())) {
                continue;
            }
            sum += calculateMetricIntensity(metric);
        }

        //handleNOPANOAC
        Metric metric = getNOPANOAC(entityWithMetrics);
        if (!metric.getValue().equals("0")) {
            sum += Double.parseDouble(metric.getValue());
            return sum / metricsForSmell.size() + 1;
        }

        return sum / metricsForSmell.size();
    }

    private Metric getNOPANOAC(EntityWithMetrics entityWithMetrics) {
        Metric NOPA = new Metric("", "0");
        Metric NOAC = new Metric("", "0");
        for (Metric metric : entityWithMetrics.getMetrics()) {
            switch (metric.getName()) {
                case "NOPA" -> NOPA = metric;
                case "NOAC" -> NOAC = metric;
            }
        }
        int valueNOPA = Integer.parseInt(NOPA.getValue());
        int valueNOAC = Integer.parseInt(NOAC.getValue());
        int NOPANOAM = valueNOPA + valueNOAC;
        return new Metric("NOPA+NOAC", String.valueOf(NOPANOAM));
    }

    private Double calculateMetricIntensity(Metric metric) {
        int intervalForMetricIndex = checkIntervalForMetric(metric);
        //over the maximum threshold -> high priority
        if (intervalForMetricIndex == (int) Constants.parts) {
            return Constants.priorityIntervals.get(intervalForMetricIndex).getInterval().right;
        }
        //get the interval in which this metric's value is found
        ImmutablePair<Double, Double> currentInterval = new ImmutablePair<>((double) 0, (double) 0);
        for (MetricIntervals metricInterval : metricIntervals) {
            if (metricInterval.getMetricName().equals(metric.getName())) {
                currentInterval = metricInterval.getIntervals().get(intervalForMetricIndex);
            }
        }
        //swap the mapping
        if (Config.getFormula(smell).get(metric.getName()).getComparison().equals("<")) {
            currentInterval = new ImmutablePair<>(currentInterval.right, currentInterval.left);
        }
        //get the target interval
        ImmutablePair<Double, Double> priorityInterval =
                Constants.priorityIntervals
                .get(intervalForMetricIndex)
                .getInterval();
        //return the scaled variable
        return Calculator.linearlyScaleVariableToNewInterval(
                Double.valueOf(metric.getValue()),
                currentInterval.left, currentInterval.right,
                priorityInterval.left, priorityInterval.right
        );
    }


    private int checkIntervalForMetric(Metric metric) {
        double metricValue = Double.parseDouble(metric.getValue());
        for (MetricIntervals metricIntervals : metricIntervals) {
            //get intervals for this metric
            if (!metricIntervals.getMetricName().equals(metric.getName())) {
                continue;
            }
            ArrayList<ImmutablePair<Double, Double>> intervals = metricIntervals.getIntervals();
            for (int i = 0; i < intervals.size(); i++) {
                //check if in interval
                if (metricValue >= intervals.get(i).left &&
                        metricValue < intervals.get(i).right) {
                    return i;
                    //over the threshold -> very high priority
                } else if (i == intervals.size() - 1) {
                    return intervals.size();
                }
            }
        }
        return 0;
    }

    private void calculateIntervalsForSmell(Smell smell) {
        HashMap<String, FormulaForMetric> formula = Config.getFormula(smell);
        Map<String, MetricThreshold> metricThresholds = Config.getThresholds(smell);

        for (Map.Entry<String, FormulaForMetric> formulaForMetricEntry : formula.entrySet()) {
            String metricName = formulaForMetricEntry.getKey();
            metricsForSmell.add(metricName);
            FormulaForMetric formulaForMetric = formulaForMetricEntry.getValue();
            ArrayList<ImmutablePair<Double, Double>> intervals = getIntervals(metricName, formulaForMetric, metricThresholds);
            metricIntervals.add(new MetricIntervals(metricName, intervals));
        }
    }

    private ArrayList<ImmutablePair<Double, Double>> getIntervals(String metricName, FormulaForMetric formulaForMetric, Map<String, MetricThreshold> metricThresholds) {
        //split interval [threshold, thresholdExtremity] (or inverse)
        double thresholdValue = Double.parseDouble(metricThresholds.get(metricName).get(formulaForMetric.getThreshold()));
        Double thresholdValueExtremity = getThresholdExtremity(metricName, formulaForMetric, metricThresholds);


        //if extremity is on the left, swap the values so the interval begins with the smaller number
        boolean reverse = false;
        if (thresholdValue > thresholdValueExtremity) {
            double exchange = thresholdValue;
            thresholdValue = thresholdValueExtremity;
            thresholdValueExtremity = exchange;
            reverse = true;
        }

        double intervalLength = Calculator.calculateIntervalLength(thresholdValue, thresholdValueExtremity);
        ArrayList<ImmutablePair<Double, Double>> intervals = new ArrayList<>();

        for (int i = 0; i < (int) Constants.parts; i++) {
            intervals.add(new ImmutablePair<>(thresholdValue + intervalLength * i, thresholdValue + intervalLength * (i + 1)));
        }

        //swap again to map to correct priority
        if (reverse) {
            Collections.reverse(intervals);
        }

        return intervals;
    }

    private Double getThresholdExtremity(String metricName, FormulaForMetric formulaForMetric, Map<String, MetricThreshold> metricThresholds) {
        if (formulaForMetric.getComparison().equals(">") || formulaForMetric.getComparison().equals(">=")) {
            return Double.valueOf(metricThresholds.get(metricName).get("VERYHIGH"));
        } else if (formulaForMetric.getComparison().equals("<") || formulaForMetric.getComparison().equals("<=")) {
            return Double.valueOf(metricThresholds.get(metricName).get("VERYLOW"));
        }
        return (double) 0;
    }
}
