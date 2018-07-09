/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import resultEditor.annotationClasses.SelectionStatusOfClasses;
import resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;
import userInterface.GUI;

/**
 *
 * @author leng
 */

/**
 * tree editor for tree node of annoatated classes.
 */
public class Treeview_ClassNodeEditor extends AbstractCellEditor implements TreeCellEditor {

    Treeview_NodeRenderer renderer = new Treeview_NodeRenderer();
    ChangeEvent changeEvent = null;
    JTree tree;
    Point point = null;
    GUI gui;

    public Treeview_ClassNodeEditor(JTree tree, GUI gui) {
        this.tree = tree;
        this.gui = gui;
    }

    /**
     * get object before modifying.
     */
    @Override
    public Object getCellEditorValue() {
        //System.out.println("01 - must return 11 or 22 ");
        Object o = renderer.getClassRenderer();
        boolean isSelectedtoCreateAnnotation = false;
        
        
        
        
        if (o instanceof JClassCheckBox) {

            
                
            //System.out.println("     - 11");
            JClassCheckBox checkbox = (JClassCheckBox) renderer.getClassRenderer();
            String classname = checkbox.classname;
            // System.out.println("classname = " + classname );
            boolean selectionstatus = SelectionStatusOfClasses.getSelectedStatus(classname);
                
            if (checkbox == null) {
                System.out.println("\n\n\n1207100414:: component is null!!!");
            }

            int x0 = checkbox.getX();
            // int y0 = checkbox.getY();

            // are we allowed to change the selection selection status of checkbox?
            boolean allowToChangeSelectionStatus = false;
                
            if (point != null) {
                // allow to change the selection status while mouse clicked on the checkbox
                int distance = point.x - x0;

                //System.out.println(distance);

                // are we allowed to change the selection selection status of checkbox?                
                if ((distance >= 0) && (distance <= 25)) {
                    allowToChangeSelectionStatus = true;
                }

                if( (DepotOfAttributes.isFilterOn() )
                        &&(DepotOfAttributes.getClassname()!=null)
                        &&(DepotOfAttributes.getClassname().compareTo("NULL_NULL_NULL")!=0)
                        //&&(DepotOfAttributes.getClassname().compareTo( ((JClassCheckBox)o).classname )!=0)
                        ){
                    if( !checkbox.isSelected()  )
                        allowToChangeSelectionStatus =  true;
                }
                
                
                
                
                
                    
                if (allowToChangeSelectionStatus) {
                    if(DepotOfAttributes.isFilterOn()) {
                        selectionstatus = true;
                    } else {
                        selectionstatus = !selectionstatus;
                    }
                    SelectionStatusOfClasses.changeSelectedStatus(classname, selectionstatus);
                }

                if ((distance >= 26) && (distance <= 50)) {
                    isSelectedtoCreateAnnotation = true;
                }                
            }
            
            UpDateNavigationPanel scitv = new UpDateNavigationPanel();


            // based on previous component, build a new node data to return;
            NodeOfClass checkBoxNode = new NodeOfClass(
                    checkbox.getText(), 
                    selectionstatus,
                    checkbox.getClassColor(),
                    checkbox.getAnnotationAmount(), 
                    checkbox.getAnnotationTypeAmount(),
                    checkbox.shortcomment,
                    checkbox.getDes()
                    );
            
            
            
            if( DepotOfAttributes.isFilterOn() )
                DepotOfAttributes.setClassname( classname );
            
            
            if(point!=null) {//&&(allowToChangeSelectionStatus)) {                
                // now, the time point is chechbox status changed but the display component are still not get assembled.
                // so we build out own status monitor here.                
                scitv.checkStatusChanged(checkbox.getClassname().trim(), selectionstatus );// checkbox.isSelected());            
                scitv.updateAttributeBranch( classname );     
                scitv.refreshTypeMemory_refresh();
                    
                    scitv.checkStatusChanged();
                    scitv.display();
                
            }
            
            point = null;
            
            return checkBoxNode;
            
            
        }

        else if (o instanceof JAttRootCheckBox) {

            JAttRootCheckBox checkbox = (JAttRootCheckBox) renderer.getClassRenderer();
            //String classname = checkbox.classname;
            // System.out.println("classname = " + classname );
            //    boolean selectionstatus = SelectionStatusOfClasses.getSelectedStatus(classname);

            if (checkbox == null) {
                System.out.println("\n\n\n1207100414:: component is null!!!");
            }

            DepotOfAttributes.setFilterOn(checkbox.isSelected());
            NodeOfAttRoot attvalueroot = new NodeOfAttRoot(checkbox.count, checkbox.classname);
            attvalueroot.setSelected(checkbox.isSelected());
            checkbox.isSelected = checkbox.isSelected();

            try {
                UpDateNavigationPanel scitv = new UpDateNavigationPanel();
                if (point != null) {//&&(allowToChangeSelectionStatus)) {                
                    
                    
                    // now, the time point is chechbox status changed but the display component are still not get assembled.
                    // so we build out own status monitor here.                
                    //scitv.checkStatusChanged(checkbox.getClassname().trim(), selectionstatus );// checkbox.isSelected());            
                    scitv.refreshTypeMemory_refresh();
                    scitv.updateAttributeBranch(DepotOfAttributes.getClassname());
                    scitv.checkStatusChanged();                                                                                
                    scitv.display();
                    
                                                            
                }

                point = null;

            } catch (Exception ex) {
                System.out.println("error 1207181315");
                ex.printStackTrace();
            }
            
            
            return attvalueroot;
            
        } 
        
        else  if (o instanceof JAttributeCheckBox) {

            JAttributeCheckBox checkbox = (JAttributeCheckBox) renderer.getClassRenderer();
            //String classname = checkbox.classname;
                // System.out.println("classname = " + classname );
            //    boolean selectionstatus = SelectionStatusOfClasses.getSelectedStatus(classname);
                
            if (checkbox == null) {
                System.out.println("\n\n\n1207100414:: component is null!!!");
            }
            NodeOfAttribute attBoxNode = new NodeOfAttribute( checkbox.getText(), checkbox.getAtt() );
            attBoxNode.setSelected( checkbox.isSelected() );
            
            syncAttStatus( attBoxNode, checkbox.isSelected() );
            
            try {
                UpDateNavigationPanel scitv = new UpDateNavigationPanel();
                if (point != null) {//&&(allowToChangeSelectionStatus)) {                
                    // now, the time point is chechbox status changed but the display component are still not get assembled.
                    // so we build out own status monitor here.                
                    //scitv.checkStatusChanged(checkbox.getClassname().trim(), selectionstatus );// checkbox.isSelected());            
                    scitv.refreshTypeMemory_refresh();
                    scitv.updateAttributeBranch(DepotOfAttributes.getClassname());
                    scitv.checkStatusChanged();
                    
                    
                    
                    
                    
                    scitv.display();
                }

                point = null;

            } catch (Exception ex) {
                System.out.println("error 1207181315");
                ex.printStackTrace();
            }
            
            
           
            return attBoxNode;
        }
        
        else if (o instanceof JAttValueCheckBox) {

            JAttValueCheckBox checkbox = (JAttValueCheckBox) renderer.getClassRenderer();
            //String classname = checkbox.classname;
                // System.out.println("classname = " + classname );
            //    boolean selectionstatus = SelectionStatusOfClasses.getSelectedStatus(classname);
                
            if (checkbox == null) {
                System.out.println("\n\n\n1207100414:: component is null!!!");
            }
            NodeOfAttValue attvalueNode = new NodeOfAttValue( checkbox.attributeName, checkbox.value, checkbox.attvaluecount );            
            syncAttValueStatus( attvalueNode, checkbox.isSelected() );
            attvalueNode.setSelected( checkbox.isSelected() );
            
            try {
                UpDateNavigationPanel scitv = new UpDateNavigationPanel();
                if (point != null) {//&&(allowToChangeSelectionStatus)) {                
                    // now, the time point is chechbox status changed but the display component are still not get assembled.
                    // so we build out own status monitor here.                
                    //scitv.checkStatusChanged(checkbox.getClassname().trim(), selectionstatus );// checkbox.isSelected());            
                    scitv.refreshTypeMemory_refresh();
                    scitv.updateAttributeBranch(DepotOfAttributes.getClassname());
                    scitv.checkStatusChanged();
                    scitv.display();
                }

                point = null;

            } catch (Exception ex) {
                System.out.println("error 1207181315");
                ex.printStackTrace();
            }
           
            return attvalueNode;
        }
        
        
        // else if (o instanceof JClassRootLabel) {
        //    JClassRootLabel classrootlabel = (JClassRootLabel) renderer.getClassRenderer();
        //    
        //    UpDateNavigationPanel scitv = new UpDateNavigationPanel();
        //    DepotOfAttributes.setClassname(null);         
        //        scitv.updateAttributeBranch( null );                            
        //    
        //    
        //    point = null;
        //    
        //    return classrootlabel;
       // }
        
        
        //System.out.println("error 1008260008 - miss type of cell editor type!!!");

        return o;
    }

