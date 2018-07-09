/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface.annotationCompare;

import adjudication.SugarSeeder;
import adjudication.statusBar.DiffCounter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.*;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import resultEditor.workSpace.WorkSet;
import userInterface.GUI;

/**
 * This class is designed to build a custom button for the "diff" function.
 * 
 * It use different colors and texts to indicate different working status of
 * the annotation diff function. And it also controls the display of the
 * diff panel: hide or display, or load some data.
 *
 * @author Chrs Jianwei Leng : 2010-11-09 1153am
 *
 */
public class ExpandButton extends JPanel{

    /**it's true only if main annotation is loaded and displayed on the editor
     * panel.*/
    private boolean isComparingInProcessing=false;

    /** predefine colors to draw the button later*/
    //private Color   out = new Color(189, 189, 189 );
    //private Color  line = new Color( 51,  51,  51 );
    //private Color inner = new Color(128, 128, 128 );
    protected JSplitPane  jsParent;
    
    /**The panel that we split into 2 columns and used to contain the editor
     * panel and diff panel by set its layout as a 2-columns gird layout.*/
    protected JPanel jpanel_motherPanel;

    /**The panel that we used as parents of the differ panel. Basically, it's
     * a container panel.*/
    protected JPanel jpanel_diffPanel_container;

    /**The panel of editor*/
    protected JPanel jpanel_editorPanel_container;

    protected JButton jbutton_spaneditor_delete;
    protected JSeparator jSeparator2;
    //protected JLabel jSeperator;
    // components imported for controlling
    protected JToolBar toolbar_comparision_buttons;
    protected userInterface.GUI gui;
    protected JList jList1;
    protected JTextPane textPaneforClinicalNotes;

    /**GUI component: the Diff panel. */
    private userInterface.annotationCompare.Comparator compartor;

    /**A flag that used to tell eHOST whether should we we display the diff
     * panel on screen and put annotation data on it if we have multiple
     * annotations got selected. */
    public static boolean isDiffPanelWorking = false;


    public void cr_disableButton(){
        if(compartor!=null) compartor.cr_disableButton();
    }
            
    /**get current selected annotation from the list of selected annotation. 
     * 
     * @return  the annotation that is current selected in the list 
     *          of "selected annotations.
     */
    public Annotation getSelectedAnnotation(){
        if(compartor!=null) 
            return compartor.getSelectedAnnotation();
        else 
            return null;
    }
    
    /**update information about current selected annotation, update their spans.
     * attributes, and relationship on the comparator panel. The annotation is
     * current selected annotation in the list on the comparator panel.
     */
    public void cr_updateAnnotation_onComparatorPanel(){
        if(compartor!=null) 
            compartor.updateAnnotation();
    }
    
    public void cr_recheckDifference(){
        if((compartor!=null)&&(compartor.isVisible()))
            compartor.recheckDifference();
    }
    
    
    
    /**Return boolean value to indicate the comparator panel has been expanded
     * or not.
     *
     * @return  true, if the comparator has been expanded;
     *          false, otherwise.
     */
    public boolean isComparatorPanelExpanded(){
        //if((statu == status.button_canbehide)||(statu == status.button_canbehide))
        //    return true;
        //else
            return false;
    }

    /**constructor*/
    public ExpandButton(JPanel jpanel_motherPanel, JSplitPane jsParent, 
            JPanel jpanel_diffPanel_container,
            JPanel jpanel_editorPanel_container){
        // call super
        super();
        this.jpanel_motherPanel = jpanel_motherPanel;
        this.jsParent = jsParent;
        this.jpanel_diffPanel_container = jpanel_diffPanel_container;
        this.jpanel_editorPanel_container = jpanel_editorPanel_container;

        

        // set additional component attributes
        setComponent();
    }


    /**somehow, java could not send the panel_container ontime in the
     * constructor. So here we need to assign this container manually.
     * This function only need to be called after components got initilized in
     * class of GUI.
     *
     */
    public void setContainerPart(JPanel panel_container,
            JButton jbutton_spaneditor_delete,
            JSeparator jSeparator2,
            //JLabel jSeperator,
            JToolBar toolbar_comparision_buttons,
            JList jList1,
            userInterface.GUI gui,
            JTextPane textPaneforClinicalNotes
            ){
        this.jpanel_diffPanel_container = panel_container;
        this.jSeparator2 =  jSeparator2;
        this.jbutton_spaneditor_delete = jbutton_spaneditor_delete;
        //this.jSeperator = jSeperator;
        this.toolbar_comparision_buttons = toolbar_comparision_buttons;
        this.jList1 = jList1;
        this.gui = gui;
        this.textPaneforClinicalNotes = textPaneforClinicalNotes;
    }




