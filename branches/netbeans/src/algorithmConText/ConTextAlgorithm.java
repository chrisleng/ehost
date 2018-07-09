package algorithmConText;


import commons.Constant.temporality;
import java.util.regex.Pattern;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jianwei "Chris" Leng
 * @time:
 */
public class ConTextAlgorithm {
        
    public void Context(
        // ******* all input parameter start with a character '_'

        // current paragraphy, divided in sentences, separator was ". "
        //String[] _sentence_array_of_current_paragraphy,

        // position parameter: this tell which sentence is in processing now
        //int _index_to_current_sentence,

        // position parameter: length of all pervious sentences
        //int _POSITION_length_of_pervious_sentences,

        // text of current paragraphy, type: java string
        String _text,

        int _overallIndex,

        // position parameter: length of all pervious paragraphy
        int _offsetInFile
        )
    {
        
        // ***** preprocessing: remove last sybols;cut into sentencs array
        String[] sentences = PreProcess( _text );

        
        // ***** search hypothetical triggers in current sentence
        int size = sentences.length;
        int offsetInParagraph = 0;
        for( int i=0; i<size; i++){
            
            // <<1>> == digger out all experiencer information ==
            if(env.Parameters.NLPAssistant.OUTPUTEXPERIENCER){
                Experiencer experiencer = new Experiencer();
                experiencer.experiencerDigger(sentences[i],
                        _offsetInFile, offsetInParagraph,
                        _overallIndex);
            }

            
            // <<2>> == temporality status ==
            if(env.Parameters.NLPAssistant.OUTPUTTEMPORALITY){
                temporality t;

                // remove all symbol in this sentence
                String cleannedSentence = removeAllSymbols( sentences[i] );
                // check temporality status
                Temporality temporalityCheck = new Temporality();
                t = temporalityCheck.temporalityDector( cleannedSentence,
                        _offsetInFile, offsetInParagraph,
                        _overallIndex);

                recordTemporalityStatus(t, _overallIndex, _offsetInFile,
                        offsetInParagraph, sentences[i].length() );
            }

            // set the offset for length of processed sentence
            // **NO** return without this offset correction
            offsetInParagraph = offsetInParagraph + sentences[i].length() + 2;

        }

        // seperate paragraphy to sentences
        //String[] Sentences = _currentParagraphy.split(". ");

        //Hypothetical_Searcher( _sentence_array_of_current_paragraphy[ _index_to_current_sentence ] );
    }

    /**record temporality status to a specific sentence in storage space*/
    private void recordTemporalityStatus(temporality _temporalityStatus,
            int _overallIndex, int _offsetInFile, int _offsetInParagraph,
            int _lengthOfThisSentence){

        // temporality status in String 
        String tempStatus;
        if (_temporalityStatus == temporality.historical)
            tempStatus = "historical";
        else if (_temporalityStatus == temporality.recent)
            tempStatus = "recent";
        else if (_temporalityStatus == temporality.hypothetical)
            tempStatus = "hypothetical";
        else  tempStatus = "recent";

        // range of window
        int sentenceStartPoint = _offsetInParagraph + _offsetInFile;
        int sentenceEndPoint   = _offsetInParagraph + _offsetInFile
                               + _lengthOfThisSentence;
        
                               
        nlp.storageSpaceDraftLevel.StorageSpace.addTemporality(_overallIndex,
                tempStatus, sentenceStartPoint, sentenceEndPoint);
        
    }
   

    /**Prepare the sentences before begining analysis.<p>
     * Including:<p>
     * 1. replace all digit+"."+digit to "000" .<p>
     * 2. split paragraph to sentence by ". ". <p>
     * 3. remove symbol in the end of each sentence.<p>
     *
     * @return <code>String[]</code>    return splitted and processed
     *          sentences in format of String[];
     */
    private static String[] PreProcess(String _originalString){

        // remove last symbol, such as , . ? : " '
        String processedString = _originalString;

        // replace all number.number to 000
        processedString = Pattern.compile("[0-9]\\.[0-9]").matcher( processedString ).replaceAll("000");

        // split the paragraphy into sentences
        String[] sentences = processedString.split("\\. ");

        // make sure to remove redundant symbols
        int size = sentences.length;
        for(int i =0; i < size; i++){
                sentences[i] = RemoveLastSymbols( sentences[i] );
        }

        return sentences;
    }


    /**remove all symbols appeared in this string*/
    private String removeAllSymbols(String _origianlString){
        char[] cleannedSentence = _origianlString.toCharArray();
        int length = cleannedSentence.length;
        char thischar;

        for(int i=0; i<length; i++){
            thischar = cleannedSentence[i];
            
            int charnumber = Integer.valueOf(thischar);
            int a = Integer.valueOf('a');
            int z = Integer.valueOf('z');
            int A = Integer.valueOf('A');
            int Z = Integer.valueOf('Z');
            int n0 = Integer.valueOf('0');
            int n9 = Integer.valueOf('9');

            if( ( charnumber > a ) && ( charnumber < z ) )
                continue;
            else if( ( charnumber > A ) && ( charnumber < Z ) )
                continue;
            else if( ( charnumber > n0 ) && ( charnumber < n9 ) )
                continue;
            else
                cleannedSentence[i] = ' ';

        }

        return String.valueOf(cleannedSentence);
    }

    /**Preprocessing: remove last symbol, such as , . ? : " ' at the end of
     * a specific sentence.<p>
     * This can improve the accuracy of regular expression and aviod some
     * term miss.<p>
     * <tt>_originalString</tt> unprocessed string.<p>
     * @return String   return processed sentence in "String"
     */
    private static String RemoveLastSymbols(String _OriginalString ){

        char[] cleannedSentence = _OriginalString.toCharArray();
        int length = cleannedSentence.length;
        char thischar;

        length--;
        

        do{
            thischar = cleannedSentence[ length ];
            
            if(thischar == ' ') cleannedSentence[length] = ' ';
            else if(thischar == '.') cleannedSentence[length] = ' ';
            else if(thischar == ',') cleannedSentence[length] = ' ';
            else if(thischar == ';') cleannedSentence[length] = ' ';
            else if(thischar == ':') cleannedSentence[length] = ' ';
            else if(thischar == '\'') cleannedSentence[length] = ' ';
            else if(thischar == '!') cleannedSentence[length] = ' ';
            else if(thischar == '`') cleannedSentence[length] = ' ';
            else if(thischar == '$') cleannedSentence[length] = ' ';
            else if(thischar == '%') cleannedSentence[length] = ' ';
            else if(thischar == '*') cleannedSentence[length] = ' ';
            else if(thischar == '#') cleannedSentence[length] = ' ';
            else if(thischar == '"') cleannedSentence[length] = ' ';
            else if(thischar == '_') cleannedSentence[length] = ' ';            
            else
                break;

            length--;

        }while( length >= 0 );

        return String.valueOf(cleannedSentence);
    }
}