    /**
     * To a node pointed by mouse, justify whether it is editable or not .
     */
    @Override
    public boolean isCellEditable(EventObject event) {

        boolean returnValue = false;
        if (event instanceof MouseEvent) {

            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());

            if (path != null) {

                Object node = path.getLastPathComponent();

                if ((node != null) && (node instanceof NodeOfClass)) {

                    // get the coordinate (x, y) of the moust clicking
                    int x = mouseEvent.getX(), y = mouseEvent.getY();
                    this.point = new Point(x, y);                                        
                    
                    returnValue = true;//= ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
                }
                
                else if ((node != null) && (node instanceof NodeOfAttribute)) {

                    // get the coordinate (x, y) of the moust clicking
                    int x = mouseEvent.getX(), y = mouseEvent.getY();
                    this.point = new Point(x, y);

                    returnValue = true;//= ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
                }
                else if ((node != null) && (node instanceof NodeOfAttRoot )) {

                    // get the coordinate (x, y) of the moust clicking
                    int x = mouseEvent.getX(), y = mouseEvent.getY();
                    this.point = new Point(x, y);

                    returnValue = true;//= ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
                }
                else if ((node != null) && (node instanceof NodeOfAttValue)) {

                    // get the coordinate (x, y) of the moust clicking
                    int x = mouseEvent.getX(), y = mouseEvent.getY();
                    this.point = new Point(x, y);

                    returnValue = true;//= ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
                } 
            }
        }
        //System.out.println("editable = " + returnValue);

