package nhs.genetics.cardiff;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final double version = 0.1;
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: <SampleSheet.csv> <runParameters.xml>");
            System.exit(1);
        }

        log.log(Level.INFO, "Make variable files v" + version);

        //parse samplesheet
        IlluminaSampleSheetFile illuminaSampleSheetFile = new IlluminaSampleSheetFile(new File(args[0]));
        illuminaSampleSheetFile.parseSampleSheet();
        illuminaSampleSheetFile.populateSampleSheetValues();

        //parse run-parameters
        IlluminaRunParametersFile illuminaRunParametersFile = new IlluminaRunParametersFile(new File (args[1]));
        illuminaRunParametersFile.parseRunParametersXml();

        //make pipeline config files
        PipelineConfigFileMaker.makeIlluminaConfigFile(illuminaSampleSheetFile, illuminaRunParametersFile);

    }
}
