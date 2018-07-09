/**
 * The contents of this file are subject to the GNU GPL v3 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at URL http://www.gnu.org/licenses/gpl.html
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is eHOST alpha.
 *
 * The Initial Developer of the Original Code is University of Utah. Copyright
 * (C) 2009 - 2012. All Rights Reserved.
 *
 * eHOST was developed by the Division of Epidemiology at the University of
 * Utah. Current information about eHOST can be located at
 * http://code.google.com/p/ehost/
 *
 * Contributor(s): Jianwei "Chris" Leng <Chris.Leng@utah.edu> (Original Author)
 *
 */
package adjudication.parameters;

import java.util.ArrayList;

/**
 * Class that is used to store information for adjudication mode, such as the
 * list of selected annotators, the list of selected classes, etc. It use static
 * variables to record these parameters.
 *
 * Created on April 23, 2012
 *
 * @version 1.0
 * @author Jianwei "Chris" Leng
 * @since JDK 1.6
 */
public class Paras {

    /**
     * Flag that used to indicated whether current project has been adjudicated
     * or not. It's for the whole project and it has following rules: 1) true
     * after loading saved adjudication 2) true after starting a new difference
     * analysis
     *
     */
    public static boolean __adjudicated = false;
    /**
     * A list of names of user selected classes. Selected classes, selected via
     * the dialog of "adjudication mode", can be changed by adjudication
     * annotation loading/reloading process.
     *
     * It's private, only can be accessed by method in same class.
     */
    private static ArrayList<String> __selectedClasses = new ArrayList<String>();
    /**
     * A list of names of user selected annotators. It's private, only can be
     * accessed by method in same class.
     */
    private static ArrayList<String> __selectedAnnotators = new ArrayList<String>();
    /**
     * static boolean variables that used as a flag to indicate what kind of
     * annotation is matches.
     */
    public static boolean CHECK_OVERLAPPED_SPANS, CHECK_ATTRIBUTES,
            CHECK_RELATIONSHIP, CHECK_CLASS, CHECK_COMMENT;

    /**
     * remove all recorded names of classes from list
     * <code>selectedClasses</code>
     */
    public static void removeClasses() {
        __selectedClasses.clear();
    }

    /**
     * remove all recorded names of classes and names of annotators.
     */
    public static void removeAll() {
        __selectedClasses.clear();
        __selectedAnnotators.clear();
        __adjudicated = false;
        
        
    }
    
    /**reset parameters to default values, these parameter are use to tell us
     * which fields of an annotation need to be compared.
     */
    public static void removeParas(){
        CHECK_OVERLAPPED_SPANS = false;
        CHECK_ATTRIBUTES = false;
        CHECK_RELATIONSHIP = false; 
        CHECK_CLASS = false; 
        CHECK_COMMENT = false;
    }
    /**Set the parameters based on the given parameter name and value. There are 
     * 5 static boolean variables are defined in this class to indicate
     * whether a specific part of annotation need to be compared in the 
     * adjudication work or not.
     */
    public static void setAnnotationCheckingParameters( String paraName, String value ){
        if ( ( paraName == null ) || ( value == null ) ){
            return;
        }
        
        if( value.trim().length() < 1 )
            return;
        
        boolean booleanvalue = value.trim().toLowerCase().compareTo( "true" ) == 0 ? true : false;
        
            
        if( paraName.trim().toUpperCase().compareTo( "CHECK_OVERLAPPED_SPANS" ) == 0 ){
            CHECK_OVERLAPPED_SPANS = booleanvalue;
        }else if( paraName.trim().toUpperCase().compareTo( "CHECK_ATTRIBUTES" ) == 0 ){
            CHECK_ATTRIBUTES = booleanvalue;
        }else if( paraName.trim().toUpperCase().compareTo( "CHECK_RELATIONSHIP" ) == 0 ){
            CHECK_RELATIONSHIP = booleanvalue;
        }else if( paraName.trim().toUpperCase().compareTo( "CHECK_CLASS" ) == 0 ){
            CHECK_CLASS = booleanvalue;
        }else if( paraName.trim().toUpperCase().compareTo( "CHECK_COMMENT" ) == 0 ){
            CHECK_COMMENT = booleanvalue;
        }
    }

    /**
     * Set the selected annotators as the given set.
     *
     * @param _annotators The given set of names of annotators
     */
    public static void setAnnotators(ArrayList<String> _annotators) {
        removeAnnotators();

        if (_annotators == null) {
            return;
        }

        if (_annotators.size() < 1) {
            return;
        }

        // copy annotator names from the given set
        for (String annotatorname : _annotators) {
            if ((annotatorname == null) || (annotatorname.trim().length() < 1)) {
                continue;
            }

            // record the annotator name
            Paras.addAnnotator(annotatorname);
        }
    }

