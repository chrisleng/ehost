/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.selectedAnnotationView;

import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.JList;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Depot;
import userInterface.GUI;

/**
 *
 * @author Chris
 */
public class AnnotationListScreen {
    protected JList list;
    protected userInterface.GUI gui;
    protected Icon icon;

    /**class contractor*/
    public AnnotationListScreen( userInterface.GUI gui, JList list, Icon icon ){
        this.list = list;
        this.gui = gui;
        this.icon = icon;
    }

    /**This method is called to build display of list of selected annotations.
     * It read selected annotations from static variable "sets" from static inner
     * class "SelectedAnnotationSet" in class "ResultEditor.Annotations.Depot".
     *
     * @param   initializedIndex
     *          Show which annotation is selected 
     */
    public void showAnnotations( Object parameter )
    {
        int initializeIndex = -1;
        Annotation previous_selected_annotation = null;
        
        if( parameter == null ){
            initializeIndex = 0;
        }else{
            if ( parameter instanceof Annotation  ){
                previous_selected_annotation = (Annotation) parameter;
            }else if ( parameter instanceof Integer  ){
                initializeIndex = (Integer) parameter;
            }
        }
            
        
        
        gui.disableAnnotationDisplay();

        if( list == null ){
            return;
        }

        // ##1## empty the list
        list.setListData( new Vector());

        try{
            Annotation selectedAnnotation = null;
            
            
            // ##2## Get indexes of selected annotations to this mouse position in
            // current text source
            Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if( selectedAnnotationIndexes == null )
                return;

            Depot depot = new Depot();

            // assemble vector v for show return results in list
            Vector<Object> listentry = new Vector<Object>();
            int size = selectedAnnotationIndexes.size();
            
            
            
            if (size > 0) {
                String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

                
                for( int i=0; i<size; i++ )
                {

                    int uniqueindex = selectedAnnotationIndexes.get(i);

                    resultEditor.annotations.Annotation annotation = null;
                    if( GUI.ReviewMode.ANNOTATION_MODE == GUI.reviewmode )
                        annotation = depot.getAnnotationByUnique( textsourcefilename, uniqueindex );
                    else if( GUI.ReviewMode.adjudicationMode == GUI.reviewmode ){
                        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                        annotation = depotOfAdj.getAnnotationByUnique( textsourcefilename, uniqueindex );
                    }
                    if(annotation == null || !annotation.visible)
                       continue;

                                        

                    // get term information from query results
                    if (previous_selected_annotation == null) {
                        if ((initializeIndex >= 0) && (i == initializeIndex)) {
                            selectedAnnotation = annotation;
                            // record current annotation to indicator current annotation
                            resultEditor.workSpace.WorkSet.currentAnnotation = annotation;
                        }
                    } else {
                        if( annotation.uniqueIndex == previous_selected_annotation.uniqueIndex ){
                            selectedAnnotation = previous_selected_annotation;
                            resultEditor.workSpace.WorkSet.currentAnnotation = previous_selected_annotation;
                            initializeIndex = i;
                        }
                    }



                    Color categorycolor = resultEditor.annotationClasses.Depot.getColor( annotation.annotationclass );
                    iListEntry le;
                    le = new iListEntry( annotation , categorycolor, icon );
                    listentry.add( le );
                }                                   
            }


            // show data of selected annotations on list and set selected item
            list.setListData( listentry );
            list.setCellRenderer(new iListCellRenderer());       
                                    
            // System.out.print("\n\n annotation = " + selectedAnnotation.toString() );
            if( selectedAnnotation != null ){
                gui.display_showAnnotationDetail_onEditorPanel(selectedAnnotation, true);                
                gui.display_markPhrase( selectedAnnotation );
            }
            
            //if( previous_selected_annotation == null ){
                if( initializeIndex < 0)
                    list.setSelectedIndex( 0 );
                else
                    list.setSelectedIndex( initializeIndex );
            //}else{
                
            //}

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1011152018:: fail to show selected " +
                    "annotations on the list!!!");
            ex.printStackTrace();
        }
         
        
        
    }

    /** to method of "showAnnotations" in same class;
     * The difference is the parameter here is the unique index of annotation;
     *
     * @param   uniqueindex
     *          if smaller than 0 : first annotation in set will be selected
     *          in the jlist;
     *          If bigger or equal to 0: the annotation whose uniqueindex has
     *          same value to this parameter will be displayed as selected
     *          in the jList.
     */
    public void showAnnotations_withSpecificUniqueindex( int uniqueindex ){
        if( list == null )
            return;

        // ##1## empty the list
        clear( list );

        String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();
        
        try{
            // ##2## Get indexes of selected annotations to this mouse position in
            // current text source
            Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if( selectedAnnotationIndexes == null )
                return;

            Depot depot = new Depot();

            // assemble vector v for show return results in list
            Vector<iListEntry> listentry = new Vector<iListEntry>();
            int size = selectedAnnotationIndexes.size();

            
            for( int i=0; i<size; i++ ) {

                int this_uniqueindex = selectedAnnotationIndexes.get(i);

                resultEditor.annotations.Annotation annotation = null;
                if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                     annotation = depot.getAnnotationByUnique( textsourcefilename, this_uniqueindex );
                else{
                    adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                    annotation = depotOfAdj.getAnnotationByUnique( textsourcefilename, this_uniqueindex );
                }
                if(annotation == null || !annotation.visible)
                    continue;

                // record current annotation to indicator current annotation
                if( annotation.uniqueIndex == uniqueindex ) {               
                    resultEditor.workSpace.WorkSet.currentAnnotation = annotation;
                
                    // get term information from query results                             
                    gui.display_markPhrase( annotation);
                }

                Color categorycolor = resultEditor.annotationClasses.Depot.getColor( annotation.annotationclass );
                iListEntry le;
                le = new iListEntry( annotation , categorycolor, icon );
                listentry.add( le );
            }


            // show data of selected annotations on list and set selected item            
            list.setListData( listentry );
            list.setCellRenderer(new iListCellRenderer());

            if(list.getSelectedIndex()<0)
                list.setSelectedIndex( 0 );

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1011152018:: fail to show selected " +
                    "annotations on the list!!!");
        }

        // display selected annotation as selected
        try{
            Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if( selectedAnnotationIndexes == null )
                return;
            Depot depot = new Depot();


            int size = selectedAnnotationIndexes.size();
            if (uniqueindex<0) {
                list.setSelectedIndex( 0 );
            } else
            // if a specific annotation need to be displayed as selected
            {
                for( int i=0; i<size; i++ ) {
                    final int this_uniqueindex = selectedAnnotationIndexes.get(i);

                    if( uniqueindex == this_uniqueindex  ) {
                         list.setSelectedIndex( i );
                         Annotation annotation = depot.getAnnotationByUnique(textsourcefilename, uniqueindex);
                    //    gui.showAnnotationDetail(annotation);
                         break;
                    }
                }
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1011161322:: fail to set an annotation" +
                    "displayed as selected!");
        }
    }


    /**Only show the given annotation from selected annotations stored in
     * static memory of "ResultEditors.Annotations.Depot.SelectedAnnotations.sets",
     * while we try to show one annotation in the editor panel as the main annotation
     * to compare differences between annotations. We can accept or reject
     * annotations in the mode of comparing annotation difference.
     *
     * @param   uniqueindex
     *          unique index of the annotation which you want to show as
     *          primary annotation to check difference between it and others.
     */
    public void onlyshowAnnotation( int uniqueindex ){
        if( list == null )
            return;

        // ##1## empty the list
        clear( list );

        try{
            
            // ##2## Get indexes of selected annotations to this mouse position in
            // current text source
            Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if( selectedAnnotationIndexes == null )
                return;

            Depot depot = new Depot();

            // assemble vector v for show return results in list
            Vector<Object> listentry = new Vector<Object>();
            String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();
            Annotation annotation = null;
            if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                annotation = depotOfAdj.getAnnotationByUnique( textsourcefilename, uniqueindex );
            } else{
                 annotation = depot.getAnnotationByUnique( textsourcefilename, uniqueindex );
            }

            resultEditor.workSpace.WorkSet.currentAnnotation = annotation;
            //System.out.println("current uid = "+annotation.uniqueIndex);

            if(annotation == null || !annotation.visible)
                return;

                // record current annotation to indicator current annotation
                //resultEditor.workSpace.WorkSet.currentAnnotation = annotation;
                
            gui.display_showAnnotationDetail_onEditorPanel(annotation);


            //gui.hilightPhrase( annotation);


            Color categorycolor = resultEditor.annotationClasses.Depot.getColor( annotation.annotationclass );
            iListEntry le;
            le = new iListEntry( annotation , categorycolor, icon );
            listentry.add( le );



            // show data of selected annotations on list and set selected item            
            list.setListData( listentry );
            list.setCellRenderer(new iListCellRenderer());
            if(list.getSelectedIndex()<0)
                list.setSelectedIndex(0);
             
            

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1011152018:: fail to show selected " +
                    "annotations on the list!!!");
        }

    }


    private void clear( JList thislist){
        Vector object = new Vector();
        list.setListData(object);
    }
}
