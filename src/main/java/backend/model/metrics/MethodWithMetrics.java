package backend.model.metrics;

import java.util.ArrayList;

public class MethodWithMetrics {
    private String name;
    private ArrayList<Metric> methodMetrics;

    public void setMethodMetrics(ArrayList<Metric> methodMetrics) {
        this.methodMetrics = methodMetrics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Metric> getMethodMetrics() {
        return methodMetrics;
    }
}