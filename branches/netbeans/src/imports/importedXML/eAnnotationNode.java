/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imports.importedXML;

import java.util.Vector;
import resultEditor.annotations.SpanSetDef;

/**
 *
 * @author Chris
 */
public class eAnnotationNode{

    /**unique annotatio index used in eHOST result editor to indentify
     * all imported or newly created annotaiton.*/
    public int    uniqueIndex;
    
    public String mention_id;
    public String annotator_id;
    public String annotator;
    public SpanSetDef spanset = new SpanSetDef();
    public String annotationText;
    public String creationDate;
    
    public String __isProcessed;
    public String __adjudication_status;
    

    /**annotation comment, one annotation has no more than one comment*/
    public String annotationComments;
    
    /**what kind of annotation data is this; default value is -1, 5 means it's a 
     * saved annotation that only appeared for adjudication mode.
     */
    public int type = -1;

    /**in the comment string, one or more verifier suggestions can be inserted
     * into normal annotation comment; In this format:
     * "(VERIFIER_SUGGESTION [suggestion1] [suggestion2] … ) other normal comment strings”*/
    public Vector<String> verifierSuggestions;

    /**Class constructor*/
    //public eAnnotationNode(){}

    /**Class constructor*/
    public eAnnotationNode(String mentionid,
            String annotationtext,
            SpanSetDef spanset,
            String annotator, 
            String annotatorid,
            String creationdate,
            String comment,
            String _isProcessed,
            String _adjudication_status,
            int type
            )
    {
        mention_id = mentionid;
        annotator_id = annotatorid;
        this.annotator = annotator;
        this.spanset = spanset;
        annotationText = annotationtext;
        creationDate = creationdate;
        this.__isProcessed = _isProcessed; 
        this.__adjudication_status = _adjudication_status;

        if ((comment != null)&&(comment.trim().length() > 0)){
            // get annotation comment
            annotationComments = comment;
            
            // dig for inserted verifier suggestions
            Vector<String> verifier_suggestions = getVerifierSuggestions(comment);
            verifierSuggestions = verifier_suggestions;
        }
        
        this.type = type;

    }
    
    public eAnnotationNode(String mentionid,
            String annotationtext,
            SpanSetDef spanset,
            String annotator, 
            String annotatorid,
            String creationdate,
            String comment,
            String _isProcessed,
            String _adjudication_status
            )
    {
        mention_id = mentionid;
        annotator_id = annotatorid;
        this.annotator = annotator;
        this.spanset = spanset;
        annotationText = annotationtext;
        creationDate = creationdate;
        this.__isProcessed = _isProcessed; 
        this.__adjudication_status = _adjudication_status;

        if ((comment != null)&&(comment.trim().length() > 0)){
            // get annotation comment
            annotationComments = comment;
            
            // dig for inserted verifier suggestions
            Vector<String> verifier_suggestions = getVerifierSuggestions(comment);
            verifierSuggestions = verifier_suggestions;
        }

    }

    
    /**Parser to get verifier suggestions*/
    private Vector<String> getVerifierSuggestions(String comment){
        if (comment == null)
            return null;

        if (comment.trim().length() < 1)
            return null;
        
        // remove all quotation marks
        comment = comment.replaceAll("\"", " ");
        //System.out.println( " - " + comment);

        // if this comment contains verifier suggestions        
        if( comment.contains( "("  ) && comment.contains( ")"  ) ){

            Vector<String> verifier_suggestions = new Vector<String>();

            // get instered string
            comment = getInsertString(comment);

            if (comment ==  null)
                return null;

            int start = 0 , end = 0, length = comment.length();
            do{
                start = comment.indexOf('[', start);
                end = comment.indexOf(']', start + 1);

                if((start == -1)||(end ==-1))
                    break;

                if((start >=0) && (end<=length) && (start<end)){
                    //System.out.println(" - " + comment.substring(start+1,end).trim());
                    verifier_suggestions.add( comment.substring(start+1,end) );
                }

                start++;

            }while( (start >=0) && (end<=length) && (start<end) );
            
            return verifier_suggestions;
        }else{
            //System.out.println(" = no insert string surrounded with paired " +
            //        "quotation marks in annotaion comment.");
        }

        

        return null;
    }

    /**get verifier strings for subsequent process*/
    private String getInsertString(String line){
        int start = line.indexOf("(VERIFIER_SUGGESTIONS");
        int end = line.indexOf(')');
        if( ( start != -1) && (end != -1))
            return line.substring(start+1+"(VERIFIER_SUGGESTIONS".length(), end);
        else
            return null;
    }
    
}
