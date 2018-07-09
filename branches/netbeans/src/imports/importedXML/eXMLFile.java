/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imports.importedXML;

import imports.importedXML.eAnnotationNode;
import imports.importedXML.eStringSlotMention;
import imports.importedXML.eComplexSlotMention;
import imports.importedXML.eClassMention;
import java.util.Vector;

/**
 * @author Jianwei Leng  2010-05-16, Sun
 */
public class eXMLFile
{

    /**the file name, in format ###.###, not absolutely filename*/
    public String filename;
    /**the absolute filename of this imported xml file,
     * use to write back information
     */
    public String absoluteFilename;
    // ArrayList which contains all information of
    // element "annotations"  of current file
    public Vector<eAnnotationNode> annotations = new Vector<eAnnotationNode>();
    // ArrayList which contains all information of element
    // "classMention" of current file
    public Vector<eClassMention> classMentions = new Vector<eClassMention>();
    // stringslotmention
    public Vector<eStringSlotMention> stringSlotMentions = new Vector<eStringSlotMention>();
    /**sub branch of complex slot mention to a XML annotation file. */
    public Vector<eComplexSlotMention> complexSlotMentions = new Vector<eComplexSlotMention> ();

    public eXMLFile(){};

    public eXMLFile(String path, String fileName) {
        String separator = "";
        if (env.Parameters.isUnixOS)        
            separator = "/";        
        else        
            separator = "\\";        
        this.filename = fileName;
        this.absoluteFilename = path + separator + fileName;
    }
}
