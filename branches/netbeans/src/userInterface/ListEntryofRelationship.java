
package userInterface;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationRelationship;

/**
 * New Class to bulid one kind of entry to show icon and text informations on
 * list of output annotations into xml files.
 *
 * @author Jianwei Leng  2010-06-29
 */
public class ListEntryofRelationship {
  
    /**the annotation that own the relationship.*/
  private Annotation thisAnnotation;
  
  private AnnotationRelationship rel;
  private boolean selected;
  
  
  public ListEntryofRelationship(Annotation thisAnnotation, AnnotationRelationship relation) {
      rel = relation;
      this.thisAnnotation = thisAnnotation;

  }
  public String getTitle() {
      return getRel().toString();
  }

    /**
     * @return the selected
     */
    public boolean isSelected()
    {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    
    
    /**
     * @return the rel
     */
    public AnnotationRelationship getRel()
    {
        return rel;
    }

    /**
     * @param rel the rel to set
     */
    public void setRel(AnnotationRelationship rel)
    {
        this.rel = rel;
    }

    Annotation getAnnotation() {
        return this.thisAnnotation;
    }


  
}