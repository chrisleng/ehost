/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses.mouse;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfRelationships;
import resultEditor.annotationClasses.navigationTree.dataCache.SubRootOfRelationship;
import resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelationship;

/**
 * Tell the tree model that one node is expanded on the navigation panel.
 * 
 * @author Chris
 */
public class NodeExpandedListener {

    public NodeExpandedListener (javax.swing.event.TreeExpansionEvent evt, JTree treeview){
        
        TreePath path = evt.getPath();
        if (path != null) {
            evt.getPath();
            javax.swing.tree.DefaultMutableTreeNode node = (javax.swing.tree.DefaultMutableTreeNode)path.getLastPathComponent();
            if ( node == null )
                return;

            // if this node is a class node
            if( node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass ){
                resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass classnode = ( resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfClass ) node;
                String classname = classnode.getText();
                if( classname == null )
                    return;

                classname = classname.trim();

                resultEditor.annotationClasses.Depot classdepot = new  resultEditor.annotationClasses.Depot();
                classdepot.setClassExpanded( classname, true );
            }
            else if( node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttribute ){
                
                // tell eHOST parameter that the a node of attributes is **expanded**
                resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttribute attnode = ( resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttribute ) node;
                String attname = attnode.attributeName();
                DepotOfAttributes.setAttExpStatus( attname, true );
                
            } else if (node instanceof resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfAttRoot) {                
                
                // tell eHOST parameter that the root node of attributes is **expanded**
                DepotOfAttributes.isExpanded = true;
                
            } 
            // a relationship node
            else if ( node instanceof NodeOfRelationship ) {
                NodeOfRelationship node_of_relationship = (NodeOfRelationship) node;                
                DepotOfRelationships.setExpanded( node_of_relationship, true );
                
            } 
            // subroot: relationship branch
            else if ( node instanceof SubRootOfRelationship ) {
                
                System.out.println( "&&&&" );
                DepotOfRelationships.isExpanded = true;                
            }
        }
    }
}
