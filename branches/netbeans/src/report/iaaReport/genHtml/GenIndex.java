/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.genHtml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import report.iaaReport.analysis.detailsNonMatches.AnalyzedAnnotator;
import report.iaaReport.analysis.detailsNonMatches.AnalyzedResult;

/**
 *
 * @author leng
 */
public class GenIndex {

    public final static String filename = "index.html";
    
    private String[] indexcontent = {
    "<html><head><title>Inter-Annotator Agreement</title></head>",
    "<body style=\"margin: 2 5 5 5; font-family: Candara; font-size: 12px;\"><ul>",
    //"<li><a href=\"class_matcher.html\">Class matcher</a></li>",
    //"<li><a href=\"span_matcher.html\">Span matcher</a></li>",
    "<li><a href=\"class_and_span_matcher.html\">Class and span matcher</a></li>"    
    };
    
    private PrintStream buildHtmlBody(PrintStream p, ArrayList<String> classes) throws Exception{
        //"<li><a href=\".html\">Unmatched Details for Annotator()</a></li>"
                if ( p == null )
                    throw new Exception("110827");
        AnalyzedResult analyzedResult = new AnalyzedResult();
        Vector<AnalyzedAnnotator> analyzedAnnotators = analyzedResult.getAll();

        p.println("<br>");

       
            
        p.println( "<li><a href=\""
                    + "SUMMARY-Matched.html\"><b>Matched Details - SUMMARY </b></a></li>");

        for(String originalclassname : classes)
        {
            if((originalclassname==null)||(originalclassname.trim().length()<1))
                continue;

            // ignore this one if it don't have any results
            if(SeparatedDetailsByClass.isNoData_ToMatches(originalclassname.trim()))
                    continue;

            String classname = originalclassname.trim();
            classname = classname.replaceAll(" ", "_");
            classname = classname.replaceAll(",", "_");
            classname = classname.replaceAll("=", "_");
            classname = classname.replaceAll("&", "_");
            classname = classname.replaceAll("!", "_");
            classname = classname.replaceAll("\\*", "_");
            classname = classname.replaceAll("@", "_");
            classname = classname.replaceAll("\\+", "_");

            p.println( "<li><a href=\""
                    + "Matched-by-class-"
                    + classname
                    + ".html\">Class( "
                    + originalclassname.trim()
                    +" ) only</a></li>");
        }

            


        p.println("<br>");

        for(AnalyzedAnnotator analyzedAnnotator: analyzedAnnotators){
            if(analyzedAnnotator==null)
                continue;
            String annotatorName = analyzedAnnotator.mainAnnotator.trim();
            annotatorName = annotatorName.trim();
            annotatorName = annotatorName.replaceAll(" ", "_");
            annotatorName = annotatorName.replaceAll(",", "_");
            annotatorName = annotatorName.replaceAll("=", "_");
            annotatorName = annotatorName.replaceAll("&", "_");
            annotatorName = annotatorName.replaceAll("!", "_");
            annotatorName = annotatorName.replaceAll("\\*", "_");
            annotatorName = annotatorName.replaceAll("@", "_");
            annotatorName = annotatorName.replaceAll("\\+", "_");

            p.println( "<li><a href=\""
                        + annotatorName
                        + "-UNMATCHED-SUMMARY"
                        + ".html\"><b>Unmatched Details for Annotator ( "
                        + analyzedAnnotator.mainAnnotator.trim()
                        + " ) SUMMARY</b></a></li>");

            for(String originalclassname : classes)
            {
                if((originalclassname==null)||(originalclassname.trim().length()<1))
                    continue;

                // ignore this one if it don't have any results
                if(SeparatedDetailsByClass.isNoData_ToNonMatches(analyzedAnnotator.mainAnnotator.trim(), originalclassname.trim()))
                    continue;

                String classname = originalclassname.trim();
                classname = classname.replaceAll(" ", "_");
                classname = classname.replaceAll(",", "_");
                classname = classname.replaceAll("=", "_");
                classname = classname.replaceAll("&", "_");
                classname = classname.replaceAll("!", "_");
                classname = classname.replaceAll("\\*", "_");
                classname = classname.replaceAll("@", "_");
                classname = classname.replaceAll("\\+", "_");
                p.println( "<li><a href=\""
                        + annotatorName
                        + "-UNMATCHED-by-class-"
                        + classname
                        + ".html\">Unmatched Details for Annotator ( "
                        + analyzedAnnotator.mainAnnotator.trim()
                        + " ) to class( "
                        + originalclassname.trim()
                        +" ) only</a></li>");
            }

            //if we need to split the detailed report by documents, we need
            //following code:
            //Vector<DiffedArticle> diffedarticles = diffedAnnotator.getAll();


        }


        
                
        return p;
    }

    public void genHtml(File reportfolder, ArrayList<String> classes) throws Exception{
        try{

            FileOutputStream output = new FileOutputStream(reportfolder.getAbsolutePath() + File.separatorChar + filename );
            PrintStream p = new PrintStream(output);

            for(String str:indexcontent){
                p.println(str);
            }
            
            p.println("<br>");

            // add hyper link for unmatched annotations
            p = buildHtmlBody(p, classes);
            
            p.close();

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1108190516:: fail to the index.html !!"
                    + ex.toString() );
            throw new Exception("error 1108190142:: fail to save the class_and_span_matcher.html !!\n"
                    + "error 1108190142::" + ex.getMessage() );
        }
    }

}
