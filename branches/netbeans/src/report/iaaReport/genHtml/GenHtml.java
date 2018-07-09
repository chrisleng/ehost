/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.genHtml;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import report.iaaReport.ClassAgreementAnnotators;
import report.iaaReport.PairWiseAgreementRecord;

/**
 *
 * @author leng
 */
public class GenHtml {



    public void genHTMLs(
            Vector<ClassAgreementAnnotators> allClassRecords,
            Vector<PairWiseAgreementRecord> pairwise,
            ArrayList<String> selectedAnnotators, //selected annotators from the IAA panel
            ArrayList<String> selectedClasses, // selected class for the IAA panel
            int totalProcessedDocuments )
    {
        try{
            log.LoggingToFile.log(Level.FINE, "eHOST is in a module to generate"
                    + " IAA html report for class and span matcher."  );

            if((pairwise==null)||(pairwise.size()<1))
            {
                log.LoggingToFile.log(Level.SEVERE, "1108181646:: method got "
                        + "parameter with empty contents!"  );
                return;
            }

            System.out.println(" #### marking dir ");
            // #### ensure we have a folder named "reports" under your current project
            File reportfolder = null;
            try{
                reportfolder = makeReportDir();
                // delete all files under this folder of "reports"
                cleanFolder(reportfolder);
            }catch(Exception ex){
                log.LoggingToFile.log(Level.SEVERE, "1108181650:: fail to ensure we can have a folder named \"reports\" under current project!!!");
                log.LoggingToFile.log(Level.SEVERE, "1108181650:: " + ex.getMessage() );
                throw ex;
            }

           

            System.out.println(" ####  gen report for class and span matcher ");
            // #### gen report for class and span matcher
            try{
                GenHtmlClassAndSpan genHtml_for_classAndSpanMatcher = new GenHtmlClassAndSpan( reportfolder, allClassRecords, pairwise, selectedAnnotators, selectedClasses, totalProcessedDocuments);
            }catch(Exception ex){
                log.LoggingToFile.log(Level.SEVERE, "1108181651:: fail to generate the html report for span and class matcher!!!");
                log.LoggingToFile.log(Level.SEVERE, "1108181651:: " + ex.getMessage() );
                throw ex;
            }

            System.out.println(" ####  gen unmatched details ");
            // #### gen the html of unmatched details for each selected annotator
            try{
                GenHtmlForMatches genHtmlUnmatchedDetails = new GenHtmlForMatches();               
                
                genHtmlUnmatchedDetails.genHtml(reportfolder);
                GenHtmlForNonMatches nonMatching = new GenHtmlForNonMatches();
                nonMatching.genHtml(reportfolder);
                
            }catch(Exception ex){
                ex.printStackTrace();
                //log.LoggingToFile.log(Level.SEVERE, "1108290659:: fail to generate the IAA html report:: fail to output detailed non-matched annotations!!!");
                //log.LoggingToFile.log(Level.SEVERE, " - RELATED ERROR: " + ex.getMessage() );
                //throw ex;
            }


            System.out.println(" ####  gen index.html ");
            // #### gen the index
            try {

                GenIndex genindex = new GenIndex();
                genindex.genHtml(reportfolder, selectedClasses);
            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "1110132115:: fail to gen "
                        + "index.html for IAA system!!!"
                        + " - RELATED ERROR: " + ex.getMessage());
                throw ex;
            }

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1108181652::error occurred while we try to generate the IAA report in HTML format."
                    +"\n - RELATED ERROR: "
                    + ex.getMessage() );
        }
    }


    /**This method is designed to delete all files under designated folder.*/
    private void cleanFolder(File folder) throws Exception{
        if(folder==null){
            throw new Exception("1109081627::this given File is null!!!");
        }

        if(!folder.exists()){
            throw new Exception("1109081629::the folder you want to delete is not existing!!!");
        }

        if(folder.isFile()){
            throw new Exception("1109081626::the folder you want to delete is not a folder! It's just a file!");
        }

        try{
            File[] files = folder.listFiles();

            if(files==null)
                return;

            for(File file:files)
            {
                if(file.getName().compareTo(".")==0)
                    continue;
                if(file.getName().compareTo("..")==0)
                    continue;

                file.delete();
            }
        }catch(Exception ex){
            throw new Exception("1109081630::fail to clean the folder:[" + folder.getAbsolutePath()+"]");
        }

    }

    /**Ensure we have a folder named "reports" under your current project. If
     * there is no this folder, create a new one.
     */
    private File makeReportDir() throws Exception
    {

        File project = env.Parameters.WorkSpace.CurrentProject;
        if((project==null)||(!project.exists())){
            throw new Exception("1108182330::can not get access to the directory of current project!");
        }
        
        if(project.isFile()){
            throw new Exception("1108182331::the directory of current project is a FILE!!!");
        }

        File reportFold = new File(project.getAbsolutePath() + File.separatorChar + "reports" + File.separatorChar );

        // if this folder "reports" already exists, do nothing
        if(reportFold.exists()){
            log.LoggingToFile.log(Level.FINE, reportFold.getAbsolutePath() + " already existed under current project.");
            return reportFold;
        }

        boolean succeed = reportFold.mkdirs();

        if( !succeed )
        {
            throw new Exception("1108182333::fail to create the reports folder under current project - "
                    + reportFold.getAbsolutePath()
                    + "!!!");
        }

        return reportFold;

    }
}
