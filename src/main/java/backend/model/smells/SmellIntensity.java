package backend.model.smells;

import backend.model.metrics.Metric;
import backend.utils.Constants;
import java.util.ArrayList;

public class SmellIntensity implements Comparable<SmellIntensity>{
    private final String entityName;
    private final String priority;
    private final ArrayList<Metric> metrics;

    public SmellIntensity(String entityName, String priority, ArrayList<Metric> metrics) {
        this.entityName = entityName;
        this.priority = priority;
        this.metrics = metrics;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPriority() {
        return priority;
    }

    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    @Override
    public int compareTo(SmellIntensity otherSmellIntensity) {
        int thisValue = Constants.priorityOrder.get(this.priority);
        int otherValue = Constants.priorityOrder.get(otherSmellIntensity.priority);
        return otherValue - thisValue;
    }
}
