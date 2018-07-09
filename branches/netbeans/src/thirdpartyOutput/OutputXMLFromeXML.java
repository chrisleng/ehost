/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thirdpartyOutput;

import imports.importedXML.eXMLFile;
import imports.importedXML.eAnnotationNode;
import imports.importedXML.eStringSlotMention;
import java.io.File;
import java.io.FileOutputStream;
import org.jdom.*;
import org.jdom.output.*;

/**
 *
 * @author Kyle
 */
public class OutputXMLFromeXML
{
    eXMLFile source;
    File output;
    public OutputXMLFromeXML(eXMLFile file, File outputDirectory)
    {
        source = file;
        output = outputDirectory;
    }
    public void writeFile()
    {
        // check: output path/filename
        String outputFilename = output.getAbsolutePath();
        if (outputFilename == null || !output.isDirectory()){
            System.out.println("Error: outputXML.java - didn't indicate original filename!!!");
            return;
        }

        // output annotation
        System.out.println("XML output path: " + outputFilename);


        // XML Assembly and Storage
        try{
                // initial the XML file and set the root node of the XML
                //System.out.println("annotationsStr is " + annotationsStr);
                Element root = new Element( "annotations" ) {};
                //System.out.println("TextSource is " + TextSource);
                root.setAttribute( "textSource", source.filename );
                Document Doc = new Document(root);

                /** output xml file to disk */
                // XML storage processing: phycial writing
                Format format = Format.getCompactFormat();
                format.setEncoding("UTF-8"); // set XML encodeing
                format.setIndent("    ");
                XMLOutputter XMLOut = new XMLOutputter(format);

                for(eAnnotationNode annotation : source.annotations){
                    /** add contents */
                    //root = BuildXMLNodesForAnnotations(annotation);
                }
                for(eStringSlotMention mention : source.stringSlotMentions)
                {

                }

                // write to disk
                XMLOut.output(Doc, new FileOutputStream( outputFilename ));

        }catch(Exception e){
            System.out.println( e.toString() );
        }

    }



}
