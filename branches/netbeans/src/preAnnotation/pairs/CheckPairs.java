/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotation.pairs;

import java.io.File;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author leng
 */
public class CheckPairs {
    public CheckPairs(File _textSourceFile, String corpusContents){
        if(corpusContents==null)
            return;
        if(corpusContents.trim().length()<1)
            return;

        Dictionary dict = new Dictionary();
        int size = dict.size();
        for(int i=0;i<size;i++){
            Entry entry = dict.get(i);
            search(_textSourceFile, corpusContents, entry);
        }
        
    }

    private void search(File _textSourceFile, String text, Entry _entry)
    {
        if (_entry==null)
            return;
        if ( (text==null)||(text.trim().length()<1))
            return;
        if ((_entry.term==null)||(_entry.term.trim().length()<1))
            return;
        if ((_entry.type==null)||(_entry.type.trim().length()<1))
            return;
        //if ((_entry.subtype==null)||(_entry.subtype.trim().length()<1))
        //    return;

        String regex = buildRegex(_entry.term);
        Matcher matcher;
        matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        boolean match_found = matcher.find();
        while(match_found){

            int start = matcher.start();
            int end = matcher.end();
            String found_text = text.substring(start, end);

            recordAnnotation(found_text, start, end, _entry, _textSourceFile);
            
            match_found = matcher.find();
        }

    }

    private String buildRegex(String _originalTerm){
        //String whitespace = "([ ]|[\t]|[\r]|[\n]|[\\v]|[\\f])";
        //String symbol = "([ ]|[\\?]|[\\*|[\\+]|[/]|[\\.]|[\\^]|[\\$]|[#]|[@]|[\t]";
        //String noncharacter = "[^A-Za-z]";
        //String start =
        String regex =  "\\b"+ _originalTerm + "\\b";
        return regex;
    }

    /**record the new found annotation by the pre-annotated dictionary */
    private void recordAnnotation(String found_text, int start, int end,
            Entry _entry, File _textSourceFile)
    {
        String createdate = commons.OS.getCurrentDate();
        String annotator_name = resultEditor.annotator.Manager.getAnnotatorName_OutputOnly();
        String annotator_id = resultEditor.annotator.Manager.getAnnotatorID_outputOnly();
        log.LoggingToFile.log(Level.INFO, "found annotation:["+found_text+"] at ("+start+", "+end+")");

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.addANewAnnotation(
                _textSourceFile.getName(), // filename of text source
                found_text, // text
                start,      // span start
                end,        // span end
                createdate,
                _entry.type.trim(),
                annotator_name,
                annotator_id,
                null,
                null,
                assignMeAUniqueIndex()
                );        
    }

    private int assignMeAUniqueIndex(){
        return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }

    
}
