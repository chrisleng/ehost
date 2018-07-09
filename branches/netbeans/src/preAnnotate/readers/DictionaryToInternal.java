/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preAnnotate.readers;

import java.util.ArrayList;
import java.util.Scanner;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.io.File;
import java.io.IOException;

/**
 * This class will extract dictionary entries and return an ArrayList of annotations.
 * These annotations will only have text and class information.. because that is all
 * that a dictionary contians.
 * @author Kyle
 */
public class DictionaryToInternal
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    //Member Variables
    private String fileName;
    private ArrayList<String> classes = null;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor
     * @param name - The name of the source file.
     */
    public DictionaryToInternal(String name)
    {
        fileName = name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This method will perform an annotation extraction on a dictionary.  The annotations that
     * are returned will only have text and class information since that is all a dictionary contains.
     * @param chosenClasses - The classes to extract, use null if you want all of them.
     * @return - an ArrayList of Annotation objects with only text and class information.
     */
    public Article ReadIn()
    {
        //The Article that will hold all of the annotations
        Article toReturn = new Article(fileName);

        //Create scanner through the file
        Scanner s = null;
        try{
        s = new Scanner(new File(fileName));
        }
        catch(IOException e)
        {
            System.out.println("file not found!");
        }

        //Loop through the entire file extracting annotations
        while (s.hasNext())
        {
            //Read in the next line and split it using the <---> as the separator
            String tempLine = s.nextLine();
            String[] split = tempLine.split("<--->");

            //Make sure it split into only two pieces... otherwise don't use it.
            if (split.length == 2)
            {
                Annotation toAdd = new Annotation(null, split[0].trim().toLowerCase(), -1, -1, null, null, split[1].trim(), null);
                toReturn.addAnnotation(toAdd);
            }

        }
        //Return all annotations
        return toReturn;
    }

    /**
     * This method will extract all of the classes contained within this dictionary.
     * @return - All classes represented in this dictionary.
     */
    public ArrayList<String> readClasses()
    {
        //Make sure we haven't already procesed the classes
        if (classes == null)
        {
            //Initialize the ArrayList
            classes = new ArrayList<String>();

            //Create Scanner
            Scanner s = new Scanner(fileName);

            //Loop through the entire file reading in classes.
            while (s.hasNext())
            {
                String tempLine = s.nextLine();
                String[] split = tempLine.split("<--->");
                if (split.length == 2)
                {
                    classes.add(split[2]);
                }

            }
        }
        return classes;
    }
    //</editor-fold>
}
