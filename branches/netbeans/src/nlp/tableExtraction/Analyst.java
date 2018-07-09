/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.tableExtraction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import userInterface.GUI;

/**
 *
 * @author imed
 */
public class Analyst {
    
    private File textfile;
    
    private GUI gui;
    
    
    public Analyst(File textfile, GUI gui){
        this.textfile = textfile;
        this.gui = gui;
    }
    
    public void analyse(){
        
        // make sure it's not null, not a folder, etc
        if( fileIsEvil( textfile ) ){
            return;
        }
        
        // get contents
        System.out.println("\n==================================================================");
        System.out.println("we are dealing with file:[" + textfile.getAbsolutePath() + "]");
        System.out.println("                    file size = " + textfile.length() );
        
        // 一行行读取文本内容，解析成block
        ArrayList<Block> blocks = getBlocks( textfile );        
        
        // use regular expression to search for specific terms 
        blocks = nlpExtraction( blocks );
        // echo found details after classification
        echo( gui, blocks );
       
        
        blocks = removeRepetive( blocks );
        
        // sort all found terms by their spans
        blocks = sort( blocks );
        
        blocks = classify( blocks );
        
        extraction( blocks );
                
    }
    
    public void preprocess(){
        
        // make sure it's not null, not a folder, etc
        if( fileIsEvil( textfile ) ){
            return;
        }
        
        // get contents
        System.out.println("\n==================================================================");
        System.out.println("we are dealing with file:[" + textfile.getAbsolutePath() + "]");
        System.out.println("                    file size = " + textfile.length() );
        
        // 一行行读取文本内容，解析成block
        ArrayList<Block> blocks = getBlocks( textfile );                                
        // preprocess
        preProcess( blocks );
    }

    /**this file can't be null, or a folder, etc*/
    private boolean fileIsEvil(File textfile) {
        if( textfile == null )
            return true;
        
        if( textfile.isDirectory() )
            return true;                
        
        return false;
    }
    
    /** 一行行读取文本内容，解析成block*/
    private ArrayList<Block> getBlocks( File textfile ){
        ArrayList<Block> blocks = new ArrayList<Block>();
        
        try{
                // load contents from document
                BufferedReader f = new BufferedReader( new FileReader( textfile ));
                String line = f.readLine();

                int length_of_processedDoc = 0;
                int index = 0;
                int count = 0;
                
                Block block = new Block(textfile);
                
                
                boolean thisboard = false; // 发现这个block的最后一行
                boolean nextfirstline = false; // findxiang下一个非separatorline
                
                while( line != null ){
                     
                    // 如果block是空的： 第一行，或任何block第一行
                    if( block.blockcontents.size() < 1 ){
                        block.blockStart = length_of_processedDoc;
                        block.index = index;
                    }
                    
                    if( isSeperatorLine( line ) >0.99f ){ // 如果是分割
                        thisboard = true; // 发现至少一个分割
                        
                        //打印分割线
                        count++;
                        System.out.println( count + line );
                    }else{
                        nextfirstline =  true;
                    }
                    
                    if( nextfirstline && thisboard ){
                        
                        block.blockEnd = length_of_processedDoc;
                        
                        blocks.add(block);
                        block = new Block(textfile);
                                                                        
                        nextfirstline = false;
                        thisboard = false;
                        index++;
                        
                        block.blockStart = length_of_processedDoc + 1;
                        block.index = index;
                        block.blockcontents.add( line );   
                        
                        length_of_processedDoc = length_of_processedDoc + line.length() + 1;
                        
                        line = f.readLine();
                        continue;
                    }
                    else
                        block.blockcontents.add( line );
                    
                    // 记录已经处理了的文件的长度
                    length_of_processedDoc = length_of_processedDoc + line.length() + 1;
                    
                    // 去下一行
                    line = f.readLine();
                    
                    if( line == null ){
                        block.blockEnd = length_of_processedDoc;
                        blocks.add( block );
                    }
                }



                // close opened file
                f.close();
                
        }catch(Exception ex){
            //ex.printStackTrace();
        }
        
        return blocks;
    }
    
