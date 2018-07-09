/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thirdpartyOutput;

/**
 *
 * @author leng
 */
public class Demo {


    String outputPath = "/Users/leng";

    public void start(){

        OutputXMLs XML = new OutputXMLs();

        // indicate the file name of input clinical report
        XML.setSourceFilename("test.txt"); // file name of the text source, not absolute file name
        // set output path
        XML.setOutputPath(outputPath);

        // add an annotation node
        int id = XML.addAnnotation("fever", "Problem", 5, 19);

        // add slots for this annotation by its id
        XML.addSlots(id, "temporality", "current");
        XML.addSlots(id, "negation", "possible");

        // final step: physical write XML to the disk
        XML.WriteToDisk();
    }
    
}
