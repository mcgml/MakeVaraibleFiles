package nhs.genetics.cardiff;

import nhs.genetics.cardiff.framework.IlluminaSampleSheetFile;
import nhs.genetics.cardiff.framework.PipelineConfigFileMaker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String version = "2.1.0";
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: <SampleSheet.csv> <RunParameters.xml>");
            System.exit(1);
        }

        log.log(Level.INFO, "Make variable files v" + version);

        IlluminaSampleSheetFile illuminaSampleSheetFile = new IlluminaSampleSheetFile(new File(args[0]));
        File runParametersFile = new File(args[1]);

        try {

            //parse samplesheet
            illuminaSampleSheetFile.parseSampleSheet();
            illuminaSampleSheetFile.populateSampleSheetValues();

            //parse run-parameters
            String runIdentifier = searchXmlFile("RunParameters", "RunID", runParametersFile);

            //make pipeline config files
            PipelineConfigFileMaker.makeIlluminaConfigFile(illuminaSampleSheetFile, runIdentifier);

        } catch (Exception e){
            log.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }

    }

    private static String searchXmlFile(String nodeName, String nodeKeyName, File runParametersFile) {

        Element fstElmnt, fstNmElmnt;
        NodeList fstNmElmntLst, fstNm;

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(runParametersFile);
            doc.getDocumentElement().normalize();

            //get flowcell arguments
            NodeList nodeLst = doc.getElementsByTagName(nodeName);
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    fstElmnt = (Element) fstNode;
                    fstNmElmntLst = fstElmnt.getElementsByTagName(nodeKeyName);
                    fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    fstNm = fstNmElmnt.getChildNodes();

                    return ((Node) fstNm.item(0)).getNodeValue();
                }

            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Could not parse RunParameters.xml: " + e.getMessage());
        }

        return null;
    }
}
