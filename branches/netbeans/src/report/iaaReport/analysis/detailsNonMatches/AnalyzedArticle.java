/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import java.util.Vector;
import java.util.logging.Level;
import resultEditor.annotations.Annotation;

/**
 *
 * @author leng
 */
public class AnalyzedArticle {

    /**the file name of related document. */
    public String filename = null;

    /**所有分析过的数据, 以mainAnnotator为主ID*/
    public Vector<AnalyzedAnnotation> rows = new Vector<AnalyzedAnnotation>();

    

    /**constructor*/
    public AnalyzedArticle(String filename) throws Exception{
        if(filename==null)
            throw new Exception("1109020151:: filename can not be null");
        if(filename.trim().length()<1)
            throw new Exception("1109020150:: filename can not be empty");

        this.filename = filename;
    }


    /**检查给予的annotation，也许重复的row项目已经建立了： 指rows中某个main annotations中
     * 已经有一个annotation，这个annotation与指定的annotation有相同的区间, or uniqueindex
     */
    public boolean isInited(Annotation annotation) throws Exception{
        if(annotation==null)
            throw new Exception("1109020301::can not search a null annotation.");

        try{
            for(AnalyzedAnnotation analyzedannotation: rows)
            {
                if(analyzedannotation==null){
                    log.LoggingToFile.log(Level.WARNING, "1109020300:: a null analyzedannotation is found in memory!");
                    continue;
                }

                Vector<Annotation> mainAnns = analyzedannotation.mainAnnotations;
                if (mainAnns==null){
                    continue;
                }

                for(Annotation ann : mainAnns)
                {
                    if( annotation.uniqueIndex == ann.uniqueIndex )
                        return true;

                    if( annotation.spanset.equals( ann.spanset ) )                         
                        return true;
                }

            }

            return false;
        }catch(Exception ex){
            throw new Exception("1109020302::error occurred while we try to "
                    + "confirm whether an annotation has be inited as a row "
                    + "head. \nError Details:: "+ ex.getMessage());
        }
    }

    /***/
    void initRow(Annotation annotations, String[] annotators) throws Exception {
        try{
            AnalyzedAnnotation analyzedAnnotation = new AnalyzedAnnotation(annotations, annotators);
            rows.add(analyzedAnnotation);
        }catch(Exception ex){
            throw new Exception("1109020303::error occurred while we try to "
                    + "initilize an annotation as a row "
                    + "head. \nError Details:: "+ ex.getMessage());
        }
    }
}
