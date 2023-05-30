package backend.model.interval;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;

public class PriorityInterval {
    private final String name;
    private final ImmutablePair<Double, Double> interval;

    public PriorityInterval(String name, ImmutablePair<Double, Double> interval) {
        this.name = name;
        this.interval = interval;
    }

    public ImmutablePair<Double, Double> getInterval() {
        return interval;
    }

    public String getName() {
        return name;
    }
}
