/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotations;

import java.util.Vector;

/**
 * Structure of article and its annotations. This is
 *
 * @author Jianwei Leng June 21, 2010 3:30pm
 *
 */
public class Article {

    /**
     * File name of related text source
     */
    public String filename;
    
    /**
     * vector which store all annotation information of this article
     */
    public Vector<Annotation> annotations = new Vector<Annotation>();
    public Article baseArticle;

    public Article(String filename) {
        this.filename = filename;
    }

    public void addAnnotation(Annotation toAdd) {
        annotations.add(toAdd);
    }

    public String toString() {
        return filename;
    }

    /**
     * Get an annotation by the given metion id.
     *
     * @param   _metion_id
     *          
     * 
     * @return The annotation with the given
     */
    public Annotation getAnnotationByMention(String _mention_id) {
        
        // go over all annotations to check for annotation with same mention_id 
        for (Annotation annot : annotations) {
            
            if( annot == null )
                continue;
            
            if (annot.mentionid.equals(_mention_id)) {
                return annot;
            }
        }
        
        // return null if no matches found
        return null;
    }

    /**
     * Make a deep/new copy of current instance.
     */
    public Article getCopy() {
        Article article = new Article(filename);
        
        if( this.annotations != null ){
            for(Annotation annotation : this.annotations ){
                if( annotation == null )
                    continue;
                else
                    article.annotations.add(annotation.getCopy());
            }
        }
        
        
        return article;
    }
}
