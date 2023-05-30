package frontend;

import backend.analysis.Analyser;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;

public class AnalyzeTask extends Task<Void> {

    List<File> files;
    String projectName;

    public AnalyzeTask(List<File> files, String projectName) {
        this.files = files;
        this.projectName = projectName;
    }

    @Override
    protected Void call() throws Exception {
        for (int i = 0; i < files.size(); i++) {
            Analyser analyser = new Analyser(files.get(i).getName(), projectName);
            analyser.analyse();
            updateProgress(i + 1, files.size());
        }
        return null;
    }
}
