/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotate.format;

/**
 *
 * @author Kyle
 */
public class Annotation
{
    //<editor-fold defaultstate="expanded" desc="Member Variables">
    private String mentionid;
    private String span;
    private String annotationText;
    private String creationDate;
    private String annotationClass;
    private String source;
    private Annotator annotator;
    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Constructor">
    /**
     * Constructor for an annotation object.
     * @param mention - the mention id number for this annotation
     * @param annotator - the annotator object for this annotation
     * @param range - the span in the following form: <###|###>
     * @param text - the actual annotated text
     * @param date - the date the annotation was created
     * @param theClass - the class(or concept) of the text
     * @param sourceFile - the source file the annotation originated from.
     */
     public Annotation(String mention, Annotator annotator, String range, String text, String date, String theClass, String sourceFile)
    {
       mentionid = mention;
       this.annotator = annotator;
       this.span = range;
       annotationText = text;
       creationDate = date;
       this.annotationClass = theClass;
       source = sourceFile;
    }
     //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    /**
     * @return the mention_id
     */
    public String getMention_id()
    {
        return mentionid;
    }

    /**
     * @return the span
     */
    public String getSpan()
    {
        return span;
    }

    /**
     * @return the spanText
     */
    public String getSpanText()
    {
        return annotationText;
    }

    /**
     * @return the creationDate
     */
    public String getCreationDate()
    {
        return creationDate;
    }

    /**
     * @return the theClass
     */
    public String getTheClass()
    {
        return annotationClass;
    }

    /**
     * @return the source
     */
    public String getSource()
    {
        return source;
    }

    /**
     * @return the annotator
     */
    public Annotator getAnnotator()
    {
        return annotator;
    }
    //</editor-fold>
}
