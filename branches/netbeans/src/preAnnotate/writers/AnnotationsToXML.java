package preAnnotate.writers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.jdom.*;
import org.jdom.output.*;
import preAnnotate.format.Annotation;

/**
 * This class will convert .annotations files to .knowtator.xml files.
 *
 * @author Chris Leng
 */
public class AnnotationsToXML {
    //<editor-fold defaultstate="collapsed" desc="Member Variables">

    private int cursorPosition = -1;
    final static String outputExtension = ".knowtator.xml";
    private int mentionID = 0;
    private String outputDirectoryAbsolute;
    private ArrayList<OutputFile> outputFiles;
    private int successfullyExtracted;
    private String errorMessages = "";
    private ArrayList<Annotation> entries;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * This is the constructor for an AnnotationsToXML object.
     *
     * @param annotations The list of annotations that waiting for output.
     * @param outputDirectory The path name for the output directory.
     */
    public AnnotationsToXML(String outputDirectory, ArrayList<Annotation> annotations) {
        entries = annotations;
        successfullyExtracted = 0;
        //sourceFileAbsolute = sourceFile;
        outputDirectoryAbsolute = outputDirectory;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    /**
     * Return some final results about the process, including any errors
     * encountered during the extraction. This should only be called after every
     * file has been extracted.
     *
     * @return - a summary of the process.
     */
    public String getEndSummary() {
        return "***************XML SUMMARY****************\n"
                + "Total clinical notes found: " + outputFiles.size() + "\n"
                + "Files Extracted succesfully: " + successfullyExtracted + "\n"
                + errorMessages
                + "************END OF XML SUMMARY************\n";
    }

    /**
     * This calculates how close to completion this process is.
     *
     * @return - a float representing the completion value(between 0 and 1).
     */
    public float getCompletePercentage() {
        return (float) (cursorPosition + 1) / (float) outputFiles.size();
    }

    /**
     * This method returns the number of output files that will be created.
     *
     * @return - The number of .xml files that the .annotations file has
     * information for.
     */
    public int getNumberOfFiles() {
        return outputFiles.size();
    }

    /**
     * This method is used to verify if there is another file to extract.
     *
     * @return - True if there is another file that needs to be extracted, false
     * otherwise.
     */
    public boolean hasNext() {
        //If the names haven't been found yet... find them.
        if (outputFiles == null) {
            preProcess();
        }
        if (cursorPosition + 1 < outputFiles.size()) {
            return true;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * This method will extract the names of all of the files that are annotated
     * in this .annotations file.
     */
    private void preProcess() {
        //Initialize member variable
        outputFiles = new ArrayList<OutputFile>();
        for (Annotation annotation : entries) {
            boolean unique = true;
            for (OutputFile file : outputFiles) {
                if (file.fileName.equals(annotation.getSource())) {
                    unique = false;

                }

            }
            if (unique) {
                outputFiles.add(new OutputFile(annotation.getSource()));
            }

        }
        populateIndividualFiles();
    }

    /**
     * This method will populate individual files with annotations, in other
     * words it'll split the large list of annotations into sublists based on
     * the file they originated from.
     */
    private void populateIndividualFiles() {
        for (Annotation annotation : entries) {
            for (OutputFile output : outputFiles) {
                if (annotation.getSource().equals(output.fileName)) {
                    output.addALine(annotation);
                }
            }
        }

    }

    /**
     * This method will create the structure for the XML output.
     *
     * @param _root - the root for the nodes to be created.
     * @param lines - The .annotations lines for this file.
     * @return - an Element for the XML file.
     */
    private Element XMLNodesforClasses(Element _root, ArrayList<Annotation> lines) {
        Element root = _root;


        int amount_of_found = 0;

        //Extract a node from each .annotations line.
        for (int nodeIndex = 0; nodeIndex < lines.size(); nodeIndex++) {
            //Get the unique mention number for this node.
            int mentionNum = env.Parameters.getLatestUsedMentionID() + 1;
            env.Parameters.updateLatestUsedMentionID(mentionNum);

            //Extract a .annotations line and extract all needed information from it.
            Annotation toExtract = lines.get(nodeIndex);

            String text = toExtract.getSpanText();
            String textClass = toExtract.getTheClass();
            String tempSpan = toExtract.getSpan();
            String start = tempSpan.split("\\|")[0];
            String end = tempSpan.split("\\|")[1];
            String date = toExtract.getCreationDate();
            String annotatorName;
            if (toExtract.getAnnotator().getAnnotatorName() != null) {
                annotatorName = toExtract.getAnnotator().getAnnotatorName();
            } else {
                annotatorName = "Unknown";
            }
            String annotatorID = toExtract.getAnnotator().getAnnotatorID();
            // -----------------------------------------------------elements
            Element annotation = new Element("annotation");
            Element mention = new Element("mention");
            mention.setAttribute("id", "ISDS_ILI_VA_Instance_" + mentionNum);
            annotation.addContent(mention);

            Element annotator = new Element("annotator");
            annotator.setAttribute("id", annotatorID);
            annotator.setText(annotatorName);
            annotation.addContent(annotator);

            Element span = new Element("span");
            span.setAttribute("start", String.valueOf(start));
            span.setAttribute("end", String.valueOf(end));
            annotation.addContent(span);

            annotation.addContent(new Element("spannedText").setText(text));
            annotation.addContent(new Element("creationDate").setText(date));

            root.addContent(annotation);
            // -----------------------------------------------------elements end


            // -----------------------------------------------------classmention begin
            // "classMention" data structure and get stored data in memory
            Element classMention = new Element("classMention");
            classMention.setAttribute("id", "ISDS_ILI_VA_Instance_" + mentionNum);
            Element mentionClass = new Element("mentionClass");
            mentionClass.setAttribute("id", textClass);
            mentionClass.addContent(text);
            //add this node <ClassMention> to xml tree
            classMention.addContent(mentionClass);
            root.addContent(classMention);
            // -----------------------------------------------------classmention end

            amount_of_found++;
        }
        return root;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This method will extract the xml for the next file contained in the
     * .annotations file.
     *
     * @return - A String summarizing the process.
     */
    public String extractNext() {
        String summary = "";
        //If filenames have not been processed yet... process them.
        if (outputFiles == null) {
            preProcess();
        }
        //Increment cursor position because we are extracting the next file.
        cursorPosition++;
        summary += outputFiles.get(cursorPosition).extractXML();
        return summary;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Internal Class for Outputting files">
    /**
     * This class represents an outputFile object. These files are created using
     * the source file names found in a .annotations file. All lines that are
     * from the same source file should be added to a single OutputFile object.
     */
    private class OutputFile {

        //Member variables
        String fileName;
        ArrayList<Annotation> lines;

        /**
         * The constructor for and OutputFile object.
         *
         * @param name - the name of the source file.
         */
        private OutputFile(String name) {
            fileName = name;
            lines = new ArrayList<Annotation>();
        }

        /**
         * This method will add a String(line) to the ArrayList of lines for
         * this OutputFile object.
         *
         * @param line - An annotation line(should be five entries with tab
         * delimiters)
         * @return - true if the line meets the five tab delimited entries
         * requirement, false otherwise.
         */
        private void addALine(Annotation line) {
            lines.add(line);

        }

        /**
         * Extract the XML from all lines that were added to this object. This
         * should only be called when all lines have been added.
         *
         * @return - A summary of the process.
         */
        private String extractXML() {
            String summary = "";

            //The root node for our xml document.
            Element root = new Element("annotations");
            root.setAttribute("textSource", this.fileName);
            Document Doc = new Document(root);

            //Add all annotation nodes along with the mention nodes.
            root = XMLNodesforClasses(root, this.lines);

            /**
             * output xml file to disk
             */
            // XML storage processing: phycial writing
            Format format = Format.getCompactFormat();
            format.setEncoding("UTF-8"); // set XML encodeing
            format.setIndent("    ");
            XMLOutputter XMLOut = new XMLOutputter(format);
            //Try to create the file.
            try {
                File output = new File(outputDirectoryAbsolute + "\\" + this.fileName + outputExtension);
                output.createNewFile();
                System.out.println(output.getAbsolutePath());
            } catch (IOException e) {
                return (e.getLocalizedMessage() + "\n");
            }
            // Try to write the output to the file.
            try {

                XMLOut.output(Doc, new FileOutputStream(outputDirectoryAbsolute + "\\" + this.fileName + outputExtension));
                successfullyExtracted++;
            } catch (IOException e) {
                return (e.getLocalizedMessage() + "\n");
            }

            summary += "XML for " + this.fileName + " created successfully at " + outputDirectoryAbsolute + "\n";
            return summary;
        }
    }
    //</editor-fold>
}
