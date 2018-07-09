/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

/**
 * Cut PINs file into block with surrounded by '(' and ')'  and blanklines
 * @author Jianwei Leng 2010-06-18
 */
public class BlockReader {
    /**pins file*/
    protected File file;
    /**all block content*/
    protected Vector<String> BLOCKs = new Vector<String>();

    /**cursor in the vector of BLOCKs*/
    protected int position = 0;

    /**constructor*/
    public BlockReader(File file){
        this.file = file;
        BLOCKs.clear();

        split(file);
    }

    /**add a new string into the list as a block string*/
    private void add(String newBlock){
        BLOCKs.add(" " + newBlock + " ");
    }

    /**to a test file, split it into blocks. each blocks should be surrounded by
     * '(' and ')', and lines before and after this line should be blank line.
     *
     * #1 find first '(' in the file
     *
     */
    private void split(File file){

        // validity check
        if(file == null)
            return;
        if(!file.exists())
            return;
        if(file.isDirectory())
            return;
        
        // open file and split it into block which surrounded with '(' and ')'.
        try {
            //FileReader fileReader = new FileReader(file);
            BufferedReader bufferreader = new BufferedReader(new FileReader(file));
            String line = bufferreader.readLine();
            while(line!=null){                
                // avoid blank lines
                if(line.length() == 0){
                    line = bufferreader.readLine();
                    continue;
                }

                line = line.trim();
                // avoid comments
                char[] c = line.toCharArray();
                if (c!=null){
                    if(c[0]==';'){
                        line = bufferreader.readLine();
                        continue;
                    }
                }

                // #1 find first '(' in the file
                if(!line.contains("(")){
                    line = bufferreader.readLine();
                    continue;
                }
                
                // From now on, include this '(', each found '(' means count++;
                // and each found ")" mean count--;
                // This search will be stopped while count equal to 0. This
                // means you found latest matched ')' to your first '('.
                int count = 1;

                // #1.1 store this line which start with a '('
                int iposition = line.indexOf("(");
                String str = line.substring(iposition+1, line.length());
                count = count + countBrackets(str);
                
                // if end in same line
                if(count==0){
                    iposition = str.lastIndexOf(")");
                    str = str.substring(0, iposition);
                    add(str);

                    line = bufferreader.readLine();
                    continue;
                }else{

                    // after found first bracket '(', we need to find matched
                    // ')'
                    // quit if meet blank line
                    do{

                        line = bufferreader.readLine();

                        // if reach the end of this file
                        if(line == null)
                            break;
                        // if meet blank line
                        if(line.length() == 0 ){
                            continue;
                        }


                        // check whether the cursor reached the matched bracket
                        count = count + countBrackets(line);                        
                        if(count==0){

                            //System.out.println("\n"+line);
                            iposition = line.lastIndexOf(")");
                            //System.out.println("position = " + position );
                            str = str + " " + line.substring(0, iposition);
                            add(str);

                        }else{
                            str = str + " " + line;                            
                        }

                    }while( ( count != 0)&&(line != null) );
                }

                // go next line for next block start with "("
                line = bufferreader.readLine();
            }
        } catch (Exception ex) {
            logs.ShowLogs.printLog(ex.toString());
        }

    }

    /**In a given string, a found character of '(' means number++ and
     * each found ")" mean count--
     * @return  count of '(' and ')'.
     */
    private int countBrackets(String givenString){
        //String  = passedIn;
        int count = 0;
        if(givenString == null)
            return 0;
        if(givenString.contains("\"")) givenString = givenString.replaceAll("\"(.*)\"", " ");
        // validity check: blank line
        if(givenString.length() == 0)
            return 0;

        // go over all characters in this string and check for '(' and ')'
        char[] c = givenString.toCharArray();
        int size = c.length;
        for(int i=0;i<size;i++) {
            if ( c[i] == '(' ) count++;
            if ( c[i] == ')' ) count--;
        }
        
        return count;
    }

    /**How many text blocks, in PINs format, found in given file. */
    public int size(){
        if (this.BLOCKs == null)    return 0;
        else return this.BLOCKs.size();
    }

    /**get content of first text block in this file.*/
    public String first(){
        if (size() <= 0 )
            return null;
        else {
            this.position = 0;
            return BLOCKs.get(0);

        }
    }
    
    /**Check whether it have next text block for this file.*/
    public boolean hasNext(){
        int size = size();
        if(( position>=(size-1) )||(position < 0))
            return false;
        else
            return true;
    }

    /**Get content of next text block which is next to current cursor.*/
    public String getNext(){
        int size = size();
        int cursor = this.position + 1;
        if(( cursor>(size-1) )||(cursor < 0))
            return null;
        else {
            this.position = cursor;
            return BLOCKs.get(cursor);
        }
    }

}
