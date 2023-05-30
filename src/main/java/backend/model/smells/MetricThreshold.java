package backend.model.smells;

public class MetricThreshold {
    private final String VERY_LOW;
    private final String LOW;
    private final String MEDIUM;
    private final String HIGH;
    private final String VERY_HIGH;

    public MetricThreshold(String VERY_LOW, String LOW, String MEDIUM, String HIGH, String VERY_HIGH) {
        this.VERY_LOW = VERY_LOW;
        this.LOW = LOW;
        this.MEDIUM = MEDIUM;
        this.HIGH = HIGH;
        this.VERY_HIGH = VERY_HIGH;
    }

    public String get(String value) {
        return switch (value) {
            case ("VERYLOW") -> this.VERY_LOW;
            case ("LOW") -> this.LOW;
            case ("MEDIUM") -> this.MEDIUM;
            case ("HIGH") -> this.HIGH;
            case ("VERYHIGH") -> this.VERY_HIGH;
            default -> "";
        };
    }
}
