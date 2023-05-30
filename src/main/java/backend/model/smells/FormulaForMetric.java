package backend.model.smells;

public class FormulaForMetric {
    private final String comparison;
    private final String threshold;

    public FormulaForMetric(String comparison, String threshold) {
        this.comparison = comparison;
        this.threshold = threshold;
    }

    public String getComparison() {
        return comparison;
    }

    public String getThreshold() {
        return threshold;
    }
}
