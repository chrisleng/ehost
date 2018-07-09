/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.mouse;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import resultEditor.annotationClasses.SelectionStatusOfClasses;
import resultEditor.annotationClasses.file_annotation;
import resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;
import resultEditor.annotationClasses.navigationTree.treeRelated.Treeview_AnnotationNode;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Depot;
import resultEditor.annotations.Depot.SelectedAnnotationSet;
import resultEditor.annotations.SpanSetDef;

/**
 *
 * @author Chris
 */
public class ClickedListener {

    protected java.awt.event.MouseEvent evt;
    protected static JTree classTreeview;
    protected static userInterface.GUI gui;
    private JPopupMenu popup;
    protected static JTextComponent comp;
    protected String selectedText = null;
    protected int selectedStart, selectedEnd;
    protected String classname;
    protected DeleteConfirm deletedialog;

    public ClickedListener(java.awt.event.MouseEvent evt, JTree classTreeview,
            userInterface.GUI gui, JTextComponent comp) {
        this.evt = evt;
        ClickedListener.classTreeview = classTreeview;
        ClickedListener.gui = gui;
        ClickedListener.comp = comp;
        this.selectedText = comp.getSelectedText();
        if (this.selectedText != null) {
            this.selectedStart = comp.getSelectionStart();
            this.selectedEnd = comp.getSelectionEnd();
        }
    }

