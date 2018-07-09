package preAnnotate.format;

import java.util.ArrayList;

public class Annotator implements Comparable
{
    //<editor-fold defaultstate="expanded" desc="Member Variables">
    private String annotatorName;
    private String annotatorID;
    private ArrayList<String> annotatorFileNames;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Annotator(String aName, String anID, String usedInFile)
    {
        this.annotatorName = aName;
        this.annotatorID = anID;
        annotatorFileNames = new ArrayList<String>();
        if(usedInFile != null)
            this.annotatorFileNames.add(usedInFile);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This method is used to add unique fileNames to the arayList of in file Names.
     * @param anotherName - an in file name that is not already contained in annotatorFileNames.
     */
    public void addInFileName(String anotherName)
    {
        getAnnotatorFileNames().add(anotherName);
    }

    /**
     * Get the string representatino of this object.
     * @return - a string representing this object.
     */
    @Override
    public String toString()
    {
        return getAnnotatorName() + " " + getAnnotatorID();
    }
    /**
     * Overriden equals method.
     * @param annotator - the annotator to compare to this annotator
     * @return true if they have the same name and id false otherwise
     */
    @Override
    public boolean equals(Object toCompare)
    {
        if(toCompare == null)
            return false;
        int compare = this.toString().compareTo(toCompare.toString());
        if(compare == 0)
            return true;
        return false;
        /*
        Annotator annotator = null;
        annotator= (Annotator)toCompare;
        if(!annotatorName.equals(annotator.annotatorName))
            return false;
        if(!annotatorID.equals(annotator.annotatorID))
            return false;
        return true;
         * 
         */
    }
    public int compare(Object first, Object second)
    {
        return first.toString().compareTo(second.toString());
    }

    public int compareTo(Object second)
    {
        return this.toString().compareTo(second.toString());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    /**
     * @return the annotatorName
     */
    public String getAnnotatorName()
    {
        return annotatorName;
    }

    /**
     * @param annotatorName the annotatorName to set
     */
    public void setAnnotatorName(String annotatorName)
    {
        this.annotatorName = annotatorName;
    }

    /**
     * @return the annotatorID
     */
    public String getAnnotatorID()
    {
        return annotatorID;
    }

    /**
     * @param annotatorID the annotatorID to set
     */
    public void setAnnotatorID(String annotatorID)
    {
        this.annotatorID = annotatorID;
    }

    /**
     * @return the annotatorFileNames
     */
    public ArrayList<String> getAnnotatorFileNames()
    {
        return annotatorFileNames;
    }

    /**
     * @param annotatorFileNames the annotatorFileNames to set
     */
    public void setAnnotatorFileNames(ArrayList<String> annotatorFileNames)
    {
        this.annotatorFileNames = annotatorFileNames;
    }
    //</editor-fold>
}
