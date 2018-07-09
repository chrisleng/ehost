package resultEditor.relationship.attributes;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Data format that used to keep information for 
 * 
 * @author chris 2012-06-15
 */
public class Row {
    
    /**name of the attribute*/
    public String attributename;
    
    /**current value of this attribute*/
    public String currentValue;
    
    /**option value of this attribute*/
    public Vector<String> allowValues = new Vector<String>();
}
