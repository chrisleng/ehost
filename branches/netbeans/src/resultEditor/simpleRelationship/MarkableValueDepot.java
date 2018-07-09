/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.simpleRelationship;

/**
 *
 * @author Chris
 */
public class MarkableValueDepot {
    private static String[] values = new String[]{"uncertainty","certainty"};

    public String[] getValues( String currentValue ) {
        if ( ( currentValue == null )||( currentValue.trim().length() < 1)){
            return values;
        } else {
            if ( isExisted(currentValue) ) {
                return createArrayString_word1st(values, currentValue);
            } else {
                return createArrayString( values, currentValue  );
            }
        }
    }

    public String[] getValues( Object currentValue ) {

        if ( currentValue == null )
            return values;
        else {
            if( currentValue instanceof String)
                return getValues( currentValue.toString() );
            else
                return values;
            /*
            String s = null;
            try{
                s = currentValue.toString();
            }catch(Exception e){

            }
            if (s == null)
                return values;
            else
                return getValues(s);
            */
        }
    }
    
    /**
     * Generate a new array of string by a given array of string and a string. 
     * Be sure the word will be located in the first slot of the new array.
     */
    private String[] createArrayString(String[] arraystr, String word){
        int oldarray_length = arraystr.length;
        String[] newarray = new String[oldarray_length+1];
        newarray[0] = word;
        for(int i=1; i< oldarray_length + 1; i++){
            newarray[i] = arraystr[i-1];
        }
        values = newarray;
        return newarray;
    }

    private String[] createArrayString_word1st(String[] arraystr, String word){
        int position = -1;
        for( int i=0; i<arraystr.length;i++ ){
            if ( arraystr[i] == null )
                continue;
            if ( arraystr[i].trim().equals( word.trim() ) ){
                position = i;
            }
        }
        if( position != -1 ){
            String tmp = arraystr[0];
            arraystr[0] = word;
            arraystr[position] = tmp;

            return arraystr;

        }else
            return arraystr;
    }

    /**check existing of a given string in the memory.*/
    private boolean isExisted(String value){
        if( values == null )
            return false;
        if( values.length < 1 )
            return false;
        for(int i=0; i< values.length; i++){
            String thisvalue = values[i];
            if ( thisvalue == null )
                continue;
            if( thisvalue.trim().equals(value.trim()) )
                return true;
        }

        return false;
    }
}
