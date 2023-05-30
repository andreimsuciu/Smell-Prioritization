package backend.io.read.xml;

import backend.model.metrics.MethodWithMetrics;
import backend.model.metrics.Metric;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class MethodParser {
    public ArrayList<MethodWithMetrics> parseForClass(Element classElement) {
        NodeList methodNodes = classElement.getElementsByTagName("Method");
        ArrayList<MethodWithMetrics> methodWithMetricsList = new ArrayList<>();
        for (int i = 0; i < methodNodes.getLength(); i++) {
            Node methodNode = methodNodes.item(i);

            //the if checks that the method's parent is the class
            Element methodParentElement = (Element) methodNode.getParentNode();
            String methodParentName = methodParentElement.getAttribute("name");
            if (methodParentName.equals(classElement.getAttribute("name"))) {
                MethodWithMetrics methodWithMetrics = parse(methodNode);
                methodWithMetricsList.add(methodWithMetrics);
            }
        }
        return methodWithMetricsList;
    }

    private MethodWithMetrics parse(Node methodNode) {
        MethodWithMetrics methodWithMetrics = new MethodWithMetrics();

        if (methodNode.getNodeType() == Node.ELEMENT_NODE) {
            Element methodElement = (Element) methodNode;
            String methodName = methodElement.getAttribute("name");
            methodWithMetrics.setName(ParserUtil.getFullPackageName(methodNode) + methodName);
            methodWithMetrics.setMethodMetrics(ParserUtil.parseMetrics(methodElement, Metric.Type.METHOD));
        }

        return methodWithMetrics;
    }
}
