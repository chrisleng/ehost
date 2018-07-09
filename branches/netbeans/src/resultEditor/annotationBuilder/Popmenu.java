/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationBuilder;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;
import resultEditor.annotations.Depot;
import resultEditor.annotations.SpanSetDef;
import umls.UMLSBrowser;
import userInterface.GUI;

/**
 *
 * @author leng
 */
public class Popmenu extends JPanel{
    protected JTextComponent tc;
    protected JPopupMenu popup;
    protected userInterface.GUI gui;
    protected int eHOSTx, eHOSTy, width, height;
    
    private String selectedText;
    private int selectedStart, selectedEnd;
    ShowAnnotationDlg ShowAnnotationDlg;
    protected java.awt.event.MouseEvent evt;

    protected static OracleFunction dialog_similarAnnotations;

    public Popmenu(JTextComponent tc, userInterface.GUI gui){
        init(tc, gui);
    }

    public Popmenu(JTextComponent tc, userInterface.GUI gui, java.awt.event.MouseEvent evt){
        init(tc, gui);
        this.evt = evt;
    }

    private void init(JTextComponent tc, userInterface.GUI gui){
        this.tc = tc;
        this.gui = gui;
        if( tc!= null ){
            this.selectedText = tc.getSelectedText();
            if ( this.selectedText != null ){
                this.selectedStart = tc.getSelectionStart();
                this.selectedEnd   = tc.getSelectionEnd();
            }
        }
    }

