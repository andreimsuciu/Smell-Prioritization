package backend.utils;

public class Calculator {
    public static Double calculateIntervalLength(Double beginning, Double ending) {
        double parts = Constants.parts;
        return (ending - beginning) / parts;
    }

    public static Double linearlyScaleVariableToNewInterval(Double x, Double min, Double max, Double targetMin, Double targetMax) {
        return (x - min) / (max - min) * (targetMax - targetMin) + targetMin;
    }
}
