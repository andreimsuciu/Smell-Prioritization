package backend.utils;

public class Comparator {
    public static boolean compare(Double firstValue, Double secondValue, String comparator) {
        return switch (comparator) {
            case ">" -> firstValue > secondValue;
            case ">=" -> firstValue >= secondValue;
            case "<" -> firstValue < secondValue;
            case "<=" -> firstValue <= secondValue;
            default -> false;
        };
    }
}