        return returnValue;
    }

    /**
     * add listener to jclasscheckbox to answer require.
     */
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row) {

        Component editor = renderer.getTreeCellRendererComponent(tree, value,
                true, expanded, leaf, row, true);

        ItemListener itemListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                if( ie == null )
                    return;
                            
                try {
                    if (stopCellEditing()) {
                        fireEditingStopped();
                    }
                } catch (Exception ex) {
                    System.out.println("WARNING:: 1207100516:: java.lang.NullPointerException at resultEditor.annotationClasses.Treeview_ClassNodeEditor$1.itemStateChanged");
                    ex.printStackTrace();
                }
            }
            //@Override
            //public void itemStateChanged(ItemEvent itemEvent) {
            //    if (stopCellEditing()) {
            //        fireEditingStopped();
            //    }
            //}
        };
        
        
        /*MouseListener mouseListener = new MouseListener() {         

            @Override
            public void mouseClicked(MouseEvent me) {
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if( me == null )
                    return;
                            
                try {
                    if (stopCellEditing()) {
                        fireEditingStopped();
                    }
                } catch (Exception ex) {
                    System.out.println("WARNING:: 1207100516:: java.lang.NullPointerException at resultEditor.annotationClasses.Treeview_ClassNodeEditor$1.itemStateChanged");
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                
            }
        };*/

        if (editor instanceof JClassCheckBox) {

            ((JClassCheckBox) editor).addItemListener(itemListener);
        }
        
        else if (editor instanceof JAttributeCheckBox) {

            ((JAttributeCheckBox) editor).addItemListener(itemListener);
        }
        
        else if (editor instanceof JAttRootCheckBox) {

            ((JAttRootCheckBox) editor).addItemListener(itemListener);
        }
        
        else if (editor instanceof JAttValueCheckBox) {

            ((JAttValueCheckBox) editor).addItemListener(itemListener);
        }
        //else if (editor instanceof JClassRootLabel){
        //    ((JClassRootLabel) editor).addMouseListener( mouseListener);
        //}

        return editor;
    }

    /**tell the DepotOfAttributes that this value node is selected or not.*/
    private void syncAttValueStatus(NodeOfAttValue attvalueNode, boolean selected) {
        
        attvalueNode.selected = selected;        
        String valuename = attvalueNode.attvalue;
        
        String attname = attvalueNode.attributeName;        
        
        DepotOfAttributes.setValueStatus( attname, valuename, selected );
    }

    private void syncAttStatus(NodeOfAttribute attBoxNode, boolean selected) {
        attBoxNode.isSelected = selected;
        String attname = attBoxNode.att.attributeName;
        DepotOfAttributes.setAttStatus( attname, selected );
    }
}
class   TreeUIUpdateThread   extends   Thread{     
    
    UpDateNavigationPanel  navigationUpdater;
    
    
    public   TreeUIUpdateThread(UpDateNavigationPanel  navigationUpdater ){ 
        this.navigationUpdater = navigationUpdater;
        start(); 
    } 
    
    public   synchronized   void   run(boolean flag)   { 
        navigationUpdater.hideAttributes( flag );
    } 
} 