    /**所有可能的block和block之间的分隔符*/
    public final char[] lineSeperatorChars = {'*', '-', '=', '?'};
    
    /**The rate that how much a line of string is like a line separator; it's 
     * separator line while it equals to 1.0f, and it's absolutely not while
     * it equals to 0.0f.
     */
    public float isSeperatorLine(String line){
        float matchrate = 0.0f;
        
        // blank line
        if( line.trim().length() < 1 )
            return 0.0f;
        
        line = line.trim();
        
        int length = line.length();
        
        for(char separatorchar : lineSeperatorChars){
            int count = countChar( line, separatorchar );
            if( count < 10 )
                continue;
            
            if( ((float)count/length) > 0.95f )
                return ((float)count/length);
        }
        
        
        return matchrate;       
    }
    
    private int countChar(String line, char separator){
        char[] chars = line.toCharArray();
        int count=0;
        for(int i=0;i<chars.length;i++){
            if( separator == chars[i] )
                count++;
        }
        return count;
    }

    /**identify the PFT table type of all blocks.*/
    private ArrayList<Block> nlpExtraction(ArrayList<Block> blocks) {
        if( blocks == null )
            return blocks;
        
        for( int i=0; i< blocks.size(); i++  ){
            Block block = blocks.get(i);
            block = nlpExtraction( block );
            blocks.set(i, block);
        }
        
        return blocks;
    }
    
    /**identify the PFT table type just for one block*/
    private Block nlpExtraction(Block block) {
        if( block == null )
            return block;
    
        // put all lines into one line
        String content = null;
        for( String line : block.blockcontents ){
            if( content ==null )
                content = line;
            content = content + line;
        }
        
        Matcher matcher;
         boolean match_found;
         
         int processed = 0;
        for( String line : block.blockcontents ){
            
            if( line == null )
                continue;
            
            //System.out.println("\n     [" + line + "]" );
            if(RegexLib.regexlib_identification==null)
                continue;
            
            for( int i=0; i< RegexLib.regexlib_identification.length; i++){
                String[] row = RegexLib.regexlib_identification[i];
                String regex = row[0];
                String type = row[1];
                
                matcher = Pattern.compile( regex, Pattern.CASE_INSENSITIVE ).matcher( line );
                match_found = matcher.find();

                if(match_found){
                    int start = matcher.start();
                    int end = matcher.end();
                    String foundstr = line.substring(start, end);    
                    //System.out.println("\n     [" + line + "]" );
                    //System.out.println("  --- " + foundstr );
                  
                    Light light = new Light();
                    light.start = processed + start - 1;
                    light.end = processed + end - 1;
                    light.line = foundstr;
                    light.variablename = type;
                    
                    block.lights.add(light);
                    
                    match_found = matcher.find();
                }
                
            }
            
            processed = processed + line.length() + 1;
            
            
        }
        
           
        return block;
    }
    
    
    

    private int assignMeAUniqueIndex(){
         return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }
    
    /**把发现的light都显示到屏幕上*/
    private void echo(GUI gui, ArrayList<Block> blocks) {
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        for( Block block : blocks ){
            String filename = block.file.getName();
            if( block.lights == null )
                continue;
            for( Light light : block.lights ){
                depot.addANewAnnotation( filename, 
                        light.line, 
                        light.start + block.blockStart, 
                        light.end + block.blockStart, 
                        commons.Tools.getTimeStamper(),
                        light.variablename, 
                        "NLP.SaucerGroup", "NLP.0001", null, null, assignMeAUniqueIndex());
                
                /*System.out.println( filename + 
                        light.line +   
                        light.start + block.blockStart + 
                        light.end + block.blockStart + 
                        commons.Tools.getTimeStamper() +
                        light.variablename +
                        "chris.leng" + "chris.leng.0000" + assignMeAUniqueIndex());                        
                        */
            }
        }
        
        
        
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.display_repaintHighlighter();        
        gui.showValidPositionIndicators();
   
    }

    /**Classify blocks based previous found terms.*/
    private ArrayList<Block> classify(ArrayList<Block> blocks) {
        if( blocks == null )
            return blocks;
        
        for( int i=0; i<blocks.size(); i++ ){
            Block block = blocks.get(i);
            if( block == null )
                continue;
            block = classify( block );
            blocks.set(i, block);
        }
        
        return blocks;
    }