    /**Init components for init eHOST. 
     * This method is to set the diff panel invisible and the diff button
     * invisible. */
    public void defaultStatus(){

        ExpandButton.isDiffPanelWorking = false;
        
        // set button invisible
        setStatusInvisible();

        // set diff panel invisible
        this.setRightPaneVisible(false);
    }

    public void setStatusInvisible(){
        try{
            
            isComparingInProcessing = false;
            this.setCursor( Cursor.getDefaultCursor() );                        
            this.statu = status.button_invisiable;            
            this.setToolbar_forNormal();

            //this.setToolbar_forCompare();

            // This is the most suitable width of these two panes in their split
            // pane, if size less then it, eHOST shoult try to regain this size for
            // them.
    


            //  load jpane from class
            // components_loadComparatorPane();
            //this.components_loadEmptyOne();

            statu = status.button_invisiable;  // button is invisible
            //this.setRightPaneVisible( ExpandButton.isDiffPanelWorking ); // display the diff panel while needed
            //this.jpanel_diffPanel_container.setEnabled(false);
            
            this.updateUI();



        }catch(Exception ex){
            System.out.println("1109182053::");
        }
        
        this.updateUI();
    }

    private void setToolbar_forCompare(){
        this.setVisible(true);
        try{
            jbutton_spaneditor_delete.setVisible(false);
            jSeparator2.setVisible( false );
            //jSeperator.setVisible(false);
            toolbar_comparision_buttons.setVisible(true);
            
            gui.display_DisabledEditorModification_forCompareMode();

        }catch(Exception ex){
            System.out.println("1109182054::");
        }
    }

