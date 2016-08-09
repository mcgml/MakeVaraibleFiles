package nhs.genetics.cardiff;

import nhs.genetics.cardiff.framework.IlluminaRunParametersFile;
import nhs.genetics.cardiff.framework.IlluminaSampleSheetFile;
import nhs.genetics.cardiff.framework.PipelineConfigFileMaker;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String version = "2.0.0";
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: <SampleSheet.csv> <RunParameters.xml>");
            System.exit(1);
        }

        log.log(Level.INFO, "Make variable files v" + version);

        IlluminaSampleSheetFile illuminaSampleSheetFile = new IlluminaSampleSheetFile(new File(args[0]));
        IlluminaRunParametersFile illuminaRunParametersFile = new IlluminaRunParametersFile(new File (args[1]));

        try {

            //parse samplesheet
            illuminaSampleSheetFile.parseSampleSheet();
            illuminaSampleSheetFile.populateSampleSheetValues();

            //parse run-parameters
            illuminaRunParametersFile.parseRunParametersXml();

            //make pipeline config files
            PipelineConfigFileMaker.makeIlluminaConfigFile(illuminaSampleSheetFile, illuminaRunParametersFile);

        } catch (Exception e){
            log.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }

    }
}