    private void setFlag_of_CreateAnnotation(java.awt.event.MouseEvent evt) {
        // if (evt.getClickCount() != 2)
        // return;
        TreePath path = classTreeview.getPathForLocation(evt.getX(), evt.getY());
        if (path != null) {
            Object node = path.getLastPathComponent();
            // Object myobject =
            // ((javax.swing.tree.DefaultMutableTreeNode)(node)).getUserObject();
            if (node == null) {
                log.LoggingToFile.log(Level.SEVERE,
                        "1008102259 - this selected node is not a markable node!!!");
            } else {
                
                try {
                    resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass classnode = null;
                    
                    // node of the class that we just selected
                    classnode = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) node;
                 
                    // change the boolean value of the flag of its 1-clicking annotation
                    boolean isAllow_toCreateAnnotation_asThisClass = classnode.isSelected_toCreateAnnotation();
                    classnode.setFlag_isSelected_toCreateAnnotation(!isAllow_toCreateAnnotation_asThisClass);
                    
                    // set text
                    String classname = classnode.getText();
                    // set the selection status
                    classnode.setSelected( SelectionStatusOfClasses.getSelectedStatus(classname) );

                    // change the depot to mark the flag of 1-clicking annotation of this class in memory
                    setCurrentMarkable_toCreateNewAnnotation(classname, !isAllow_toCreateAnnotation_asThisClass);

                   
                    ((javax.swing.tree.DefaultMutableTreeNode) (node)).setUserObject(classnode);
                    classTreeview.updateUI();

                } catch (Exception e) {
                    log.LoggingToFile.log(Level.SEVERE,
                            "1008102300 - error while convert \"object\" to \"classnode\"");
                }
            }
        }
    }

    /**
     * Process if user clicked a node of this tree view.
     */
    public void process() {
        
        // make sure we got the moust event and the component
        if ( (evt == null) || (classTreeview == null)) {
            return;
        }

        
        // clear any selected node highlight backgroup in treeview
        UpDateNavigationPanel scit = new UpDateNavigationPanel(classTreeview, gui);
        scit.removeAllNodehighlight();

        // ##1## mouse right key pressed
        if (evt.getModifiers() == java.awt.event.InputEvent.BUTTON3_MASK) {

            TreePath path = classTreeview.getPathForLocation(evt.getX(), evt.getY());

            // ##1.1## process if user's click got some tree node
            if (path != null) {

                classTreeview.setSelectionPath(path);

                Object node = path.getLastPathComponent();

                if (node == null) {
                    return;
                }

                // ##1.1.1## clicked class node in the tree view is class tree
                // node
                if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) {

                    setFlag_of_CreateAnnotation(evt);

                    // ##1.1.1.1## show all vaild position indicators
                    showAllValidPositionIndicator();
                    return;
                    
                } else if (!((javax.swing.tree.DefaultMutableTreeNode) node).isRoot()) {
                    // ##1.1.2## if node is annotation
                    String str = ((javax.swing.tree.TreeNode) node).toString();
                    str = getCleanAnnotationName(str);
                    // System.out.println("\n - annotation = ["+str+"]");
                    // only show position indicator to you just clicked
                    // annotation in current article

                    onlyShowValidPositionIndicator_for_DesignatedAnnotation(str);
                    
                    buildPopupMenu(node, true);

                    return;

                }
                
            } else {
                // ##1.2## if no node got selected by user's mouse click
                showAllValidPositionIndicator();
              
                return;
            }
        } else {
            // ##2## left key or midkey pressed, not right key of mouse
            TreePath path = classTreeview.getPathForLocation(evt.getX(),
                    evt.getY());

            // ##2.1## process if user's click got some tree node
            if (path != null) {

                classTreeview.setSelectionPath(path);
                Object node = path.getLastPathComponent();
                if (node == null) {
                    return;
                }

                // ##2.1.1## clicked class node in the tree view is class tree
                // node
                if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) {
                    // ##2.1.1.1## show all vaild position indicators
                    showAllValidPositionIndicator();
                    

                }else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClassRoot) {

                    
                    UpDateNavigationPanel scitv = new UpDateNavigationPanel();
                    scitv.updateAttributeBranch( null );
                    scitv.refreshTypeMemory_refresh();
                    //scitv.updateAttributeBranch( null);
                    scitv.checkStatusChanged();
                    scitv.display( null );
                
                    // ##1.1.1.1## show all vaild position indicators
                    //showAllValidPositionIndicator();
                    
                    UpDateNavigationPanel ascitv = new UpDateNavigationPanel();
                    //ascitv.updateAttributeBranch( null );
                    //ascitv.refreshTypeMemory_refresh();
                    
                    ascitv.checkStatusChanged();
                    ascitv.display( null );
                
                    // ##1.1.1.1## show all vaild position indicators
                    showAllValidPositionIndicator();
                    
                    return;
                    
                }  
                else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelAnnotation) {
                    // ##2.1.1.1## show all vaild position indicators
                    showAllValidPositionIndicator();
                    
                    resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelAnnotation nra = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelAnnotation) node;
                    if(nra.isCurrentFile)
                    //gui.showSelectedAnnotations_inList( nra.getAnnotation() );
                        gui.display_annotation_inListOnEditorPanel( nra.getAnnotation().uniqueIndex );
                    else{
                        buildPopupMenu2(node, false);
                    }
                    

                } else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttAnnotation) {
                    // ##2.1.1.1## show all vaild position indicators
                    showAllValidPositionIndicator();
                    
                    resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttAnnotation nra = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttAnnotation) node;
                    //gui.showSelectedAnnotations_inList( nra.getAnnotation() );
                    gui.display_annotation_inListOnEditorPanel( nra.getAnnotation().uniqueIndex );
                    

                } else if( node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelationship ){
                } else if (!((javax.swing.tree.DefaultMutableTreeNode) node).isRoot()) {
                    // ##2.1.2## if node is annotation
                    String str = ((javax.swing.tree.TreeNode) node).toString();
                    str = getCleanAnnotationName(str);

                    // only show position indicator to you just clicked
                    // annotation in current article
                    onlyShowValidPositionIndicator_for_DesignatedAnnotation(str);

                    buildPopupMenu(node, false);
                }
                
                return;

            } else {
                // ##2.2## if no node got selected by user's mouse click

                showAllValidPositionIndicator();
                

                return;
            }
        }
    }

    /**
     * check whether annotations are appearing in other documents. And if they
     * appeared in other document, we will pop up a popup menu which list these
     * docuemtns and allow you jump to that docuement by clicking it.
     */
    private void buildPopupMenu(Object node, boolean is_right_button) {

        if ((!is_right_button) && (!gui.isShowAnnotations_toAllDoc())) {
            return;
        }

        if (node instanceof Treeview_AnnotationNode) {
            Treeview_AnnotationNode annotationnode = (Treeview_AnnotationNode) node;
            if (annotationnode != null) {
                Vector<file_annotation> annotations = annotationnode.getAnnotations();
                if (annotations != null) {

                    if (annotationnode.isNotAppeared_in_CurrentArticle) {
                        popmenu_for_multipleDocument(
                                annotationnode.getUniqueAnnotationText(),
                                annotations, true);
                    } else {
                        popmenu_for_multipleDocument(
                                annotationnode.getUniqueAnnotationText(),
                                annotations, false);
                    }
                }
            }
        }
    }
    
    private void buildPopupMenu2(Object node, boolean is_right_button) {

        if ((!is_right_button) && (!gui.isShowAnnotations_toAllDoc())) {
            return;
        }

        if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelAnnotation) {
            resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelAnnotation adjannotationnode 
                    = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelAnnotation) node;
            if (adjannotationnode != null) {
                Annotation annotation = adjannotationnode.getAnnotation();
                boolean isCurrentFile = adjannotationnode.isCurrentFile;
                
                if ((annotation != null) && (!isCurrentFile) ) {

                    
                        popmenuToOtherDocument(
                                annotation, adjannotationnode.filename );
                    
                }
            }
        }
    }

    private String getCleanAnnotationName(String text) {
        if ((text == null) || (text.trim().length() < 1)) {
            return "";
        }

        text = text.replaceAll("<html>", " ");
        text = text.replaceAll("<//html>", " ");
        text = text.replaceAll("<//b>", " ");
        text = text.replaceAll("<b>", " ");
        text = text.replaceAll("<font(.*)?>", " ");

        return text.trim();
    }

    private void showAllValidPositionIndicator() {
        gui.showValidPositionIndicators_setAll();
        gui.showValidPositionIndicators();
    }

    private int assignMeAUniqueIndex() {
        return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }

    private void ShowDialogToCreateAnnotation(String classname) {
        // ShowAnnotationDlg ShowAnnotationDlg = new ShowAnnotationDlg( gui );
        // ShowAnnotationDlg.showDialog( gui.getX(), gui.getY(), gui.getWidth(),
        // gui.getHeight(),
        // this.selectedText, this.selectedStart, this.selectedEnd, classname);

        String testsourceFilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.articleInsurance(testsourceFilename);
        String createdate = commons.OS.getCurrentDate();
        String annotator_name = resultEditor.annotator.Manager.getAnnotatorName_OutputOnly();
        String annotator_id = resultEditor.annotator.Manager.getAnnotatorID_outputOnly();
        depot.addANewAnnotation(testsourceFilename, this.selectedText,
                this.selectedStart, this.selectedEnd,
                // creation date
                createdate,
                // annotation classname
                this.classname, annotator_name, annotator_id, null, null,
                assignMeAUniqueIndex());

        resultEditor.annotations.Annotation annotation = new resultEditor.annotations.Annotation();
        annotation.annotationText = this.selectedText;

        SpanSetDef spanset = new SpanSetDef();
        spanset.addSpan(selectedStart, selectedEnd);
        annotation.spanset = spanset;

        annotation.creationDate = createdate;
        annotation.annotationclass = classname;
        annotation.setAnnotator("EHOST_Result_Editor");

        comp.setSelectionEnd(0);
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.display_repaintHighlighter();

        gui.showValidPositionIndicators();

        setSelectedDatainDepot(testsourceFilename, annotation);
        // gui.showAnnotationDetail(annotation);
        gui.showSelectedAnnotations_inList(0);

    }

    private void onlyShowValidPositionIndicator_for_DesignatedAnnotation(
            String _annotationText) {
        // ##1## only show indicator marks for annotations who have same
        // annotation text
        gui.showValidPositionIndicators_setDesignated(_annotationText);
        gui.showValidPositionIndicators();

        // get filename of current text source
        String testsourceFilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

        // ##2## selected annotation who have same annotation text in the list
        // resultEditor.annotations.Depot depot = new
        // resultEditor.annotations.Depot();
        resultEditor.annotations.Depot.SelectedAnnotationSet.selectedAnnotations_byTheirText(testsourceFilename,
                _annotationText);

        // gui.showAnnotationDetail(annotation);
        gui.display_SetStatusInsiable_ToComparisionPanel();
        gui.showSelectedAnnotations_inList(0);

        gui.highlightAnnotations(_annotationText);
        
        try{
            int uniqueindex = SelectedAnnotationSet.getSelectedAnnotationSet().get(0);
            Depot depot = new Depot();
            Annotation ann = depot.getAnnotation(testsourceFilename, uniqueindex);
            gui.display_setCarpetOn( ann.spanstart );
        }catch(Exception ex){
        }
        

    }

    private void popMemu(String classname, java.awt.event.MouseEvent evt) {
        // popupDialog the popmenu
        popup = new JPopupMenu();
        popup = buildpopmenu(popup, "Create new annotation");
        // popup.setLabel("Justification");
        javax.swing.border.Border lines = new javax.swing.border.LineBorder(
                Color.black, 2);
        popup.setBorder(lines);

        popup.show(classTreeview, evt.getX(), evt.getY());

    }

    /**
     * To an annotation node in the treeview, if it not appear in current
     * document, show popmenu with "delete" and "navigator panel". Otherwise,
     * only show "delete" menu.
     */
    private void popmenu_for_multipleDocument(String annotationtext,
            Vector<file_annotation> file_annotations,
            boolean need_navigation_to_other_file) {
        popup = new JPopupMenu();
        popup = buildpopmenu(annotationtext, popup, file_annotations,
                need_navigation_to_other_file);
        javax.swing.border.Border lines = new javax.swing.border.LineBorder(
                Color.black, 2);
        popup.setBorder(lines);

        popup.show(classTreeview, evt.getX(), evt.getY());
    }

    
    private void popmenuToOtherDocument(final Annotation annotation, final String filename) {
        popup = new JPopupMenu();
        javax.swing.border.Border lines = new javax.swing.border.LineBorder(
                Color.black, 2);
        
        JMenuItem item;
        final ActionListener menuListener = new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        jumpToDesignated_fileAndAnnotations(
                                filename, annotation );
                    }
                };
        
        item = new JMenuItem("in \"" + filename+ " ", new ImageIcon("1.gif"));
                item.setHorizontalTextPosition(JMenuItem.RIGHT);
                item.addActionListener(menuListener);
                popup.add(item);
        
        popup.show(classTreeview, evt.getX(), evt.getY());
    }
    private JPopupMenu buildpopmenu(final String annotationtext,
            final JPopupMenu popup,
            final Vector<file_annotation> file_annotations,
            final boolean need_navigation_to_other_file) {
        if ((file_annotations == null) && (file_annotations.size() < 1)) {
            return popup;
        }

        final File_Annotations_Depot depot = new File_Annotations_Depot();
        depot.clear();

        for (file_annotation fa : file_annotations) {
            depot.add(fa);
        }

        if ((need_navigation_to_other_file) && (depot.file_annotations != null)) {
            for (final File_ANNOTATIONS faa : depot.file_annotations) {

                final ActionListener menuListener = new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        jumpToDesignated_fileAndAnnotations(
                                faa.Filename, faa.annotations);
                    }
                };

                JMenuItem item;
                item = new JMenuItem("in \"" + faa.Filename + "\"("
                        + faa.annotations.size() + ")", new ImageIcon("1.gif"));
                item.setHorizontalTextPosition(JMenuItem.RIGHT);
                item.addActionListener(menuListener);
                popup.add(item);
            }

            JSeparator seperator = new JSeparator();
            popup.add(seperator);
        }

        JMenuItem deleteitem = new JMenuItem("delete", new ImageIcon("1.gif"));
        final ActionListener deletemenuListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                ClickedListener.this.showDialog_delelteUniqueTypeAnnotation(
                        annotationtext, need_navigation_to_other_file);
            }
        };
        deleteitem.addActionListener(deletemenuListener);
        popup.add(deleteitem);

        popup.setLabel("J");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        return popup;
    }

    private void showDialog_delelteUniqueTypeAnnotation(String annotationtext,
            boolean is_appeared_in_current_doc) {
        if (annotationtext == null) {
            return;
        }

        if (this.deletedialog != null) {
            deletedialog.dispose();
        }

        deletedialog = new DeleteConfirm(gui, annotationtext,
                is_appeared_in_current_doc);
        deletedialog.setVisible(true);

    }

    private void jumpToDesignated_fileAndAnnotations(final String filename,
            final Annotation annotation) {

        // System.out.println("annotationtext=["+ annotationtext+"");
        // ##1## open disignated text source by filename
        gui.goUserDesignated(filename);
        gui.display_repaintHighlighter();
        
        // reset screen
        gui.display_RelationshipPath_Remove();
        gui.remove_all_underline_highlighter();

        String annotationtext = annotation.annotationText;

        //resultEditor.annotations.Depot.SelectedAnnotationSet.selectedAnnotations_byTheirText(filename, annotationtext);

        gui.display_SetStatusInsiable_ToComparisionPanel();
        //gui.showSelectedAnnotations_inList(annotation);
        gui.display_annotation_inListOnEditorPanel(annotation.uniqueIndex);

        // set focus and highlight first selected annotations
        //gui.setFocusOn(annotations.get(0).spanstart);
        
        gui.display_markPhrase( annotation );        
        gui.setFocusOn( annotation.spanset.getSpanAt(0).start );
        //gui.showValidPositionIndicators_setDesignated(annotationtext);
        
        
        
        //gui.display_showSelectedAnnotations_inListOnEditorPanel(-1);
        
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.showValidPositionIndicators();
        
        // gui.displa
    }
    
    private void jumpToDesignated_fileAndAnnotations(String filename,
            final Vector<Annotation> annotations) {

        // System.out.println("annotationtext=["+ annotationtext+"");
        // ##1## open disignated text source by filename
        gui.goUserDesignated(filename);

        String annotationtext = annotations.get(0).annotationText;

        resultEditor.annotations.Depot.SelectedAnnotationSet.selectedAnnotations_byTheirText(filename, annotationtext);

        gui.display_SetStatusInsiable_ToComparisionPanel();
        gui.showSelectedAnnotations_inList(0);

        // set focus and highlight first selected annotations
        gui.setFocusOn(annotations.get(0).spanstart);
        gui.display_markPhrase(annotations.get(0));

        gui.showValidPositionIndicators_setDesignated(annotationtext);
        gui.showValidPositionIndicators();
        // gui.displa
    }

    private JPopupMenu buildpopmenu(final JPopupMenu popup,
            final String annotationClassname) {
        final ActionListener menuListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                ShowDialogToCreateAnnotation(annotationClassname);
            }
        };

        JMenuItem item;
        popup.add(item = new JMenuItem(annotationClassname, new ImageIcon(
                "1.gif")));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);

        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        return popup;
    }

    private void setSelectedDatainDepot(String filename,
            resultEditor.annotations.Annotation annotation) {
        int index = getAnnotationIndex(filename, annotation);
        if (index < 0) {
            return;
        }

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.setSelectedAnnotationsIndexes(index);
    }

    private int getAnnotationIndex(String filename,
            resultEditor.annotations.Annotation annotation) {
        if (annotation == null) {
            return -1;
        }
        if (filename == null) {
            return -1;
        }

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        resultEditor.annotations.Article article = depot.getArticleByFilename(filename.trim());
        if (article == null) {
            return -1;
        }

        if (article.annotations == null) {
            return -1;
        }

        int size = article.annotations.size();
        for (int i = 0; i < size; i++) {
            resultEditor.annotations.Annotation thisannotation = article.annotations.get(i);
            if (thisannotation == null) {
                continue;
            }

            if (thisannotation.annotationText.trim().compareTo(
                    annotation.annotationText.trim()) == 0) {
                if (thisannotation.spanstart == annotation.spanstart) {
                    if (thisannotation.spanend == annotation.spanend) {
                        if (thisannotation.creationDate.trim().compareTo(
                                annotation.creationDate.trim()) == 0) {
                            if (thisannotation.getAnnotator().trim().compareTo(
                                    annotation.getAnnotator().trim()) == 0) {
                                if (annotation.annotationclass.trim().compareTo(
                                        annotation.annotationclass.trim()) == 0) {
                                    return i;
                                }
                            }
                        }
                    }
                }
            }

        }

        return -1;
    }

    /**
     * While you just enabled a markable as current markable type for creation
     * of new annotation, only this markable could be indicated by crossed lines
     * in its colorful markable category bar. If no markable was set, all
     * markables should be
     */
    private void setCurrentMarkable_toCreateNewAnnotation(String markablename,
            boolean isSelected) {
        if (classTreeview == null) {
            log.LoggingToFile.log(Level.SEVERE,
                    "1008110000 - could not found class tree view while try to set status to markables.");
            return;
        }
        if (markablename == null) {
            setCurrentMarkable_inParametersSpace(null);
        }

        if (markablename.trim().length() < 1) {
            log.LoggingToFile.log(Level.SEVERE,
                    "1008110019 - could not found valid markable name while try to set status to markables.");
            return;
        }

        if (isSelected) {
            setCurrentMarkable_inParametersSpace(markablename.trim());
        } else {
            setCurrentMarkable_inParametersSpace(null);
        }

        // get access to the root of this treeview
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) classTreeview.getModel().getRoot();
        if (root == null) {
            return;
        }
        // get access to the subroot of "classes"
        DefaultMutableTreeNode subroot_of_classes = (DefaultMutableTreeNode) root.getChildAt(0);
        if (subroot_of_classes == null) {
            return;
        }
        
        int amount_of_markables = subroot_of_classes.getChildCount();
        
        for (int i = 0; i < amount_of_markables; i++) {
            Object object = subroot_of_classes.getChildAt(i);
            if (object instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) {
                resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass classnode = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) object;
                System.out.println("["+classnode.getText()+"]=" + classnode.isSelected());
                if (classnode.getText().trim().compareTo(markablename.trim()) == 0) {
                    continue;
                }                
                
                classnode.setSelected( SelectionStatusOfClasses.getSelectedStatus( classnode.getText() ) );
                classnode.setFlag_isSelected_toCreateAnnotation(false);                
                System.out.println("["+classnode.getText()+"]=" + classnode.isSelected());
                ((javax.swing.tree.DefaultMutableTreeNode) object).setUserObject(classnode);
            }
        }
    }

    /**Enable the function of 1-clicking to create annotation by marking a class name.*/
    private void setCurrentMarkable_inParametersSpace(String markableName) {
        env.Parameters.currentMarkables_to_createAnnotation_by1Click = markableName;
    }
}

