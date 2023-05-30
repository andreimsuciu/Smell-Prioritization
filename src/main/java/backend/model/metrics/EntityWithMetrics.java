package backend.model.metrics;

import java.util.ArrayList;

public class EntityWithMetrics {
    private final String name;
    private final ArrayList<Metric> metrics;

    public EntityWithMetrics(String name, ArrayList<Metric> metrics) {
        this.name = name;
        this.metrics = metrics;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Metric> getMetrics() {
        return metrics;
    }
}
