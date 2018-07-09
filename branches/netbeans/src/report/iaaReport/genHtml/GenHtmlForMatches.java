/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport.genHtml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import java.util.logging.Level;
import report.iaaReport.IAA;
import report.iaaReport.analysis.detailsNonMatches.*;
import resultEditor.annotations.Annotation;

/**
 *
 * @author Chris 2011-8-26 21:33
 */
public class GenHtmlForMatches {
    
     public void genHtml(File reportfolder) throws Exception{
        try{
            
            AnalyzedResult analyedResult = new AnalyzedResult();
            Vector<AnalyzedAnnotator> analyzedAnnotators = analyedResult.getAll();
        
            //for(AnalyzedAnnotator analyzedAnnotator: analyzedAnnotators)

                AnalyzedAnnotator analyzedAnnotator = analyzedAnnotators.get(0);
                if(analyzedAnnotator==null)
                    throw new Exception("1109301500::");

                if(analyzedAnnotator.mainAnnotator==null){
                    throw new Exception("1109020443::");
                }
                
                File file = new File(reportfolder.getAbsolutePath() + File.separatorChar + "SUMMARY-Matched.html" );
                FileOutputStream output = new FileOutputStream( file );
                
                PrintStream p = new PrintStream(output);

                // #### assemble html head
                p = printhtmlhead(p, analyzedAnnotator.mainAnnotator.trim() );

                
                SeparatedDetailsByClass.clear();                
                // #### html assemble: output each non-matched annotaions of current annotator
                outputNonMatchedDetails( p, analyzedAnnotator,
                        analyzedAnnotator.mainAnnotator.trim(),
                        analyzedAnnotator.annotators
                        );

                // print separated details of matches
                buildSeparatedDetailsByClass( reportfolder, analyzedAnnotator.mainAnnotator.trim() );

                // record classes which has nonmatches so we can remove class without any annotations in the index.html
                SeparatedDetailsByClass.registerMatchesClass();

                p.close();
            //}

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1108262132A:: fail to the html of unmatched details !!"
                    + ex.toString() );
            throw new Exception("error 1108262133:: fail to save the html of unmatched details !!\n"
                    + "error 1108190142::" + ex.getMessage() );
        }
    }


     

     /**prepare the html head for each html. This only can be called from
      * method "genHtml" in same class.
      */
     private PrintStream printhtmlhead(PrintStream p, String annotatorName){
         p.println("<html> ");
         p.println("<head><title>Matches annotations of Annotator ("+annotatorName+") </title></head>");
         p.println("<body style=\"margin: 2 5 5 5; font-family: Candara;\">");
         p.println("<div><a href=\"index.html\"><b>Back to the index.html</b></a><br></div>");
         p.println("<h1>Matches for Annotator: ("+annotatorName+") , </h1>");
         p.println("Each annotation that was considered as matched is " +
                 "shown in the text that it was found in.  If user selected, then overlapping " +
                 "annotations from the other annotation sets are also shown.");
         p.println("</font><hr>");
         
         return p;
     }

     /*print separated details of matches*/
     private void buildSeparatedDetailsByClass(File reportfolder, String annotatorName ){
         if( SeparatedDetailsByClass.separatedDetails == null )
         {
             log.LoggingToFile.log(Level.WARNING, "1109301328:: empty results of matches to a specific class.");
             return;
         }

         try{
             for(ClassedFormat cf : SeparatedDetailsByClass.separatedDetails)
             {
                File file = new File( reportfolder.getAbsolutePath() + File.separatorChar + cf.filename );
                FileOutputStream output = new FileOutputStream( file );
                PrintStream p = new PrintStream(output);

                p.println("<html> ");
                p.println("<head><title>Matches annotations of Annotator ("+annotatorName+") to certain class </title></head>");
                p.println("<body style=\"margin: 2 5 5 5; font-family: Candara;\">");
                p.println("<div><a href=\"index.html\"><b>Back to the index.html</b></a><br></div>");
                p.println("<h1>Matches for Annotator: ("+annotatorName+")  to class("
                        + cf.classname
                        +"), </h1>");
                p.println("Each annotation that was considered as matched is " +
                     "shown in the text that it was found in.  If user selected, then overlapping " +
                     "annotations from the other annotation sets are also shown.");
                p.println("</font><hr>");

                for(String code : cf.htmlcodes )
                {
                    p.println(code);
                }

                p.println("<div><a href=\"index.html\"><b><br> [Back to the index.html]</b></a><br></div>");

                p.close();
             }
         }catch(Exception ex){
             log.LoggingToFile.log(Level.SEVERE, "1109301030::fail to output separated details of matched annotations");
         }    
     }


     

    /**To the given record of non-matched annotations of an annotator, we
     * list them and assemble them in a html report.
     *
     * @param   p
     *          the object of the PrintStream of the html which we are generating.
     *
     */
    private PrintStream outputNonMatchedDetails( PrintStream p, 
            AnalyzedAnnotator analyzedAnnotator,
            String annotatorName,
            String[] annotators
            ) throws Exception{

        if(p==null)
            throw new Exception("1108292228:: null instance of Class PrintStream is given.");

        if(analyzedAnnotator==null)
            return p;

        try{

            // #### get all diffed articles of current annotator
            Vector<AnalyzedArticle> articles = analyzedAnnotator.analyzedArticles;
            for( AnalyzedArticle article : articles)
            {
                if(article==null)
                    continue;

                if(article.rows==null)
                    continue;

                PreLoadDocumentContents.load(article.filename);

                // #### get all diffed annotations of current article of current annotator
                Vector<AnalyzedAnnotation> analyzedAnnotations = article.rows;

                

                for(AnalyzedAnnotation analyzedAnnotation : analyzedAnnotations )
                {
                    if(analyzedAnnotation==null){
                        log.LoggingToFile.log(Level.WARNING,  "1110041858: null value.");
                        continue;
                    }
                        
                    Classes classes = new Classes();

                    int maxsize=0;
                    // #### get max rows of this table

                    maxsize = analyzedAnnotation.mainAnnotations.size();


                    for(OthersAnnotations otherannotations : analyzedAnnotation.othersAnnotations){
                        if(otherannotations==null)
                            continue;

                        Vector<AnalyzedAnnotationDifference> differences = otherannotations.annotationsDiffs;
                        if( maxsize < differences.size() )
                            maxsize = differences.size();

                    }

                    
                    // this flag use to indicate whether we found differences
                    // between these annotations
                    // Default value is false.
                    boolean allSame = true;
                    Vector<String> strs = new Vector<String>(); // for text recording


                    strs.add("<div>File: " + article.filename + "</div>");
                    Annotation mainAnnotation0 = getMainAnnotation(0, analyzedAnnotation);
                    if(mainAnnotation0==null)
                        continue;
                    
                    classes.add( mainAnnotation0.annotationclass ); // record we have this class 

                    String textcontent = PreLoadDocumentContents.getSurroundText( mainAnnotation0 );
                    strs.add("<div>"+textcontent+"</div>");
                    strs.add("<table border=\"1\">");

                    //#### print the table head
                    strs.add("<tr>");
                    strs.add("<th></th>");
                    for(String annotator : annotators )
                    {
                        strs.add("<th> Annotator:[ "+ annotator +" ]</th>");
                    }
                    strs.add("</tr>");

                    //#### print the rest rows
                    for(int i=0; i<maxsize; i++)
                    {

                        Annotation mainAnnotation = getMainAnnotation(i, analyzedAnnotation);
                        Annotation mainAnnotation_first = getMainAnnotation(0, analyzedAnnotation);

                        if((mainAnnotation==null)||(mainAnnotation_first==null))
                            continue;
                        
                        classes.add( mainAnnotation.annotationclass ); // record we have this class
                        
                        //#### get annotation text
                        strs.add("<tr>");
                        strs.add("<td>Annotation Text</td>");
                        if(mainAnnotation!=null)
                            strs.add("<td>"+mainAnnotation.annotationText+"</td>");
                        else
                            strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                        
                        int size_other = analyzedAnnotation.othersAnnotations.length;

                        boolean foundNull = false;
                        for(int j=0; j<size_other; j++)
                        {
                            Vector<AnalyzedAnnotationDifference> diffs = analyzedAnnotation.othersAnnotations[j].annotationsDiffs;
                            AnalyzedAnnotationDifference diff = getOtherAnnotation(i, diffs);                            
                            if((diff!=null)&&(diff.annotation.annotationText!=null))
                            {
                                // if main annotation is null, and the diff annotation isn't null
                                // they are different
                                if(mainAnnotation_first==null){
                                   strs.add("<td BGCOLOR=\"#FFD0D0\">"+diff.annotation.annotationText+"</td>");
                                   allSame = false;
                                }else {
                                    // if main and diff has same span
                                    if (IAA.CHECK_OVERLAPPED_SPANS) {
                                        if( (Comparator.isSpanOverLap(mainAnnotation_first, diff.annotation))
                                          ||(Comparator.equalSpans(mainAnnotation_first, diff.annotation))){
                                            strs.add("<td>" + diff.annotation.annotationText + "</td>");
                                        } else {
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">" + diff.annotation.annotationText + "</td>");
                                            allSame = false;
                                        }
                                    } else {
                                        if (Comparator.equalSpans(mainAnnotation_first, diff.annotation)) {
                                            strs.add("<td>" + diff.annotation.annotationText + "</td>");
                                        } else {
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">" + diff.annotation.annotationText + "</td>");
                                            allSame = false;
                                        }
                                    }
                                }

                            }
                            // if the diff annotation is null
                            else{
                                foundNull = true;
                                strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                            }
                        }                        
                        strs.add("</tr>");

                        if((foundNull)&&(mainAnnotation_first!=null))
                            allSame = false;

                        
                        //#### get annotation span
                        strs.add("<tr>");
                        strs.add("<td>Span</td>");
                        if((mainAnnotation!=null)){
                            strs.add("<td>");
                            strs.add(  mainAnnotation.getSpansInText() );                            
                            strs.add("</td>");
                            
                        }else
                            strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                        
                        
                        for(int j=0; j<size_other; j++)
                        {
                            Vector<AnalyzedAnnotationDifference> diffs = analyzedAnnotation.othersAnnotations[j].annotationsDiffs;
                            AnalyzedAnnotationDifference diff = getOtherAnnotation(i, diffs);
                            if(diff!=null){
                                // if main annotation is null, and the diff annotation isn't null
                                // they are different
                                if(mainAnnotation_first==null)
                                {
                                    strs.add("<td BGCOLOR=\"#FFD0D0\">");
                                    strs.add( diff.annotation.getSpansInText() );
                                    strs.add("</td>");
                                    allSame = false;
                                }
                                else {
                                    if (IAA.CHECK_OVERLAPPED_SPANS) {
                                        if ((Comparator.isSpanOverLap(mainAnnotation_first, diff.annotation))
                                                || (Comparator.equalSpans(mainAnnotation_first, diff.annotation))) 
                                        {
                                            strs.add("<td >");
                                            strs.add( diff.annotation.getSpansInText() );
                                            strs.add("</td>");
                                        }
                                        else 
                                        {
                                            // if main and diff has same span
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">");
                                            strs.add( diff.annotation.getSpansInText() );
                                            strs.add( "</td>");
                                            allSame = false;
                                        }
                                    } else {
                                        // if main and diff has same span
                                        if ( Comparator.equalSpans(mainAnnotation_first, diff.annotation)) 
                                        {
                                            strs.add("<td >");
                                            strs.add( diff.annotation.getSpansInText() );
                                            strs.add( "</td>");
                                        } 
                                        else 
                                        {
                                            // if main and diff has same span
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">");
                                            strs.add( diff.annotation.getSpansInText() );
                                            strs.add( "</td>");
                                            allSame = false;
                                        }
                                    }
                                }

                        }else
                                strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                        }                        
                        strs.add("</tr>");


                        //#### get annotation class
                        strs.add("<tr>");
                        strs.add("<td>Class</td>");
                        if(mainAnnotation!=null)
                            strs.add("<td>"+mainAnnotation.annotationclass + "</td>");
                        else
                            strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");


                        for(int j=0; j<size_other; j++)
                        {
                            Vector<AnalyzedAnnotationDifference> diffs = analyzedAnnotation.othersAnnotations[j].annotationsDiffs;
                            AnalyzedAnnotationDifference diff = getOtherAnnotation(i, diffs);
                            if(diff!=null)
                            {
                                if(diff.diffInClass){
                                    allSame = false;
                                    strs.add("<td BGCOLOR=\"#FFD0D0\"> "+diff.annotation.annotationclass + "</td>");
                                }else{
                                    strs.add("<td> "+diff.annotation.annotationclass + "</td>");
                                }
                            }
                            else{
                                strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                            }
                        }
                        strs.add("</tr>");

                        if(IAA.CHECK_RELATIONSHIP)
                        {
                            //#### get annotation complex relationship
                            strs.add("<tr>");
                            strs.add("<td>Relationship</td>");

                            if(mainAnnotation!=null){
                                String complexstr = mainAnnotation.getComplexRelationshipString();
                                if(complexstr!=null)
                                    strs.add("<td>"+ complexstr + "</td>");
                                else
                                    strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");

                            }else{
                                strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                            }


                            for(int j=0; j<size_other; j++)
                            {
                                Vector<AnalyzedAnnotationDifference> diffs = analyzedAnnotation.othersAnnotations[j].annotationsDiffs;
                                AnalyzedAnnotationDifference diff = getOtherAnnotation(i, diffs);
                                if(diff!=null)
                                {
                                    String complexstr = diff.annotation.getComplexRelationshipString();
                                    if(complexstr!=null)
                                        if(diff.diffInRelationship){
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">"+ complexstr + "</td>");
                                            allSame = false;
                                        }else
                                            strs.add("<td>"+ complexstr + "</td>");
                                    else{
                                        if(diff.diffInRelationship){
                                            strs.add("<td BGCOLOR=\"#FFD0D0\"></td>");
                                            allSame = false;
                                        }else
                                            strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                                    }
                                }
                                else
                                {
                                        strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                                }
                            }
                        }


                        //#### get annotation attributes
                        if(IAA.CHECK_ATTRIBUTES)
                        {
                            strs.add("<tr>");
                            strs.add("<td>Attributes</td>");

                            if(mainAnnotation!=null){
                                String attributeStr = mainAnnotation.getAttributeString();
                                if(attributeStr!=null)
                                    strs.add("<td>"+ attributeStr + "</td>");
                                else
                                    strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");

                            }else{
                                strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                            }


                            for(int j=0; j<size_other; j++)
                            {
                                Vector<AnalyzedAnnotationDifference> diffs = analyzedAnnotation.othersAnnotations[j].annotationsDiffs;
                                AnalyzedAnnotationDifference diff = getOtherAnnotation(i, diffs);
                                if(diff!=null)
                                {
                                    String attributeStr = diff.annotation.getAttributeString();
                                    if(attributeStr!=null)
                                        if(diff.diffInAttribute){
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">"+ attributeStr + "</td>");
                                            allSame = false;
                                        }
                                        else
                                            strs.add("<td>"+ attributeStr + "</td>");
                                    else{
                                        if(diff.diffInAttribute){
                                            strs.add("<td BGCOLOR=\"#FFD0D0\"></td>");
                                            allSame = false;
                                        }
                                        else
                                            strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                                    }
                                }
                                else
                                {
                                        strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                                }
                            }
                        }
                        
                        //#### get annotation attributes
                        if(IAA.CHECK_COMMENT)
                        {
                            strs.add("<tr>");
                            strs.add("<td>Comments</td>");

                            if(mainAnnotation!=null){
                                String comments = mainAnnotation.comments;
                                if(comments!=null)
                                    strs.add("<td>"+ comments + "</td>");
                                else
                                    strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");

                            }else{
                                strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                            }


                            for(int j=0; j<size_other; j++)
                            {
                                Vector<AnalyzedAnnotationDifference> diffs = analyzedAnnotation.othersAnnotations[j].annotationsDiffs;
                                AnalyzedAnnotationDifference diff = getOtherAnnotation(i, diffs);
                                if(diff!=null)
                                {
                                    String commentsStr = diff.annotation.comments;
                                    if(commentsStr!=null)
                                        if(diff.diffInComment){
                                            strs.add("<td BGCOLOR=\"#FFD0D0\">"+ commentsStr + "</td>");
                                            allSame = false;
                                        }
                                        else
                                            strs.add("<td>"+ commentsStr + "</td>");
                                    else{
                                        if(diff.diffInComment){
                                            strs.add("<td BGCOLOR=\"#FFD0D0\"></td>");
                                            allSame = false;
                                        }
                                        else
                                            strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                                    }
                                }
                                else
                                {
                                        strs.add("<td BGCOLOR=\"#E0E0E0\"></td>");
                                }
                            }
                        }


                        strs.add("</tr>");


                    }

                    strs.add("</table>");
                    strs.add("<br>");

                    // output only while there is no no unmatches in current annotations
                    if( (strs != null) && (allSame) )
                    {
                        for(String str : strs){
                            p.println(str);
                        }
                        for(String cla : classes.allclasses)
                        {
                            SeparatedDetailsByClass.addHtmlLine(cla, strs, true);
                        }

                    }
                    
                }

                

                
            }
            p.println("<div><a href=\"index.html\"><b><br> [Back to the index.html]</b></a><br></div>");
        }catch(Exception ex){
            throw new Exception( "1101" + ex.getLocalizedMessage() );
        }


        return p;
    }
    
    private Annotation getMainAnnotation(int index, AnalyzedAnnotation analyzedAnnotation){
        try{
            int size = analyzedAnnotation.mainAnnotations.size();
            if(index>=size)
                return null;
        
            return analyzedAnnotation.mainAnnotations.get(index);
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1109020549::fail to get main annotation to build detail matched/nonmatched annotations.");
            return null;
        }
    }

    private AnalyzedAnnotationDifference getOtherAnnotation(int i, Vector<AnalyzedAnnotationDifference> diffs) {
        try{
            int size = diffs.size();
            if(i>=size)
                return null;

            return diffs.get(i);

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1109020554::fail to get other annotation to build detail matched/nonmatched annotations.");
            return null;
        }
    }

    
}