    /**识别一个block含有那种类型的表格*/
    private Block classify(Block block) {
        if(( block.lights == null )||(block.lights.size()==1)) {
            block.type = 0;  // its type can't be identiy
            return block;
        }
        
        // 查查，看有没有表头，有表头，定type=1
        for( Light light : block.lights ){
            if( light == null )
                continue;
            
            // 是标准表格
            if( light.variablename.compareTo("table.columnheads") == 0 ){
                block.type = 1;                
                System.out.println("set block type = " + block.type );
                block = doAnalysis_isMultiple( block );
                continue;
            }                                                    
        }
        return block;
    }

    
    /**移走所有blocks里重复的span*/
    private ArrayList<Block> removeRepetive(ArrayList<Block> blocks) {
        if( blocks == null )
            return null;
        
        for(int i=0;i<blocks.size(); i++ ){
            Block block = blocks.get(i);
            blocks.set(i, removeRepetive(block));                                
        }
        
        return blocks;
    }

    /**移走一个指定block里重复的span*/
    private Block removeRepetive(Block block) {
        if( block != null ){
            if(block.lights !=null){
                for(int i=0; i<block.lights.size()-1; i++){
                    Light light1 = block.lights.get(i);
                    if( light1 == null )
                        continue;
                    for(int j=i+1; j<block.lights.size(); j++){
                        Light light2 = block.lights.get(j);
                        if( light2 == null )
                            continue;
                        if(( light2.start > light1.start )&&(light2.end < light1.end))
                        {
                            block.lights.set(j, null);
                        }
                        
                        if(( light2.start < light1.start )&&(light2.end > light1.end))
                        {
                            block.lights.set(i, null);
                            break;
                        }
                    }
                }
            }
        }
        return block;
    }

    private ArrayList<Block> preProcess(ArrayList<Block> blocks) {
        String name = this.textfile.getName();
        if(( name != null)&&(name.length()>4)){
            if( name.substring(0, 4).compareTo("modi") == 0 )
                return blocks;
        }
            
        ArrayList<Block> nblocks = mergeLines( blocks ); 
        String file = textfile.getAbsolutePath().substring(0, 
                textfile.getAbsolutePath().length() - textfile.getName().length() -1 ) 
                + File.separatorChar + "modi" + textfile.getName();
        write( nblocks, file);
        
        
        return blocks;
    }

    private void write(ArrayList<Block> blocks, String file) {
        try{
            File copy = new File(file);
            
            FileOutputStream output = new FileOutputStream( copy );

            PrintStream p = new PrintStream(output);
            String outstr;
            
            if( blocks == null ){
                p.close();
                return;
            }
            
            for( Block block : blocks ){
                if(block.blockcontents != null )
                    for( String line : block.blockcontents)
                        if(line!=null)
                            p.println( line );
            }
            
            p.close();
            
            //this.textfile = copy;
            
            gui.refreshFileList();
            
        }catch(Exception ex){
        }
        
        
    }

    private ArrayList<Block> mergeLines(ArrayList<Block> blocks) {
        if( blocks != null ){
            for(int i=0;i<blocks.size();i++){                
                blocks.set(i, mergeLines( blocks.get(i) ) );
            }
        }
        
        return blocks;
    }

    private Block mergeLines(Block block) {
        if( block !=null  ){
            if(block.blockcontents!= null){
                for(int i=1;i<block.blockcontents.size(); i++){
                    
                    if( block.blockcontents.get(i) == null )
                        continue;
                    
                    if( foundSignal( block.blockcontents.get(i)  ) ){
                        block.blockcontents.set(i, 
                                block.blockcontents.get(i-1) 
                                +
                                block.blockcontents.get(i)
                                );
                        block.blockcontents.set(i, 
                                null
                                );
                        
                    }
                }
            }
        }
        return block;
    }

