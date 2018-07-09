/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.simpleRelationship;

/**
 *
 * @author Chris
 */
public class sTableData {
    public String type;
    public String typevalue;
    public String attributeName;
    public String attributeValue;

    /**Constructor*/
    public sTableData(){
    }

    /**Constructor*/
    public sTableData(String type, String typevalue, String attributeName, String attributeValue ){
        this.type = type;
        this.typevalue = typevalue;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
}