    public void pop(int x, int y, final int eHOSTx, final int eHOSTy, final int width, final int height){

        this.eHOSTx = eHOSTx; this.eHOSTy = eHOSTy;
        this.width = width; this.height = height;
        
        // do not pop menu if no text selected in the textpane
        //if((!isTextSelected()||(!isXMLorPinImported()))){
        if(!isTextSelected()){
            commons.Tools.beep();
            return;
        }

        // pop the popmenu
        popup = new JPopupMenu();
        final ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ShowDialogToCreateAnnotation();
            }
        };
        
        

        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));

        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        if ( depot != null){
            depot.sort();
            String[] classnames = depot.getAnnotationClasssnamesString();
            if (( classnames != null )&&( classnames.length > 0)){
                
                for(String classname : classnames){
                    popup = buildpopmenu( popup, classname );
                }
                
                //JSeparator seperator = new JSeparator();
                //popup.add(seperator);
            }
        }

        JMenuItem item;
        //popup.add(item = new JMenuItem("New via dialog", new ImageIcon("1.gif")));
        //item.setHorizontalTextPosition(JMenuItem.RIGHT);
        //item.addActionListener(menuListener);
        
        
        final ActionListener menuListener_UMLS = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                openUMLSBrowser();
    }
        };
    
        
        String selectedtext = tc.getSelectedText();
    	if( ( selectedtext != null )&&( selectedtext.trim().length()>0 ) )
        {
        
        //JSeparator seperator = new JSeparator();
        //popup.add(seperator);
    
        //popup.add(item = new JMenuItem("Search \""+ selectedtext +"\" using UMLS", new ImageIcon("1.gif")));
        //item.setHorizontalTextPosition(JMenuItem.RIGHT);
        //item.addActionListener(menuListener_UMLS);
        }
    
        
        
        final String text = selectedtext;
        JSeparator seperator = new JSeparator();
        popup.add(seperator);
        JMenuItem item1;
        popup.add(item1 = new JMenuItem("Copy to clipboard"));
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {                
            StringSelection stringSelection = new StringSelection(selectedText);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            }
        });
        
        
        popup.show(tc, x, y);

    }

    private JPopupMenu buildpopmenu(final JPopupMenu popup, final String annotationClassname){
        final ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                gui.setModified();
                createAnnotation( annotationClassname );
            }
        };


        JMenuItem item;
        popup.add(item = new JMenuItem("New \"" + annotationClassname + "\"", new ImageIcon("1.gif")));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);

        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        return popup;
    }

    /**Open the UMLS browser to search concepts which may match the selected term(s).*/
    private void openUMLSBrowser(){
    	String selectedtext = tc.getSelectedText();
    	if( ( selectedtext == null )||( selectedtext.trim().length()<1 ) )
    		return;
    	if( this.gui.umlsbrowser != null )
            this.gui.umlsbrowser.dispose();
        
        this.gui.umlsbrowser = new UMLSBrowser(selectedtext, null, this.gui );
        
    	this.gui.umlsbrowser.setVisible( true );
    }
    
    private void ShowDialogToCreateAnnotation(){
        String selectedtext = tc.getSelectedText();
        int selectedtext_start = tc.getSelectionStart();
        int selectedtext_end = tc.getSelectionEnd();

        int offset = resultEditor.conflicts.ConflictButtonPainter
                .getScreenOffset(selectedtext_start, 1);
        selectedtext_start = selectedtext_start - offset;
        selectedtext_end   = selectedtext_end - offset;

        ShowAnnotationDlg = new ShowAnnotationDlg(gui);
        ShowAnnotationDlg.showDialog(eHOSTx, eHOSTy, width, height,
                selectedtext, selectedtext_start, selectedtext_end);
    }

    public boolean isGood()
    {
        if(ShowAnnotationDlg != null)
            return ShowAnnotationDlg.isGood();
        return false;
    }
    
    public void getFocus()
    {
        if(ShowAnnotationDlg != null)
            ShowAnnotationDlg.getFocus();
    }

    /**check whehter any texts in the text pane have got selected.*/
    private boolean isTextSelected(){
        if(tc.getSelectedText() != null)
            return true;
        else return false;
    }

    /**check whether any xml or pin files got imported*/
    private boolean isXMLorPinImported(){
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        if( depot.getSize() > 0)
            return true;
        else
            return false;
    }

    private int assignMeAUniqueIndex(){
         return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }


    /**
     * One click to create new annotation with designate markable category.
     *
     * @param   markableName
     *          The name string of the markable which selected and highlighted by
     *          cross-lines. New annotation's markable category will be this
     *          string.
     */
    public void oneClicktoCreateAnnotation(String markableName ){
        if (( markableName != null )&&( markableName.trim().length() > 0 )) {
            createAnnotation( markableName.trim() );
            gui.setModified();
        }
    }
    
    
   

    /**
     * Local method to create new annotation by one clicking with designated
     * markable category.
     * This method used to be called by method "oneClicktoCreateAnnotation"
     * in the same class.
     *
     * @param   classname
     *          The name string of the markable which selected and highlighted by
     *          cross-lines. New annotation's markable category will be this
     *          string.
     */
    private void createAnnotation(final String classname){
        //ShowAnnotationDlg ShowAnnotationDlg = new ShowAnnotationDlg( gui );
        //ShowAnnotationDlg.showDialog( gui.getX(), gui.getY(), gui.getWidth(), gui.getHeight(),
        //        this.selectedText, this.selectedStart, this.selectedEnd, classname);

        String testsourceFilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();


        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
        depot.articleInsurance( testsourceFilename );
        String createdate = commons.OS.getCurrentDate();
        String annotator_name = resultEditor.annotator.Manager.getAnnotatorName_OutputOnly();
        if( GUI.reviewmode == GUI.reviewmode.adjudicationMode )
            annotator_name = "ADJUDICATION";
        String annotator_id = resultEditor.annotator.Manager.getAnnotatorID_outputOnly();

        SpanSetDef spanset = new SpanSetDef();
        spanset.addSpan(this.selectedStart, this.selectedEnd);

        int newuniqueindex = assignMeAUniqueIndex();
        
        
        if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
            depot.addANewAnnotation( testsourceFilename,
                this.selectedText,
                spanset,
                // creation date
                createdate,
                // annotation classname
                classname,
                annotator_name,
                annotator_id,
                null,
                null,
                newuniqueindex
                );
        else if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
            depotOfAdj.addANewAnnotation( testsourceFilename,
                this.selectedText,
                spanset,
                // creation date
                createdate,
                // annotation classname
                classname,
                annotator_name,
                annotator_id,
                null,
                null,
                newuniqueindex
                );
        
        depot.setAttributeDefault( testsourceFilename, newuniqueindex );



        if(gui.reviewmode == GUI.reviewmode.adjudicationMode ){
            gui.refresh_adjudication();
        }
        
        tc.setSelectionEnd(0);
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.display_repaintHighlighter();
        gui.showValidPositionIndicators();
        



        
        

        // record this annotaion as selected
        Depot.SelectedAnnotationSet.selectJustOneAnnotation(newuniqueindex);
        // show this newly built annotation in list on editor panel and deails
        gui.display_showSelectedAnnotations_inListOnEditorPanel(newuniqueindex);
        // enable editor button        
        gui.enable_AnnotationEditButtons();

        if( env.Parameters.oracleFunctionEnabled ){
            // lets find similar annotations who has same text as the annotation we just found
            new Thread(){
                    @Override
                    public void run(){
                        find_simlar_annotations(Popmenu.this.selectedText, Popmenu.this.tc,
                                Popmenu.this.selectedStart, Popmenu.this.selectedEnd, classname );
                    }
            }.start();
        }
        
        
        
    }


    private void find_simlar_annotations(String annotationtext, JTextComponent tc, 
            int start, int end, String classname)
    {
        // define variables
        String documentContent = null;
        boolean found_SimilarTxt_inOtherDoc = false;
        File thefile;
        
        try{
            thefile = get_current_file();
            if (thefile == null)
                return;
            if (!thefile.exists())
                return;
            

            // validity checking
            if ( annotationtext == null)
                return;
            if ( annotationtext.trim().length() < 1)
                return;
            if ( tc == null )
                return;
            if ( tc.getDocument() == null )
                return;

            // get document content
            int size = tc.getDocument().getLength();

            try {
                documentContent = tc.getDocument().getText(0, size);
            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1008261212 - fail to get content of current docuement.");
            }
            if( documentContent == null )
                return;
            if( documentContent.trim().length() < 1  )
                return;
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1010202347::"+ex.toString());
            return;
        }

        try{
            // run expression
            Vector<Term> terms = search_trim_inDesignatedDocument( annotationtext, documentContent );
            if ( terms == null )
                return;

            // if found result in current document
            if ( terms.size() > 1 ){

                terms = remove_found_term(terms, start, end);
                
                //terms = removeDuplicates(terms, thefile);
                

       
                if((dialog_similarAnnotations !=null )
                 &&(dialog_similarAnnotations instanceof OracleFunction)){
                    try{
                        dialog_similarAnnotations.dispose();
                    }catch(Exception ex){
                        log.LoggingToFile.log(Level.SEVERE, "error 1010111641::fail to dispose old windows of oracle!!!");
                    }
                }


                // pop the dialog
                dialog_similarAnnotations = new OracleFunction(gui, classname, thefile);
                dialog_similarAnnotations.firstload(terms);
                dialog_similarAnnotations.setVisible(true);
                                
                // found_SimilarTxt_inOtherDoc = true;
                found_SimilarTxt_inOtherDoc = true;
                
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1010202351::" + ex.toString());
            return;
        }

        if( env.Parameters.working_on_file ){
            found_SimilarTxt_inOtherDoc = false;
            return;
        }
        // try to find annotations in all documents
        // if do not find any simlar annotations in current document
        {
            
            try{
                
                File[] filelist = get_filelist();

                if(filelist == null)
                    return;

                for(File file: filelist) {
                    if ( file==null )
                        continue;

                    if (file.getName().trim().equals( thefile.getName().trim() ))
                        continue;

                    // get content of document
                    String document = get_content_fromDocument(file);
                    if (document == null)
                        continue;

                    // search trim of document
                    Vector<Term> O2terms = search_trim_inDesignatedDocument( annotationtext, document );
                    
                    if((O2terms==null)||(O2terms.size()<=0))
                        continue;

                    //if((O2terms!=null)&&(O2terms.size()>0)){
                        
                    if( found_SimilarTxt_inOtherDoc == true) {

                        if((dialog_similarAnnotations !=null )
                         &&(dialog_similarAnnotations instanceof OracleFunction)) {
                            dialog_similarAnnotations.showData_onNewTab(O2terms, classname, file);
                            dialog_similarAnnotations.setVisible(true);
                        }

                    }else{

                        if((dialog_similarAnnotations !=null )
                         &&(dialog_similarAnnotations instanceof OracleFunction))
                            dialog_similarAnnotations.dispose();

                        // pop the dialog
                        dialog_similarAnnotations = new OracleFunction(gui, classname, file);
                        dialog_similarAnnotations.firstload( O2terms );
                        dialog_similarAnnotations.setVisible(true);
                        found_SimilarTxt_inOtherDoc = true;
                    }
                    
                }
                
            }catch(Exception ex){
                log.LoggingToFile.log(Level.SEVERE, "error 1010202353::"+ex.toString());
            }
            
        }

        
    }


    /**read text content from document.*/
    private String get_content_fromDocument(File file){
        String document = "";
        try{
            BufferedReader docFile = new BufferedReader(new FileReader(file));
            String line = docFile.readLine();

            while (line != null) {
                document = document + line+ " " ;
                line = docFile.readLine();
            }
        }catch(Exception ex) {
            document = null;
            log.LoggingToFile.log(Level.SEVERE, "Error 10271456:: - fail to read text content from document.");
        }
        
        return document;
    }

    /**get filename of current document*/
    private File get_current_file(){
        File file = null;
        try{
            file = resultEditor.workSpace.WorkSet.getCurrentFile();
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 10271426:: - fail to get filename of current document.");
            file = null;
        }

        return file;
    }

    private File[] get_filelist(){
        File[]  filelist = null;
        try{
            filelist = resultEditor.workSpace.WorkSet.getAllTextFile();
            if(filelist!=null){
                if(filelist.length < 1)
                    return null;
            }
        }catch(Exception ex){
            filelist = null;
            log.LoggingToFile.log(Level.SEVERE, "Error 10271429:: - fail to get filename of current document.");
        }

        return filelist;
    }

    private Vector<Term> remove_found_term( Vector<Term> terms, int start, int end){
        if (terms == null) {
            log.LoggingToFile.log(Level.SEVERE, "error 1008261328 - should not be an empty term list!!!" );
            return null;
        }

        Vector<Term> newterms = terms;
        int size = newterms.size();
        for(int i=0;i<size;i++){
            Term term = newterms.get(i);
            if ( term == null )
               continue;
            if ((term.start == start)&&(term.end == end)) {
                newterms.remove(i);
                return newterms;
            }
        }

        log.LoggingToFile.log(Level.SEVERE, "error 1008261332 - did not remove the existed term from list of founds!!!" );
        return newterms;
    }
    
    //private Vector<Term> removeDuplicates

    private Vector<Term> search_trim_inDesignatedDocument(String originalTerm, String documentContent){
        if ( (documentContent==null)||(originalTerm==null) )
            return null;
        if (( documentContent.trim().length() < 1)||(originalTerm.trim().length()<1))
            return null;

        Matcher matcher;
        Vector<Term> found_terms = new Vector<Term>();

        try{
            String regex = buildRegex( originalTerm );
            matcher = Pattern.compile( regex, Pattern.CASE_INSENSITIVE ).matcher( documentContent );

            boolean match_found = matcher.find();

            int start, end; String text;
            int surroundstart, surroundend; String surroundtext;
            int start_in_surroundtext, end_in_surroundtext;
            int count = 0 ;

            while(match_found){
                count++;
                // get matched details
                start = matcher.start();
                end = matcher.end();
                text = documentContent.substring(start, end);
                
                // ****ECHO
                // System.out.println("    - found text ["+ text +"]");

                // assemble data in structure of term, so we can send out result to other
                // method
                Term term = new Term();
                term.termtext = text;
                term.start = start;
                term.end = end;
                surroundstart = start - 20;
                surroundend = end + 20;
                start_in_surroundtext = 20;
                end_in_surroundtext = 20 + text.length();

                if ( start - 20 < 0 ){
                    surroundstart = 0;
                    start_in_surroundtext = start;
                }else if ( end + 20 > documentContent.length() - 1 ){
                    surroundend = documentContent.length() - 1;
                    end_in_surroundtext = documentContent.length() - 1 - end;
                }

                surroundtext = documentContent.substring( surroundstart , surroundend );
                term.surroundtext = surroundtext;
                //System.out.println("    - surround text ["+ surroundtext +"]");
                term.start_in_surroundtext = start_in_surroundtext;
                term.end_in_surroundtext = end_in_surroundtext;

                found_terms.add(term);


                // go next record if existing
                match_found = matcher.find();
            }

            return found_terms;
            
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,  "error 1008261257 - " + e.toString() );
        }

        return null;
    }

    private String buildRegex(String originalRegex){
        if( originalRegex == null )
            return null;
        if ( originalRegex.trim().length() < 1 )
            return null;

        String regex = originalRegex;
        regex = regex.replaceAll("\\[", "\\\\[");
        regex = regex.replaceAll("\\]", "\\\\]");
        regex = regex.replaceAll("\\.", "\\\\.");
        regex = regex.replaceAll("\\?", "\\\\?");
        regex = regex.replaceAll("\\*", "\\\\*");

        // ##2## if we need to search wholeword matched term only
        if(env.Parameters.Oracle.search_matchWholeWord){
            regex = "\\b"+regex+"\\b";
        }

        return regex;
    }



}
