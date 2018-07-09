 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.logging.Level;
import resultEditor.annotationClasses.navigationTree.dataCache.*;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;
import userInterface.GUI;

/**Depot for gathering and management of annotationText classnames.<p>
 *
 * @author Jianwei Chris Leng 2010-06-16 11:14pm
 */

public class Depot {

    /**list to store */
    protected static Vector<AnnotationClass> Classes= new Vector<AnnotationClass>();

    //####MUTED, as we integrate classname.cfg into the single schema file
    /**filename of the configure file of annotationText classnames*/
    //protected final String annotationClassnameConfigureFile = "classname.cfg";
    //####END OF MUTE

    /**debug flag*/
    private final boolean DEBUG = true;


    /**This method is designed to check whether a color has been used as a
     * highlight background color of a class. It will return true if color has
     * been used, or return false if the color are still available.
     *
     * @param   _color
     *          The color that we want to check whether it has been used as a
     *          highlight background color or not.
     *
     * @return  true if specific color has been used in some class;
     *          false if the color has not been used yet.
     */
    public static boolean isColorUsedInClass(Color _color){

        // null value can not been allocated to any class as highlight 
        // background color
        if(_color == null)
            return true;

        // usually, "Classes" could not be null
        // but, we recreate it if it's null
        if(Classes==null){
            Classes= new Vector<AnnotationClass>();
            return false;
        }

        // check each class to check whether the designated color has been used
        // or not
        for(AnnotationClass thisAnnotationClass: Classes){
            if(thisAnnotationClass==null)
                continue;

            /*System.out.println("("+thisAnnotationClass.backgroundColor.getRed()
                    +","
                    +thisAnnotationClass.backgroundColor.getGreen()
                    +","
                    +thisAnnotationClass.backgroundColor.getBlue()
                    +") - ("
                    + _color.getRed()
                    +", "
                    + _color.getGreen()
                    +", "
                    + _color.getBlue()
                    + " )");
            */if( (thisAnnotationClass.backgroundColor.getRed()==_color.getRed())
                    &&(thisAnnotationClass.backgroundColor.getGreen()==_color.getGreen())
                    &&(thisAnnotationClass.backgroundColor.getBlue()==_color.getBlue())
                    )
                return true;

        }

        //System.out.println("\n");

        // all other situations will be considered as that the color
        // has NOT been took yet.
        // We return false;
        return false;
    }

    
    /**empty the list of annotationText class names.*/
    public static void clear(){
        Classes.clear();
        annotationTypes.clear();
    }

    /**amount of all annotated classes*/
    public static int size(){
        return Classes.size();
    }

    /**add a list of annotation class names into current list. Repetitive checking
     * is needed before really added into the list.
     */
    public void addElements( Vector<AnnotationClass> classes ){
        // check and add each element in afferent list into list of depot
        for( AnnotationClass thisclass : classes ){
            if ( thisclass == null )    continue;
            addElement( thisclass );
        }
    }

    public static boolean isExisting( String classname ){
        // check repetitive
        if ( classname == null )    return true;
        if ( Classes == null )  return false;
        for( AnnotationClass thisclass : Classes ){
            if ( thisclass == null )    continue;
            if ( thisclass.annotatedClassName == null )    continue;
            if ( thisclass.annotatedClassName.trim().compareTo( classname.trim() ) == 0 ){
                return true;
            }
        }
        return false;
    }

