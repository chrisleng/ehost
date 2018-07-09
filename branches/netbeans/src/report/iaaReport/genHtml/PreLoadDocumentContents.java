/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.genHtml;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.SpanDef;
import resultEditor.annotations.SpanSetDef;

/**
 * Load text content from designated text source. And return a string, which use
 * html code to highlight annotation in Red, to IAA report system.
 * @author leng
 */
public class PreLoadDocumentContents {

    private static String documentContent = null;
    
    static void load(String filename) throws Exception{

        if((filename==null)||(filename.trim().length()<1))
            throw new Exception("1110132108:: parameter filename is null or empty!");

        try{
            documentContent = "";

            if((filename==null)||(filename.trim().length()<1))
                throw new Exception("1109020643::");

            File file = getFile(filename.trim());

            if(!file.exists())
                throw new Exception("1110132107:: file doesn't exist!");

            if( file.isDirectory() )
                throw new Exception("1110132106:: file is a directory!");

            ArrayList<String> document = commons.Filesys.ReadFileContents(file);

            for(String line:document){
                documentContent = documentContent + line + " ";
            }
        }catch(Exception ex){
            throw new Exception("1109020715::fail to load document content.\nError Details:" + ex.getMessage() );
        }
    }



    public static String get(int spanstart, int spanend ) throws Exception{
        try{
            String colorbegin = "<span style=\"background-color: #CCCCFF;\">";
            String colorend = "</span>";
            int max = documentContent == null ? 0 : documentContent.length();
            int before = 150, after = 150;
            int start = spanstart - before;
            int end = spanend + after;

            if(start<0)
                start = 0;
            if(end>=max)
                end = max - 1;

            try{                
                String str3 = documentContent.substring(start, spanstart)
                        + colorbegin
                        + documentContent.substring(spanstart, spanend)
                        + colorend
                        + documentContent.substring(spanend, end)
                        ;

                return str3;
            } catch(Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "1109021009:: error while we try to highlight annotation.");
                return documentContent.substring(start, end);
            }
                               
        }catch(Exception ex){
            throw new Exception("1109020716A::fail to get a substring from loaded document .\nError Details:" + ex.getMessage() );
        }
    }
    
    public static String get( Annotation annotation ) throws Exception{
        try{
            String colorbegin = "<span style=\"background-color: #CCCCFF;\">";
            String colorend = "</span>";
            int max = documentContent == null ? 0 : documentContent.length();
            int before = 150, after = 150;
            int start = annotation.spanset.getMinimumStart() - before;
            int end = annotation.spanset.getMaximumEnd() + after;

            
            if(start<0)
                start = 0;
            if(end>=max)
                end = max - 1;
            
            // ---- 2 ----
            // sort spans and intergrate overlapping spans, so we can 
            // easily build the text section with highlighted terms.
            
            // copy spans into array            
            SpanDef[] spans = new SpanDef[annotation.spanset.size()];
            for( int i=0; i<annotation.spanset.size(); i++ ){
                spans[i] = annotation.spanset.getSpanAt(i);
            }
            
            // resort, and integrate overlapping/duplicate spans
            for( int i=0; i<spans.length; i++){
                if(spans[i]==null)
                    continue;
                for( int j=0; j<spans.length; j++){
                    if( i==j)
                        continue;                
                    if(spans[j]==null)
                        continue;
                    
                    // integrate them if they are duplicates to each other
                    if( spans[i].isEqual( spans[j] )){
                        spans[j] = null;
                        continue;
                    }
                    
                    // integrate them if they are overlapping
                    if( spans[i].isOverlapping( spans[j]) ){
                        int s=spans[i].start, e=spans[i].end;
                        s = s > spans[j].start? spans[j].start: s; 
                        e = e < spans[j].end? spans[j].end: e;
                        spans[i] = new SpanDef(s, e);
                        spans[j] = null;
                        
                        j=0;
                        continue;
                    }
                    
                    
                    // resort
                    if( spans[i].start > spans[j].start ){
                        int s=spans[i].start, e=spans[i].end;
                        spans[i] = spans[j];
                        spans[j] = new SpanDef( s, e );
                    }
                                                    
                }
            }
                
            try{                
                           
                String str3 = "_";
                for( int i=0; i< spans.length; i++){
                    // get text before this term
                    if(i==0){
                        if ( spans[i].start > documentContent.length() )
                        str3 = "_";
                        else 
                        str3 = documentContent.substring(start, spans[i].start);
                    }else
                        if((spans[i-1].end<documentContent.length()) &&(spans[i].start<documentContent.length()))
                            str3 = documentContent.substring(spans[i-1].end, spans[i].start);
                    
                                        
                        str3 += colorbegin;
                        if((spans[i].end<documentContent.length()) &&(spans[i].start<documentContent.length()))
                            str3 += documentContent.substring(spans[i].start, spans[i].end);
                        str3 += colorend;
                    
                    if((i == (spans.length - 1) ) && (spans[i].end<documentContent.length())    )          
                        str3 += documentContent.substring(spans[i].end, end);
                                        
                        
                }                                                

                return str3;
                
            } catch(Exception ex) {
                ex.printStackTrace();
                /*
                log.LoggingToFile.log(Level.SEVERE, "1109021009:: error while we try to highlight annotation.");
                System.out.println("document = "+documentContent);
                System.out.println("document length = "+documentContent.length());
                System.out.println("start = "+start);
                System.out.println("end = "+end);
                */ 
                //return documentContent.substring(start, end);
            }
                               
        }catch(Exception ex){
            ex.printStackTrace();
            //throw new Exception("1109020716B::fail to get a substring from loaded document .\nError Details:" + ex.getMessage() );
        }
        
        return null;
    }
    
     public static String getSurroundText( Annotation annotation ) throws Exception{
         return getSurroundText( annotation, null );         
     }
     
     static Article lastarticle;
    public static String getSurroundText( Annotation annotation, Article article ) throws Exception{
        try{
            
            if((annotation==null)||(annotation.spanset == null ))
                throw new Exception("ERROR 1203011319A:: null annotation!");                
            
            if( annotation.spanset.size_nonNull() < 1 )
                throw new Exception("ERROR 1203011319B:: null annotation!");
            
            if( lastarticle!=null ){
                if((article != null)&&(article.filename.compareTo( lastarticle.filename ) != 0))
                   load( article.filename );
            }else{
                if(article != null)
                    load( article.filename );
            }
                
                
            
            if( documentContent == null ){                
                throw new Exception("ERROR 1203011319C:: null annotation!");
            }
            
            
            int left = annotation.spanset.getMinimumStart();
            int right = annotation.spanset.getMaximumEnd();            
            
            String colorbegin = "<span style=\"background-color: #CCCCFF;\">";
            String colorend = "</span>";
            
            
            
            int max = documentContent == null ? 0 : documentContent.length();
            int before = 150, after = 150;
            int start = left - before;
            int end = right + after;
            
            if(start<0)
                start = 0;
            
            if(end>=max)
                end = max - 1;
            
            // copy spans from the spanset of current annotation to an array,
            // and sort it.
            
            SpanDef[] sortedSpans = sortSpanSet( annotation.spanset );

            
            String str3 = "";
            if ((sortedSpans != null) && (sortedSpans.length > 0)) {
                for(int x=0; x<sortedSpans.length; x++){
                    if(x==0){
                        // UNPROFECT CODE: if removing cpmpaing the number to the document length, it will broken on Brett's project
                         if(sortedSpans[x].start>=documentContent.length())
                             str3 = "_";
                         else
                             str3 = documentContent.substring(start, sortedSpans[x].start );
                        
                    }
                    str3 = str3 + colorbegin;
                    //System.out.println("str3=" + str3);
                    //System.out.println("x=" + x);
                    //System.out.println("documentContent =" + documentContent);
                    //System.out.println("sortedSpans length =" + sortedSpans.length);
                    //System.out.println("sortedSpans[x].start =" + sortedSpans[x].start);
                    //System.out.println("sortedSpans[x].end =" + sortedSpans[x].end);
                    
                    // UNPROFECT CODE: if removing cpmpaing the number to the document length, it will broken on Brett's project
                    if((sortedSpans[x].start<documentContent.length()) && (sortedSpans[x].end<documentContent.length()))
                    str3 = str3 + documentContent.substring(sortedSpans[x].start, sortedSpans[x].end); 
                    
                    str3 = str3 + colorend;
                    
                    if(( x < sortedSpans.length - 1 ) && (sortedSpans[x].end<documentContent.length()) && (sortedSpans[x + 1].start<documentContent.length()))
                        str3 = str3 + documentContent.substring(sortedSpans[x].end, sortedSpans[x + 1].start); 
                    else if ( ( x == sortedSpans.length - 1 )&& (sortedSpans[x].end<documentContent.length() ) )
                        str3 = str3 + documentContent.substring(sortedSpans[x].end, end); 
                }
            }
            
            return str3;
                
                /*
                try {
                    String str3 = documentContent.substring(start, spanstart)
                            + colorbegin
                            + documentContent.substring( spanstart, spanend )
                            + colorend
                            + documentContent.substring(spanend, end);

                    return str3;
                } catch (Exception ex) {
                    log.LoggingToFile.log(Level.SEVERE, "1109021009:: error while we try to highlight annotation.");
                    return documentContent.substring(start, end);
                }*/
            }
                               
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("1109020716C::fail to get a substring from loaded document .\nError Details:" + ex.getMessage() );
            
        }
    }

    /**Copy spans from a given spanset to an array and sort then by start 
     * coordinate of each span.
     * 
     * Notice(1): as we assume all of them are not overlapped to any other.
     * So we have to merge overlapped spans in this method. 
     * 
     * Notice(2): the array may have different size compared to the spanset, 
     * because we dropped all null spans or illegal spans we found.
     */
    private static SpanDef[] sortSpanSet( SpanSetDef spanset ){
        if( spanset == null )
            return null;
        
        SpanDef[] spanarray = new SpanDef[spanset.size()];
        
        // copy spans from spanset to array
        for( int i=0; i<spanset.size(); i++){
            spanarray[i]=spanset.getSpanAt(i);
        }
        
        
        // merge overlapped spans
        for( int i=0; i<spanarray.length; i++){
            SpanDef span1 = spanarray[i];
            if(span1 == null)
                continue;
            
            for( int j=0; j<spanarray.length; j++){
                if( i == j)
                    continue;                
                SpanDef span2 = spanarray[j];
                if(span2==null)
                    continue;
                
                // if this two spans are overlapping
                if( span1.isOverlapping( span2 ) )
                {
                    int x1 = span1.start;
                    int y1 = span1.end;
                    int x2 = span2.start;
                    int y2 = span2.end;
                                        
                    x1 =  x2<x1 ? x2 : x1;
                    y1 =  y2>y1 ? y2 : y1;
                    
                    spanarray[i] = new SpanDef(x1,y1);
                    spanarray[j] = null;
                    
                    span1 = spanarray[i];
                }                
            }
        }
        
        // remove null span
        int count = 0;
        for( int i=0; i<spanarray.length; i++){
            SpanDef span1 = spanarray[i];
            if(span1 == null)
                count++;
        }
        
        SpanDef[] toReturn = new SpanDef[spanarray.length-count];
        int index = 0;
        for( int i=0; i<spanarray.length; i++){
            SpanDef span1 = spanarray[i];
            if(span1 != null){
                toReturn[index] = span1;
            }else{
                index++;
            }                
        }
        
        // sort them
        for( int i=0; i<toReturn.length; i++){
            SpanDef span1 = toReturn[i];
            if(span1 == null)
                continue;
            
            for( int j=0; j<toReturn.length; j++){
                if( i == j)
                    continue;                
                SpanDef span2 = toReturn[j];
                if( span2 == null )
                    continue;
                
                // sort them if they are not overlapping 
                if (span2.start < span1.start) {
                    int x1 = span1.start;
                    int y1 = span1.end;
                    int x2 = span2.start;
                    int y2 = span2.end;

                    toReturn[i] = new SpanDef(x2, y2);
                    toReturn[j] = new SpanDef(x1, y1);

                    span1 = toReturn[i];
                }
            }
        }
        
        // count for number of non=null items
        int count_nonnull = 0;
        
        for(SpanDef span : toReturn){
            if(span!=null)
               count_nonnull++; 
        }
        
        if(count_nonnull < 1)
            return null;
        
        SpanDef[] sorted_spans = new SpanDef[count_nonnull];
        int idx = 0;
        for(SpanDef span : toReturn){
            if(span!=null){
               sorted_spans[idx] = span;
               idx++;
            }
        }
        
        return sorted_spans;
    }

    private static File getFile(String filename){

        String corpus = env.Parameters.WorkSpace.CurrentProject.getAbsoluteFile() + String.valueOf(File.separatorChar) + "corpus" + String.valueOf( File.separatorChar );
        File project = new File( corpus );
        File[] files = project.listFiles();

        if(files==null)
            return null;

        for(File file: files){
            if(file.isDirectory())
                continue;
            if((file.getName().trim().compareTo(".")==0)
                || (file.getName().trim().compareTo("..")==0))
                continue;

            if(file.getName().trim().compareTo(filename.trim())==0)
                return file;
        }

        return null;

    }

}
