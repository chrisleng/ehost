/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations.findConflicts;

import imports.importedXML.eAnnotationNode;
import java.util.Vector;

/**
 * Find all protential conflict annotations from imported annotations in memory.
 * @author leng 2010-6-9 3:19pm Wednesday
 */
public class findConflict {

    // found Conflicts
    protected static Vector<Range> conflictAnnotations
            = new Vector<Range>();

    
    public Vector<Range> get(){
        return conflictAnnotations;
    }

    /** Dig out all protential conflict annotaions for a text filename*/
    public findConflict(String _filename){

        // clear
        conflictAnnotations.clear();

        // figout how many files have been imported
        if  ( _filename == null) return;
        if  ( _filename.trim().length() < 1) return;
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();

        resultEditor.annotations.Article article  = depot.getArticleByFilename(_filename);
        if (article == null)  return;
        int size = article.annotations.size();
        if(size < 1)
            return;



            // go over each annotation in this list
            for(resultEditor.annotations.Annotation annotation: article.annotations){

                // get span range of each annotation
                int start = annotation.spanstart;
                int end = annotation.spanend;

                // check this range and try to
                Range jointRange = checkJoint( start, end, article.annotations );

                if ( jointRange != null ) {
                    conflictAnnotations.add(jointRange);
                }
            }
                    
        

        // check and remove repetitive in the list of conflictAnnotations
        conflictAnnotations = removeRepetitive(conflictAnnotations);
               
    }

    /**remove ".knowtator.xml" from the filename*/
    private String getTxtName(String _filename){
        String tragger = ".knowtator.xml";
        int i = _filename.indexOf(tragger);
        return _filename.substring(0, i);
    }

    // check and remove repetitive in the list of conflictAnnotations
    private Vector<Range> removeRepetitive(Vector<Range> _conflictAnnotations){
        int size = _conflictAnnotations.size();
        for(int i=0; i<size;i++){
            
            int a = _conflictAnnotations.get(i).left;
            int b = _conflictAnnotations.get(i).right;

            // avoid invalid data
            if((a==-1)&&(b==-1)) continue;

            for(int j=0; j<size;j++){
                // do not check itself
                if(i == j) continue;

                // compare range
                int c = _conflictAnnotations.get(j).left;
                int d = _conflictAnnotations.get(j).right;

                if(( a>=c )&&( b<=d )){
                    _conflictAnnotations.set(i, new Range(-1,-1));
                    break;
                }

                if(( a<=c)&&(b>=d)){
                    _conflictAnnotations.set(j, new Range(-1,-1));
                }
            }

        }

        // removed invalid object from the vector
        for(int i=size-1;i>=0; i--){
            if((_conflictAnnotations.get(i).left==-1)
                &&(_conflictAnnotations.get(i).right==-1)){
                _conflictAnnotations.remove(i);
            }
        }


        return _conflictAnnotations;
    }

    /**To a given annotation in a set, there maybe have some protential
     * conflicts to this annotation. And conflict here means these annotations
     * may have some joint span range, except these annotations who have totally
     * same range.
     * Two kind of situations:
     * 1. two range get a joint area
     * 2. one small range in a bigger one
     */
    private Range checkJoint(int _start, int _end,
            Vector<resultEditor.annotations.Annotation> annotations ){
        
        // range defined for return
        Range jointRange = new Range();

        
        // amount of all annotations in this set
        int size = annotations.size();

        // set variables before comparation
        
        int start, end;
        boolean foundConflict = false;
        jointRange.left = _start;
        jointRange.right = _end;
        // compare range with each annotation in the set
        for(resultEditor.annotations.Annotation annotation : annotations ){

            // get span range of this annotation
            
            start = annotation.spanstart;
            end = annotation.spanend;

            // to range [a,b] and [c,d]
            // situation 1: get [a,d] if [a<c<b] and [d>=b]
            if(( jointRange.left < start )&&( start < jointRange.right )
                    &&( end >= jointRange.right )){

                // Echo for DEBUG
                // System.out.print("["+ jointRange.left+", " + jointRange.right+ "]"
                //        + " [ " + start + ", "+end+"]");
                
                // set flag to indicate found something
                foundConflict = true;
                // jointRange.left= jointRange.left ;
                jointRange.right= end ;

                // echo for DEBUG
                // System.out.print("-> ["+jointRange.left+", "+jointRange.right+"]\n");
                
                continue;
            }

            // situation 2: get [c,b] if [c<a<d] and [b>=d]
            if( ( start < jointRange.left )&&( jointRange.left < end )
                    &&( jointRange.right >= end )){
                // set flag to indicate found something
                foundConflict = true;

                jointRange.left= start ;
                //jointRange.right= jointRange.right ;
                continue;
            }


            // situation 3: get [c,d] if c<a<b<d
            if( ( start < jointRange.left )
                    &&( jointRange.left < jointRange.right )
                    &&( jointRange.right < end )){
                // set flag to indicate found something
                foundConflict = true;

                jointRange.left= start ;
                jointRange.right= end ;
                continue;
            }

            // situation 4: get [a,b] if a<c<d<b
            if( ( jointRange.left < start )&&( start < end )
                    &&( end < jointRange.right )){
                // set flag to indicate found something
                foundConflict = true;

                //jointRange.left= jointRange.left ;
                //jointRange.right= jointRange.right ;
                continue;
            }


        }

        // return range if found a joint range, otherwise return null
        if ( foundConflict == true )
            return jointRange;
        else
            return null;
    }


}



/***/
class Table_Conflict{
    int conflictLowerPoint = -1;
    int conflictUpperPoint = -1;
}
