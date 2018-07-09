/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.save;

import java.io.File;

/**
 *
 * @author Chris
 */
public class Save {
    protected userInterface.GUI gui;

    public Save(userInterface.GUI gui){
        this.gui = gui;
        
    }

    public Save(){}

    /**Enter point to save all annotations and their attributes into XML or Pins
     files.*/
    public void savechanges(){
        gui.setEnabled(false);
        SaveDialog savedialog = new SaveDialog(gui);
        savedialog.setVisible(true);
    }


    /**save function: save annotaitons from memory to xmls under the "saved"
     * folder in your current project.
     * The difference between "save" and "save as" is that user are allowed to
     * save annotations belongs to designated files and assigned directionary.
     */ 
    public String quickXMLSaving(){
        try{
            // ##1## get original imported xml files w/ path
            // names of all imported xml files saved in env.parameters.xmlimportingcorrelated.xmls
            // XMLS is a static accessable vector, item type is "FILE"
            //Vector<File> xmls = env.Parameters.AnnotationsImportingCorrelated.getXMLList();
            int total_document = env.Parameters.corpus.getSize();
            if (total_document<1)
                return "No XML files in list for processing!";
            

            
            // ##2## start save
            for(int i=0;i<total_document;i++)
            {
                File txtfile = env.Parameters.corpus.getFile(i);
                if(txtfile==null)
                    continue;
                

                OutputToXML toxml = new OutputToXML();
                toxml.directsave(txtfile);

            }

            // return "Annotations were saved into XML("+ total_document +") under your current project folder successfully!";
            return "Annotations are saved in " + total_document + " XMLs under "  ;

        }catch(Exception ex){
            System.out.println("error 1101022114:: fail to save");
            return "Error happened while saving annotations back to XML files!!!";
        }
        // ##3## end
    }
    
}
