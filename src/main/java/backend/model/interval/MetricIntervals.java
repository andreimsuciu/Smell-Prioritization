package backend.model.interval;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;

public class MetricIntervals {
    private final String metricName;
    private final ArrayList<ImmutablePair<Double, Double>> intervals;

    public MetricIntervals(String metricName, ArrayList<ImmutablePair<Double, Double>> intervals) {
        this.metricName = metricName;
        this.intervals = intervals;
    }

    public String getMetricName() {
        return metricName;
    }

    public ArrayList<ImmutablePair<Double, Double>> getIntervals() {
        return intervals;
    }
}
