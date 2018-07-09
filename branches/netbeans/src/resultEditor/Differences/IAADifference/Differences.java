/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.Differences.IAADifference;

import java.awt.Dimension;
import java.util.Vector;

/**
 * This is a static class that we use this one to store the difference we found
 * after run IAA difference analysis.
 *
 *
 * @author Jianwei Chris leng, 10-07-2011
 */
public class Differences {

    /**The static vector is used to store all found differences that we
     * found via IAA difference analysis. It will be used to indicate
     * non-matches on screen via red wavy underlines.
     */
    private static Vector<ArticleDifference> IAADifferences = new Vector<ArticleDifference>();


    /**delete all saved data of annotation */
    public static void clear(){
        IAADifferences.clear();
    }


    /**return non-matched annotations for the given article name.
     *
     * @param   filename
     *          the basic filename of the article
     *
     * @return  non-matched annotations to the given article name
     *
     */
    public static ArticleDifference getArticle(String filename) throws Exception{
        try{
            if((filename==null)||(filename.trim().length()<1))
                throw new Exception("1110131645:: null/empty filename!");

            for(ArticleDifference ad : IAADifferences ){
                if(ad==null)
                    continue;

                if(ad.filename.trim().compareTo(filename.trim())==0)
                    return ad;

            }

            return null;
                    
        }catch(Exception ex){
            throw new Exception("1110131634:: fail to find non-matched annotations for the given article bane");
        }
    }

    /**record an difference span in the static memory "IAADifferences". They
     * will be used to draw out differences with red wavy underlines.
     *
     * @param   filename
     *          the filename that use to tell system which file is current
     *          difference cames from.
     *
     * @param   spanDiffStart
     *          the span start point of this annotation difference
     *
     * @param   spanDiffend
     *          the span end point of this annotation differences
     */
    public static void add(String filename, int spanDiffStart, int spanDiffEnd ) throws Exception{
        //System.out.println("filename:["+filename+"], diff at("+ spanDiffStart + ", " + spanDiffEnd + ");");
        if((spanDiffStart<0)||(spanDiffEnd<0))
            throw new Exception("1110101557::wrong span interval: position < 0 !");

        if((filename==null)||(filename.trim().length()<1))
            throw new Exception("1110101556::filename are null or empty!");

        try{

            // flag use to indicate if we has this article with differences or not
            boolean foundArticle = false;

            // go over all recorded Article differences to see whether
            for(ArticleDifference articledifference : IAADifferences){

                if(articledifference==null)
                    throw new Exception("1110111105:: found null item in vector.");

                if(articledifference.filename==null)
                    throw new Exception("1110111106:: found null item in vector.");                

                if(articledifference.filename.trim().compareTo(filename.trim())==0){

                    foundArticle = true; // kark that this article was ready

                    articledifference.add(spanDiffStart, spanDiffEnd);

                    break;
                }                
            }

            // if no existing article of difference for this file, we need
            // to cread a new one
            if(foundArticle == false){
                ArticleDifference articleDifference = new ArticleDifference(filename.trim());
                articleDifference.add(spanDiffStart, spanDiffEnd);
                IAADifferences.add( articleDifference );
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("1110101556:: error occurred while recording annotation differences via IAA report system!");
            
        }
    }

    /**remove the relvant records of nonmatches*/
    public static void remove(String filename, Dimension span) throws Exception {
        try {
            int x = span.height, y = span.width;

            for(int i = 0; i< IAADifferences.size(); i++){
                ArticleDifference articledifference = IAADifferences.get(i);
                if(articledifference==null)
                    continue;
                if(articledifference.filename==null)
                    continue;
                if(filename==null)
                    throw new Exception("1110141308:: given filename is null");
                if(filename.trim().compareTo(articledifference.filename.trim())==0){
                    if(articledifference.differences==null)
                        throw new Exception("1110141309:: find null value!");
                    for(int j=articledifference.differences.size()-1; j>=0; j--){
                        Difference difference = articledifference.differences.get(j);
                        if(difference==null)
                            continue;
                        if((x<=difference.differenceStart)&&(y>=difference.differenceEnd)){
                            articledifference.differences.remove(j);
                        }
                    }
                }
            }

            
        } catch (Exception ex) {
            throw new Exception("1110141307::fail to remove recorded "
                    + "non-matches\n - RELATED ERROR: " + ex.getMessage());
        }
    }
    
    public static void remove(String filename) throws Exception {
        try {            

            for(int i = 0; i< IAADifferences.size(); i++){
                ArticleDifference articledifference = IAADifferences.get(i);
                if(articledifference==null)
                    continue;
                if(articledifference.filename==null)
                    continue;
                if(filename==null)
                    throw new Exception("1110141308:: given filename is null");
                if(filename.trim().compareTo(articledifference.filename.trim())==0){
                    articledifference.differences = new Vector<Difference>();
                    break;
                }
            }

            
        } catch (Exception ex) {
            throw new Exception("1110141307::fail to remove recorded "
                    + "non-matches\n - RELATED ERROR: " + ex.getMessage());
        }
    }

}
