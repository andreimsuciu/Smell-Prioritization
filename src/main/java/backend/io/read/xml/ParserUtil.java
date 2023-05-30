package backend.io.read.xml;

import backend.utils.Config;
import backend.model.metrics.Metric;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ParserUtil {
    public static ArrayList<Metric> parseMetrics(Element classElement, Metric.Type type) {
        //check what kind of metric is being parsed
        List<String> configList;
        if (type == Metric.Type.CLASS) {
            configList = Config.classMetrics;
        } else {
            configList = Config.methodMetrics;
        }

        //get Metric nodes inside the Metrics node for current Element
        Element metricsElement = (Element) classElement.getElementsByTagName("Metrics").item(0);
        NodeList metricsNodeList = metricsElement.getElementsByTagName("Metric");
        ArrayList<Metric> metrics = new ArrayList<>();
        //iterate through Metric nodes
        for (int i = 0; i < metricsNodeList.getLength(); i++) {
            Node metricNode = metricsNodeList.item(i);
            if (metricNode.getNodeType() == Node.ELEMENT_NODE) {
                Element metricElement = (Element) metricNode;
                //Build Metric and add to metrics list
                String metricName = metricElement.getAttribute("name");
                if (configList.contains(metricName)) {
                    Metric metric = new Metric(metricElement.getAttribute("name"), metricElement.getAttribute("value"));
                    metrics.add(metric);
                }
            }
        }
        return metrics;
    }

    public static String getFullPackageName(Node classNode) {
        StringBuilder name = new StringBuilder();
        Node currentClassNode = classNode;
        //go through parent nodes until we get null, and put the name in front
        while (true) {
            Node parentNode = currentClassNode.getParentNode();
            if (parentNode == null) {
                break;
            }
            if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element parentElement = (Element) parentNode;
                String parentName = parentElement.getAttribute("name");
                if (!parentName.isEmpty()) {
                    name.insert(0, parentName + ".");
                }
            }
            currentClassNode = parentNode;
        }
        return name.toString();
    }
}
