package backend.model.metrics;

import java.util.ArrayList;

public class ClassWithMetrics {
    private String name;
    private ArrayList<Metric> classMetrics;
    private ArrayList<MethodWithMetrics> methodWithMetrics;

    public ClassWithMetrics() {
        classMetrics = new ArrayList<>();
        methodWithMetrics = new ArrayList<>();
    }

    public void setClassMetrics(ArrayList<Metric> classMetrics) {
        this.classMetrics = classMetrics;
    }

    public void setAnalysedMethods(ArrayList<MethodWithMetrics> methodWithMetrics) {
        this.methodWithMetrics = methodWithMetrics;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Metric> getClassMetrics() {
        return classMetrics;
    }

    public ArrayList<MethodWithMetrics> getMethodWithMetrics() {
        return methodWithMetrics;
    }
}