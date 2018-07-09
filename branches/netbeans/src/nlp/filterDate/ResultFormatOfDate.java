package nlp.filterDate;

/**
 * It's a format that used to recorded found dates or times from the given 
 * documents.
 *  
 * @author  Jianwei "Chris" Leng
 * @version  1.0
 * @since   jdk 1.6
 */
public class ResultFormatOfDate {
    
    /**Text content of found term, which is a date or a time. */
    public String dateTermText;
    
    /**Integer, It's the span start of the found term. */
    public int start;
    
    
    public int end;

}
