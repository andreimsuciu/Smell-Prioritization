package backend.io.read.xml;

import backend.io.read.xml.ClassParser;
import backend.model.metrics.ClassWithMetrics;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    public ArrayList<ClassWithMetrics> parseXML(String pathToXml) throws ParserConfigurationException, IOException, SAXException {
        //arrange XML
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        File xmlFile = new File(pathToXml);
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        //get Analysed Classes
        ClassParser classParser = new ClassParser();
        return classParser.parseAllClasses(doc);
    }
}