    /**User clicked button of "accept" on editor panel in comparation
     * mode to only accept primary annotation in this annotation set.
     */
    public void acceptreject_acceptPrimaryAnnotation(){

        // user click accept button on editor panel
        try{// ##1## delete
            Depot.SelectedAnnotationSet.data_onlyKeepPrimaryAnnotation();
            

            // ##2## remove components on comparator panel
            this.setStatusInvisible();
            
            recheckUnMatches();
            
            // preset: leave from comparision mode if it was in compraision status
            this.setStatusInvisible();
            this.noDiff();
            // ##3## show primary annotation on editor panel (annotation on list
            // and details on other fileds)   
            gui.display_RelationshipPath_Remove();
            gui.remove_all_underline_highlighter();
            gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
            gui.display_repaintHighlighter();
            gui.showAnnotationCategoriesInTreeView_refresh();
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011171324:: fail to accept primiary " +
                    "annotation on editor panel" + ex.toString());
        }
        
    }
    
    /**This method is purpose to refresh screen after adding a new annotation in 
     * the adjudication mode.*/
    public void refresh_addingAnnotation(){
        // preset: leave from comparision mode if it was in compraision status
            this.setStatusInvisible();
            this.noDiff();
            
            recheckUnMatches();
            
            // ##3## show primary annotation on editor panel (annotation on list
            // and details on other fileds)   
            gui.display_RelationshipPath_Remove();
            gui.remove_all_underline_highlighter();
            //gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
            gui.display_repaintHighlighter();
    }


    /**User clicked button of "accept" on comparator panel in comparation
     * mode to only accept a specific annotation in this annotation set.
     *
     * @param   uniqueindex
     *          The unique index you want to accept in the annotation set.
     */
    public void acceptreject_acceptAlternativeAnnotation(int uniqueindex){

        // user click accept button on editor panel
        try{// ##1## delete
            Depot.SelectedAnnotationSet.data_onlyKeepAnnotation(uniqueindex);

            // ##2## remove components on comparator panel
            this.setStatusInvisible();
            this.noDiff();

            
            recheckUnMatches();
            
            // ##3## show primary annotation on editor panel (annotation on list
            // and details on other fileds)
            gui.display_RelationshipPath_Remove();
            gui.remove_all_underline_highlighter();
            gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
            gui.display_repaintHighlighter();
            gui.showAnnotationCategoriesInTreeView_refresh();
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011171324:: fail to accept primiary " +
                    "annotation on editor panel" + ex.toString());
        }

    }
    
    public void recheckUnMatches(){
        
        
        try{            
            
            // get current file
            String currentFileName = WorkSet.getCurrentFile().getName();            
            if( currentFileName == null )
                return;
            if( currentFileName.trim().length() < 1)
                return;
            
            // get annotations of current file
            Article currentArticle = null;
            
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE  )
                currentArticle = Depot.getArticleByFilename(currentFileName);
            else{
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                currentArticle = depotOfAdj.getArticleByFilename(currentFileName);
            }
            
            // reset annotations before searching for unmatches again
            adjudication.Adjudication.resetAnnotations(currentFileName);
            
            
            // search non-matches again and record difference for the 
            // mark-function
            adjudication.Adjudication.searchDifferenceinArticle( 
                    currentArticle,
                    adjudication.parameters.Paras.getAnnotatorAt( 0) );
            
            
            
        }catch(Exception ex){
            System.out.println( "ERROR " + ex.toString() );
        }
    }


    /**User clicked button of "reject" on editor panel in comparation
     * mode to select next annotation in this annotation set as primary
     * annotation.
     */
    public void acceptreject_rejectPrimaryAnnotation(){
        
        try{
            //##1## delete primary annotation
            Depot.SelectedAnnotationSet.data_onlyDeletePrimaryAnnotation();

            if( Depot.SelectedAnnotationSet.size()==0){
                
                isComparingInProcessing = false;
                recheckUnMatches();
                gui.display_repaintHighlighter();
                
                    // ##2## remove components on comparator panel
                this.setStatusInvisible();
                this.noDiff();


                // ##3## show primary annotation on editor panel (annotation on list
                // and details on other fileds)
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
                gui.display_repaintHighlighter();
                gui.showAnnotationCategoriesInTreeView_refresh();
                DiffCounter.accept();

            } else 
            // ##2## if there only have 2 annotations in current selected
            // annotation set
            if (Depot.SelectedAnnotationSet.size() == 1) {

                // ##2.1## remove components on comparator panel
                //this.setStatusInvisible();
                this.setVisible(false);
                this.setRightPaneVisible2(false);
                //this.setStatusInvisible();
                recheckUnMatches();
                
                int uniqueindex = Depot.SelectedAnnotationSet.getSelectedAnnotationSet().get(0);
                Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = uniqueindex;

                // ##2.2## show primary annotation on editor panel (annotation
                // on list and details on other fileds)
                gui.display_RelationshipPath_Remove();
                //gui.remove_all_underline_highlighter();
                gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
                gui.display_repaintHighlighter();
                gui.showAnnotationCategoriesInTreeView_refresh();
                //gui.showAnnotationCategoriesInTreeView_refresh();
                //DiffCounter.accept();

            } else {

                // ##3## find first one annotation in rest annotations and show
                // it on
                int uniqueindex = Depot.SelectedAnnotationSet.getSelectedAnnotationSet().get(0);
                Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = uniqueindex;
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                gui.display_showOneAnnotation_inListOnEditorPanel(uniqueindex);
                gui.showAnnotationCategoriesInTreeView_refresh();
                isComparingInProcessing = true;

                recheckUnMatches();

                this.display_showAnnotations_inComparatorList();

                gui.display_repaintHighlighter();
            }

        }catch(Exception ex){
            //log.LoggingToFile.log( Level.SEVERE,"error 1011171325:: fail to reject primiary " +
            //        "annotation on editor panel" + ex.toString());
        }
    }
    
    /**User clicked button of "reject" on editor panel in comparation
     * mode to select next annotation in this annotation set as primary
     * annotation.
     */
    public void acceptAll(){

    
                // user click accept button on editor panel
        try{// ##1## delete
            //Depot.SelectedAnnotationSet.data_onlyKeepPrimaryAnnotation();
            Depot.SelectedAnnotationSet.data_acceptAll();

            // ##2## remove components on comparator panel
            this.setStatusInvisible();
            
            recheckUnMatches();
            
            // preset: leave from comparision mode if it was in compraision status
            this.setStatusInvisible();
            this.noDiff();
            // ##3## show primary annotation on editor panel (annotation on list
            // and details on other fileds)   
            gui.display_RelationshipPath_Remove();
            gui.remove_all_underline_highlighter();
            gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
            gui.display_repaintHighlighter();
            gui.showAnnotationCategoriesInTreeView_refresh();
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011171324:: fail to accept primiary " +
                    "annotation on editor panel" + ex.toString());
        }
    }


    /**Sometimes, these is one or more annotation(s) appreared in the
     * annotations set which you selected for compare. But these annotations
     * do not belong this set. We click the "ignore" button in comparasion
     * mode to kick this/these annotation(s) out of our annotation set and
     * then we can continue our accept/reject operation to solve our
     * conflicts.
     */
    public void acceptreject_ignorePrimaryAnnotation(){
        try{
            //##1## delete primary annotation
            Depot.SelectedAnnotationSet.data_onlyIgnorePrimaryAnnotation();

            // ##2## if there only have 2 annotations in current selected
            // annotation set
            if( Depot.SelectedAnnotationSet.size()==1){

                // ##2.1## remove components on comparator panel
                this.setStatusInvisible();

                recheckUnMatches();
                
                // ##2.2## show primary annotation on editor panel (annotation
                // on list and details on other fileds)
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
                gui.display_repaintHighlighter();

            }else{
                // ##3## find first one annotation in rest annotations and show
                // it on
                int uniqueindex = Depot.SelectedAnnotationSet.getSelectedAnnotationSet().get( 0 );
                Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = uniqueindex;
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                gui.display_showOneAnnotation_inListOnEditorPanel( uniqueindex );
                isComparingInProcessing = true;

                this.display_showAnnotations_inComparatorList();

                gui.display_repaintHighlighter();
            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011171325:: fail to reject primiary " +
                    "annotation on editor panel" + ex.toString());
        }
    }



    /**User clicked button of "reject" on comprator panel in comparation
     * mode to select next annotation in this annotation set as primary
     * annotation.
     *
     * @param   uniqueindex
     *          unique index of annotation which you want to reject from
     *          selected annotation set.
     */
    public void acceptreject_rejectAlternativeAnnotation(int uniqueindex){

        try{
            //##1## delete primary annotation
            Depot.SelectedAnnotationSet.data_onlyDeleteSpcificAnnotation(uniqueindex);

            // ##2## if there only have 2 annotations in current selected
            // annotation set
            if( Depot.SelectedAnnotationSet.size()==1){
                // DiffCounter.accept();
                // ##2.1## remove components on comparator panel
                this.setVisible( false );
                this.setRightPaneVisible2(false);
                
                //this.setStatusInvisible();                
                //this.noDiff();

                recheckUnMatches();
                
                // ##2.2## show primary annotation on editor panel (annotation
                // on list and details on other fileds)
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
                gui.display_repaintHighlighter();
                gui.showAnnotationCategoriesInTreeView_refresh();
            }else{
                // ##3## find first one annotation in rest annotations and show
                // it on
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                this.display_showAnnotations_inComparatorList();
                gui.display_repaintHighlighter();
                gui.showAnnotationCategoriesInTreeView_refresh();
                
            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011171325:: fail to reject primiary " +
                    "annotation on editor panel" + ex.toString());
        }
    }



    /**User clicked button of "ignore" on comprator panel in comparation
     * mode to kick out one annotation from current set. This annotation
     * will be move out from current set but it still stay in the memory
     * of annotations depot.
     *
     * @param   uniqueindex
     *          unique index of annotation which you want to kick out from
     *          selected annotation set.
     */
    public void acceptreject_ignoreAlternativeAnnotation(int uniqueindex){

        try{
            //##1## delete primary annotation
            Depot.SelectedAnnotationSet.data_onlyIgnoreSpcificAnnotation(uniqueindex);

            // ##2## if there only have 2 annotations in current selected
            // annotation set
            if( Depot.SelectedAnnotationSet.size()==1){

                // ##2.1## remove components on comparator panel
                this.setStatusInvisible();
                this.noDiff();

                recheckUnMatches();
                
                // ##2.2## show primary annotation on editor panel (annotation
                // on list and details on other fileds)
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
                gui.display_repaintHighlighter();

            }else{
                // ##3## find first one annotation in rest annotations and show
                // it on
                gui.display_RelationshipPath_Remove();
                gui.remove_all_underline_highlighter();
                this.display_showAnnotations_inComparatorList();
                gui.display_repaintHighlighter();
            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011221425:: fail to ignore selected " +
                    "annotation on compare panel" + ex.toString());
        }
    }




    private void setToolbar_forNormal(){
        if (gui != null) {
            isComparingInProcessing = false;

            jbutton_spaneditor_delete.setVisible(true);
            jSeparator2.setVisible(true);
            //jSeperator.setVisible(true);
            toolbar_comparision_buttons.setVisible(false);            
            gui.display_removeRectangle_toPotensionMarkedDifferences();
            gui.display_EnabledEditorModification_forLeaveCompareMode();

        }
//        }catch(Exception ex){
//        }
    }

    /**while there are more than two annotations can be selected at current
     * mouse position, the button of "diff" should be visible.
     */
    public void setStatusVisible(){
        this.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ));
        this.statu = status.button_visiable;        
        this.setToolbar_forNormal();
        this.updateUI();
    }


    public void startDiff()
    {

        if((this.jpanel_motherPanel==null)||(jsParent==null))
            return;


        if( (gui.reviewmode == GUI.ReviewMode.ANNOTATION_MODE) && (!env.Parameters.EnableDiffButton) ){
            
            setRightPaneVisible( false ); // hide the diff pane
            showAnnotations_All_forExitCompare();
            return;
        }
        
        // Expand right pane on this split pane if allow you to expand the right
        // side of the split pane
        if(( isDiffPanelWorking )||(gui.reviewmode == GUI.ReviewMode.adjudicationMode))
        {
            
            setseed();
            
            components_loadComparatorPane();
            setRightPaneVisible( true ); // display the diff pane
            //expandRightPane();

            isComparingInProcessing = false;
            display_showAnnotations_inEditor();
            display_showAnnotations_inComparatorList();
            this.setToolbar_forCompare();
            
        }
        else
        {            
            setRightPaneVisible( false ); // hide the diff pane
            showAnnotations_All_forExitCompare();
            
        }

    }

    private void setseed(){
        Vector<Integer> indexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
        if( indexes == null )
            return;
        String filename = WorkSet.getCurrentFile().getName();
        String old = null;
        resultEditor.annotations.Depot depotOfAnn = new resultEditor.annotations.Depot();
        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
        for(int uniqueindex : indexes){
            
            Annotation ann = null;
            
            if(GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE ) {
                ann = depotOfAnn.getAnnotationByUnique( filename , uniqueindex);
            } else if(GUI.reviewmode == GUI.ReviewMode.adjudicationMode ) {                
                ann = depotOfAdj.getAnnotationByUnique( filename , uniqueindex );
            }
            
            if( ann == null )
                continue;
            if(( ann.adjudicationAlias != null ) && (ann.adjudicationAlias.trim().length()>0))
            {
                old = ann.adjudicationAlias;
                break;
            }            
        }
        
        if( old != null ){
        for(int uniqueindex : indexes){
            
            Annotation ann = depotOfAnn.getAnnotationByUnique( filename , uniqueindex);
            if( ann == null )
                continue;
            if(( ann.adjudicationAlias == null ) || (ann.adjudicationAlias.trim().length()<1))
            {
                ann.adjudicationAlias = old;
            }            
        }}else{
            String seed = SugarSeeder.getSeed();
            for(int uniqueindex : indexes){
            
            Annotation ann = depotOfAnn.getAnnotationByUnique( filename , uniqueindex);
            if( ann == null )
                continue;
            if(( ann.adjudicationAlias == null ) || (ann.adjudicationAlias.trim().length()<1))
            {
                ann.adjudicationAlias = seed;
            }            
            }
        }
        
        
        
    }
    
    public void noDiff()
    {

        isComparingInProcessing = false; // no diffing are in processing

        if((this.jpanel_motherPanel==null)||(jsParent==null))
            return;

        this.setToolbar_forNormal();

        // Expand right pane on this split pane if allow you to expand the right
        // side of the split pane
        if( isDiffPanelWorking )
        {                        
            components_loadEmptyOne();
            setRightPaneVisible( true ); // display the diff pane

            showAnnotations_All_forExitCompare();

        }
        else
        {
            setRightPaneVisible( false ); // hide the diff pane            
        }

    }
    

    private void setComponent(){

        this.statu = status.button_invisiable;

        // set component size
        int fixed_height = 28;
        int fixed_width = 45;
        this.setPreferredSize( new Dimension(fixed_width, fixed_height));
        //this.setBackground(Color.yellow);
        // set cursor style
        //this.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );


        this.addMouseListener( new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE  ){
                    if(!env.Parameters.EnableDiffButton)
                        return;
                }
                mouseClicked_OnButton();
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased( MouseEvent me ) {

            }

            @Override
            public void mouseEntered( MouseEvent me ) {
                if(ExpandButton.this.statu == status.button_visiable){
                    ExpandButton.this.statu = status.button_mouseon;
                    ExpandButton.this.updateUI();

                    return;

                }

            }

            @Override
            public void mouseExited( MouseEvent me ) {
                if(ExpandButton.this.statu == status.button_mouseon){
                    ExpandButton.this.statu = status.button_visiable;
                    ExpandButton.this.updateUI();

                    return;
                } 

            }
        });

    }

    
    /**five status to this button*/
    public enum status{
        button_invisiable,  // default status to this button
        button_visiable,
        button_mouseon,
        //button_canbeExpand,
        //button_canbeExpand_mouseon,
        //button_canbehide,
        //button_canbehide_mouse
    }

    
    
    
    private void mouseClicked_OnButton()
    {
        ExpandButton.isDiffPanelWorking = !this.isDiffPanelWorking;
        
        if((this.jpanel_motherPanel==null)||(jsParent==null))
            return;


        // Expand right pane on this split pane if allow you to expand the right
        // side of the split pane
        if( isDiffPanelWorking ){
            setRightPaneVisible( true ); // display the diff pane
            expandRightPane();
            display_showAnnotations_inEditor();
            display_showAnnotations_inComparatorList();
            return;
        }
        else
        {               
            setRightPaneVisible( false ); // hide the diff pane
            showAnnotations_All_forExitCompare();            
            return;
        }
    }

    /**show just one annotation on the main editor panel*/
    public void display_showAnnotations_inEditor(){
        if (gui==null)
            return;
        try{
            if( isComparingInProcessing == false ){
                int selectedIndex = 0;
                if(!jList1.isSelectionEmpty())
                    selectedIndex = jList1.getSelectedIndex();
                int uniqueindex = Depot.SelectedAnnotationSet.getSelectedAnnotationSet().get( selectedIndex );
                Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = uniqueindex;
                
                gui.display_showOneAnnotation_inListOnEditorPanel( uniqueindex );

                
                //System.out.println("saved uid=" + Depot.SelectedAnnotationSet.ui_objectAnnotation);
                isComparingInProcessing = true;
            }else{
                int uid = Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor;
                gui.display_showOneAnnotation_inListOnEditorPanel( uid );                
                isComparingInProcessing = false;

            }
        }catch(Exception ex){
            System.out.println("1109182143::");
        }
    }

    private void showAnnotations_All_forExitCompare(){
        if (gui==null)
            return;
        try{
            //System.out.println("forall uid=" + Depot.SelectedAnnotationSet.ui_objectAnnotation);
            gui.display_showSelectedAnnotations_inListOnEditorPanel( Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor );

        }catch(Exception ex){
        }
    }


    private void expandRightPane(){

        this.setToolbar_forCompare();

        components_loadComparatorPane();

        statu = status.button_visiable;
        this.updateUI();

    }

    /**Show all annotations except the primary annotation in the list
     * on comparator panel.
     */
    private void display_showAnnotations_inComparatorList(){
        try{
            if (this.compartor !=null){
                compartor.display_showAlternativeAnnotations();
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 10111423:: fail to show annotations in" +
                    "comparator panel\n" + ex.toString());
        }
    }

    private void components_loadEmptyOne(){
        if (jpanel_diffPanel_container == null)
            return;

        int comp_count = jpanel_diffPanel_container.getComponentCount();
        if( comp_count > 0 ){
            jpanel_diffPanel_container.removeAll();
        }
        try{
            //panel_container.add(new UserInterface.AnnotationCompare.Comparator() );
            userInterface.annotationCompare.EmptyComparator  empty = new
                    userInterface.annotationCompare.EmptyComparator();
            empty.setOpaque(true);
            jpanel_diffPanel_container.setLayout(new BorderLayout());
            jpanel_diffPanel_container.add(empty);
            jpanel_diffPanel_container.setVisible(true);

            //layout.
            jpanel_diffPanel_container.validate();
            jpanel_diffPanel_container.updateUI();
        } catch(Exception ex) {
            log.LoggingToFile.log( Level.SEVERE,"error 1109160952::fail to show" +
                    " empty component loads from form frame!!!\n" + ex.toString());
        }
    }

    private void components_loadComparatorPane(){
        if (jpanel_diffPanel_container == null)
            return;

        int comp_count = jpanel_diffPanel_container.getComponentCount();
        if( comp_count > 0 ){
            jpanel_diffPanel_container.removeAll();
        }

        try{
            //panel_container.add(new UserInterface.AnnotationCompare.Comparator() );
            compartor = new userInterface.annotationCompare.Comparator(gui, this, textPaneforClinicalNotes);
            compartor.setOpaque(true);
            jpanel_diffPanel_container.setLayout(new BorderLayout());
            jpanel_diffPanel_container.add(compartor);
            jpanel_diffPanel_container.setVisible(true);

            //layout.
            jpanel_diffPanel_container.validate();
            jpanel_diffPanel_container.updateUI();
        } catch(Exception ex) {
            log.LoggingToFile.log( Level.SEVERE,"error 1011161419::fail to show" +
                    " component loads from form frame!!!\n" + ex.toString());
        }

    }


    /**Display or Hide the diff pane.
     * 
     * @param   isVisible
     *          Set the diff pane displayed on screen while it's "true"; or hide
     *          the diff pane while it's "false".
     */
    private void setRightPaneVisible(Boolean isVisible )
    {

        this.setToolbar_forNormal();

        if(this.jpanel_motherPanel==null || jpanel_diffPanel_container == null)
            return;

        this.jpanel_diffPanel_container.setEnabled(false);

        this.jpanel_motherPanel.removeAll();

        if( isVisible )
        {
            this.jpanel_motherPanel.setLayout(new java.awt.GridLayout(1, 2, 4, 0));
            this.jpanel_diffPanel_container.setVisible(true);
            jpanel_motherPanel.add(this.jpanel_editorPanel_container);
            jpanel_motherPanel.add(this.jpanel_diffPanel_container);
        }
        // hide right pane by remove all components from the
        else
        {
            this.jpanel_motherPanel.setLayout(new java.awt.GridLayout(1, 1, 0, 0));
            this.jpanel_diffPanel_container.setVisible(false);
            jpanel_motherPanel.add(this.jpanel_editorPanel_container);
        }

        jpanel_motherPanel.updateUI();

    }

    private void setRightPaneVisible2(Boolean isVisible )
    {

        if (gui != null) {
            //isComparingInProcessing = false;

            //jbutton_spaneditor_delete.setVisible(true);
            //jSeparator2.setVisible(true);
            //jSeperator.setVisible(true);
            //toolbar_comparision_buttons.setVisible(false);            
            gui.display_removeRectangle_toPotensionMarkedDifferences();
            //gui.display_EnabledEditorModification_forLeaveCompareMode();

        }

        if(this.jpanel_motherPanel==null || jpanel_diffPanel_container == null)
            return;

        this.jpanel_diffPanel_container.setEnabled(false);

        this.jpanel_motherPanel.removeAll();

        if( isVisible )
        {
            this.jpanel_motherPanel.setLayout(new java.awt.GridLayout(1, 2, 4, 0));
            this.jpanel_diffPanel_container.setVisible(true);
            jpanel_motherPanel.add(this.jpanel_editorPanel_container);
            jpanel_motherPanel.add(this.jpanel_diffPanel_container);
        }
        // hide right pane by remove all components from the
        else
        {
            this.jpanel_motherPanel.setLayout(new java.awt.GridLayout(1, 1, 0, 0));
            this.jpanel_diffPanel_container.setVisible(false);
            jpanel_motherPanel.add(this.jpanel_editorPanel_container);
        }

        jpanel_motherPanel.updateUI();

    }


    
    public status statu;

 


    /**custom paint() method to this curstom component.*/
    @Override
    public void paint(Graphics g){
        
        super.paint(g);
        
        if( GUI.ReviewMode.ANNOTATION_MODE == GUI.reviewmode ){
            if( !env.Parameters.EnableDiffButton )
                return;
        }

        // ##1## draw nothing if button is invisiable
        if(statu == status.button_invisiable )
            return;


        int rules_height = 18;
        int rules_width  = 40;

        int this_width = this.getWidth(), this_height = this.getHeight();
        int start_x = ( this_width>rules_width ? ((int)(this_width-rules_width)) : 0);
        int start_y = ( this_height>rules_height? ((int)(this_height-rules_height)/2): 0 );
        int real_wholeWidth = ( this_width>rules_width ? rules_width-2: this_width-2);
        int real_wholeHeight = (this_height>rules_height? rules_height : this_height);
        int real_width = real_wholeWidth - (int)(real_wholeHeight/2);        

        // draw the outer rounder rectangle        
        // draw the outer rounder rectangle
        if ((statu == status.button_mouseon))
            g.setColor(new Color(20,189,159));
        else
            g.setColor(new Color(2,89,159));


        // 填充渐进色
        int ir = g.getColor().getRed();
        int ig = g.getColor().getGreen();
        int ib = g.getColor().getBlue();
        int rr,gg,bb;

        for(int i=0; i<real_wholeHeight; i++){
            rr=ir+i*2;
            gg=ig+i*6;
            bb=ib+i*6;
            rr=(rr<255? rr:255);
            bb=(bb<255? bb:255);
            gg=(gg<255? gg:255);
            g.setColor(new Color(rr, gg, bb));
            int y = start_y + real_wholeHeight - 1 -i;
            if(i<=(int)(real_wholeHeight/2))
                g.drawLine(start_x + 1, y, start_x  + real_width + i, y );
            else
                g.drawLine(start_x + 1, y, start_x  + real_wholeWidth - (i-(int)(real_wholeHeight/2)), y );
        }

        // draw the outer rounder rectangle
        if (statu == status.button_visiable)
            g.setColor(new Color(20,189,159));
        else
            g.setColor(new Color(2,89,159));
        g.drawLine(start_x, start_y, start_x + real_width, start_y);

        g.drawLine(start_x + real_width, start_y,
                start_x + real_wholeWidth, start_y + (int)(real_wholeHeight/2) );


        g.drawLine(start_x + real_width, start_y+real_wholeHeight,
                start_x + real_wholeWidth, start_y + (int)(real_wholeHeight/2) );

        g.drawLine(start_x + real_width, start_y+real_wholeHeight,
                start_x, start_y+real_wholeHeight);

        g.drawLine(start_x, start_y,
                start_x, start_y+real_wholeHeight);

        // ##4## print text on button
        g.setColor( Color.black );
        g.setFont( new java.awt.Font("Verdana", 0, 9) );

        g.drawString("Diff", start_x + 8, start_y + real_wholeHeight - 6);



        /*
        // draw outline
        g.setColor(out);
        g.drawRoundRect(start_x, start_y, real_width, real_height, 6, 6);

        // fill inner
        g.setColor(inner);
        g.fillRect(start_x+3, start_y+3, real_width-5, real_height-5);

        // draw inner line
        g.setColor(line);
        g.drawRoundRect(start_x+1, start_y+1, real_width-2, real_height-2, 5, 5);
        g.drawRoundRect(start_x+2, start_y+2, real_width-4, real_height-4, 4, 4);

        g.setColor( new Color(82,35,35) );
        g.setColor( Color.white );
        g.setFont( new java.awt.Font("Ayuthaya", 1, 14) );
        g.drawString("Diff >>", start_x + 8, start_y + real_height - 5);
        */



        /* 描绘一个大圆角按钮
        // ##1## draw outside round rectangle
        Color color_outline;
        if((statu == status.button_canbeExpand)||(statu == status.button_canbeExpand_mouseon)){        
            color_outline = new Color(80,14,78);
        }else
            color_outline = new Color( 51,  51,  51 );

        g.setColor( color_outline );
        g.drawRoundRect(start_x, start_y, real_width, real_height, 8, 8);

        // ##2## fill the round rectangle area
        Color color_fill = Color.white;
        if(statu == status.button_canbeExpand){
            color_fill = new Color(80,14,78);
        }else if (statu == status.button_canbeExpand_mouseon){
            color_fill = new Color(160,24,156);
        }else if(statu == status.button_canbehide){
            color_fill = new Color(128, 128, 128 );
        }else if (statu == status.button_canbehide_mouse){
            color_fill = new Color(200,200,200);
        }
        g.setColor(color_fill);
        g.fillRoundRect(start_x+1, start_y+1, real_width-2, real_height-2, 8,8);


        // ##3## draw a round rectangle with same coefficients with step3, but
        // color are white.
        g.setColor(Color.white);
        g.drawRoundRect(start_x+1, start_y+1, real_width-2, real_height-2, 8, 8);

        

        // ##4## print text on button
        g.setColor( Color.white );
        g.setFont( new java.awt.Font("Calibri", 1, 14) );
        if((statu == status.button_canbehide)||(statu == status.button_canbehide_mouse)){
            g.drawString("Hide <<", start_x + 8, start_y + real_height - 6);
        }
        else
        {
            g.drawString("Diff >>", start_x + 8, start_y + real_height - 6);
        }
         *
         */
    }
}
