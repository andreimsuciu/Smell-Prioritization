package backend.analysis;

import backend.model.metrics.EntityWithMetrics;
import backend.model.smells.SmellIntensity;
import backend.utils.Config;
import backend.io.write.Writer;
import backend.model.metrics.ClassWithMetrics;
import backend.model.metrics.MethodWithMetrics;
import backend.model.smells.Smell;
import backend.model.smells.SmellChecker;
import backend.io.read.xml.Parser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Analyser {
    String pathToVersionAnalysisFolder;
    String pathToXml;

    public Analyser(String pathToXml, String projectName) throws IOException {
        this.pathToXml = pathToXml;
        String version = getVersionFromXmlPath(pathToXml);
        this.pathToVersionAnalysisFolder = Config.analysisFolderPath + "/" + projectName + "/" + version;
        createDirs();
    }

    public void analyse() throws IOException, SAXException, ParserConfigurationException {
        Parser parser = new Parser();
        ArrayList<ClassWithMetrics> classWithMetricsList = parser.parseXML(pathToXml);
        calculateSmell(Smell.GODCLASS, classWithMetricsList);
        calculateSmell(Smell.DATACLASS, classWithMetricsList);
        calculateSmell(Smell.BRAINMETHOD, classWithMetricsList);
        calculateSmell(Smell.DISPERSEDCOUPLING, classWithMetricsList);
    }

    private void calculateSmell(Smell smell, ArrayList<ClassWithMetrics> classWithMetricsList) throws IOException, SAXException, ParserConfigurationException {
        ArrayList<SmellIntensity> smellIntensities;
        if (smell.equals(Smell.GODCLASS) || smell.equals(Smell.DATACLASS)) {
            smellIntensities = getClassSmellIntensities(smell, classWithMetricsList);
        } else {
            smellIntensities = getMethodSmellIntensities(smell, classWithMetricsList);
        }
        Writer.writeSmellToFile(smellIntensities, pathToVersionAnalysisFolder, smell);
    }

    private ArrayList<SmellIntensity> getClassSmellIntensities(Smell smell, ArrayList<ClassWithMetrics> classWithMetricsList) {
        ArrayList<SmellIntensity> smellIntensities = new ArrayList<>();
        ArrayList<ClassWithMetrics> smellClasses = calculateClassSmells(classWithMetricsList, smell);
        IntensityCalculator intensityCalculator = new IntensityCalculator(smell);
        for (ClassWithMetrics smellClass : smellClasses) {
            smellIntensities.add(
                    intensityCalculator.calculateSmellIntensity(
                            new EntityWithMetrics(smellClass.getName(), smellClass.getClassMetrics())
                    )
            );
        }
        return smellIntensities;
    }

    private ArrayList<SmellIntensity> getMethodSmellIntensities(Smell smell, ArrayList<ClassWithMetrics> classWithMetricsList) {
        ArrayList<SmellIntensity> smellIntensities = new ArrayList<>();
        ArrayList<MethodWithMetrics> smellMethods = calculateMethodSmells(classWithMetricsList, smell);
        IntensityCalculator intensityCalculator = new IntensityCalculator(smell);
        for (MethodWithMetrics smellMethod : smellMethods) {
            smellIntensities.add(
                    intensityCalculator.calculateSmellIntensity(
                            new EntityWithMetrics(smellMethod.getName(), smellMethod.getMethodMetrics())
                    )
            );
        }
        return smellIntensities;
    }

    private ArrayList<ClassWithMetrics> calculateClassSmells(ArrayList<ClassWithMetrics> classList, Smell smell) {
        SmellChecker smellChecker = new SmellChecker(smell);
        ArrayList<ClassWithMetrics> smellClasses = new ArrayList<>();
        for (ClassWithMetrics classWM : classList) {
            if (smellChecker.checkClass(classWM)) {
                smellClasses.add(classWM);
            }
        }
        return smellClasses;
    }

    private ArrayList<MethodWithMetrics> calculateMethodSmells(ArrayList<ClassWithMetrics> classList, Smell smell) {
        SmellChecker smellChecker = new SmellChecker(smell);
        ArrayList<MethodWithMetrics> smellMethods = new ArrayList<>();
        for (ClassWithMetrics classWM : classList) {
            for (MethodWithMetrics methodWm : classWM.getMethodWithMetrics()) {
                if (smellChecker.checkMethod(methodWm)) {
                    smellMethods.add(methodWm);
                }
            }
        }
        return smellMethods;
    }

    private String getVersionFromXmlPath(String pathToXml) {
        String[] elements = pathToXml.split("/");
        String[] versionxml = elements[elements.length - 1].split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < versionxml.length - 1; i++) {
            sb.append(versionxml[i]);
            sb.append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void createDirs() throws IOException {
        Files.createDirectories(Paths.get(pathToVersionAnalysisFolder));
    }
}
