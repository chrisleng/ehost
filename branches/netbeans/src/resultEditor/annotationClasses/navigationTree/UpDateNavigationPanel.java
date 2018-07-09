/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import resultEditor.annotationClasses.Depot;
import resultEditor.annotationClasses.SelectionStatusOfClasses;
import resultEditor.annotationClasses.file_annotation;
import resultEditor.annotationClasses.navigationTree.dataCache.*;
import resultEditor.annotationClasses.navigationTree.treeRelated.*;
import resultEditor.workSpace.WorkSet;

/**
 *
 * @author leng
 */
public class UpDateNavigationPanel {

    // the treeview component in the GUI of result editor
    protected static JTree treeview;
    // handle of main GUI for function recall
    protected static userInterface.GUI gui;
    
    protected static String filename = null;

    /**
     * Constructor
     *
     * @param treeview Introduce the component from GUI of result editor.
     *
     * @param gui Introduce the handle of main GUI for function recall.
     */
    public UpDateNavigationPanel(JTree treeview, userInterface.GUI gui) {
        this.treeview = treeview;
        UpDateNavigationPanel.gui = gui;
    }
    private static String currentArticle = null;

    public UpDateNavigationPanel() {
    }

    public void display() {
        final String classname = DepotOfAttributes.getClassname();                
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                display( classname );
            }
        });
    }
    /**
     * Show the navigation tree on the navigation panel, it lists classes and 
     * their annotations, a filter of attribtues and relationships and their 
     * annotations.
     * 
     * @param   classname
     *          If a classname node is 
     */
    public void display(String classname) {

        String currentFilename = null;
        try {
            // ##1## clear contents and reset root node in the tree view
            clearDisplay();
            System.out.println( DepotOfRelationships.isExpanded );
            // Build the basic nodes of this treeview, such as nodes: 
            // "Attributes", "Relationships:", "Classes:" 
            buildTreeFrame();



            File f = resultEditor.workSpace.WorkSet.getCurrentFile();
            if (f == null) {
                log.LoggingToFile.log(Level.SEVERE, "####ERROR 1106091547:: can not get current document - CURRENTFILE==NULL !!!");
                return;
            }

            currentFilename = f.getName();

            if ((currentFilename == null) || (currentFilename.trim().length() < 1)) {
                log.LoggingToFile.log(Level.SEVERE, "####ERROR 1103211007:: can not find current raw file name!!!");
                return;
            }
            if (treeview == null) {
                log.LoggingToFile.log(Level.SEVERE, "####ERROR 1103211006:: lost point for accessing the component of treeview!!!");
                return;
            }


            //refreshTypeMemory();


            // get root of treeview
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
            if (root == null) {
                log.LoggingToFile.log(Level.SEVERE, "####ERROR 1103211002:: fail to get access to the root!!!");
                return;
            }


            // show class
            addBranchOfClasses(root, currentFilename);

            // classname: the name of the focused classname            
            updateAttributeBranch( treeview, classname);

            addBranchOfRelationships( root );
            //root.add( relationshipNodes );

            

            // #### 3 ####
            // Initialize the treeview, set cell renderer and editor
            // set cell renderer for this treeview
            Treeview_NodeRenderer renderer = new Treeview_NodeRenderer();
            
            treeview.setCellRenderer(renderer);
            // set cell editor for this treeview
            treeview.setCellEditor(new Treeview_ClassNodeEditor(treeview, this.gui));
            //treeview.addTreeSelectionListener(new SelectionListener());
            treeview.setEditable(true);



            // set node status : selected and expansion status
            setNodesStatus();

            // update
            treeview.updateUI();
        } catch (Exception e) {
            log.LoggingToFile.log(Level.SEVERE, "ERROR 1104212200::" + e.toString());
            e.printStackTrace();
        }
    }

    
    public void updateAttributeBranch( final String classname ){

        // treeview.stopEditing();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                recountAttributes( classname );        
                //addBranchOfClasses()
                updateAttributeBranch( treeview, classname );
                treeview.updateUI();
                setNodesStatus();
            }
        });
        
        
    }
    
    public void hideAttributes(boolean hideAttributes){
        // System.out.println(" hide it = " + hideAttributes);
        
        if( treeview == null )
            return;
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        DefaultMutableTreeNode subroot_of_atts = (DefaultMutableTreeNode) root.getChildAt(1);
        if( hideAttributes )
            treeview.collapsePath( new TreePath(subroot_of_atts.getPath()) );
        else
            treeview.expandPath( new TreePath(subroot_of_atts.getPath()) );
                
                //tree.collapsePath( tree.getPathForRow( 13 ) );
        
        
        // treeview.stopEditing();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                treeview.updateUI();
            }
        });
    }
    
    public void updateAttributeBranch( JTree treeview, String classname ){
        
        // #### 1 ####
        // delete possible children nodes
        
        
        
        //Object rootobj = treeview.getModel().getRoot();
        //DefaultMutableTreeNode root = (DefaultMutableTreeNode) rootobj;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        int depth = treeview.getModel().getChildCount(root);
        if( depth < 2 )
            return;
        DefaultMutableTreeNode subroot_of_atts = (DefaultMutableTreeNode) root.getChildAt(1);
        if( subroot_of_atts == null )
            return;
        subroot_of_atts.removeAllChildren();
        
        // #### 2 ####
        // Put all attribute information we collected on screen from the depot 
        // of "DepotOfAttributes" 
        AttributesOfAClass attributes_of_class = DepotOfAttributes.getAttributes(classname);
        if( attributes_of_class == null )
            return;
        
        
        for (String attributeName : attributes_of_class.attributes.keySet()) { // name is             

            //int count = DepotOfAttributes.atts.get(name).count;

            // ##2.1## add node of class to the root of the treeview
            NodeOfAttribute node_of_att = new NodeOfAttribute(
                    attributeName, 
                    attributes_of_class.attributes.get(attributeName) );
            //new NodeOfAttribute(name, count, DepotOfAttributes.atts.get( name) );                        
            // node_of_att.setText( DepotOfAttributes.atts.get(name).attributeName );

            HashMap<String, AttValue> map_values = attributes_of_class.attributes.get(attributeName).values;
            node_of_att.setSelected(attributes_of_class.attributes.get(attributeName).isSelected);

            if (map_values != null) {

                for (String valuename : map_values.keySet()) {

                    if (valuename != null) {
                        String attvalue = map_values.get(valuename).value;
                        int attvaluecount = map_values.get(valuename).count;
                        if( map_values.get(valuename).count <1 )
                            continue;
                        NodeOfAttValue mtn_value = new NodeOfAttValue(attributeName, attvalue, attvaluecount);
                        mtn_value.setSelectionStatus(map_values.get(valuename).isSelected);
                        node_of_att.add(mtn_value);
                    }
                }
            }

            // add annotation type to this newly built class node            
            subroot_of_atts.add(node_of_att);

        }


        // update text contents of the root node of attributes
        NodeOfAttRoot nar = (NodeOfAttRoot) subroot_of_atts;
        nar.setClassname(DepotOfAttributes.getClassname());
        nar.setCount(DepotOfAttributes.getAttributes().size());
        nar.setSelected(DepotOfAttributes.isFilterOn());
        subroot_of_atts.setUserObject(nar);



    }
    
    
    private void recountAttributes(String classname){        
        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        depot.checkAllAnnotationTypes(UpDateNavigationPanel.currentArticle, classname);
    
    }   
        
    
    
    private void addBranchOfClasses(DefaultMutableTreeNode root, String currentFilename) {

        
        // get all classnames in array of string
        String[] classnames = classnames(classnames());
        
        DefaultMutableTreeNode subroot_of_classes = (DefaultMutableTreeNode) root.getChildAt(0);
        subroot_of_classes.removeAllChildren();

        // ##2## assemble nodes of markables/classes
        // reach depot of all classes(markables)
        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();

        for (int i = 0; i < classnames.length; i++) {

            treeview.setRootVisible(true);
            String classname = classnames[i];

            if ((classname == null) || (classname.trim().length() < 1)) {
                continue;
            }

            resultEditor.annotationClasses.AnnotationClass annotatedclass = depot.getAnnotatedClass(classname);
            if (annotatedclass == null) {
                return;
            }

            boolean isSelected = SelectionStatusOfClasses.getSelectedStatus(classname);

            int annotationAmount_toThisClass = annotatedclass.getAnnotationAmount();
            int annotationTypeAmount_toThisClass = annotatedclass.getAnnotationTypeAmount();

            // ##2.1## add node of class to the root of the treeview
            NodeOfClass classnode = new NodeOfClass(classname.trim(), isSelected,
                    annotatedclass.backgroundColor, annotationAmount_toThisClass,
                    annotationTypeAmount_toThisClass, annotatedclass.shortComment, annotatedclass.des  );

            if (env.Parameters.currentMarkables_to_createAnnotation_by1Click != null) {
                if (classname.trim().equals(env.Parameters.currentMarkables_to_createAnnotation_by1Click)) {
                    classnode.setFlag_isSelected_toCreateAnnotation(true);
                }
            }


            // ##2.1.1## get color of this class/markable
            Color classcolor = this.getColorByClass(classname.trim());


            // ##3## assemble nodes of annotation types
            if (annotatedclass != null) {
                for (resultEditor.annotationClasses.AnnotationType at : annotatedclass.getAnnotationTypes()) {
                    Treeview_AnnotationNode annotationnode = new Treeview_AnnotationNode(at.name.trim(), at.amount);

                    boolean isAppeared_in_CurrentArticle = false;
                    if (at.subAnnotations != null) {
                        //System.out.println( "1 - " + at.subAnnotations.size() );
                        for (file_annotation fa : at.subAnnotations) {
                            if (fa == null) {
                                continue;
                            }

                            annotationnode.addAnnotations(fa);
                            //System.out.println( " - " );

                            if ((fa.filename == null) || (currentFilename == null)) {
                                continue;
                            }
                            if (currentFilename.equals(fa.filename.trim())) {
                                isAppeared_in_CurrentArticle = true;
                            }
                        }
                    } else {
                        log.LoggingToFile.log(Level.SEVERE, "1008190215 - data lost!!!");
                    }

                    annotationnode.isNotAppeared_in_CurrentArticle = !isAppeared_in_CurrentArticle;
                    annotationnode.setClassColor(classcolor);


                    classnode.add(annotationnode);
                }
            }
            // add annotation type to this newly built class node
            //root.add( classnode );
            
            subroot_of_classes.add(classnode);

            SelectionStatusOfClasses.add(classnames[i].trim(), isSelected);
        }
    }

    
    
    private void addBranchOfRelationships(DefaultMutableTreeNode root) {

        int depth = treeview.getModel().getChildCount(root);
        if( depth < 3 )
            return;

        //for( int i=0; i<DepotOfAttributes.atts.size(); i++ )
        //Iterator iter = DepotOfRelationships.rels.entrySet().iterator();

        for (String name : DepotOfRelationships.rels.keySet()) {


            int count = DepotOfRelationships.rels.get(name).count;

            // ##2.1## add node of class to the root of the treeview
            NodeOfRelationship node_of_rel = new NodeOfRelationship(name, count);

            ArrayList<RefAnnotation> refannotations = DepotOfRelationships.rels.get(name).refannotations;
            if (refannotations != null) {
                for (RefAnnotation refannotation : refannotations) {
                    if ((refannotation != null) && (refannotation.annotation != null)) {
                        
                        //System.out.println("[" + refannotation.filename );

                        boolean isCurrentFile = false;
                        if (WorkSet.getCurrentFile().getName().compareTo(refannotation.filename) == 0) {
                            isCurrentFile = true;

                        }
                        
                        if (env.Parameters.working_on_file) {
                            if (!isCurrentFile) {
                                continue;
                            }
                        } else {
                            
                        }

                        NodeOfRelAnnotation mtn_annotation = new NodeOfRelAnnotation(refannotation.annotation,
                                name, refannotation.filename, isCurrentFile);
                        if (mtn_annotation != null) {
                            node_of_rel.add(mtn_annotation);
                        }
                    }
                }
            }

            // add annotation type to this newly built class node
            //root.add( classnode );
            DefaultMutableTreeNode subroot_of_rels = (DefaultMutableTreeNode) root.getChildAt(2);
            subroot_of_rels.add(node_of_rel);

        }
    }

    @SuppressWarnings("static-access")
    private Color getColorByClass(String classname) {
        if (classname == null) {
            return Color.black;
        }

        resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
        Color color = classdepot.getColor(classname);
        if (color == null) {
            return Color.black;
        } else {
            return color;
        }
    }

    /**
     * To all node in the treeview, set their selected status and expansion
     * status as previous tree view.
     */
    public void setNodesStatus() {
        //System.out.println("1we are updating " + DepotOfRelationships.isExpanded );
        resetNodeExpandtionStatusOfClass();
        
        //System.out.println("2we are updating " + DepotOfRelationships.isExpanded );
        resetNodeExpandtionStatusOfAtts();
        
        //System.out.println("3we are updating " + DepotOfRelationships.isExpanded );
        resetNodeExpandtionStatusOfRels();
        

    }
    
    private void resetNodeExpandtionStatusOfAtts(){
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        
        // restore the expand/ collis sitituation of nodes of attribute
        DefaultMutableTreeNode subroot_of_attributes = (DefaultMutableTreeNode) root.getChildAt(1);
        TreePath path_subattributes = new TreePath(getTreeModel().getPathToRoot(subroot_of_attributes));
        if( DepotOfAttributes.isExpanded ) {
            treeview.expandPath(path_subattributes);
        }else
            treeview.collapsePath(path_subattributes);
            
        if( DepotOfAttributes.isExpanded ) {
        int number_of_attribtues = subroot_of_attributes.getChildCount();

        for (int i = 0; i < number_of_attribtues; i++) {
            NodeOfAttribute attnode = (NodeOfAttribute) subroot_of_attributes.getChildAt(i);
            if (attnode.attributeName() != null) {
                String attributename = attnode.attributeName().trim();
                //System.out.print( "\nt="+i+", att [" +  attributename + "] = " );
                if (isAttNodeExpanded(attributename)) {
                    //System.out.print( "true\n");
                    TreePath path = new TreePath(getTreeModel().getPathToRoot(attnode));
                    if (path != null) {
                        treeview.expandPath(path);
                    }
                }//else
                    //System.out.print( "false\n");
            }
            }
        }
    
    }
    
    private void resetNodeExpandtionStatusOfRels(){
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        
        // restore the expand/ collis sitituation of nodes of attribute
        DefaultMutableTreeNode subroot_of_rel = (DefaultMutableTreeNode) root.getChildAt(2);
        TreePath root_of_relationship = new TreePath(getTreeModel().getPathToRoot(subroot_of_rel));
        // System.out.println("we are updating " + DepotOfRelationships.isExpanded );
        if( DepotOfRelationships.isExpanded ) {
            treeview.expandPath(root_of_relationship);
        }else {
            treeview.collapsePath(root_of_relationship);
        }
            
        
        if (DepotOfRelationships.isExpanded) {
            int number_of_rels = subroot_of_rel.getChildCount();

            for (int i = 0; i < number_of_rels; i++) {
                NodeOfRelationship node_of_rel = (NodeOfRelationship) subroot_of_rel.getChildAt(i);
                if (node_of_rel != null) {
                    //String attributename = node_of_rel.attributeName().trim();
                    //System.out.print( "\nt="+i+", att [" +  attributename + "] = " );
                    if (isRelNodeExpanded(node_of_rel)) {
                        //System.out.print( "true\n");
                        TreePath path = new TreePath(getTreeModel().getPathToRoot(node_of_rel));
                        if (path != null) {
                            treeview.expandPath(path);
                        }
                    }//else
                    //System.out.print( "false\n");
                }
            }
        }            
    }

    /**
     * To a tree node of class/markables, its expension status may be expanded
     * or not in the latest view. This method help us to find the latest
     * expension status of your designated class/markable.
     *
     * @return true - If node was expanded; false - if node was not expaned.
     */
    public boolean isClassNodeExpanded(String classname) {

        // validity check
        if (classname == null) {
            return false;
        }
        if (classname.trim().length() < 1) {
            return false;
        }

        // try to get the class/markable
        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        resultEditor.annotationClasses.AnnotationClass markable = depot.getAnnotatedClass(classname);
        if (markable == null) {
            return false;
        }

        // get saved expansion status
        return markable.isNodeExpanded;

    }
    
    public boolean isAttNodeExpanded(String attname) {

        // validity check
        if (attname == null) {
            return false;
        }
        if (attname.trim().length() < 1) {
            return false;
        }

        // try to get the class/markable
        AttributesOfAClass attributesOfAClass = DepotOfAttributes.getAttributes();
        
        
        HashMap<String, Att> currentAttributes = attributesOfAClass.attributes;
        if( currentAttributes == null )
            return false;
        
        Att att = currentAttributes.get( attname );
        if( att == null )
            return false;

        // get saved expansion status
        return att.isExpanded;

    }

    public void refreshTypeMemory_currentArticle(String currentArticle) {
        if ((currentArticle != null) && (currentArticle.trim().length() < 1)) {
            currentArticle = null;
        }
        UpDateNavigationPanel.currentArticle = currentArticle;

        refreshTypeMemory_refresh();
    }

    public void refreshTypeMemory_all() {
        UpDateNavigationPanel.currentArticle = null;
        refreshTypeMemory_refresh();
    }

    public void refreshTypeMemory_refresh(boolean force) {
        // while force==true, it force method to do smartreset in depotofAttribute
        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        depot.checkAllAnnotationTypes(UpDateNavigationPanel.currentArticle, null, force);
    }
    
    public void refreshTypeMemory_refresh() {
        refreshTypeMemory_refresh( false );
    }

    private Color getColor(String classname) {
        if (classname == null) {
            return null;
        }
        
        classname = classname.trim();
        return resultEditor.annotationClasses.Depot.getColor(classname);
    }

    /**
     * Remove all nodes of this tree view except the root node.
     */
    private void clearDisplay() {
        try {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
            //if ( root == null )  
            //    return;
            root.removeAllChildren();
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "####ERROR 1103211005:: fail to remove all items from the treeview!!!");
        }
    }

    /**
     * set text of root node of the tree view. Show class and amount of classes
     * and related subAnnotations.
     */
    private void buildTreeFrame() {
        try {

            // get the root
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
            if (root == null) {
                return;
            }

            root.setUserObject("<html><font color=black>Annotation Navigator</font></html>");
            

            //Vector<String> roots = new Vector<String>();

            // assemble the text of the subroot node of the branch of "classes"
            

            NodeOfClassRoot mtn_classes = new NodeOfClassRoot(
                    resultEditor.annotationClasses.Depot.size(),
                    Depot.TYPE_getAnnotationTypeAmount(),
                    getAnnotationsAmount() );          
            root.add(mtn_classes);


            // add the subroot node of the branch of "attribute"
            NodeOfAttRoot mtn = new NodeOfAttRoot();
            
                if( DepotOfAttributes.getAttributes() != null )
                    mtn.setCount( DepotOfAttributes.getAttributes().size() );            
                else
                    mtn.setCount( 0 );            
                
                mtn.setClassname( DepotOfAttributes.getClassname() );
                mtn.setSelected( DepotOfAttributes.isFilterOn() );
                
            root.add(mtn);

            
            // add the root node of "relationships"
            SubRootOfRelationship mtn2 = new SubRootOfRelationship(DepotOfRelationships.getTotal(), DepotOfRelationships.rels.size());                        
            root.add(mtn2);





            //roots.add("Relationships: ");





        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "####ERROR 1103211004:: fail to put roots on the treeview!!!");
            ex.printStackTrace();
        }
    }

    /**
     * get amount of all subAnnotations in memory(depot of subAnnotations)
     */
    private int getAnnotationsAmount() {
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        if (depot == null) {
            return 0;
        }

        // count annotation amount of each article
        int amount = 0;
        for (resultEditor.annotations.Article article : depot.getAllArticles()) {
            if (article.annotations == null) {
                continue;
            }
            if ((UpDateNavigationPanel.currentArticle != null) && (UpDateNavigationPanel.currentArticle.trim().length() > 0)) {
                if ((article.filename != null) && (article.filename.trim().length() > 0)) {
                    if (article.filename.trim().compareTo(currentArticle.trim()) != 0) {
                        continue;
                    }
                } else {
                    continue;
                }
            }


            amount = amount + article.annotations.size();
        }

        return amount;
    }

    /**
     * get all classnames in array of objects
     */
    private String[] classnames() {
        Depot depot = new Depot();
        return depot.getAnnotationClasssnamesString();
    }

    /**
     * get strings of classnames by converting object to string.
     */
    private String[] classnames(Object[] objects) {
        if (objects == null) {
            return null;
        }

        int size = objects.length;
        String[] returnClassnames = new String[size];

        Arrays.sort(objects);

        for (int i = 0; i < size; i++) {
            returnClassnames[i] = (String) objects[i];
        }

        return returnClassnames;
    }

    /**
     * every time while user change the selected staus of a class node, this
     * method bring change to the manager of class selected status: class
     * SelectedStatus. And then only show annotation belongs to classes which
     * selected by user.
     */
    public void checkStatusChanged(String classname, boolean isSelected) {
        SelectionStatusOfClasses.changeSelectedStatus(classname, isSelected);

        setAnnotationsVisile();

        repaintBackgroundHighlight();
        gui.showValidPositionIndicators();
    }
    
    public void checkStatusChanged() {        
        setAnnotationsVisile();

        repaintBackgroundHighlight();
        
        
        
        gui.showValidPositionIndicators();
        
        gui.setFlag_of_DifferenceMatching_Display(env.Parameters.enabled_Diff_Display); // update differences
                    gui.display_repaintHighlighter();
        
    }

    private void repaintBackgroundHighlight() {
        gui.display_repaintHighlighter();
    }

    /**
     * Set visible attributes of all subAnnotations in the memory. To each
     * annotation, its attribute 'visible' will be set to false if its annotated
     * class appeared in the list of unchosen classes in class 'SelectedStatus'.
     */
    public void setAnnotationsVisile() {
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.setAnnotationVisible(SelectionStatusOfClasses.getVisibleClasses());
    }

    private DefaultTreeModel getTreeModel() {
        return (DefaultTreeModel) treeview.getModel();
    }

    /**
     * clear for any highlighted specific category node in the treevide's
     * background showed as focused.
     */
    public void removeAllNodehighlight() {

        try {
            if (treeview == null) {
                return;
            }

            // get root
            DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode) treeview.getModel().getRoot();
            if (rootnode == null) {
                return;
            }

            // go over all category node
            int categoryNodeAmount = rootnode.getChildCount();
            for (int i = 0; i < categoryNodeAmount; i++) {
                DefaultMutableTreeNode thisnode = (DefaultMutableTreeNode) rootnode.getChildAt(i);
                if (thisnode == null) {
                    continue;
                }
                if (thisnode instanceof NodeOfClass) {

                    try {
                        int size = thisnode.getChildCount();
                        for (int j = 0; j < size; j++) {
                            Object o = thisnode.getChildAt(j);
                            if ((o != null) && (o instanceof Treeview_AnnotationNode)) {

                                DefaultMutableTreeNode temp = (DefaultMutableTreeNode) thisnode.getChildAt(j);
                                Treeview_AnnotationNode an = (Treeview_AnnotationNode) o;
                                an.isHighLighted = false;
                                ((DefaultMutableTreeNode) o).setUserObject(an);

                            }

                        }

                    } catch (Exception ex) {
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("errors 1010281344:: fail to clear all nodes");
        }

    }

    /**
     * let a specific category node in the treevide's background showed as focused.
     */
    public void setCategoryNodeSelected(String category, String annotation_type_text) {

        // Do NOTHING if user's mouse clicked on navigator panel, so that
        // the selection indicator can works normal.
        if (resultEditor.workSpace.WorkSet.mouse_clicked_on == 1) {
            return;
        }

        // force change and display the selection items
        if (category == null) {
            return;
        }
        if (category.trim().length() < 1) {
            return;
        }
        if (treeview == null) {
            return;
        }

        DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        if (rootnode == null) {
            return;
        }

        int categoryNodeAmount = rootnode.getChildCount();
        for (int i = 0; i < categoryNodeAmount; i++) {
            DefaultMutableTreeNode thisnode = (DefaultMutableTreeNode) rootnode.getChildAt(i);
            if (thisnode == null) {
                continue;
            }
            if (thisnode instanceof NodeOfClass) {
                String categoryname = ((NodeOfClass) thisnode).classname;
                if (categoryname == null) {
                    continue;
                }
                if (categoryname.trim().length() < 1) {
                    continue;
                }
                if (categoryname.trim().compareTo(category.trim()) == 0) {
                    TreePath visiblePath = new TreePath(getTreeModel().getPathToRoot(thisnode));
                    treeview.setSelectionPath(visiblePath);

                    // set selection indicator for annotation
                    if (annotation_type_text != null) {
                        int size = thisnode.getChildCount();
                        for (int j = 0; j < size; j++) {
                            Object o = thisnode.getChildAt(j);
                            if ((o != null) && (o instanceof Treeview_AnnotationNode)) {

                                DefaultMutableTreeNode temp = (DefaultMutableTreeNode) thisnode.getChildAt(j);
                                Treeview_AnnotationNode an = (Treeview_AnnotationNode) o;
                                String text = an.uniqueAnnotationText;
                                if (an.uniqueAnnotationText.equals(annotation_type_text)) {
                                    an.isHighLighted = true;
                                } else {
                                    an.isHighLighted = false;
                                }

                                ((DefaultMutableTreeNode) thisnode).setUserObject(an);

                            }

                        }
                    }

                } else {
                    int size = thisnode.getChildCount();
                    for (int j = 0; j < size; j++) {
                        Object o = thisnode.getChildAt(j);
                        if ((o != null) && (o instanceof Treeview_AnnotationNode)) {

                            DefaultMutableTreeNode temp = (DefaultMutableTreeNode) thisnode.getChildAt(j);
                            Treeview_AnnotationNode an = (Treeview_AnnotationNode) o;
                            String text = an.uniqueAnnotationText;

                            an.isHighLighted = false;


                            ((DefaultMutableTreeNode) thisnode).setUserObject(an);

                        }

                    }
                }

            }
        }
        // System.out.println( "current path in treeview = " + visiblePath );
        treeview.updateUI();
        return;
    }

    /**Check whether a given relationship node is expanded, only true is really 
     * trustable to say this relationship node is expanded.
     */
    private boolean isRelNodeExpanded(NodeOfRelationship node_of_rel) {
        
        // validity check
        if( node_of_rel == null ) {
            return false;
        }                        

        String relationshipname = node_of_rel.getRelName();
        if( relationshipname == null ) {
            return false;
        }
        
        // try to get the class/markable
        //DepotOfRelationships depotOfRel = new DepotOfRelationships();
        if(DepotOfRelationships.rels == null) {
            return false;
        }
        
        Rel rel = DepotOfRelationships.rels.get( relationshipname );
        if( rel == null) {
            return false;
        }
        
        return rel.isExpanded;                
    }

    private void resetNodeExpandtionStatusOfClass() {
        // ##1##  try to get root node
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        if (root == null) {
            return;
        }

        //treeview.expandRow( 1 );
        //treeview.expandRow( 2 );



        // ##2## try to get each class/markable node from the root node
        DefaultMutableTreeNode subroot_of_classes = (DefaultMutableTreeNode) root.getChildAt(0);
        TreePath path_subclasses = new TreePath(getTreeModel().getPathToRoot(subroot_of_classes));
        treeview.expandPath(path_subclasses);


        int markables_amount = subroot_of_classes.getChildCount();

        for (int i = 0; i < markables_amount; i++) {
            NodeOfClass classnode = (NodeOfClass) subroot_of_classes.getChildAt(i);
            if (classnode.getText() != null) {
                String classname = classnode.getText().trim();
                if (isClassNodeExpanded(classname)) {
                    TreePath path = new TreePath(getTreeModel().getPathToRoot(classnode));
                    if (path != null) {
                        treeview.expandPath(path);
                    }
                }
            }
        }
    }
}




