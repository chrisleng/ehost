package nlp.filterCustomDefined;

/**
 * Title: 
 * CustomRegexResultFormat.java
 * 
 * Description:
 * The format of results that found by NLP assistant using custom regular 
 * expressions.
 * 
 * @author  Jianwei "Chris" Leng
 * @version  1.0
 * @since   jdk 1.6
 */
public class CustomRegexResultFormat {
    
    /**The word (String content) that we just found by the eHOST NLP assistant
     * using custom regular expressions.*/
    public String termText;
    
    /**The name of the category or the class to this newly found term.*/
    public String classname;
    
    /**Span start of this newly found term.*/
    public int start;
    
    /**Span end of this newly found term.*/
    public int end;

}