class File_ANNOTATIONS {

    String Filename;
    Vector<Annotation> annotations = new Vector<Annotation>();
}

class File_Annotations_Depot {

    Vector<File_ANNOTATIONS> file_annotations = new Vector<File_ANNOTATIONS>();

    public void clear() {
        file_annotations.clear();
    }

    public void add(file_annotation fa) {
        if (fa == null) {
            return;
        }

        if (existed(fa)) {
            addAnnotation_toExistedFile(fa);
        } else {
            File_ANNOTATIONS faa = new File_ANNOTATIONS();
            faa.Filename = fa.filename;
            faa.annotations.add(fa.annotation);
            file_annotations.add(faa);
        }
    }

    private void addAnnotation_toExistedFile(file_annotation fa) {
        if (file_annotations == null) {
            return;
        }

        for (File_ANNOTATIONS faa : file_annotations) {
            if (faa == null) {
                continue;
            }

            if (faa.Filename.equals(fa.filename)) {
                faa.annotations.add(fa.annotation);
            }
        }
    }

    public boolean existed(file_annotation fa) {
        if (file_annotations == null) {
            file_annotations = new Vector<File_ANNOTATIONS>();
            return false;
        }

        for (File_ANNOTATIONS faa : file_annotations) {
            if (faa == null) {
                continue;
            }

            if (faa.Filename.equals(fa.filename)) {
                return true;
            }
        }

        return false;
    }
}