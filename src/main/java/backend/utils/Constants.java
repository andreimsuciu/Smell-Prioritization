package backend.utils;

import backend.model.interval.PriorityInterval;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {
    //number of parts in which a metric range is split
    public static final double parts = 4.0;
    //
    public static final ArrayList<PriorityInterval> priorityIntervals = new ArrayList<>() {
        {
            add(new PriorityInterval("VERYLOW", new ImmutablePair<>(1.0, 3.25)));
            add(new PriorityInterval("LOW", new ImmutablePair<>(3.25, 5.5)));
            add(new PriorityInterval("MEDIUM", new ImmutablePair<>(5.5, 7.75)));
            add(new PriorityInterval("HIGH", new ImmutablePair<>(7.75, 10.0)));
            add(new PriorityInterval("VERYHIGH", new ImmutablePair<>(10.0, 10.0)));
        }
    };
    //
    public static final HashMap<String,Integer> priorityOrder = new HashMap<>(){
        {
            put("VERYLOW",0);
            put("LOW",1);
            put("MEDIUM",2);
            put("HIGH",3);
            put("VERYHIGH",4);
        }
    };
}
