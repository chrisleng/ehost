/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relationship.complex.dataTypes;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import resultEditor.annotations.*;

/**
 * This class is to keep track of a list of Complex Relationships and provide
 * methods for interacting with the list.
 *
 * @author Chris Leng, Feb 2, 2012
 */
public class RelSchemaList
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    /**
     * The relationships in the list.
     */
    private TreeSet<RelationshipDef> rels;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor
     */
    public RelSchemaList()
    {
        rels = new TreeSet<RelationshipDef>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Get all of the relationships in this schema.
     * @return - all relationships in the schema.
     */
    public TreeSet<RelationshipDef> getRelationships()
    {
        return rels;
    }
    /**
     * Return the name of the relationship that matches the list of classes.
     * @param classes - the list of classes from beginning to end of the relationship.
     * @return - the name of the relationship if it is found, null otherwise.
     */
    public String getNameByRegex(ArrayList<String> classes)
    {
        for(RelationshipDef rel: rels)
        {
            if(rel.checkContainsClasses(classes))
            {
                return rel.getName();
            }
        }
        return null;
    }
    /**
     * Check to see if a relationship name or the allowed regex already exists in
     * the schema.
     * @param name - the name to check to see if it exists
     * @param regex - the regex to check to see if it exists
     * @return true if either one exists, false otherwise
     */
    public boolean checkExists(String name, String regex)
    {
        return checkRegexExists(regex) || checkNameExists(name);
    }
    /**
     * Check to see if a regular expression exists in the current schema.
     * THIS SHOULD PROBABLY ALSO TEST FOR SUBSETS IF POSSIBLE!!!
     * @param regex - the regular expression to locate in the current schema
     * @return - true if the regular expression exists, false otherwise.
     */
    public boolean checkRegexExists(String regex)
    {
        try {
            if ((regex == null) || (regex.trim().length() < 1)) {
                return false;
            }

            for (RelationshipDef rel : rels) {
                String rule = rel.getAllowed();
                if (rule.equals(regex)) {
                    return true;
                } else {
                    Pattern p = Pattern.compile(rule);
                    Matcher m = p.matcher(regex);
                    if(m.find())
                        return true;
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR 1202221422");
        }
        return false;
    }
    
    /**To a given annotation pair, this method check how many suitable can be applied 
     * for this annotation pair.
     * 
     * @param	a
     * 			the annotation in the start point of a relationship.
     * 
     * @param	b
     * 			the annotation in the end point of a relationship
     * 
     * @return	the number of suitable relationships for the given annotation pairs.
     * 			the range should be around 0-n; if the returned value is smaller than
     * 			0, that means an error occurred. 
     * 			
     * */
    public int getPossibleRels(Annotation a, Annotation b){
    	
    	// validity checking 
    	if(( a == null ) ||(b==null))
    		return 0;
    	if( (a.annotationclass == null ) || (a.annotationclass.trim().length()<1))
    		return 0;
    	if( (b.annotationclass == null ) || (b.annotationclass.trim().length()<1))
    		return 0;
    	
    	// put to class names together
    	String str = a.annotationclass.trim() + b.annotationclass.trim();

    	int toReturn = 0;
    	// use regular expression to filter out possible rules
    	try {
    		
            for (RelationshipDef rel : rels) {
                String rule = rel.getAllowed();
         
                    Pattern p = Pattern.compile(rule);
                    Matcher m = p.matcher( " " + str + " " );
                    if(m.find()&&(m.start()==1||m.start()==0)
                            &&( (m.end()==str.length()+2-1) || (m.end()==str.length()+2) ) 
                            ){
                        toReturn++;
                        continue;
                    }
                
            }
        } catch (Exception ex) {
            System.out.println("ERROR 1202221422");
            return -1;
        }
    	
    	return toReturn;
    	
    }
    
    public int getPossibleRels(String a, String b){
    	
    	// validity checking 
    	if(( a == null ) ||(b==null))
    		return 0;
    	if( (a.trim().length()<1 ) || (b.trim().length()<1))
    		return 0;
    	
    	
    	// put to class names together
    	String str = a.trim() + b.trim();

    	int toReturn = 0;
    	// use regular expression to filter out possible rules
    	try {
    		
            for (RelationshipDef rel : rels) {
                String rule = rel.getAllowed();
         
                    Pattern p = Pattern.compile(rule);
                    Matcher m = p.matcher( " " + str + " " );
                    if(m.find()&&(m.start()==1||m.start()==0)
                            &&( (m.end()==str.length()+2-1) || (m.end()==str.length()+2) ) 
                            ){
                        toReturn++;
                        continue;
                    }
                
            }
        } catch (Exception ex) {
            System.out.println("ERROR 1202221422");
            return -1;
        }
    	
    	return toReturn;
    	
    }
    
    /**use the name of relationship to get the schema*/
    public RelationshipDef getRelationshipSchema(String name){
        if( name == null )
            return null;
        
        for (RelationshipDef rel : rels) {
            if(rel==null)
                continue;
            String name_of_rel = rel.getName();
            if(( name_of_rel == null)||(name_of_rel.trim().length()<1))
                continue;
            
            if( name_of_rel.trim().compareTo( name.trim() ) == 0 )
                return rel;
        }
        
        return null;
    }
    
    /**To a given annotation pair, this method check how many suitable can be applied 
     * for this annotation pair.
     * 
     * @param	a
     * 			the annotation in the start point of a relationship.
     * 
     * @param	b
     * 			the annotation in the end point of a relationship
     * 
     * @return	the number of suitable relationships for the given annotation pairs.
     * 			the range should be around 0-n; if the returned value is smaller than
     * 			0, that means an error occurred. 
     * 			
     * */
    public Object[] getPossibleRelNames(Annotation a, Annotation b){
    	
    	// validity checking 
    	if(( a == null ) ||(b==null))
    		return null;
    	if( (a.annotationclass == null ) || (a.annotationclass.trim().length()<1))
    		return null;
    	if( (b.annotationclass == null ) || (b.annotationclass.trim().length()<1))
    		return null;
    	
    	// put to class names together
    	String str = a.annotationclass.trim() + b.annotationclass.trim();

        ArrayList<String> rulnames = new ArrayList<String>();
    	int toReturn = 0;
    	// use regular expression to filter out possible rules
    	try {
    		
            for (RelationshipDef rel : rels) {
                String rule = rel.getAllowed();
         
                    Pattern p = Pattern.compile(rule);
                    Matcher m = p.matcher( " " + str + " " );
                    if(m.find()&&(m.start()==1||m.start()==0)
                            &&( (m.end()==str.length()+2-1) || (m.end()==str.length()+2) ) 
                            ){
                        rulnames.add(rel.getName());
                        toReturn++;
                        continue;
                    }
                
            }
            
            return rulnames.toArray();
        } catch (Exception ex) {
            System.out.println("ERROR 1202221422");
            return null;
        }
    	
        
    	
    }

    /**get the relationship name by the class name of the source annotation and
     * the class name of the dest annotation. Return null if don't find matched
     * or get error.
     *
     * @param   sourceClass
     *          The name of the class of the source annotation.
     *
     * @param   objClass
     *          The name of the class
     *
     * @return  The name of the relationship whose regex matches these two class
     *          Return null if don't find matched or get error.
     */
    public String getRelName(String sourceClass, String objClass){
        try{
        ArrayList<String> classes = new ArrayList<String>();

            //Build Arraylist of classes to check
            classes.add( sourceClass );
            classes.add( objClass );
            
            //Get the name of the matching relationship (returns null if nothing matches).
            String name = env.Parameters.RelationshipSchemas.getNameByRegex(classes);
            return name;
        }catch(Exception ex){
            return null;
        }
    }
    /**
     * Check to see if a relationship name exists in this schema.
     * @param name - the name to check for uniqueness.
     * @return - true if it exists, false otherwise.
     */
    public boolean checkNameExists(String name)
    {
        for(RelationshipDef rel: rels)
        {
            if(rel.getName().equals(name))
                return true;
        }
        return false;
    }
    /**
     * Add the complex relationship to this schema.
     * @param rel - the relationships to add.
     */
    public void add(RelationshipDef rel)
    {
        if(rel == null)
            return;
        if(rel.getAllowed() == null)
            return;
        if(rel.getName() == null)
            return;
        
        boolean existing = false;
        for(RelationshipDef relationship: rels)
        {
            if(relationship ==null)
                continue;
            if(relationship.getName() == null)
                continue;
            if(relationship.getAllowed()==null)
                continue;
            
            if(relationship.getName().equals(rel.getName()) && relationship.getAllowed().equals(rel.getAllowed()))
            {                
                existing = true;
            }
        }
        
        if(!existing)
            rels.add(rel);
    }
    /**
     * Remove the given relationship from the list of relationships.
     * @param rel - the element to remove.
     */
    public void remove(RelationshipDef rel)
    {        
        rels.remove( rel );
        
        //System.out.println(removed);
    }
    /**
     * Search newly imported annotations to see if they contain Complex Relationships
     * outside of the schema.
     *
     * @see ComplexRelImportReturn
     * @return The newly imported complex relationships that are outside of the current
     * schema.
     */
    public TreeSet<ComplexRelImportReturn> checkUniqueRegex()
    {
        ArrayList<Article> newArticles = getArticlesContainingNew();
        //####Extract ComplexRel objects from annotations####
        TreeSet<RelationshipDef> newlyExtracted = new TreeSet<RelationshipDef>();
        for (Article article : newArticles)
        {

            if (article.annotations == null)
                continue;

            for (Annotation annot : article.annotations)
            {
                if(annot.relationships == null)
                    continue;
                for (AnnotationRelationship rel : annot.relationships)
                {
                    String relName = rel.getMentionSlotID();
                    Vector<AnnotationRelationshipDef> linked = rel.getLinkedAnnotations();
                    //If invalid relationship just continue
                    if (relName == null || linked == null || linked.size() == 0)
                    {
                        continue;
                    }

                    //Create new Relationships
                    //ComplexRel newRel = new ComplexRel(relName, false);
                    //newRel.addFrom(annot.annotationclass);

                    //##########BUILD REGEX FOR CLASSES############
                    String regex = "(" + annot.annotationclass + ")";
                    ArrayList<Annotation> annots = new ArrayList<Annotation>();
                    annots.add(annot);
                    for(AnnotationRelationshipDef link: linked)
                    {

                        Annotation linkAnnot = article.getAnnotationByMention(link.mention);
                        annots.add(linkAnnot);
                        if((linkAnnot==null)||( linkAnnot.annotationclass == null ))
                        {
                            regex = null;
                            break;
                        }
                        String aToClass = linkAnnot.annotationclass;
                        regex += "(" + aToClass + ")";
                    }
                    if(regex != null)
                    {
                        /*Compare newly extracted relationship against existing relationships to see
                         * if the newly extracted relationship is a new, or just a repeat.
                         */
                        //Assume it is a new one
                        boolean newOne = true;
                        for(RelationshipDef existing: rels)
                        {
                            if(existing.getName().equals(relName))
                            {
                                if(existing.checkContains(annots))
                                {
                                    newOne = false;
                                    break;
                                }
                            }
                        }
                        if(newOne)
                        {
                            RelationshipDef newRel = new RelationshipDef(relName, false);
                            newRel.setAllowed(regex);
                            newlyExtracted.add(newRel);
                        }

                    }

                }
            }

        }
        //Combine new ones
        TreeSet<ComplexRelImportReturn> modified = combineSameNameRegex(newlyExtracted);


        //return
        return modified;

    }
    //</editor-fold>

    public void clear(){
        rels.clear();
    }

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Get all of the articles that contain newly imported Annotations
     * @return - any articles containing newly imported annotations
     */
    private ArrayList<Article> getArticlesContainingNew()
    {
        Depot depot = new Depot();
        ArrayList<Article> newArticles = new ArrayList<Article>();
        ArrayList<Article> articles = depot.getAllArticles();

        //####Find all articles that have newly imported annotations####
        for (Article article : articles)
        {
            Article newlyFound = null;
            for (Annotation annotation : article.annotations)
            {
                if (!annotation.isVerified())
                {
                    if (newlyFound == null)
                    {
                        newlyFound = new Article(article.filename);
                    }
                    newlyFound.addAnnotation(annotation);
                }
            }
            if (newlyFound != null)
            {
                newArticles.add(newlyFound);
            }
        }
        return newArticles;
    }
   
   
    /**
     *  * Combine a set of attributes so that any attributes that have the same name are combined into
     * Attribute object.  This is done by adding the allowedEntries of the repeat item to the
     * allowedEntries of the existing item(the one before it in the list).
     * @param atts - the attributes to retur
     * @return - the Attributes combined so that each one has a unique name and contains
     * all allowedEntries from before.
     */
    
    private TreeSet<ComplexRelImportReturn> combineSameNameRegex(TreeSet<RelationshipDef> atts)
    {
        //The Set to Return
        TreeSet<ComplexRelImportReturn> toReturn = new TreeSet<ComplexRelImportReturn>();
        ArrayList<ComplexRelImportReturn> modifiedReturn = new ArrayList<ComplexRelImportReturn>();
        for(RelationshipDef rel: atts)
        {
            ComplexRelImportReturn temp = new ComplexRelImportReturn(rel.getName());
            temp.addRegex(rel.getAllowed());
            modifiedReturn.add(temp);
        }

        //Loop through each element finding all repeat elements farther in the list
        for(int i = 0; i< modifiedReturn.size(); i++)
        {
            //Loop ahead of the current index to find any repeat names
            for(int j = i + 1; j< modifiedReturn.size(); j++)
            {
                //If the names match than the item at position j is a repeat so...
                //add all of the allowed entries in at position j to position i.
                if(modifiedReturn.get(i).getName().equals(modifiedReturn.get(j).getName()))
                {
                    modifiedReturn.get(i).getRegex().addAll(modifiedReturn.get(j).getRegex());
                    modifiedReturn.remove(j);
                    j--;
                }
            }
        }
        //Put the results back into a TreeSet and return
        toReturn.addAll(modifiedReturn);

        return toReturn;
    }
    //</editor-fold>

}
