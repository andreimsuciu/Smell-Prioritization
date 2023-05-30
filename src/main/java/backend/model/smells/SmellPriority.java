package backend.model.smells;

public class SmellPriority {
    private final String priority;
    private final Smell smell;

    public SmellPriority(String priority, Smell smell) {
        this.priority = priority;
        this.smell = smell;
    }

    public String getPriority() {
        return priority;
    }

    public Smell getSmell() {
        return smell;
    }
}
