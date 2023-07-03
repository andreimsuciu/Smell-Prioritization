package frontend.tasks;

import backend.io.read.csv.AnalysisReader;
import backend.model.smells.Smell;
import frontend.model.SmellPriority;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleProjectsTask extends Task<HashMap<String, List<SmellPriority>>> {
    List<String> selectedVersions;
    String projectName;

    public MultipleProjectsTask(List<String> selectedVersions, String projectName) {
        this.selectedVersions = selectedVersions;
        this.projectName = projectName;
    }

    @Override
    protected HashMap<String, List<SmellPriority>> call() throws Exception {
        HashMap<String, List<SmellPriority>> versionSmells = new HashMap<>();
        int i = 0;
        for (String version : selectedVersions) {
            List<SmellPriority> smellPriorities = AnalysisReader.getSmellIntensity(projectName, version, "godClass.csv").stream()
                    .map(smellInt -> new SmellPriority(smellInt.getPriority(), Smell.GODCLASS)).collect(Collectors.toList());
            updateProgress(++i, selectedVersions.size());

            smellPriorities.addAll(AnalysisReader.getSmellIntensity(projectName, version, "dataClass.csv").stream()
                    .map(smellInt -> new SmellPriority(smellInt.getPriority(), Smell.DATACLASS)).collect(Collectors.toList()));
            updateProgress(++i, selectedVersions.size());

            smellPriorities.addAll(AnalysisReader.getSmellIntensity(projectName, version, "brainMethod.csv").stream()
                    .map(smellInt -> new SmellPriority(smellInt.getPriority(), Smell.BRAINMETHOD)).collect(Collectors.toList()));
            updateProgress(++i, selectedVersions.size());

            smellPriorities.addAll(AnalysisReader.getSmellIntensity(projectName, version, "dispersedCoupling.csv").stream()
                    .map(smellInt -> new SmellPriority(smellInt.getPriority(), Smell.DISPERSEDCOUPLING)).collect(Collectors.toList()));

            versionSmells.put(version, smellPriorities);
            updateProgress(++i, selectedVersions.size());
        }
        return versionSmells;
    }
}
