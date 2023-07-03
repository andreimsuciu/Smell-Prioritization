package frontend.model;

import backend.model.metrics.Metric;
import backend.model.smells.Smell;
import backend.utils.Constants;

import java.util.ArrayList;

public class SmellVersionComparison implements Comparable<SmellVersionComparison>{
    private final String entityName;
    private final String priorityOld;
    private String priorityNew;
    private final Smell smell;
    private final ArrayList<Metric> metricOld;
    private ArrayList<Metric> metricsNew;

    public SmellVersionComparison(String entityName, String priorityOld, Smell smell, ArrayList<Metric> metricOld) {
        this.entityName = entityName;
        this.priorityOld = priorityOld;
        this.smell = smell;
        this.metricOld = metricOld;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPriorityOld() {
        return priorityOld;
    }

    public String getPriorityNew() {
        return priorityNew;
    }

    public ArrayList<Metric> getMetricOld() {
        return metricOld;
    }

    public ArrayList<Metric> getMetricsNew() {
        return metricsNew;
    }

    public Smell getSmell() {
        return smell;
    }

    public void setMetricsNew(ArrayList<Metric> metricsNew) {
        this.metricsNew = metricsNew;
    }

    public void setPriorityNew(String priorityNew) {
        this.priorityNew = priorityNew;
    }

    @Override
    public int compareTo(SmellVersionComparison otherSmellIntensity) {
        int thisValue = Constants.priorityOrder.get(this.priorityOld);
        int otherValue = Constants.priorityOrder.get(otherSmellIntensity.priorityOld);
        return otherValue - thisValue;
    }
}
