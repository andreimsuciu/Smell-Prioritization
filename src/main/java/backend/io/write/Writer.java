package backend.io.write;

import backend.model.metrics.ClassWithMetrics;
import backend.model.metrics.MethodWithMetrics;
import backend.model.metrics.Metric;
import backend.model.smells.Smell;
import backend.model.smells.SmellIntensity;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {

    public static void writeSmellToFile(ArrayList<SmellIntensity> smellClasses, String path, Smell smell) throws IOException {
        String fileString = path;
        switch (smell) {
            case GODCLASS -> fileString += "/godClass.csv";
            case DATACLASS -> fileString += "/dataClass.csv";
            case BRAINMETHOD -> fileString += "/brainMethod.csv";
            case DISPERSEDCOUPLING -> fileString += "/dispersedCoupling.csv";
        }

        File file = new File(fileString);
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile,';', CSVWriter.NO_QUOTE_CHARACTER);

        for (SmellIntensity smellIntensity : smellClasses) {

            StringBuilder metricStringBuilder = new StringBuilder();
            for(Metric metric: smellIntensity.getMetrics()){
                metricStringBuilder.append(metric.getName()).append(":").append(metric.getValue()).append("&");
            }
            metricStringBuilder.deleteCharAt(metricStringBuilder.length()-1);
            String[] data = {smellIntensity.getEntityName(), smellIntensity.getPriority(), metricStringBuilder.toString()};
            writer.writeNext(data);
        }

        // closing writer connection
        writer.close();
    }
}
