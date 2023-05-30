package backend.model.metrics;

public class Metric {
    private final String name;
    private final String value;

    public Metric(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public enum Type {
        CLASS,
        METHOD
    }
}

