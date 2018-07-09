/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport.genHtml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import report.iaaReport.ClassAgreementAnnotators;
import report.iaaReport.ClassAgreementRecord;
import report.iaaReport.IAA;
import report.iaaReport.PairWiseAgreementRecord;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;

/**
 *
 * @author leng
 */
public class GenHtmlClassAndSpan {

    public final String filename_classAndSpanMatcher = "class_and_span_matcher.html";

    public GenHtmlClassAndSpan(File reportsFolder,
            Vector<ClassAgreementAnnotators> allClassRecords,
            Vector<PairWiseAgreementRecord> pairwises,
            ArrayList<String> selectedAnnotators, // selected annotators in the comparsion processing
            ArrayList<String> selectedClasses,
            int totalProcessedDocuments) throws Exception {
        if ((reportsFolder == null) || (!reportsFolder.exists())) {
            throw new Exception("1108190051:: reports folder under current project could not be reached!");
        }

        if ((pairwises == null) || (pairwises.size() < 1)) {
            throw new Exception("1108190052:: no pair-wise information was collected from the span and class matcher!");
        }

        genHTML(reportsFolder, allClassRecords, pairwises, selectedAnnotators,
                totalProcessedDocuments, selectedClasses);
    }

    private void genHTML(
            File reportsFolder,
            Vector<ClassAgreementAnnotators> allClassRecords,
            Vector<PairWiseAgreementRecord> pairwises,
            ArrayList<String> annotatorsInComparing, // selected annotators in the comparsion processing
            int totalProcessedDocuments, ArrayList<String> selectedClasses) throws Exception {

        String[] head = {
            "<html>",
            "<head><title>Class and span matcher</title></head>",
            "<body style=\"margin: 2 5 5 5; font-family: Candara;\">",
            "<div><a href=\"index.html\"><b> [Back to the index.html]</b></a><br></div>",
            "<h1>Class and span matcher</h1>",
            "Annotations match if they have the same class assignment and the same spans.",
            "<hr>",
            "<p>",
            "<h2>2-way IAA Results</h2>"
        };

        String explain = "Annotations match if they have";
        
        boolean more1 = false, more2 = false, more3 = false, more4 =false;
        if ( IAA.CHECK_OVERLAPPED_SPANS)
            explain = explain + " same or overlapping spans";
        else 
            explain = explain + " exact same spans";
                    
        
        if ( IAA.CHECK_CLASS ){
            more1 = true;
            explain =  explain + ", with same classes";
        }
        
        if ( IAA.CHECK_ATTRIBUTES ){
            more2 = true;
            if( more1 == false )
                explain =  explain + ", with same attributes";
            else
                explain =  explain + ", same attributes";
        }
        
        if(IAA.CHECK_RELATIONSHIP) {
            more3 = true;
            
            if( more1 || more2  )            
                explain =  explain + ", same relationships";
            else
                explain =  explain + ", with same relationships";
        }
        
        if(IAA.CHECK_COMMENT) {
            more4 = true;
            
            if( more1 || more2 || more3  )            
                explain =  explain + " and same comments";
            else
                explain =  explain + ", with same comments";
        }
         
        head[5] = explain + "." ;

        String calculated_documents = "IAA calculated on " + totalProcessedDocuments + " documents.";

        String[] body1 = {
            "<div>all annotations = matches + non-matches",
            "<br> IAA = matches / all annotations",
            "</div>",};




        /*
         * "<h2>match data</h2>", "<ul>", "<li><a href=\"Class and span
         * matcher.matches.Jan , .html\">matches for Jan , </a></li>", "<li><a
         * href=\"Class and span matcher.matches.Tyler , .html\">matches for
         * Tyler , </a></li>", "<li><a href=\"Class and span
         * matcher.matches.Ryan , .html\">matches for Ryan , </a></li>",
         * "</ul><hr>", "<h2>non-match data</h2>", "<ul>", "<li><a href=\"Class
         * and span matcher.nonmatches.Jan , .html\">non-matches for Jan ,
         * </a></li>", "<li><a href=\"Class and span matcher.nonmatches.Tyler ,
         * .html\">non-matches for Tyler , </a></li>", "<li><a href=\"Class and
         * span matcher.nonmatches.Ryan , .html\">non-matches for Ryan ,
         * </a></li>",
            "</ul><hr>",
         */

        String[] pairwise_agreement_tablehead = {
            "<h2>Pair-wise agreement</h2>",
            "<table border=1><tr><td><b>Gold standard set</b></td><td><b>compared set</b></td><td><b>true positives</b></td><td><b>false positives</b></td><td><b>false negatives</b></td><td><b>recall</b></td><td><b>precision</b></td><td><b>F-measure</b></td></tr>"
        };

        String[] pairwise_agreement_tablebody;
        int size = pairwises.size();
        pairwise_agreement_tablebody = new String[size];
        for (int i = 0; i < size; i++) {
            PairWiseAgreementRecord pw = pairwises.get(i);
            pairwise_agreement_tablebody[i] =
                    "<tr><td>"
                    + pw.gold_standard_set
                    + "</td><td>"
                    + pw.compared_set
                    + "</td><td>"
                    + pw.true_positive
                    + "</td><td>"
                    + pw.false_negatives
                    + "</td><td>"
                    + pw.false_positives
                    + "</td><td>"
                    + floatToString(pw.precision * 100)
                    + "</td><td>"
                    + floatToString(pw.recall * 100)
                    + "</td><td>"
                    + floatToString(pw.f_score * 100)
                    + "</td></tr>";
        }

        String[] pairwise_agreement_tableend = {
            "</table>",
            "Precision and recall are given equal weight for the F-score."
        };

        try {

            FileOutputStream output = new FileOutputStream(reportsFolder.getAbsolutePath() + File.separatorChar + filename_classAndSpanMatcher);
            PrintStream p = new PrintStream(output);

            p = printStringArray(p, head);
            p.println(calculated_documents);
            p = printStringArray(p, body1);
            p = build2WayIAATable(p, allClassRecords);

            // build the 3 way IAA report only while we have more than 3 annotators
            if ((annotatorsInComparing != null) && (annotatorsInComparing.size() > 2)) {
                p = build3WayIAATable(p, allClassRecords, annotatorsInComparing.size(), selectedClasses);
            }

            p = printStringArray(p, pairwise_agreement_tablehead);
            p = printStringArray(p, pairwise_agreement_tablebody);
            p = printStringArray(p, pairwise_agreement_tableend);
            p.println("<div><a href=\"index.html\"><b><br>[Back to the index.html]</b></a><br></div>");
            p.close();

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1108190142:: fail to save the class_and_span_matcher.html !!"
                    + ex.toString());
            throw new Exception("error 1108190142:: fail to save the class_and_span_matcher.html !!\n"
                    + "error 1108190142::" + ex.getMessage());
        }
    }

    private PrintStream build2WayIAATable(PrintStream p, Vector<ClassAgreementAnnotators> allClassRecords) throws Exception {
        String[] tablehead = {
            "<table border=1><tr><td><b>Type</b></td><td><b>IAA</b></td>",
            "<td><b>matches</b></td>",
            "<td><b>non-matches</b></td>",
            "</tr>"
        };


        // processed annotators, use it to avoid repetitive annotator pairs
        ProcessedNames pNames = new ProcessedNames();
        pNames.clear();

        for (ClassAgreementAnnotators caa : allClassRecords) {
            if (caa == null) {
                continue;
            }

            if (caa.annotator1.compareTo("3way--------") == 0) {
                continue;
            }



            if (pNames.isProcessed(caa.annotator1, caa.annotator2)) {
                continue;
            }


            pNames.add(caa.annotator1, caa.annotator2);

            int totalmatched = 0, totalunmatched = 0;
            for (ClassAgreementRecord car : caa.AllClassAgreementRecords) {
                if (car == null) {
                    continue;
                }
                totalmatched = totalmatched + car.matches;
                totalunmatched = totalunmatched + car.nonmatches;
            }


            String[] class_agreement_tablebody;
            int size1 = caa.AllClassAgreementRecords.size();
            class_agreement_tablebody = new String[size1 + 1];
            for (int i = 0; i < size1; i++) {
                ClassAgreementRecord car = caa.AllClassAgreementRecords.get(i);

                if (car.classname == null) {
                    continue;
                } else {
                    if (!IAA.isClassSelected(car.classname)) {
                        continue;
                    }
                }

                class_agreement_tablebody[i + 1] =
                        "<tr><td><b>"
                        + car.classname
                        + "</b></td><td>"
                        + floatToString(car.IAAscore * 100)
                        + "</td><td>"
                        + car.matches 
                        + "</td><td>"
                        + +car.nonmatches
                        + "</td></tr>";

            }

            String totalIAAScore = floatToString((float) totalmatched * 100 / ((float) totalmatched + (float) totalunmatched));

            class_agreement_tablebody[0] =
                    ("<tr><td><b>All selected classes</b></td><td>"
                    + totalIAAScore
                    + "</td><td>"
                    + totalmatched
                    + "</td><td>"
                    + +totalunmatched
                    + "</td></tr>");


            String class_agreement_tableend = "</table>";

            p.println("<p>For annotations between Annotator[" + caa.annotator1 + "] and Annotator[" + caa.annotator2 + "]:</p>");
            p = printStringArray(p, tablehead);
            p = printStringArray(p, class_agreement_tablebody);
            p.println(class_agreement_tableend);
        }

        p.println("<br>");

        return p;
    }

    /**Build the multiple way IAA report in HTML if there is more than one 
     * annotator selected for comparation.
     * 
     */
    private PrintStream build3WayIAATable(
            PrintStream p, 
            Vector<ClassAgreementAnnotators> allClassRecords, 
            int number_of_annotators,
            ArrayList<String> selectedClasses) throws Exception {
        
        String[] threeway = {
            "<p>",
            "<h2>" + number_of_annotators + "-way IAA Results ( " + number_of_annotators + " Annotators)</h2>"
        };
        p = printStringArray(p, threeway);

        String[] tablehead = {
            "<table border=1><tr><td><b>Type</b></td><td><b>IAA</b></td>",
            "<td><b>matches</b></td>",
            "<td><b>non-matches</b></td>",
            "</tr>"
        };


        ClassAgreementAnnotators multiWayReportData = new ClassAgreementAnnotators();
        
        
       
        /*for (ClassAgreementAnnotators caa : allClassRecords) {
            if (caa == null) {
                continue;
            }
            
            if (caa.annotator1.compareTo("3way--------") != 0) {
                
            }
        }*/

        for (ClassAgreementAnnotators caa : allClassRecords) {
            if (caa == null) {
                continue;
            }

            if (caa.annotator1.compareTo("3way--------") == 0) {

                int totalmatched = 0, totalunmatched = 0;
                for (ClassAgreementRecord car : caa.AllClassAgreementRecords) {
                    if (car == null) {
                        continue;
                    }
                    
                    if( !isClassSelected( car.classname, selectedClasses ) )
                        continue;
                    
                    totalmatched = totalmatched + car.matches;
                    totalunmatched = totalunmatched + car.nonmatches;
                }



                String[] class_agreement_tablebody;
                int size1 = caa.AllClassAgreementRecords.size();
                class_agreement_tablebody = new String[size1 + 1];
                for (int i = 0; i < size1; i++) {
                    ClassAgreementRecord car = caa.AllClassAgreementRecords.get(i);


                    if (car.classname == null) {
                        continue;
                    } else if (!IAA.isClassSelected(car.classname)) {
                        continue;
                    }

                    class_agreement_tablebody[i + 1] =
                            "<tr><td><b>"
                            + car.classname
                            + "</b></td><td>"
                            + floatToString(car.IAAscore * 100)
                            + "</td><td>"
                            + car.matches 
                            + "</td><td>"
                            + +car.nonmatches
                            + "</td></tr>";

                }
                String totalIAAScore = floatToString((float) totalmatched * 100 / ((float) totalmatched + (float) totalunmatched));

                class_agreement_tablebody[0] =
                        ("<tr><td><b>All selected classes</b></td><td>"
                        + totalIAAScore
                        + "</td><td>"
                        + totalmatched 
                        + "</td><td>"
                        + +totalunmatched
                        + "</td></tr>");


                String class_agreement_tableend = "</table>";

                p.println("<p>For annotations among all Annotators in current dataset:</p>");
                p = printStringArray(p, tablehead);
                p = printStringArray(p, class_agreement_tablebody);
                p.println(class_agreement_tableend);
                p.print("<br>");
                return p;
            }
        }

        return p;
    }
    
     /**
     * check whether a class is selected in IAA reported system.
     */
    private boolean isClassSelected(String _classname, ArrayList<String> selectedClasses) {
        
        if(_classname==null)
            return false;
        
        if ( selectedClasses == null) {
            return false;
        }

        if ((selectedClasses == null) || (selectedClasses.size() < 1)) {
            return false;
        }


        for (String classname : selectedClasses) {
            if (classname == null) {
                continue;
            }

            if (classname.trim().compareTo(_classname.trim()) == 0) {
                return true;
            }
        }

        return false;

    }

    private void setAllAnnotationUnMatched_3Way() {
        try {
            Depot depot = new Depot();

            ArrayList<Article> articles = depot.getAllArticles();

            if (articles == null) {
                // error process
            }

            // #### go though all articles
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                int size = article.annotations.size();

                // reset all processed flags
                for (int i = 0; i < size; i++) {
                    Annotation annotation0 = article.annotations.get(i);
                    if (annotation0 == null) {
                        continue;
                    }
                    


                    /*
                     * if(( annotation0.annotationclass != null ) && (
                     * annotation0.annotationclass.trim().length() > 0 ) ){ if(
                     * !isClassSelected( annotation0.annotationclass ) ){
                     * annotation0.setUnProcessed(); } }else{
                     * annotation0.setProcessed(); }
                     */

                    //annotation0.setUnProcessed();
                    //annotation0.matchedFor3WayIAATable = true;
                }
            }

        } catch (Exception ex) {
        }
    }

    private PrintStream printStringArray(PrintStream p, String[] strarray) throws Exception {
        if (p == null) {
            throw new Exception("1108190139:: ehost gave an empty object of PrintStream to output IAA report in html format. Failed!!");
        }

        if (strarray == null) {
            throw new Exception("1108190140:: ehost send empty string array to build the IAA report in html format.!!");
        }

        for (String str : strarray) {
            if (str == null) {
                continue;
            }

            p.println(str);
        }

        return p;
    }

    private String floatToString(float floatnumber) {

        try {
            int scale = 1; // 2 bumber after the dot
            int roundingMode = 4; // drop if small than 5, and keep if bigger than 4
            BigDecimal bd = new BigDecimal((double) floatnumber);
            bd = bd.setScale(scale, roundingMode);
            float ft = bd.floatValue();
            return String.valueOf(ft) + "%";

            //String converted = new DecimalFormat("##.##").format(ft);
            //return converted + "%";
        } catch (Exception ex) {
            return String.valueOf(floatnumber);
        }

    }
}

