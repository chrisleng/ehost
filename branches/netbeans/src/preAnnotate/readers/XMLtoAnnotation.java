/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preAnnotate.readers;

import imports.importedXML.eXMLFile;
import java.util.ArrayList;
import java.util.Vector;
import preAnnotate.format.Annotation;
import preAnnotate.format.Annotator;

/**
 * This class is used to convert XML information into Annotation information
 * @author Kyle
 */
public class XMLtoAnnotation
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    //Member variables
    private eXMLFile aNewOne;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Properties">
    public String lastProcessSummary;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Construct
     * @param file - the eXMLFile that contains annotations
     */
    public XMLtoAnnotation(eXMLFile file)
    {
        aNewOne = file;
        lastProcessSummary = "";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This method will extract the annotations from the eXMLFile
     * @param chosenAnnotators - the annotators to extract annotations from
     * @param chosenClasses - the classes to filter by
     * @return all of the annotations contained in this file.
     */
    public ArrayList<Annotation> extractXML(ArrayList<Annotator> chosenAnnotators, ArrayList<String> chosenClasses)
    {
        //init
        ArrayList<Annotation> toReturn = new ArrayList<Annotation>();
        
        //Keep track of annotations added and filtered
        int added = 0;
        int classFiltered = 0;
        int annotatorFiltered = 0;

        //Extract the information that will be used to create the annotations objects
        Vector<imports.importedXML.eAnnotationNode> annotations = aNewOne.annotations;
        Vector<imports.importedXML.eClassMention> classes = aNewOne.classMentions;

        // Loop through all of the eAnnotations and match them to their classes
        for (imports.importedXML.eAnnotationNode annotation : annotations)
        {
            //extract from the annotation object

//            String range = annotation.span_start + "\\|" + annotation.span_end;
            String range = annotation.spanset.toHTML();
            String mention = annotation.mention_id;
            String date = annotation.creationDate;
            String source = aNewOne.filename;
            String text = annotation.annotationText;
            String annotatorName = annotation.annotator;
            String annotatorID = annotation.annotator_id;
            
            //Make sure it is from a chosen annotator
            boolean rightAnnotator = false;
            if (chosenAnnotators != null)
            {
                for (Annotator chosen : chosenAnnotators)
                {
                    if (chosen.getAnnotatorID().equals(annotatorID) && chosen.getAnnotatorName().equals(annotatorName))
                    {
                        rightAnnotator = true;
                    }
                }
            }
            //If chosen annotators is null then user didn't want to filter by annotator so set rightAnnotator to true
            else
            {
                rightAnnotator = true;
            }
            //Increment annotator filtered if it wasn't from a correct annotator
            if (!rightAnnotator)
            {
                annotatorFiltered++;
                continue;
            }
            //Create a new annotator object using the gathered informaiton
            Annotator annotator = new Annotator(annotatorName, annotatorID, "");
            String textClass = "";
            for (imports.importedXML.eClassMention aClass : classes)
            {
                if (aClass.classMentionID.trim().toLowerCase().equals(mention.trim().toLowerCase()))
                {
                    textClass = aClass.mentionClassID;
                }
            }
            //If the user is filtering by class then make sure it is from the correct class
            if (chosenClasses != null)
            {
                if (!chosenClasses.contains(textClass))
                {
                    classFiltered++;
                    continue;
                }
            }
            //If program made it to this point then we have all required information.. so create annotation object
            //increment added and add it to the arrayList of results
            added++;
            Annotation toAdd = new Annotation(mention, annotator, range, text, date, textClass, source);
            toReturn.add(toAdd);
        }
        lastProcessSummary = "\nExtraction From: " + aNewOne.absoluteFilename + " Complete!\n" +
                             "Annotations Added: " + added + "\n" +
                             "Annotations Filtered Because Of Class: " + classFiltered + "\n" +
                             "Annotations Filtered Because Of Annotator: " + annotatorFiltered +"\n";
        return toReturn;
    }
    /**
     * This method will extract all of the classes from this object.
     * @return - all of the classes(no repeats).
     */
    public ArrayList<String> getAllClasses()
    {
        ArrayList<String> allClasses = new ArrayList<String>();


        Vector<imports.importedXML.eClassMention> classes = aNewOne.classMentions;
        for (imports.importedXML.eClassMention aClass : classes)
        {
            if (!allClasses.contains(aClass.mentionClassID))
            {
                allClasses.add(aClass.mentionClassID);
            }
        }

        return allClasses;
    }
    /**
     * This method will extract all of the annotators from this object.
     * @return - an ArrayList of annotator objects.
     */
    public ArrayList<Annotator> getAllAnnotators()
    {
        ArrayList<Annotator> toReturn = new ArrayList<Annotator>();

        Vector<imports.importedXML.eAnnotationNode> annotations = aNewOne.annotations;
        //Loop through all annotations to extract their annotator
        for (imports.importedXML.eAnnotationNode annotation : annotations)
        {
            //Extract informaitno required for an annotator object
            String annotatorName = annotation.annotator;
            String annotatorID = annotation.annotator_id;

            //Create a new annotator object.
            Annotator annotator = new Annotator(annotatorName, annotatorID, "");
            boolean unique = true;
            //Make sure it is unique.
            for (Annotator existing : toReturn)
            {
                if (annotatorID.equals(existing.getAnnotatorID()) && annotatorName.equals(existing.getAnnotatorName()))
                {
                    unique = false;
                }
            }
            if (unique)
            {
                toReturn.add(annotator);
            }
        }
        //return unique annotators
        return toReturn;
    }
    //</editor-fold>
}
