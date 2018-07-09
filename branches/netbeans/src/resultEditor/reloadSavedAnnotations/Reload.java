/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.reloadSavedAnnotations;

import java.io.File;
import java.util.Vector;
import java.util.logging.Level;

/**
 * This class is to reload saved annotations from "saved" folder under your
 * current project, and show them on screen for current document.
 *
 * @author Jianwei Chris Leng, April 19, 2011 at Williams Building
 */
public class Reload {

    /**constructor*/
    public Reload(){}

    /**the enterance method which can be called to start load annotations */
    public static void load(final userInterface.GUI  _gui){
        //new Thread()
        //{
            //@Override
            //public void run() {
                try{
                    
                    clearAnnotationDepot();

                     // get the path of the "saved" folder under your current project
                    File saved = getObjectPath();

                    // exit if eHOST can not find "saved" folder under current project
                    if(saved==null) {
                        display_NoInput();
                    }

                    // get all knowtator xml files under "saved"
                    Vector<File> knowtatorXmlFiles = listXMLs(saved);

                    if((knowtatorXmlFiles==null)||((knowtatorXmlFiles.size()<1))) {
                        display_NoInput();
                    }

                    Vector<File> selectedXmlFiles = selectMatchedXMLs(knowtatorXmlFiles);
                    if((selectedXmlFiles==null)||(selectedXmlFiles.size()<1)) {
                        display_NoInput();
                    }

                    /*for(File f:selectedXmlFiles){
                        System.out.println("--"+f.toString());
                    }*/

                    extractAnnotation_fromXML( selectedXmlFiles );
                    
                    //Set annotations visible
                    _gui.setAnnotationVisible();
    //                _gui.showFirstFile_of_corpus()

                    // clear all attributes value display and disable those buttons as
                    // no annotation are selected just after annotations been imported.
                    _gui.disableAnnotationDisplay();

                    // as we just have extracted and stored annotated classname while
                    // importing annotation from xml knowtator files, we should write new
                    // found annotated class name into disk.
                    config.project.ProjectConf projectconf = new
                            config.project.ProjectConf(
                                env.Parameters.WorkSpace.CurrentProject
                                );
                    projectconf.saveConfigure();

                    // show all classes in the tree view
                    //_gui.showAnnotationCategoriesInTreeView_CurrentArticle();
                    _gui.showValidPositionIndicators_setAll();
                    _gui.showValidPositionIndicators();

                    //_gui.setNAVCurrentTab(3);


                    _gui.totalRefresh();
                    _gui.refreshInfo();
                    _gui.display_repaintHighlighter();

               
                // show clinical notes in color
                //int selected = jComboBox_InputFileList.getSelectedIndex();
                //__gui.showFileContextInTextPane(selected, 0);

                /*
                 // start loading these XML annotations files into memory
                __gui.importAnnotations();

                // refresh screen
*/              }catch(Exception ex){
                    log.LoggingToFile.log(Level.SEVERE, "/n#### fail to load saved annotations!!!/n"
                            + ex.getMessage());
                }
            //}
        //}.start();
    }

    /**clear the annotation depot while open a new project*/
    private static void clearAnnotationDepot(){
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.clear();
    }

