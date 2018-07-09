/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config;

import java.util.Vector;

/**
 *
 * @author leng
 */
public class Block {
    public String parameterName=null;
    public Vector<String> values = new Vector<String>();

    public Block(String _parameterName){
        if(( _parameterName==null)||(_parameterName.trim().length()<1))
            return;
        
        this.parameterName = _parameterName;
    }

    
    public Block(String _parameterName, String _value){
        if(( _parameterName==null)||(_parameterName.trim().length()<1))
            return;

        this.parameterName = _parameterName;
        values.add(_value);
    }

    public void addValue(String _value){

        if(_value==null)
            return;

        if(_value.trim().length()<1)
            return;

        values.add(_value.trim());
    }
}
