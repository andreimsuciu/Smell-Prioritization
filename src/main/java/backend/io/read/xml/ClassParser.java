package backend.io.read.xml;

import backend.model.metrics.ClassWithMetrics;
import backend.model.metrics.Metric;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class ClassParser {
    MethodParser methodParser;

    public ClassParser() {
        methodParser = new MethodParser();
    }

    public ArrayList<ClassWithMetrics> parseAllClasses(Document doc) {
        //get all Class nodes in XML
        NodeList classNodeList = doc.getElementsByTagName("Class");
        ArrayList<ClassWithMetrics> classWithMetricsList = new ArrayList<>();
        //iterate through class nodes and parse them
        for (int i = 0; i < classNodeList.getLength(); i++) {
            Node classNode = classNodeList.item(i);
            ClassWithMetrics classWithMetrics = parseClass(classNode);
            classWithMetricsList.add(classWithMetrics);
        }
        return classWithMetricsList;
    }

    private ClassWithMetrics parseClass(Node classNode) {
        ClassWithMetrics classWithMetrics = new ClassWithMetrics();

        if (classNode.getNodeType() == Node.ELEMENT_NODE) {
            Element classElement = (Element) classNode;
            String className = classElement.getAttribute("name");
            classWithMetrics.setName(ParserUtil.getFullPackageName(classNode) + className);
            classWithMetrics.setClassMetrics(ParserUtil.parseMetrics(classElement, Metric.Type.CLASS));
            classWithMetrics.setAnalysedMethods(methodParser.parseForClass(classElement));
        }
        return classWithMetrics;
    }
}