    /**We only select XML files who can be matched by the raw documents of
     * current project.
     */
    private static Vector<File> selectMatchedXMLs(Vector<File> _savedrXmlFiles){

        Vector matchedAnnotationFiles = new Vector();
        try{
            // compare to the raw text documents and only selected xml files who have
            // same names occurred in the list of raw documents
            int loops = _savedrXmlFiles.size();
            int size_InputedClinicalNotes = env.Parameters.corpus.getSize();
            // to all selected files to import, let's get knowtator xml files whose filenames are
            // appeared in the list of text sources after string ".knowtator.xml" removed

            for (int i = 0; i < loops; i++)
            {
                // get each xml file in the list
                String filename = _savedrXmlFiles.get(i).getName();
                if(filename==null)
                    continue;
                filename = filename.trim().toLowerCase();


                for (int j = 0; j < size_InputedClinicalNotes; j++) {
                    
                    // get textsourceFilename of each imported clinical notes
                    if(env.Parameters.corpus.LIST_ClinicalNotes.get(j)==null)
                        continue;
                    if(env.Parameters.corpus.LIST_ClinicalNotes.get(j).file==null)
                        continue;

                    String inputfilename = env.Parameters.corpus.LIST_ClinicalNotes.get(j).file.getName();                    

                    inputfilename = (inputfilename + ".knowtator.xml").trim().toLowerCase();

                    boolean matched = false;
                    // than we can compare them, and record matched annotation xml file
                    if ( filename.compareTo(inputfilename) == 0 ){
                        log.LoggingToFile.log(Level.INFO,"Found saved annotation in format of knowtator XML file: " + filename);
                        matchedAnnotationFiles.addElement(_savedrXmlFiles.get(i));
                    }

                    try {
                        String inputfilename2 = inputfilename.substring(0, inputfilename.length() - 4) + ".knowtator.xml";
                        if (filename.compareTo(inputfilename2) == 0) {
                            log.LoggingToFile.log(Level.INFO, "Found saved annotation in format of knowtator XML file: " + filename);
                            matchedAnnotationFiles.addElement(_savedrXmlFiles.get(i));
                        }
                    } catch (Exception ex) {
                        // No need to do anything here
                    }

                }
            }
        }catch(Exception ex){
            System.out.println("ERROR 1201311016:: Got problem to find saved annotations. " + ex.getMessage() );
            return null;
        }

        //**** In above list of selected XML files, they may have some repetitive
        // items. We need to remove all repetitive items.
        try{
            if(matchedAnnotationFiles!=null){
                for(int i=0;i<matchedAnnotationFiles.size();i++){
                    if (matchedAnnotationFiles.get(i)==null)
                        continue;
                    for(int j=0;j<matchedAnnotationFiles.size();j++){
                        if(i==j)
                            continue;
                        if (matchedAnnotationFiles.get(j)==null)
                            continue;
                        if (matchedAnnotationFiles.get(j).equals(matchedAnnotationFiles.get(i))){
                            matchedAnnotationFiles.setElementAt(null, j);
                        }
                    }
                }
            }

            // drop all null values
            for(int i=matchedAnnotationFiles.size()-1; i>=0; i--){
                if(matchedAnnotationFiles.get(i)==null)
                    matchedAnnotationFiles.removeElementAt(i);
            }
        }catch(Exception ex){
        }

        return matchedAnnotationFiles;
    }


    /**load XML files and then extract annotations from them.
     * @param   XMLFiles
     */
    private static void extractAnnotation_fromXML(Vector<File> XMLFiles) {
        // ##0## record file information about all imported xml files
        try{
            if(XMLFiles==null)
                return;

            for(File f:  XMLFiles){
                if(f==null)
                    continue;
                env.Parameters.AnnotationsImportingCorrelated.allImportedXMLs.add(f);
                    //System.out.println("xml annotation file name and path saved! -" + f.toString() );
                
            }

        }catch(Exception ex){}

        // ##1## start annotation extraction
        resultEditor.annotations.ImportAnnotation imports = new
                resultEditor.annotations.ImportAnnotation();
        imports.XMLImporter(XMLFiles);

    }

    /**list all xml files under "saved" folder*/
    private static Vector<File> listXMLs(File savedfolder){

        Vector<File> xmlfiles = new Vector<File>();

        try{
            File[] files = savedfolder.listFiles();
            if(files==null)
                return null;
            if(files.length<1)
                return null;


            for(File file:files){
                if(file==null)
                    continue;
                if(!file.exists())
                    continue;
                if(file.isDirectory())
                    continue;
                String filename = file.getName();
                if((filename==null)||(filename.trim().length()<14))
                    continue;

                // get the last 4 characters from the filename
                String last14chars = filename.trim().substring(
                        filename.trim().length()-14,
                        filename.trim().length()
                        );

                //System.out.println("last [" + last14chars+"]");

                // if this file is a knowtator annotation file
                if(last14chars.toLowerCase().compareTo( ".knowtator.xml" ) == 0 ){
                    xmlfiles.add(file);
                }
            }
        }catch(Exception ex){
            return null;
        }
        
        return xmlfiles;
    }

    private static void display_NoInput(){
    }

    /**get the path of the "saved" folder under current project*/
    private static File getObjectPath(){
        // get path of current project
        File project = getPathOfCurrentProject();

        if(project==null)
            return null;

        String path = project.getAbsolutePath() + File.separatorChar + "saved"
                + File.separator;
        File saved = new File(path);
        if(saved==null)
            return null;

        if(!saved.exists())
            return null;

        if(saved.isFile())
            return null;

        return saved;
    }

    /**get path of current project*/
    private static File getPathOfCurrentProject(){
        try{
            // get current project
            File currentProject = env.Parameters.WorkSpace.CurrentProject;
            if (currentProject == null)
                return null;

            if(!currentProject.exists())
                return null;

            if(currentProject.isFile())
                return null;

            return currentProject;

        }catch(Exception ex){
            return null;
        }
    }
}