    /**
     * Set the selected classes as the given set.
     *
     * @param _classes The given set of names of classes
     */
    public static void setClasses(ArrayList<String> _classes) {

        removeClasses();

        if (_classes == null) {
            return;
        }

        if (_classes.size() < 1) {
            return;
        }

        // copy annotator names from the given set
        for (String classname : _classes) {
            if ((classname == null) || (classname.trim().length() < 1)) {
                continue;
            }

            // record the annotator name
            Paras.addClass(classname);
        }
    }

    /**
     * Return all selected annotators.
     *
     * @return names of all selected annotators in a list of string.
     */
    public static ArrayList<String> getAnnotators() {
        return Paras.__selectedAnnotators;
    }

    /**
     * Get the designated annotator.
     *
     * @param _index The index position of the annotator.
     * @return the name of the designated annotator.
     */
    public static String getAnnotatorAt(int _index) {
        if (__selectedAnnotators == null) {
            return null;
        }
        int size = __selectedAnnotators.size();
        if ((_index < 0) || (_index > size - 1)) {
            return null;
        } else {
            return __selectedAnnotators.get(_index);
        }
    }

    /**
     * Return all selected classes..
     *
     * @return names of all selected classes in a list of string.
     */
    public static ArrayList<String> getClasses() {
        return Paras.__selectedClasses;
    }

    /**
     * remove all recorded names of classes from list
     * <code>selectedClasses</code>
     */
    public static void removeAnnotators() {
        __selectedAnnotators.clear();
    }

    /**
     * add a class into recorded list of selected classes.
     *
     * @param _classname the name of the class that we want to add into the
     * list.
     */
    public static void addClass(String _classname) {
        if (_classname == null) {
            return;
        }

        if (_classname.trim().length() < 1) {
            return;
        }

        if (classExists(_classname)) {
            return;
        }

        __selectedClasses.add(_classname.trim());
    }

    /**
     * add a class into recorded list of selected classes.
     *
     * @param _classname the name of the class that we want to add into the
     * list.
     */
    public static void addAnnotator(String _annotator) {
        if (_annotator == null) {
            return;
        }

        if (_annotator.trim().length() < 1) {
            return;
        }

        if (annotatorExists(_annotator)) {
            return;
        }

        __selectedAnnotators.add(_annotator.trim());
    }

    /**
     * Check whether a class has been recorded yet.
     *
     * @param _classname The name of the class that we want to check it's saved
     * or not.
     *
     * @return true if it's saved, or false for otherwise.
     *
     */
    public static boolean classExists(String _classname) {
        if (__selectedClasses == null) {
            return false;
        }

        if (__selectedClasses.size() < 1) {
            return false;
        }

        for (String classname : __selectedClasses) {
            if (classname == null) {
                continue;
            }
            if (classname.trim().length() < 1) {
                continue;
            }

            if (classname.trim().compareTo(_classname.trim()) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether an annotator has been recorded here yet or not.
     *
     * @param _annotatorname The name of the annotator that we want to check
     * it's saved or not.
     * @return true if it's saved, or false for otherwise.
     */
    public static boolean annotatorExists(String _annotatorname) {
        if (__selectedAnnotators == null) {
            return false;
        }

        if (__selectedAnnotators.size() < 1) {
            return false;
        }

        for (String annotatorname : __selectedAnnotators) {
            if (annotatorname == null) {
                continue;
            }
            if (annotatorname.trim().length() < 1) {
                continue;
            }

            if (annotatorname.trim().compareTo(_annotatorname.trim()) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether do we have enough data to enter the adjudication mode
     */
    // public static boolean (){
    //
    // }
    /**
     * Count number of valid annotators in the list of selected annotators.
     *
     * @return number of valid annotators in the list of selected annotators.
     */
    public static int size_of_validAnnotators() {
        return valids(Paras.__selectedAnnotators);
    }

    /**
     * Count number of valid classes in the list of selected classes.
     *
     * @return number of valid classes in the list of selected classes.
     */
    public static int size_of_validClasses() {
        return valids(Paras.__selectedClasses);
    }

    /**
     * count number of valid items in the given list.
     *
     * @return the number of valid items in the given list.
     */
    public static int valids(ArrayList<String> _list) {
        int count = 0;

        if (_list != null) {
            for (String item : _list) {
                if (item == null) {
                    continue;
                }
                if (item.trim().length() < 1) {
                    continue;
                }

                count++;

            }
        }

        return count;
    }
    

    /**check selected annotators, make sure there are at least 2 annotators are 
     * selected and the first one is not null.
     * 
     * @return  true    if it didn't find any problem.
     *          false   if number of annotators are lease then 2, or the first one
     *                  if null.
     */
    public static boolean isReadyForAdjudication() {

        if (Paras.getAnnotators().size() < 2) {
            return false;
        }

        if (Paras.getAnnotatorAt(0) == null) {
            return false;
        }

        if (Paras.getAnnotatorAt(0).trim().length() < 1) {
            return false;
        }

        return true;
    }
}
