package resultEditor.annotator;

import javax.swing.JLabel;

/**
 * This class helps recording and managing current annotator and the possible 
 * login information.
 * 
 * @author Jianwei Leng
 */
public class Manager {

    /**label component on eHOST GUI which used to show current annotator's
     * name on screen*/
    protected JLabel annotatorDisplayLabel = null;
    public final static String backup_annotator_name = "Extensible_Human_Oracle_Suite_of_Tools";
    public final static String backup_annotator_id = "eHOST_2010";
    private static String output_annotator_name;
    private static String output_annotator_id;
    
    

    /**class construct*/
    public Manager(JLabel annotatorDisplayLabel ){
        this.annotatorDisplayLabel = annotatorDisplayLabel;
    }

    /**get a string of annotator name only for output/create/modify annotations.*/
    public static String getAnnotatorName_OutputOnly(){
        try{
            String name = resultEditor.workSpace.WorkSet.current_annotator_name;

            if((name!=null)&&(name.trim().length()>0))
                output_annotator_name = name.trim();
            else output_annotator_name = backup_annotator_name;

            return output_annotator_name;
        }catch(Exception ex){

        }

        return backup_annotator_name;

    }
    
    /**get a string of annotator id only for output/create/modify annotations.*/
    public static String getAnnotatorID_outputOnly(){
        try{
            String id = resultEditor.workSpace.WorkSet.current_annotator_id;

            if((id!=null)&&(id.trim().length()>0))
                output_annotator_id = id.trim();
            else output_annotator_id = backup_annotator_id;

            return output_annotator_id;
        }catch(Exception ex){

        }

        return backup_annotator_id;
    }
    /**
     * Set current annotator.
     *
     * @param   annotatorName
     *          The name of current annotator. It will be assignated to new
     *          annotations.
     */
    public static void setCurrentAnnotator(String annotatorName){
        try{
            if (annotatorName == null)
                return;
            if (annotatorName.trim().length() < 1)
                return;

            // record current annotator name in memory for future use
            resultEditor.workSpace.WorkSet.current_annotator_name = annotatorName.trim();

        }catch(Exception ex){
            
        }
    }

    /**
     * Set current annotator ID.
     *
     * @param   annotatorID
     *          The ID of current annotator. It will be assignated to new
     *          annotations.
     */
    public static void setCurrentAnnotatorID(String annotatorID){
        try{
            if (annotatorID == null)
                return;
            if (annotatorID.trim().length() < 1)
                return;

            // record current annotator name in memory for future use
            //resultEditor.workSpace.WorkSet.current_annotator_id = annotatorID.trim();

        }catch(Exception ex){

        }
    }
    

    /**Get current annotator's name*/
    public static String getCurrentAnnotator(){
        try{
            String annotatorname = resultEditor.workSpace.WorkSet.current_annotator_name.trim();
            return annotatorname;
        }catch(Exception ex){
        }

        return null;
    }

    /**Get current annotator's id*/
    public static String getCurrentAnnotatorID(){
        try{
            String annotatorid = resultEditor.workSpace.WorkSet.current_annotator_id.trim();
            return annotatorid;
        }catch(Exception ex){
        }

        return null;
    }

    /**Update current annotator's name to screen.*/
    public void showCurrentAnnotator(){
        try{
            String annotator_name = resultEditor.workSpace.WorkSet.current_annotator_name;
            if( annotator_name == null) {
                setAnnotatorDisplay("        ");
                return;
            }else if( annotator_name.trim().length() < 1 ){
                setAnnotatorDisplay("        ");
                return;
            }

            setAnnotatorDisplay( annotator_name + " " );

        }catch(Exception ex){

        }
    }

    /**Show text on label component of current annotator on screen of eHOST.*/
    private void setAnnotatorDisplay(String text){
        try{
            if (this.annotatorDisplayLabel == null)
                return;
            this.annotatorDisplayLabel.setText(text);
        }catch(Exception ex){

        }
    }
}
