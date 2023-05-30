package backend.io.read.csv;

import backend.model.metrics.Metric;
import backend.model.smells.SmellIntensity;
import backend.utils.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalysisReader {

    public static List<String> getProjectVersions(String projectName){
        String path = Config.analysisFolderPath + "/" + projectName;
        File file = new File(path);
        String[] directories = file.list((dir, name) -> new File(dir, name).isDirectory());
        if (directories != null) {
            return Arrays.asList(directories);
        }
        return new ArrayList<>();
    }

    public static List<SmellIntensity> getSmellIntensity(String projectName, String projectVersion, String smellCSV) throws IOException {
        String path = Config.analysisFolderPath + "/" + projectName + "/" + projectVersion + "/" + smellCSV;
        ArrayList<SmellIntensity> smellIntensities = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(";");

            ArrayList<Metric> metrics = new ArrayList<>();
            String[] metricsString = values[2].split("&");
            for (String s : metricsString) {
                String[] metric = s.split(":");
                metrics.add(new Metric(metric[0], metric[1]));
            }

            smellIntensities.add(new SmellIntensity(values[0],values[1], metrics));
        }
        return smellIntensities;
    }
}
