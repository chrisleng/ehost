/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class Extracter {

    private Vector<Block> blocks = new Vector();

    public Extracter(File configurefile){
        extractFromConfigureFile(configurefile);
    }



    public Extracter(String configurefile){

        if(configurefile==null)
            return;

        if(configurefile.trim().length()<1)
            return;

        try{
            File f = new File(configurefile);
            
            if(f.exists())
            {
                
                if(f.isFile())
                {
                    extractFromConfigureFile(f);
                }
                else
                {
                }

            }
            else
            {

            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1102091904::" + ex.toString());
            
        }
    }


    private void extractFromConfigureFile(File configurefile){

        blocks.clear();

        // ##1## cut text file into sveral different blocks, each block contains
        // the parameter's name in the first line and others are all values to
        // this paramter name;
        blocks = getblock(configurefile);

       
    }

    public Vector<Block> getBlocks()
    {
        return this.blocks;
    }

    


    /**To a given file name, if it is a text file, this method will cut text
     * file into sveral different blocks, each block contains the parameter's
     * name in the first line and others are all values to this paramter name
     *
     * Note: here we set a litmit to each block is that no more than 1000 values
     *       belong to one parameter;
     */
    private Vector<Block> getblock(File _textfile)
    {
        Vector<Block> thisblocks = new Vector<Block>();
        
        if( _textfile==null)
            return null;

        if(!_textfile.exists())
            return null;

        if(!_textfile.isFile() )
            return null;

        try{
            
            BufferedReader iniFile = new BufferedReader(new FileReader(_textfile));
            String line = iniFile.readLine();

            while (line != null)
            {
                // ignore comment
                if(isComment(line)){
                    line= iniFile.readLine();
                    continue;
                }

                // if this is a start of block
                if(isParameterName(line))
                {
                    Block block = new Block(this.getParameterName(line));

                    do{
                        // read next line
                        line = iniFile.readLine();

                        // if is empty, stop current contructing current block
                        if((line==null)||(line.trim().length()<1)||(this.isParameterName(line)))
                        {                                                        
                            break;
                        }
                        
                        block.addValue( line.trim() );

                    }while(line!=null);

                    // record this if this block is valid
                    if(block.parameterName!=null)
                    {
                        thisblocks.add(block);
                    }

                }
                else
                {
                    // go to next line
                    line = iniFile.readLine();
                }

            }

            iniFile.close();
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1102082354:: fail to split configure file into blocks"
                    + ex.toString() );
        }

        return thisblocks;
    }


    /**report whether this line is a comment which have two sympol of '/' in
     * the front of this line. */
    public boolean isComment(String _line){
        if(_line==null)
            return false;

        if(_line.trim().length()<2)
            return false;

        if((_line.charAt(0)=='/')&&(_line.charAt(1)=='/'))
            return true;
        
        return false;
    }

    
    /**Verify a given line to make sure whether it's a paramater name which
     * surround with '[' and ']'.
     */
    public boolean isParameterName(String _line){
        
        // NULL input
        if(_line==null)
            return false;

        if(this.getParameterName(_line)!=null)
            return true;

        return false;
    }

    
    /**Get the parameter name from a given line if have. The paramater name
     * should be surround with '[' and ']'.
     */
    public String getParameterName(String _line){

        // NULL input
        if(_line==null)
            return null;

        try{
            // empty line or length of line smaller than 3
            if(_line.trim().length()<3)
                return null;

            _line = _line.trim();
            char[] line = _line.toCharArray();

            if(line==null)
                return null;

            int size = line.length;

            // if first character is '[' and last character is ']', and length
            // is more than 3, this one is a valid parameter name;
            if((line[0]=='[')&&(line[size-1]==']'))
            {
                String para = _line.trim().substring(1, size-1);
                //System.out.println(para);
                return para;
            }


        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1102081946:: fail to check the validity of " +
                    "string to current line\n"
                    + "current line=["+_line+"]\n"
                    + ex.toString() );
        }

        return null;
    }

    
}
