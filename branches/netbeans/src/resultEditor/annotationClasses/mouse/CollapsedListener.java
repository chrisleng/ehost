/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.mouse;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfRelationships;
import resultEditor.annotationClasses.navigationTree.dataCache.SubRootOfRelationship;
import resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelationship;

/**
 *
 *
 * @author Chris 20100719 17:01pm monday, division of epidemiology
 */
public class CollapsedListener {

    public CollapsedListener(javax.swing.event.TreeExpansionEvent evt, JTree treeview) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeview.getModel().getRoot();
        
        TreePath path = evt.getPath();
        
        //int a = evt.getPath().hashCode();
        //int b = treeview.getptreeview.getModel().getRoot()        
        int[] selected = treeview.getSelectionRows();

        
        if (path != null) {

            javax.swing.tree.DefaultMutableTreeNode node = (javax.swing.tree.DefaultMutableTreeNode) path.getLastPathComponent();

            System.out.println( "\ndepth = " + node.getDepth() );
            System.out.println( "level = " + node.getLevel() );

                
            // all child of root must be expanded at any time
            if (node.isRoot()) {
                treeview.expandPath(path);
                return;
            } //else if( node.getDepth() == 2 ){
            //    treeview.expandPath(path);
            //    return;
            //}
            // expand class branch by user's command
             else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) {

                //treeview.expandPath(path);

                resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass classnode = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass) node;
                String classname = classnode.getText();
                if (classname == null) {
                    return;
                }

                classname = classname.trim();

                resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
                classdepot.setClassExpanded(classname, false);
            } else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttribute) {
                resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttribute attnode = (resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttribute) node;
                String attname = attnode.attributeName();
                DepotOfAttributes.setAttExpStatus(attname, false);
            } else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttRoot) {
                DepotOfAttributes.isExpanded = false;
                return;
            } 
            // subroot: relationship branch
            else if ( node instanceof SubRootOfRelationship ) {
                
                DepotOfRelationships.isExpanded = false;
                System.out.println("sub node of relationship is just collapsed!");
                return;
                
                //node_of_relAnnotation.setClassExpanded( true );
                
            } 
            // a relationship node
            else if ( node instanceof NodeOfRelationship ) {
                NodeOfRelationship node_of_relationship = (NodeOfRelationship) node;                
                DepotOfRelationships.setExpanded( node_of_relationship, false );
                
            } else {
                TreePath visiblePath = new TreePath( ( (DefaultTreeModel) treeview.getModel()).
                                        getPathToRoot(root.getChildAt(0)));
                if ( path.equals( visiblePath ) ) {                                
                treeview.expandPath(path);
                return;
                }
            }
        }

    }
}
