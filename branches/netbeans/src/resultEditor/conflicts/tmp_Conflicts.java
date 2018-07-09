/*
 * This class is used to to keep track of conflicts between annotations within the working set.
 * last modified on June 29, 2011
 */
package resultEditor.conflicts;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;


public class tmp_Conflicts
{
    /**
     * Get All class conflicts between this file and the working set.  Only class conflicts
     * which involve annotations from the given fileName will be returned.
     * @param fileName - File to find class conflicts from.
     * @return - All Class conflicts involving this file.
     */
    public static Vector<classConflict> getClassConflicts(String fileName)
    {
        try{
        //Get Annotations from 'this' file.
        Depot depot = new Depot();
        Article source = Depot.getArticleByFilename(fileName);
        
        //Match Annotations with their fileNames(Two corresponding vectors)
        //fileNames.get(i) contains the fileName for conflictedAnnotations.get(i)
        Vector<String> fileNames = new Vector<String>();
        Vector<Annotation> conflictedAnnotations = new Vector<Annotation>();

        //Loop through all Annotations from our source file
        for (Annotation annotation : source.annotations)
        {
            if(annotation.annotationText == null || annotation.annotationclass == null)
                continue;
            //These two ArrayLists will hold Annotations that have matching text and classes
            //We keep track of these just in case we find a conflict later on... we want the counts
            // of how many times each class appeared.
            ArrayList<Annotation> matches = new ArrayList<Annotation>();
            ArrayList<String> matchFileNames = new ArrayList<String>();

            //True if we want to record the above matches for our class conflict
            boolean reportingAll = false;
            
            try{
                ArrayList<Article> articles = depot.getAllArticles();
                if(articles==null)
                    continue;


                //Loop through all Articles to find class conflicts
                for (Article otherArticle : articles)
                {
                    if((otherArticle==null)||(otherArticle.annotations==null))
                        continue;
                    //Loop through all Annotations (could be frome source or not from source)
                    for (Annotation otherAnnotation : otherArticle.annotations)
                    {
                        if(otherAnnotation==null)
                            continue;

                        if(otherAnnotation.annotationText == null || otherAnnotation.annotationclass == null)
                            continue;
                        //If text is equal but class is not then we have a class conflict...
                        if (annotation.annotationText.toLowerCase().equals(otherAnnotation.annotationText.toLowerCase())
                                && !(annotation.annotationclass.equals(otherAnnotation.annotationclass)))
                        {
                            //Add conflictedAnnotations and their fileNames
                            conflictedAnnotations.add(annotation);
                            fileNames.add(source.filename);
                            conflictedAnnotations.add(otherAnnotation);
                            fileNames.add(otherArticle.filename);

                            //Set to true so all matching classes will be counted as well
                            reportingAll = true;
                        }
                        //Matching text and class - keep track of these in case we find a conflict later on.
                        if (annotation.annotationText.toLowerCase().equals(otherAnnotation.annotationText.toLowerCase())
                                && (annotation.annotationclass.equals(otherAnnotation.annotationclass)))
                        {
                            //Add the annotation and its' filename
                            matches.add(otherAnnotation);
                            matchFileNames.add(otherArticle.filename);
                        }
                    }
                }
                //If we found a conflict then add previously non conflict entries to the conflict list
                //Because they are now involved in a class conflict.
                if (reportingAll)
                {
                    for (int i = 0; i < matches.size(); i++)
                    {
                        conflictedAnnotations.add(matches.get(i));
                        fileNames.add(matchFileNames.get(i));
                    }
                }
            }catch(Exception ex){
                log.LoggingToFile.log(Level.SEVERE, "#### ERROR  #### 1106291232:: "
                        + ex.getMessage());
            }
        }

        //getConflicts will sort through all the Annotations involved in Conflicts and return
        //the classConflict objects
        return getConflicts(fileNames, conflictedAnnotations, fileName);
        }catch(Exception ex){
            return null;
        }

    }
    /**
     * This function will return all overlapping(non-equal) annotations that are in the file
     * with the given fileName
     *
     * @param fileName - the file name to find span overlaps for.
     * @return - spanOverlaps objects for all annotations with overlapping spans.
     */
    public static Vector<spanOverlaps> getSpanConflicts(String fileName)
    {
 
        Article source = Depot.getArticleByFilename(fileName);
        Vector<spanOverlaps> problems = new Vector<spanOverlaps>();
        Vector<Annotation> allAnnotations = new Vector<Annotation>();
        allAnnotations.addAll(source.annotations);

        //Loop through all annotations to find span overlaps
        for (int i = 0; i < allAnnotations.size(); i++)
        {
            //This will store all Vectors currently in this span conflict
            Vector<Annotation> forThisProblem = new Vector<Annotation>();

            //Extract the currently working annotation
            Annotation firstPass = allAnnotations.get(i);

            //Get any overlapping annotations for this Annotatoins
            Vector<Annotation> temp = getOverLappingForSpecificAnnotation(firstPass, allAnnotations);
            forThisProblem.addAll(temp);

            //If there is more than one Annotation in here then it is probably a conflict
            if (forThisProblem.size() > 1)
            {
                //Create a new spanOverlaps object from the conflicted Annotations
                spanOverlaps toAdd = new spanOverlaps(forThisProblem);

                //Make sure this possible conflict has overlapping spans - could just be
                //exact copies
                if(toAdd.isGood())
                {
                    //Add the Annotation to our return list
                    problems.add(toAdd);

                    //Remove all involved elements so we don't use them again.
                    for(Annotation toRemove: toAdd.getInvolved())
                    {
                        //Loop through all annotations to remove duplicates
                        for(int j = 0; j< allAnnotations.size(); j++)
                        {
                            Annotation maybeRemove = allAnnotations.get(j);

                            //Check for matching spans and text... remove
                            if( toRemove.spanset.isDuplicates( maybeRemove.spanset ) )                                
                            {
                                //Remove duplicate annotation
                                allAnnotations.remove(j);

                                //Have to redo index j cause we deleted the annotation that used
                                //to hold this spot
                                j--;
                            }
                        }
                    }

                    //If we found a match then we'll have to reDo index i cause we deleted
                    //the annotation that used to hold this spot.
                    i--;
                }
            }
        }
        return problems;
    }
    /**
     * This function will find all overlapping annotations for a given annotation.
     * Recursive.
     * @param check - The annotation to find overlapping spans with
     * @param checkAgainst - All other Annotations
     * @return
     */
    private static Vector<Annotation> getOverLappingForSpecificAnnotation(Annotation check, Vector<Annotation> passedIn)
    {
        Vector<Annotation> toReturn = new Vector<Annotation>();
        try{
        Vector<Annotation> checkAgainst = new Vector<Annotation>();
        checkAgainst.addAll(passedIn);
        //Return Vector(contains all overlapping annotations with this Annotation).
        

        //Loop through all other annotations
        for (int i = 0; i < checkAgainst.size(); i++)
        {
            //Extract the annotation to compare against
            Annotation annotation = checkAgainst.get(i);

            //Make sure it isn't the annotation we're checking against.
            if (check.equals(annotation))
            {
                continue;
            }
            //If it is overlapping then add it to our return array and remove from the array that we're checking against
            //if (isOverlapping(check.spanstart, check.spanend, annotation.spanstart, annotation.spanend))
            if ( check.spanset.isOverlapping( annotation.spanset ))
            {
                //Add an overlapping annotation
                Annotation overLapping = annotation;
                toReturn.add(overLapping);

                //Remove from our compare list to prevent future overlaps
                checkAgainst.remove(i);

                //Decrement i so we don't miss the one after the one we deleted.
                i--;

                //Recursively get all Annotations that overlap with the one we just found.
                toReturn.addAll(getOverLappingForSpecificAnnotation(overLapping, checkAgainst));
                for(Annotation toRemove: toReturn)
                {
                    for(int j = 0; j< checkAgainst.size(); j++)
                    {
                        Annotation maybeRemove = checkAgainst.get(j);
                        if(toRemove.spanstart == maybeRemove.spanstart && toRemove.spanend == maybeRemove.spanend && toRemove.annotationText.toLowerCase().trim().equals(maybeRemove.annotationText.toLowerCase().trim()))
                        {
                            checkAgainst.remove(j);
                            j--;
                        }
                    }
                }
                checkAgainst.removeAll(toReturn);
            }
        }
        }catch(Exception ex){
            
        }
        return toReturn;
    }