    private boolean foundSignal(final String line) {
        String number = "((\\d+\\.\\d+)|(\\d+\\.)|(\\.\\d+)|((\\d+)[%])|(\\d+))";
        String unit = "(([%]|(L)|(L/SEC)){0,1})";
        String regex = unit + "(([ ]+" + number + ")*)";
        
        String str = line.replaceAll( regex, " ");
        str = str.replaceAll( number, " ");
        str = str.replaceAll( unit, " ");
        if(str.trim().length()<1)
            return true;
        
        return false;
    }

    /**分析一个block里面的每个发现的内容（light），分析他们出现的次数.*/
    private Block doAnalysis_isMultiple(Block block) {
        
        // 用来稍后给的记录词出现次数
        HashMap<String, Integer> vars = new HashMap<String, Integer>();
                
        // 检测当前block中已发现term的出现次数
        if( block.lights != null ){
            for( int i=0; i<block.lights.size(); i++ ){
                Light light = block.lights.get(i);
                if( light == null )
                    continue;
                String regexname = light.variablename;
                if(( regexname==null ))//||(regexname.trim().length()<10))
                    continue;
                
                if( vars.containsKey( regexname ) ){
                    vars.put(regexname, vars.get( regexname ) + 1);
                }else{
                    vars.put(regexname, 1);
                }
                
                // tell system the repeat is 
                int repeat = vars.get( regexname );
                light.repeat = repeat;
                
                // update
                block.lights.set(i, light);
            }
        }
        
        
        return block;
    }

    private ArrayList<Block> sort(ArrayList<Block> blocks) {
        if( blocks == null )
            return blocks;
        
        for( int i=0; i<blocks.size(); i++ ){
            Block block = blocks.get(i);
            block = sort( block );
            blocks.set(i, block);
        }
        
        return blocks;
    }

    private Block sort(Block block) {
        if(( block != null )&&(block.lights!=null)){
            
            // get the first light to compare span
            for( int i=0; i< block.lights.size()-1; i++){
                Light light_a = block.lights.get(i);
                if( light_a ==null )
                    continue;
                
                // get the second span
                for( int j=i+1; j< block.lights.size(); j++){
                    Light light_b = block.lights.get(j);
                    if( light_b ==null )
                        continue;
                    
                    // compare the second span with the 1st one
                    if(( light_a.start > light_b.start )||(light_a.end > light_b.end))
                    {
                        Light temp = light_a;
                        light_a = light_b;
                        block.lights.set(i, light_b);
                        block.lights.set(j, light_a);                        
                    }
                }
            }
        }
        return block;
    }

    private void extraction(ArrayList<Block> blocks) {
        if( blocks == null )
            
        for( int i=0; i< blocks.size(); i++){
            Block block = blocks.get(i);
            block = extraction( block );
            blocks.set(i, block);
        }
    }

    
    private Block extraction(Block block) {
        
        if( block != null ){
            if( block.type == 1 ){
                block = extraction_simpleTable( block );
                return block;
            }
        }
        
        return block;
    }

    private Block extraction_simpleTable(Block block) {
        if(( block != null )&&(block.lights!=null)){
            for( int i=0; i< block.lights.size(); i++){
                if( block.lights.get(i) == null )
                    continue;
                String name = getVariableName( block.lights.get(i).variablename );
                //Light light;
                //block.lights.set(i, light);
            }
        }
        return block;
    }
    
    private String getVariableName( String line ){
        if(( line == null )||(line.length()<=10))
        return null;
        
        if( line.substring(0, 10).compareTo( "table.var.") != 0 )
            return null;
        
        String var = line.substring(10, line.length());
        
        if( var.compareTo("FVC") == 0 ){
            var = "FVC";
        }else if ( var.compareTo("FEV1") == 0 ){
            var = "FEV1";
        }else if ( var.compareTo("PF") == 0 ){
            var = "PF";
        }else if ( var.compareTo("fev1fvc") == 0 ){
            var = "FEV1//FVC";
        }else if ( var.compareTo("fef2575") == 0 ){
            var = "FEF25-75";
        }else if ( var.compareTo("dlco_sb") == 0 ){
            var = "FEF25-75";
        }
            
        System.out.println(var);
        return var;
    }
  
}
