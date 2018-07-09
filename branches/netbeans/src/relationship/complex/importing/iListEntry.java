
package relationship.complex.importing;

import java.util.Collection;
import java.util.Vector;

/**
 * New Class to bulid one kind of entry to show icon and text informations on
 * list of output annotations into xml files.
 *
 * @author Jianwei Leng  2010-06-29
 */
public class iListEntry {
  public String classname = null;
  public String term;
  public Vector<String> values = new Vector<String>();
  
  public Vector<String> subTerms2 = new Vector<String>();
  public boolean isCheckBoxSelected = true;
  public boolean selected = true;
  private boolean nameKnown;
  public boolean isUnknownRelationship;

  public void setClass(String classname){
      this.classname = classname;
  }
  public iListEntry(String term, Collection values, Collection subTerm2, boolean isRelationship, boolean isAttributeNameKnown) {
      if(values != null)
        this.values.addAll(values);
      else
          this.values = new Vector<String>();
      
      if(subTerm2 != null)
          subTerms2.addAll(subTerm2);
      else
          subTerms2 = new Vector<String>();
      
      this.term = term;
      this.isUnknownRelationship = isRelationship;
      this.nameKnown = isAttributeNameKnown;

  }

  public String getTitle() {
      String str = "<html>" ;
      if(isUnknownRelationship)
          str += "<b>Relationship Name: </b>";
      else
      {
          str += "<b>Attribute </b>";
      }
      if(!nameKnown)
          str+= "<font color = \"red\">";
      str+= "[ <font color=blue>" + term + "</font> ] <b>of class</b> [ <font color=green>" + classname + "</font> ] ";
      if(!nameKnown)
          str+= "</font>";
      if(!isUnknownRelationship)
      {
          if(values.size() > 0 )
              str += "<br> <b>Values:</b> ";
          for(String s : values)
          {
              str+= "<br><font color = \"red\">&nbsp; &nbsp; &nbsp; &nbsp;"+ s +"</font>";
          }
      }
      else
      {
          if(values.size() > 0 )
          {
              if(subTerms2 != null)
                  str += "<br> <b>From Classes:</b> ";
              else
                  str += "<br> <b>Allowable Regex:</b> ";
          }
          for(String s : values)
          {
              str+= "<br><font color = \"red\">&nbsp; &nbsp; &nbsp; &nbsp;"+ s +"</font>";
          }
          if(subTerms2 != null)
          {
              if(subTerms2.size() > 0 )
                  str += "<br> <b>To Classes:</b> ";
              for(String s : subTerms2)
              {
                  str+= "<br><font color = \"red\">&nbsp; &nbsp; &nbsp; &nbsp;"+ s +"</font>";
              }
          }
      }
      str+= "</html>";
      return str;
  }
    public void setCheckBoxStatus(boolean isCheckBoxSelected ){
        this.isCheckBoxSelected = isCheckBoxSelected;
    }

  // Override standard toString method to give a useful result
  public String toString() {
      return term;
  }
}