    /**
     * This method will determine if two annotations are overlapping
     * @param firstStart - start of the first annotation
     * @param firstEnd - end of the first annotation
     * @param secondStart - start of the second annotation
     * @param secondEnd - end of the second annotation
     * @return - true if they overlap, false otherwise
     */
    private static boolean isOverlapping(int firstStart, int firstEnd, int secondStart, int secondEnd)
    {
        //first start is inbetween second start and end(or equal)
        if (firstStart >= secondStart && firstStart <= secondEnd)
        {
            return true;
        }
        //first end is inbetween second start and end(or equal)
        if (firstEnd >= secondStart && firstEnd <= secondEnd)
        {
            return true;
        }
        //second start is inbetween first start and end(or equal).
        if (secondStart >= firstStart && secondStart <= firstEnd)
        {
            return true;
        }
        //second end is inbetween first start and end(or equal)
        if (secondEnd >= firstStart && secondEnd <= firstEnd)
        {
            return true;
        }
        return false;
    }

    /**
     * This function will sort through all Annotations involved in classConflicts and return
     * the classConflict objects.
     *
     * @param fileNames - File names for each Annotation(has to match annotations Vector)
     * @param annotations - All annotations to compare(has to match fileNames Vector)
     * @param thisFile - The name of the current file
     * @return
     */
    private static Vector<classConflict> getConflicts(Vector<String> fileNames, Vector<Annotation> annotations, String thisFile)
    {
        //Vector to return
        Vector<classConflict> conflictWithWorking = new Vector<classConflict>();
        //Annotations from the source
        Vector<Annotation> fromSource = new Vector<Annotation>();
        
        //Find all annotations from the Source File
        for (int i = 0; i < annotations.size(); i++)
        {
            if (fileNames.get(i).equals(thisFile))
            {
                fromSource.add(annotations.get(i));
            }
        }
        
        //Remove 'source' annotations from regular list.
        for(int i = 0; i< fromSource.size(); i++)
        {
            annotations.remove(fromSource.get(i));
        }

        //Loop through all source Annotations that are part of conflicts
        //to make sure we create all conflicts that need to be created.
        for (Annotation source : fromSource)
        {
            //Assume this one is a new or 'unique' annotation
            boolean unique = true;

            //The conflict we're currently working on
            classConflict toChange = null;

            //Loop through all current Conflicts to see if we already have one for this
            //Annotation, to avoid adding a repeat conflict.
            for (int i = 0; i < conflictWithWorking.size(); i++)
            {
                //Get the conflict
                classConflict aConflict = conflictWithWorking.get(i);

                //If it is the same annotation text then It is the same Conflict
                if (aConflict.getText().toLowerCase().equals(source.annotationText.toLowerCase()))
                {
                    //This one isn't unique so set toChange to the existing one.. we still need to add
                    //some things to it.
                    unique = false;
                    toChange = aConflict;
                    conflictWithWorking.remove(aConflict);
                    toChange.addSource(source);
                }
            }
            //If this conflict does not exist yet then create it.
            if (unique)
            {
                toChange = new classConflict(source);
            }
            //Add all matches that are not from the source file.
            for (Annotation notSource : annotations)
            {
                //If the text is equal then it belongs to this conflict
                if (toChange.getText().toLowerCase().equals(notSource.annotationText.toLowerCase()))
                {
                    //Add as a annotation that is not from the source
                    toChange.addNotSource(notSource);
                }
            }
            //Add this conflict to be reuturned.
            conflictWithWorking.add(toChange);
        }
        return conflictWithWorking;
    }
}
