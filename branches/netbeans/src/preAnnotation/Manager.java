/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotation;

import resultEditor.annotationClasses.Depot;
import java.io.File;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class Manager {

    private userInterface.GUI gui = null;
    public Manager(){}
    public Manager(userInterface.GUI _gui) {
        this.gui = _gui;
    }


    public void preannotation_toPairs()
    {
        // ##1## get txt file list
        File[] files = getCorpusFileList();
        if(files==null)
        {
            log.LoggingToFile.log(Level.WARNING, "Warning 1101260434::Corpus return NULL!");
            return;
        }

        // ##2## get pairs item from dictionary
        preAnnotation.pairs.Dictionary dict = new preAnnotation.pairs.Dictionary();
        dict.loadDictionary();
        
        // ##3## to each file, get its text contens
        for(File corpus: files)
        {
            if(corpus==null)
                continue;

            // ##4## get text content of this text source
            Reader reader = new Reader();
            String contents = reader.readContents(corpus);

            // ##5## run pre-annotation to pairs and record results in memory
            preAnnotation.pairs.CheckPairs checkpairs = new preAnnotation.pairs.CheckPairs(corpus, contents);
            
            // ##6## update screen
            updateUI();
        }

        // ##7## free memory for pairs dictionary
        //dict.close();
    }

    public void confirmClassPerson(){
        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        if(!resultEditor.annotationClasses.Depot.isExisting("Person")){
            Depot.addElement("Person", resultEditor.annotator.Manager.getCurrentAnnotator(), 128, 192, 255);
        }

        if(!resultEditor.annotationClasses.Depot.isExisting("Pronoun")){
            Depot.addElement("Pronoun", resultEditor.annotator.Manager.getCurrentAnnotator(), 228, 192, 255);
        }
    }

    private void updateUI(){
        if(gui==null)
            return;

        gui.display_repaintHighlighter();
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.display_repaintHighlighter();
        gui.showValidPositionIndicators_setAll();
        gui.showValidPositionIndicators();
        gui.remove_all_underline_highlighter();

        

        //String testsourceFilename = ResultEditor.WorkSpace.WorkSet.getCurrentFile().getName().trim();
        //setSelectedDatainDepot( testsourceFilename, depot.get(0).terms.get(0).termtext );
        //gui.showAnnotationDetail(annotation);
        //gui.showSelectedAnnotations_inList(0);

        
    }

    /**get file list of all text source, which are in precessing now.*/
    private File[] getCorpusFileList()
    {
        File[] to_return = env.Parameters.corpus.getFiles();
        return to_return;
    }

    public void MarkPairs(){
        preAnnotation.pairs.Marker marker = new preAnnotation.pairs.Marker();
        marker.MarkerPairs();
    }
}