/**
 * format of the list of reported annotators in 2-way IAA results
 */
class ProcessedName {

    String annotator1 = null;  //
    String annotator2 = null;
}

/**
 * ist of reported annotators in 2-way IAA results
 */
class ProcessedNames {

    Vector<ProcessedName> processedNames = new Vector<ProcessedName>();

    public void clear() {
        processedNames.clear();
    }

    public void add(String annotator1, String annotator2) {
        ProcessedName pd = new ProcessedName();
        pd.annotator1 = annotator1;
        pd.annotator2 = annotator2;
        processedNames.add(pd);
    }

    boolean isProcessed(String annotator1, String annotator2) {


        for (ProcessedName pd : processedNames) {
            boolean found1 = false, found2 = false;

            if ((pd.annotator1 == null) || (pd.annotator2 == null)) {
                continue;
            }


            if (pd.annotator1.trim().compareTo(annotator1.trim()) == 0) {
                found1 = true;
            }
            if (pd.annotator1.trim().compareTo(annotator2.trim()) == 0) {
                found1 = true;
            }
            if (pd.annotator2.trim().compareTo(annotator1.trim()) == 0) {
                found2 = true;
            }
            if (pd.annotator2.trim().compareTo(annotator2.trim()) == 0) {
                found2 = true;
            }

            if ((found1) && (found2)) {
                return true;
            }
        }

        return false;

    }
}