    public void addElement(AnnotationClass thisclass){
        if ( thisclass == null )    return;
        try{
            addElement(
                    thisclass.annotatedClassName,
                    thisclass.source,
                    thisclass.backgroundColor,
                    thisclass.isVisible,
                    thisclass.isNodeExpanded );
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "1007180452 - " + e.toString());
        }
    }
    
    /**check and add a annotationText classname in to the list*/
    public static void addElement(String annotationClassname, String sources, Color color,
            boolean isVisible, boolean isExpanded ){

        addElement(  annotationClassname,   sources,   color,
              isVisible,   isExpanded, null, null );

    }
    

    /**check and add a annotationText classname in to the list*/
    public static void addElement(String annotationClassname, String sources, Color color,
            boolean isVisible, boolean isExpanded, String shortcomment, String desdoc ){

        // validity check
        if (annotationClassname == null)
            return;
       
        annotationClassname = annotationClassname.trim();
        if (sources != null ) sources = sources.trim();
        if((annotationClassname.length() < 1))
            return;

        // check repetitive         
        if( isExisting( annotationClassname.trim() ) ) {
            return;
        } else {
            
            if ( color == null ) color = defaultColor( annotationClassname.trim() );
            // add this annotationText classname into the list for storage
            if ( color == null ) color = Color.BLACK;
            
            AnnotationClass aclass = new AnnotationClass( annotationClassname.trim(), color, sources, isVisible, isExpanded );
            aclass.shortComment = shortcomment;
            aclass.des = desdoc;
                    

            Classes.add( aclass );
            //System.out.println("class has background color: " + color.toString() );
            //if(DEBUG==true)
            //  System.out.println("classname: ["
            //        + annotationClassname.trim()
            //        + "], sources = ["
            //        + cc.sources
            //        + "], color ="
            //        + cc.color.toString() + "] added into memory");
        }

    }

    private static Color defaultColor(String classname){
        log.LoggingToFile.log(Level.SEVERE, " Class-["+classname+"] has a null color value.");
        classname = classname.trim().toLowerCase();
        if( classname.compareTo("test")==0) return Color.CYAN;
        else if( classname.compareTo("treatment")==0) return Color.orange;
        //else if( classname.compareTo("problem")==0) return Color.BLUE;
        //else if( classname.compareTo("dark_gray")==0) return Color.DARK_GRAY;
        //else if( classname.compareTo("gray")==0) return Color.GRAY;
        //else if( classname.compareTo("green")==0) return Color.GREEN;
        //else if( classname.compareTo("light_gray")==0) return Color.LIGHT_GRAY;
        //else if( classname.compareTo("magenta")==0) return Color.MAGENTA;
        else if( classname.compareTo("problem")==0) return Color.PINK;
        else if( classname.compareTo("date")==0) return Color.RED;
        else if( classname.compareTo("ssn")==0) return Color.YELLOW;

        return null;
    }

    //public void addElement(String annotationClassname, String comments){
    //    addElement( annotationClassname, comments, null);
    //}

    public static void addElement(String annotationClassname, String comments, int r, int g, int b){
        
        Color rgb = new Color(r,g,b);
        addElement( annotationClassname, comments, rgb, true, false);

    }
    
    public static void addElement(String annotationClassname, String comments, int r, int g, int b, String shortcomment, String desdoc){
        
        Color rgb = new Color(r,g,b);
        addElement( annotationClassname, comments, rgb, true, false, shortcomment, desdoc);

    }


    /**This method is used to ensure the folder of config folder is existing
     * under current project. If it do not existed, build it. And then return
     * the folder. But, return will be NULL if we could not build the folder.
     */
    private File getConfigureFolder()
    {
        try{
            File project = env.Parameters.WorkSpace.CurrentProject;
            if(project==null)
                return null;

            if(!project.exists())
                return null;

            if(project.isFile())
                return null;

            String config = project.getAbsolutePath() + File.separator + "config";
            File configfolder = new File(config);

            if(!configfolder.exists()){
                return null;
            }

            return configfolder;

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1102110252::" + ex.toString());
        }

        return null;
    }




    //########################################################################//
    //##   THIS METHOD IS MUTED
    //##   AS WE INTEGREATE CONFIGURE FILE "CLASSNAME.CFG" INTO THE SINGLE
    //##   SCHEMA FILE "PROJECTSCHEMA.CFG" UNDER "CONFIG" FOLDER UNDER
    //##   THE FOLDER OF EACH PROJECT.
    //########################################################################//    
    /**load saved annotationText classnames from configure file into the list
     * "annotationClassnames" in this class. "annotationClassnames" is a
     * hashtable, key of the hashtable is annotationText classnames. And value
     * is attributes of it.
     * @return  int return the amount of imported annotationText class names;
     */
    /*public int loadAnnotationClassnamesFromConfigureFile()
    {

        // empty the hashtable before loading new contents
        removeAllElement();

        File config = getConfigureFolder();
        if(config==null)
            return -1;

        String classcfgpath = config.getAbsolutePath() + File.separator
                + annotationClassnameConfigureFile;
        int count = 0;
        File file = new File( classcfgpath );

        // return, if file do NOT existed or NOT be a file
        if((!file.exists())||(!file.isFile()))
            return 0;

        // keep handle configure file for annotationText classnames, if it existed.
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            // debug
            if(DEBUG==true) 
                System.out.println("starting reading annotation configure file");
            line = reader.readLine();

            // read each line of the configure file of annotationText classnames
            while (line != null){
                line = line.trim();
                if(line.length()<1){
                    line = reader.readLine();
                    continue;
                }

                // do nothing, if first character is ';', means sources
                if(line.charAt(0)==';') {
                    line = reader.readLine();
                    continue;
                }

                // check the span surrounded with '[' and ']'
                if ((line.charAt(0)=='[')&&(line.charAt(line.length()-1)==']')) {

                    int r=0, g=0, b=0;
                    String comments = null;// get annotationClassname
                    String annotationClassname = line.substring(1, line.length() - 1 );                    
                    // do nothing, if annotationText classname is "" or " "
                    annotationClassname = annotationClassname.trim();
                    if (annotationClassname.length() < 1) {
                        line = reader.readLine();
                        continue;
                    }
                    
                    // handle all sentences before next line who are surround with backets '[' and ']'
                    do{
                        line = reader.readLine();
                        if( line == null ) { break;}
                        
                        // stop if meet another annotated class entry
                        if ((line != null )&&(line.trim().length() > 0)){
                            if (line.contains("[")||(line.contains("]")))
                                break;
                        }

                        // try to get rbg color code
                        if( (line.contains("("))&&(line.contains(")")) ){
                                String colorstr = line.replaceAll("\\(", " ");
                            colorstr = colorstr.replaceAll("\\)", " ").trim();
                            try{
                                String[] rgbs = colorstr.split(",");
                                if (rgbs.length == 3){
                                    r = Integer.valueOf( rgbs[0].trim() );
                                    g = Integer.valueOf( rgbs[1].trim() );
                                    b = Integer.valueOf( rgbs[2].trim() );
                                }
                            }catch(Exception e){ }

                        } else { // try to get sources
                            if( line.trim().length() > 0 )
                                comments = line.trim();
                        }

                        
                        
                    } while(line!=null);

                    //System.out.println("import class and its color information: class=" + annotationClassname.trim());
                    // save
                    if ((r>=0)&&(b>=0)&&(g>=0)){
                        // check and save this annotationText classname into
                        // hashtable of this class.
                        addElement( annotationClassname.trim(), comments, r, g, b);
                    }else {
                        addElement( annotationClassname.trim(), comments, null, true, false );
                    }
                    
                } else {

                    // go next line if possible
                    line = reader.readLine();
                }
            }
            reader.close();
            if(DEBUG==true)
                System.out.println("end of reading annotation configure file");

        } catch (Exception e) {
            System.out.println("1007180453 - " + e.toString());
        }

        // method end
        return count;
    }*/


    /**This method is used to ensure the folder of config folder is existing
     * under current project. If it do not existed, build it. And then return
     * the folder. But, return will be NULL if we could not build the folder.
     */
    private File ensureConfigureFolder()
    {
        try{
            File project = env.Parameters.WorkSpace.CurrentProject;
            if(project==null)
                return null;

            if(!project.exists())
                return null;

            if(project.isFile())
                return null;

            String config = project.getAbsolutePath() + File.separator + "config";
            File configfolder = new File(config);

            if(!configfolder.exists()){
                if(!configfolder.mkdirs())
                    return null;                
            }

            return configfolder;

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1102110252::" + ex.toString());
        }

        return null;
    }




    //########################################################################//
    //##   THIS METHOD IS MUTED
    //##   AS WE INTEGREATE CONFIGURE FILE "CLASSNAME.CFG" INTO THE SINGLE
    //##   SCHEMA FILE "PROJECTSCHEMA.CFG" UNDER "CONFIG" FOLDER UNDER
    //##   THE FOLDER OF EACH PROJECT.
    //########################################################################//
    /**Save all annotationText classname stored in hashtable of this class into
     * a configure table.
     */
    /*public void saveAnnotationClassnamesToConfigureFile(){

        try{
            
            // get path of current project
            File config = ensureConfigureFolder();
            if(config==null)
                return;
            
            String classcfg = config.getAbsolutePath() + File.separator 
                    + annotationClassnameConfigureFile;


            FileOutputStream output = new FileOutputStream( classcfg );

            PrintStream p = new PrintStream(output);
            String outstr;

            // sources
            String comments = ";Configure file for storage of annotaion classnames\n";
            comments += ";Words surround with character '[' and ']' are annotation classnames.\n";
            comments += ";Line below each annotation classname is attribute" 
                      + " of this annotation classname.\n";
            p.println(comments);

            // get and save all annotationText classname and its attribues
            for( AnnotatedClass thisclass: Classes ){
                String key = thisclass.annotatedClassName;
                if (( key == null )||(key.trim().length() < 1))   continue;
                outstr = "[" + key + "]";
                p.println( outstr );
                if ( thisclass.backgroundColor != null ) {
                    p.println( "( " + thisclass.backgroundColor.getRed() + ", " + thisclass.backgroundColor.getGreen()
                             + ", " + thisclass.backgroundColor.getBlue() + " )");
                } else {
                    p.println( "( -1, -1, -1 )");
                }

                p.println( thisclass.source + "\n" );
            }

            // save and closed
            p.close();

        } catch(Exception e) {
            System.out.println("error 1103301926:: userInterface.annot" +
                    "ationclassname.depot.java - 01 - "+e.toString());
        }


    }*/

    public String[] getAnnotationClasssnamesString(){
        if ( Classes == null )
            return null;

        // prepare the String array for return
        int length = Classes.size();
        String[] results =  new String[length];

        int pointer = 0;
        // get all annotationText classnames in string
        for( AnnotationClass thisclass : Classes ){
            if ( thisclass == null )
                continue;
            results[pointer] = thisclass.annotatedClassName;
            pointer++;
        }
        return results;
    }

    /**get display background color for a specific class*/
    public static Color getColor(String classname) {
        
        if ( ( Classes == null) || ( classname == null ) )
            return null;
        if( classname.trim().length() < 1 )
            return null;
        
        
        // return null if no matched annotated class
        if ( !isExisting( classname.trim() ) )
            return null;
        
        // get display color of this annotated class if we can
        for( AnnotationClass thisclass : Classes ) {
            if ( thisclass == null )
                continue;
            if ( thisclass.annotatedClassName.trim().compareTo( classname.trim() ) == 0 )
                return thisclass.backgroundColor;
        }
        
        return null;

    }

    /**return all data*/
    public final Vector<AnnotationClass> getAll(){
        return Classes;
    }


    public void changeColor(String classname, Color color){
        if ((classname == null )||( color == null )) return;
        
        if( isExisting( classname.trim() ) ) {
            if ( Classes == null )  return;
            for( AnnotationClass thisclass : Classes ){
                if ( thisclass.annotatedClassName.trim().compareTo(classname.trim()) == 0 ){
                    thisclass.backgroundColor = color;
                    return;
                }
            }
        }
    }

     public void changeClassname(String oldclassname, String newclassname){
         if (( oldclassname == null )||( newclassname == null )) return;
         if (( oldclassname.trim().length() < 1 )||( newclassname.trim().length() < 1 )) return;

         if( !isExisting( oldclassname.trim() ) )
            return;          

         for( AnnotationClass thisclass : Classes ) {
             if ( thisclass == null )   continue;
             if ( thisclass.annotatedClassName.trim().compareTo( oldclassname.trim() ) == 0 ){
                thisclass.annotatedClassName = newclassname.trim();
                return;
             }
         }
    }

     public void delete(String classname){
         if(( classname == null )||( classname.trim().length() < 1))
             return;
         if( Classes == null )
             return;

         if( !isExisting( classname.trim() ) )
            return;

         for( int i = 0; i < size(); i++ ){
             AnnotationClass thisclass = Classes.get(i);
             if ( thisclass == null )
                 continue;
             if ( thisclass.annotatedClassName.trim().compareTo( classname.trim() ) == 0 ){
                Classes.remove(i);
                return;
             }
         }
     }

     /**sort the array by string of annotated classname */
     public void sort(){
        Comparator comp = new AnnotatedClassComparator();
        Collections.sort( Classes , comp);
     }

     /**to annotationText, record type of this annotationText and count numbers to its related class.*/
     public void recordAnnotation( String filename, resultEditor.annotations.Annotation annotation ){
         if( annotation == null )
             return;
         if (( annotation.annotationText == null )||(annotation.annotationclass == null))
             return;

         // if in adjudication mode, we only need to count information about 
         if(GUI.reviewmode == GUI.ReviewMode.adjudicationMode){
            if((annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK )
                &&(annotation.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES))
                return;
         }
         
         String classname = annotation.annotationclass;
         String annotationText = annotation.annotationText;
         // validity check         
         if ( ( classname.trim().length() < 1 )||( annotationText.trim().length() < 1 ) )
             return;

         if ( !isExisting( classname.trim() ) )
             return;

         // get designated annoatated class
         AnnotationClass thisclass = getAnnotatedClass( classname.trim() );
         if( thisclass == null )
             return;

         thisclass.recordAnnotation( annotationText.trim(), filename, annotation );

         if ((filename != null)&&(filename.trim().length() > 0))
            TYPE_recordAnnotation( filename.trim(), annotation );

     }


     /**
      * Find how many different types of markable/class to a given file or all
      * files.
      *
      * @param  currentArticle
      *         If currentArticle equal to "null", means we need to find
      *         types for all files.
      * 
      * @param  classname
      *         it will be ignore while it's null.
      */
     public void checkAllAnnotationTypes(String currentArticle, String classname  ){
         checkAllAnnotationTypes( currentArticle,  classname, false );
     }
     public void checkAllAnnotationTypes(String currentArticle, String classname, boolean forceupdate ){
         
         // ##1.1## reset annotationText counter to each annotated class memory
         DepotOfAttributes.smartReset( classname, env.Parameters.WorkSpace.CurrentProject.getAbsolutePath() );
         DepotOfRelationships.clear();
         
         if (( Depot.Classes != null)){
             for( AnnotationClass ac : Depot.Classes ){
                 if ( ac == null )
                     continue;
                 ac.resetAmount_of_AnnotationCount();
                 ac.resetDepot_of_AnnotationTypes();
             }
         }
         
         

         // ##1.2## empty the memory space which record all annotationText type
         // information for all import files;
         this.TYPE_resetAnnotationTypes();


        // ##2## go over all annotation in depot of annotation and record them to related class
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        if ((depot == null) || (depot.getAllArticles() == null)) {
            return;
        }

        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
        ArrayList<resultEditor.annotations.Article> articles = null;
        
        
        
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
            articles = depotOfAdj.getAllArticles();
        else
            articles = depot.getAllArticles();
        
        if( articles == null )
            return;
        
        for (resultEditor.annotations.Article article : articles ) {
            if (article == null) {
                continue;
            }
            if ((currentArticle != null) && (currentArticle.trim().length() > 0)) {
                if ((article.filename == null) || (article.filename.trim().length() < 1)) {
                    continue;
                }
                if (article.filename.trim().compareTo(currentArticle.trim()) != 0) {
                    continue;
                }
            }
            for (resultEditor.annotations.Annotation annotation : article.annotations) {
                if (annotation == null) {
                    continue;
                }


                recountAttribute( annotation );
                
                // filter by attributes and their values
                if (filterByAttribute(annotation)) {
                    continue;
                }

                
                
                this.recordAnnotation(article.filename.trim(), annotation);

                DepotOfRelationships.count(annotation, annotation.relationships, article.filename);


                //DepotOfAttributes.count( annotation, annotation.attributes );

            }
        }
    }
     
    private void recountAttribute(Annotation annotation) {
        if (annotation == null) {
            return;
        }
        if (annotation.attributes == null) {
            return;
        }

        //if( annotation.visible == false )
        //    return;

        if (annotation.annotationclass == null) {
            return;
        }

        if ((GUI.reviewmode == GUI.ReviewMode.adjudicationMode)
                && (annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)) {
            return;
        }

        for (AnnotationAttributeDef attDef : annotation.attributes) {
            if (attDef == null) {
                continue;
            }

            if ((attDef.name == null) || (attDef.name.trim().length() < 1)
                    || (attDef.value == null) || (attDef.value.trim().length() < 1)) {
                continue;
            }
            
            // to public
            // NULL_NULL_NULL
            {
                AttributesOfAClass attributeOfClass = DepotOfAttributes.attributeDepot.get("NULL_NULL_NULL");
                if (attributeOfClass == null) {
                    System.out.println("ERROR: A- failed to get the attribute of this class.");
                    continue;
                }

                Att att = attributeOfClass.attributes.get(attDef.name.trim());
                if (att == null) {
                    //NEEDNEED
                    continue;
                }

                AttValue attvalue = att.values.get(attDef.value.trim());
                if (attvalue == null) {
                    //NEEDNEED
                    continue;
                }

                attvalue.count = attvalue.count + 1;

                att.values.put(attDef.value.trim(), attvalue);

                att.count = att.count + 1;
                attributeOfClass.attributes.put(attDef.name.trim(), att);
                DepotOfAttributes.attributeDepot.put("NULL_NULL_NULL", attributeOfClass);
            }
            

            {
                AttributesOfAClass attributeOfAAttribute = DepotOfAttributes.attributeDepot.get(annotation.annotationclass.trim());
                if (attributeOfAAttribute == null) {
                    System.out.println("ERROR: failed to get the attribute of this class:" + annotation.annotationclass.trim() );
                    continue;
                }

                Att att = attributeOfAAttribute.attributes.get(attDef.name.trim());
                if (att == null) {
                    //NEEDNEED
                    continue;
                }

                AttValue attvalue = att.values.get(attDef.value.trim());
                if (attvalue == null) {
                    //NEEDNEED
                    continue;
                }

                attvalue.count = attvalue.count + 1;

                att.values.put(attDef.value.trim(), attvalue);

                att.count = att.count + 1;
                attributeOfAAttribute.attributes.put(attDef.name.trim(), att);
                DepotOfAttributes.attributeDepot.put(annotation.annotationclass, attributeOfAAttribute);
            }

            
        }
    }


     
    public boolean filterByAttribute(Annotation annotation ) {
        
        return DepotOfAttributes.filterByAttribute(annotation);
        
    }
     /**Get annotated class in the depot of all annotated class by classname.*/
     public AnnotationClass getAnnotatedClass( String annotatedClassname ) {
         if ( annotatedClassname == null )
             return null;

         if ( Classes == null )
             return null;

         for( AnnotationClass thisclass : Classes ) {
             if ( thisclass == null )
                 continue;

             if ( thisclass.annotatedClassName.trim().compareTo( annotatedClassname.trim() ) == 0 )
                 return thisclass;
         }

         return null;
    }

    //=================== overall annotationText types ===========================//
    protected static Vector<AnnotationType> annotationTypes = new Vector<AnnotationType>();

    /**check existing of a type of annotationText. */
    private boolean TYPE_isAnnotationTypeExisting( String annotation ){
        if ( ( annotation == null )||( annotation.trim().length() < 1 ))
            return false;

        for( AnnotationType annotationtype : annotationTypes ){
            if ( annotationtype == null )
                continue;
            if( annotationtype.name.compareTo( annotation.trim() ) == 0 ){
                return true;
            }
        }
        return false;
    }

    private void TYPE_annotationTypePlus(String annotationText, String filename, Annotation annotation){
        if ( ( annotationText == null )||( annotationText.trim().length() < 1 ))
            return;

        for( AnnotationType annotationtype : annotationTypes ){
            if ( annotationtype == null )
                continue;
            if( annotationtype.name.compareTo( annotationText.trim() ) == 0 ){
                annotationtype.amount = annotationtype.amount + 1;

                file_annotation fa = new file_annotation();
                fa.filename = filename;
                fa.annotation = annotation;

                annotationtype.subAnnotations.add(fa);
                //System.out.println(" + " + fa.filename);

                return;
            }
        }
    }

    public final Vector<AnnotationType> TYPE_getAnnotationTypes(){
        TYPE_sort();
        return Depot.annotationTypes;
    }

    public void TYPE_sort(){
        Comparator comp = new AnnotationTypeComparator();
        Collections.sort( Depot.annotationTypes , comp);
    }

    /**to an annotationText, record it if it's a new type; or count++ if existed.*/
    public void TYPE_recordAnnotation( String filename, Annotation annotation ){
        if ( annotation == null )
            return;
        if ( annotation.annotationText == null )
            return;
        String annotationText = annotation.annotationText.trim();
        
        if ( ( annotationText == null )||( annotationText.length() < 1 ))
            return;



        if( this.TYPE_isAnnotationTypeExisting( annotationText ) ){
            TYPE_annotationTypePlus( annotationText, filename, annotation );
        } else {

            AnnotationType at = new AnnotationType( annotationText.trim(), 1, annotation.spanset.getSpanAt(0).start );

            file_annotation fa = new file_annotation();
            fa.filename = filename;
            fa.annotation = annotation;

            at.subAnnotations.add(fa);

            annotationTypes.add(at);
        }
    }

    public void TYPE_resetAnnotationTypes(){
        Depot.annotationTypes.clear();
    }

    public static int TYPE_getAnnotationTypeAmount(){
        if ( annotationTypes == null )
            return 0;

        return annotationTypes.size();
    }

 
    /**
     * To a designated class/markable entry, set its expansion status.
     * 
     * @param   classname
     *          Name of the class/markable.
     * 
     * @param   isExpanded
     *          True if expanded; false if not expanded.
     */
    public void setClassExpanded( String classname , boolean isExpanded){
        if ( classname == null )
            return;

        if ( classname.trim().length() < 1 )
            return;

        if ( Classes == null  )
            return;

        for ( AnnotationClass this_annotation_class : Classes ){
            if ( this_annotation_class == null )
                continue;

            if ( this_annotation_class.annotatedClassName.trim().equals( classname.trim() ) ){
                this_annotation_class.setExpansionStatus( isExpanded );
                return;
            }
        }

        log.LoggingToFile.log(Level.SEVERE, "1008152333 - fail to set expansion status to node - " + classname );
    }

    void setShortComment(String classname, String text) {        
         if ( classname == null ) return;
         if ( classname.trim().length() < 1 ) 
             return;

         if( this.getAnnotatedClass( classname.trim() ) == null )
             return;        

         for( AnnotationClass thisclass : Classes ) {
             if ( thisclass == null )   continue;
             if ( thisclass.annotatedClassName.trim().compareTo( classname.trim() ) == 0 ){
                thisclass.shortComment = text;
                return;
             }
         }
    
    }
    
    void setDes(String classname, String text) {        
         if ( classname == null ) return;
         if ( classname.trim().length() < 1 ) 
             return;

         if( this.getAnnotatedClass( classname.trim() ) == null )
             return;        

         for( AnnotationClass thisclass : Classes ) {
             if ( thisclass == null )   continue;
             if ( thisclass.annotatedClassName.trim().compareTo( classname.trim() ) == 0 ){
                thisclass.des = text;
                return;
             }
         }
    
    }
}
