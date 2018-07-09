/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import resultEditor.annotationClasses.navigationTree.dataCache.Att;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 *
 * @author leng
 */
/**
 * Tree node renderer for annotated classes in tree view.
 */
public class Treeview_NodeRenderer extends DefaultTreeCellRenderer {
    
    //public static Icon icon_attribute = null;  
    
    public Icon icon_relationship = new javax.swing.ImageIcon( getClass().getResource("/res/classRelDef.gif"));;  

    private JClassCheckBox component_of_class = new JClassCheckBox();
    private JAnnotationCheckBox component_of_annotation = new JAnnotationCheckBox();
    private JPanel rel = new JPanel();
    
    private Component return_component;
    //private JList annotatedclassRenderer_list = new JList();
    private DefaultTreeCellRenderer normalRenderer = new DefaultTreeCellRenderer();
    Color selectionBorderColor, selectionForeground, selectionBackground,
            textForeground, textBackground;

    protected Object getClassRenderer() {
        return this.return_component;
    }

    public Treeview_NodeRenderer() {
        super();


        /*
         * super.setClosedIcon( new
         * javax.swing.ImageIcon(getClass().getResource("/res/arrow_right.gif"))
         * ); super.setOpenIcon( new
         * javax.swing.ImageIcon(getClass().getResource("/res/arrow_down.gif")));
         * super.setLeafIcon( new
         * javax.swing.ImageIcon(getClass().getResource("/res/annotation1.jpeg")));
         *
         * Icon closedicon = new
         * javax.swing.ImageIcon(getClass().getResource("/res/arrow_right.gif"));
         * this.setClosedIcon( closedicon ); Icon openedicon = new
         * javax.swing.ImageIcon(getClass().getResource("/res/arrow_down.gif"));
         *
         * this.setOpenIcon( openedicon ); this.setLeafIcon( new
         * javax.swing.ImageIcon(getClass().getResource("/res/arrow_down.gif")));
         */

        Font fontValue;
        //fontValue = UIManager.getFont("Arial");
        //if (fontValue != null) {
        //classRenderer_checkbox.setFont(fontValue);
        component_of_class.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        component_of_annotation.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        component_of_class.setFocusable(false);
        component_of_annotation.setFocusable(false);
        //}
        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        component_of_class.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));
        //annotationRendere_checkbox.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));

        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");

    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {

        //Component returnValue;
        if (value == null) {
            return this.return_component;
        }

        if (((DefaultMutableTreeNode) value).isRoot()) {
            JLabel label = new JLabel();
            if (selected) {
                label.setBackground(backgroundSelectionColor);
            } else {
                label.setBackground(backgroundNonSelectionColor);
            }
            label.setText(null); //value.toString() );
            label.setPreferredSize(new Dimension(0, 2));
            label.setMaximumSize(new Dimension(0, 2));
            this.return_component = label;
            return label;
        }

        // ##1## to node of class/markable
        if (value instanceof NodeOfClass) {

            if (hasFocus) {
                component_of_class.setForeground( Color.BLACK );
                component_of_class.setBackground(selectionBackground);
            } else {
                component_of_class.setForeground( Color.BLACK );
                component_of_class.setBackground(textBackground);
            }



            String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
            //component_of_class.setText(stringValue);
            //component_of_class.setSelected(false);

            component_of_class.setEnabled(tree.isEnabled());


            //if ( (value != null) && (value instanceof Treeview_ClassNode) ) {
            Color c = ((NodeOfClass) value).getClassColor();
            component_of_class.setClassColor(c);
            //component_of_class.setText(((NodeOfClass) value).getText());
            component_of_class.setSelected(((NodeOfClass) value).isSelected());
            component_of_class.setText(((NodeOfClass) value).getDisplayText());
            component_of_class.setAnnotationAmount(((NodeOfClass) value).getAnnotationAmount());
            component_of_class.setAnnotationTypeAmount(((NodeOfClass) value).getAnnotationTypeAmount());
            component_of_class.setClassname(((NodeOfClass) value).classname);
            component_of_class.setflag_isSelected_toCreateAnnotation(((NodeOfClass) value).isSelected_toCreateAnnotation);
            component_of_class.shortcomment = ((NodeOfClass) value).getShortComment();
            component_of_class.setDes( ((NodeOfClass) value).getDes() );
            
            

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof NodeOfClass) {
                NodeOfClass node = (NodeOfClass) userObject;
                component_of_class.setText(node.getDisplayText());
                component_of_class.setSelected(node.isSelected());
                //classRenderer_checkbox.setflag_isSelected_toCreateAnnotation( node.isSelected_toCreateAnnotation );
            }

            this.return_component = component_of_class;
            return this.return_component;

        }


        if (value instanceof NodeOfAttribute) {


            Att att = ((NodeOfAttribute) value).att;
            JAttributeCheckBox component_of_attribute = new JAttributeCheckBox(att);
            component_of_attribute.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));

            if (hasFocus) {
                component_of_attribute.setForeground(Color.white);
                component_of_attribute.setBackground(Color.blue);
            } else {
                component_of_attribute.setForeground(Color.black);
                component_of_attribute.setBackground(Color.white);
            }

            //String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
            //component_of_attribute.setText(stringValue);

            component_of_attribute.setEnabled(tree.isEnabled());
            component_of_attribute.setAtt(att);
            component_of_attribute.setSelected(((NodeOfAttribute) value).isSelected()&&DepotOfAttributes.isFilterOn());

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof NodeOfAttribute) {
                NodeOfAttribute node = (NodeOfAttribute) userObject;
                component_of_attribute.setText(node.getDisplayText());
                component_of_attribute.setSelected(node.isSelected()&&DepotOfAttributes.isFilterOn());

                //classRenderer_checkbox.setflag_isSelected_toCreateAnnotation( node.isSelected_toCreateAnnotation );
            }


            this.return_component = component_of_attribute;
            return component_of_attribute;

        }

        if (value instanceof NodeOfAttRoot) {


            //if( this.icon_attribute == null )
            // this.icon_attribute = new javax.swing.ImageIcon( getClass().getResource("/res/attributeDef.gif"));
            
            JAttRootCheckBox component_of_attributeroot = new JAttRootCheckBox( );
            component_of_attributeroot.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));


            component_of_attributeroot.setEnabled(tree.isEnabled());
            component_of_attributeroot.setText(((NodeOfAttRoot) value).text);

            // System.out.println("node: " + ((NodeOfAttRoot) value).text + " is selected="+ ((NodeOfAttRoot) value).isSelected());

            component_of_attributeroot.setSelected(((NodeOfAttRoot) value).isSelected());
            component_of_attributeroot.setClassname(((NodeOfAttRoot) value).classname);
            component_of_attributeroot.setCount(((NodeOfAttRoot) value).count);

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (userObject instanceof NodeOfAttRoot) {
                NodeOfAttRoot node = (NodeOfAttRoot) userObject;
                component_of_attributeroot.setText(node.text);
                component_of_attributeroot.setSelected(node.isSelected());
                //classRenderer_checkbox.setflag_isSelected_toCreateAnnotation( node.isSelected_toCreateAnnotation );
            }


            this.return_component = component_of_attributeroot;
            return component_of_attributeroot;

        }

        if (value instanceof NodeOfClassRoot) {



            JClassRootLabel component_of_classroot = new JClassRootLabel();
            component_of_classroot.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));

            if (selected) {
                component_of_classroot.setForeground(Color.black);
                component_of_classroot.setBackground(Color.blue);
            } else {
                component_of_classroot.setForeground(Color.black);
                component_of_classroot.setBackground(Color.white);
            }


            component_of_classroot.setEnabled(tree.isEnabled());
            component_of_classroot.setText(((NodeOfClassRoot) value).displayText);

            // System.out.println("node: " + ((NodeOfAttRoot) value).text + " is selected="+ ((NodeOfAttRoot) value).isSelected());
            component_of_classroot.totalClassType = ((NodeOfClassRoot) value).totalClassType;
            component_of_classroot.totalAnnotationType = ((NodeOfClassRoot) value).totalAnnotationType;
            component_of_classroot.totalAnnotation = ((NodeOfClassRoot) value).totalAnnotation;

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (userObject instanceof NodeOfClassRoot) {
                NodeOfClassRoot node = (NodeOfClassRoot) userObject;
                //component_of_classroot.setText( node.displayText );                
                component_of_classroot.setText(((NodeOfClassRoot) value).displayText);

                // System.out.println("node: " + ((NodeOfAttRoot) value).text + " is selected="+ ((NodeOfAttRoot) value).isSelected());
                component_of_classroot.totalClassType = node.totalClassType;
                component_of_classroot.totalAnnotationType = node.totalAnnotationType;
                component_of_classroot.totalAnnotation = node.totalAnnotation;
            }


            this.return_component = component_of_classroot;
            return component_of_classroot;

        }

        if (value instanceof NodeOfAttValue) {


            String attvalue = ((NodeOfAttValue) value).attvalue;
            String attname = ((NodeOfAttValue) value).attributeName;
            int attvaluecount = ((NodeOfAttValue) value).attvaluecount;
            JAttValueCheckBox component_of_attributevalue = new JAttValueCheckBox(attname, attvalue, attvaluecount);
            component_of_attributevalue.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));


            component_of_attributevalue.setEnabled(tree.isEnabled());
            component_of_attributevalue.setText(((NodeOfAttValue) value).attvalue);
            // component_of_attribute.setText(((NodeOfAttribute) value).getDisplayText());
            // System.out.println("rendering attribute text: " + ((NodeOfAttribute) value).getDisplayText() );
            component_of_attributevalue.setSelected(((NodeOfAttValue) value).isSelected());



            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (userObject instanceof NodeOfAttValue) {
                NodeOfAttValue node = (NodeOfAttValue) userObject;
                component_of_attributevalue.setText(node.getText());
                component_of_attributevalue.setSelected(node.isSelected());
                //classRenderer_checkbox.setflag_isSelected_toCreateAnnotation( node.isSelected_toCreateAnnotation );
            }


            this.return_component = component_of_attributevalue;
            return component_of_attributevalue;

        }

        if (value instanceof NodeOfRelAnnotation) {
            
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setFont( new Font("Arial Unicode MS", Font.PLAIN, 12) );
            renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            renderer.setIcon( this.icon_relationship );
            return renderer;
            
            /*
            rel.setFont(new java.awt.Font("Calibri", 0, 13) );
            rel.setOpaque( false );
            rel.removeAll();
             
             
            if (selected) {
                this.rel.setForeground(Color.black);
                this.rel.setBackground(Color.blue);
            } else {
                this.rel.setForeground(Color.black);
                this.rel.setBackground(Color.white);
            }

            //this.rel.setBackground(Color.blue);


            //this.rel.setEnabled(tree.isEnabled());


             
            this.rel.add( new javax.swing.JLabel(
                    ((NodeOfRelAnnotation) value).getDisplayText() 
                    )
                    );



            this.return_component = rel;
            return this.rel;*/

        }

        if (value instanceof NodeOfRelationship) {
            
            
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setFont( new Font("Arial Unicode MS", Font.PLAIN, 12) );
            renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            renderer.setIcon( this.icon_relationship );
            return renderer;
            
            /*//nodes use to list the name of relationships
            rel.setFont(new java.awt.Font("Calibri", 0, 13) );
            rel.setOpaque( false );
            rel.removeAll();
            
            if (selected) {
                this.rel.setForeground(Color.gray);
                this.rel.setBackground(Color.blue);
            } else {
                this.rel.setForeground(Color.gray);
                this.rel.setBackground(Color.white);
            }
            
            rel.setBackground(Color.blue);

            //String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);

            //this.attribute.setText(stringValue);
            //this.attribute.setSelected(false);

            this.rel.setEnabled(tree.isEnabled());
            
            


            //if ( (value != null) && (value instanceof Treeview_ClassNode) ) {
            //Color c = ((NodeOfAttribute) value).getClassColor();
            //this.attribute.setClassColor(c);
            //this.rel.setText(((NodeOfRelationship) value).getText());
            //this.attribute.setSelected( ((NodeOfAttribute) value).isSelected() );
            //this.rel.setText(((NodeOfRelationship) value).getDisplayText());
            javax.swing.JLabel names = new javax.swing.JLabel(
                    ((NodeOfRelationship) value).getDisplayText());
            names.setIcon( this.icon_relationship );
            this.rel.add( names );
            this.return_component = rel;
            return this.rel;*/

        }


        if (value instanceof Treeview_AnnotationNode) {
            //System.out.println("hello");
            //JAnnotationCheckBox annotationCheckBox = new JAnnotationCheckBox();

            // build the annotation diamond icon
            Color color = ((Treeview_AnnotationNode) value).getClassColor();
            component_of_annotation.iconcolor = color;
            if (((Treeview_AnnotationNode) value).isNotAppeared_in_CurrentArticle) {
                color = Color.BLACK;
            }
            DiamondIcon icon = new DiamondIcon(color);
            component_of_annotation.setIcon(icon);

            String text = ((Treeview_AnnotationNode) value).getText();
            component_of_annotation.setText(text);
            component_of_annotation.setOpaque(true);

            //System.out.println("selected = " + selected);         
            if (selected) {
                component_of_annotation.setForeground(selectionForeground);
                component_of_annotation.setBackground(selectionBackground);
            } else {
                component_of_annotation.setForeground(textForeground);
                component_of_annotation.setBackground(textBackground);
            }

            // Get the length of text showed on the checkbox and set the size of
            // this check box.
            Graphics2D g2d = (Graphics2D) component_of_annotation.getGraphics();
            if (g2d != null) {
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                String annotationText = ((Treeview_AnnotationNode) value).getUniqueAnnotationText();
                int fontcharwidth = g2d.getFontMetrics().stringWidth(annotationText);
                String annotationAmountString = String.valueOf(((Treeview_AnnotationNode) value).getUniqueAnnotationAmount());
                fontcharwidth = fontcharwidth + g2d.getFontMetrics().stringWidth(annotationAmountString + " " + "[]");
                fontcharwidth = fontcharwidth + 60;
                //annotationRendere_checkbox.setMinimumSize( new Dimension(fontcharwidth,22));
                component_of_annotation.setPreferredSize(new Dimension(fontcharwidth, 22));
                //annotationRendere_checkbox.setSize( 0,22);



            }





            if ((((Treeview_AnnotationNode) value).isHighLighted) || (selected)) {
                component_of_annotation.setForeground(selectionForeground);
                component_of_annotation.setBackground(selectionBackground);
            } else {
                component_of_annotation.setForeground(textForeground);
                component_of_annotation.setBackground(textBackground);
            }

            component_of_annotation.setUniqueAnnotationText(((Treeview_AnnotationNode) value).getText());
            component_of_annotation.setUniqueAnnotationAmount(((Treeview_AnnotationNode) value).UniqueAunotationAmount);

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (userObject instanceof Treeview_AnnotationNode) {
                Treeview_AnnotationNode anode = (Treeview_AnnotationNode) userObject;
                component_of_annotation.setUniqueAnnotationText(anode.getText());
                component_of_annotation.setUniqueAnnotationAmount(anode.UniqueAunotationAmount);

                //boolean isselected = true;

                component_of_annotation.setOpaque(true);
                //if ( isselected ) {
                //    annotationRendere_checkbox.setForeground(selectionForeground);
                //    annotationRendere_checkbox.setBackground(selectionBackground);
                //} else {
                //    annotationRendere_checkbox.setForeground(textForeground);
                //     annotationRendere_checkbox.setBackground(textBackground);
                // }


                component_of_annotation.isNotAppeared_in_CurrentArticle = anode.isNotAppeared_in_CurrentArticle;
                //annotationRendere_checkbox.setSelected(anode.isSelected());

                component_of_annotation.setText(anode.getText());
            }

            //returnValue = annotationCheckBox;
            //annotatedclassRendere_jlabel = annotationCheckBox;
            this.return_component = component_of_annotation;
            return this.return_component;

        } else {
            this.return_component = normalRenderer.getTreeCellRendererComponent(tree,
                    value, selected, expanded, leaf, row, hasFocus);
            //System.out.println("error 1008260042 - returned wrong type of component for treeview cell!!!");
        }
        
        this.return_component.setFont( new Font("Arial Unicode MS", Font.PLAIN, 12) );

        return this.return_component;
    }
}