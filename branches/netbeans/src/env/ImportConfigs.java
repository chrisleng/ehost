/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env;

/**
 *
 * @author Kyle
 */
public class ImportConfigs {
    private boolean iRels;
    private boolean iAtts;
    private boolean schemaH;

    public ImportConfigs( boolean rels, boolean atts, boolean schemaHandler)
    {
        iRels = rels;
        iAtts = atts;
        schemaH = schemaHandler;
    }

    /**
     * @return the iRels
     */
    public boolean isiRels()
    {
        return iRels;
    }

    /**
     * @return the iAtts
     */
    public boolean isiAtts()
    {
        return iAtts;
    }

    /**
     * @return the schemaH
     */
    public boolean isSchemaH()
    {
        return schemaH;
    }

}
