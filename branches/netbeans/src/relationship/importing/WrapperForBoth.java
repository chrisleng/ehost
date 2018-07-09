/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relationship.importing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import relationship.complex.dataTypes.ComplexRelImportReturn;
import relationship.complex.dataTypes.RelationshipDef;
import relationship.complex.importing.Handler;
import relationship.complex.importing.UserComplexImport;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Depot;

/**
 *
 * @author Kyle
 */
public class WrapperForBoth implements iListener, iFinished
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    /**
     * Attributes outside of the schema.
     */
    private Hashtable<String, Hashtable>  unknownAtts;
    /**
     * Complex Relationships outside of the schema
     */
    private Vector<ComplexRelImportReturn> unknownRels;
    /**
     * The user interface that called this
     */
    private userInterface.GUI father;
    /**
     * The string to show in the Attribute importer.
     */
    private String toShow;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor for a WrapperForBoth object.
     * @param atts - Attributes to try to import.
     * @param rels - Relationships to try to import.
     * @param gui - the GUI that called this.
     * @param toShow - the String to show in the Attributes importer.
     */
    public WrapperForBoth( Hashtable<String, Hashtable> unknownatts, Collection<ComplexRelImportReturn> rels, userInterface.GUI gui, String toShow)
    {
        unknownAtts = new Hashtable<String, Hashtable>();
        unknownAtts.putAll( unknownatts );
        unknownRels = new Vector<ComplexRelImportReturn>();
        unknownRels.addAll(rels);
        this.toShow = toShow;
        father = gui;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Open up the GUI for both Complex Relationship importing and Attribute
     * importing.
     */
    public void doImportForBoth()
    {
        if((unknownAtts!=null)&&(unknownAtts.keySet().size() > 0))
        {
            Handler handle = new Handler(this, father.getX(), father.getY(), father.getWidth(), father.getHeight(), unknownAtts, new Vector<RelationshipDef>(), toShow);
            handle.setVisible(true);
        }
        else
        {
            complexImport();
        }
    }
    /**
     * Implemented from iListener interface.  This will be called when the Attribute
     * Handler is closing.
     */
    public void listeneeDead()
    {
        father.refreshResultEditor();
        father.totalRefresh();
        complexImport();
    }
    /**
     * Start complex relationship importing.
     */
    private void complexImport()
    {
        if((unknownRels!=null)&&(unknownRels.size() >0))
        {
            UserComplexImport relImport = new UserComplexImport(father.getX(), father.getY(), father.getWidth(), father.getHeight(),unknownRels, this);
            relImport.setVisible(true);
        }
    }
    public void finished()
    {
        Depot depot = new Depot();
        ArrayList<Annotation> annotations = depot.getAllAnnotations();
        for(Annotation annotation: annotations)
        {
            annotation.setVerified(true);
        }
    }
    //</editor-fold>
}
