package preAnnotate.readers;

import resultEditor.annotations.Article;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import preAnnotate.format.Annotator;

/** This is a helper class for BatchFolderExtract which does the low level string manipulations required
 *  to extract the information that BatchFolderExtract needs.
 *
 * @author Kyle
 *
 */
public class InformationExtractor
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private pinsFile user;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * The constructor for an InformationExtractor object.
     */
    public InformationExtractor(pinsFile user)
    {
        this.user = user;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This method will extract a dictionary entry from an ArrayList of lines.
     *
     * @param theInfo - The lines that make up the dictionary entry
     * @return An ArrayList with the instanceNum in the first index and the class in the second index
     * if they were extracted correctly, otherwise return null.
     */
    public ArrayList<String> dictionaryExtractor(ArrayList<String> theInfo)
    {
        ArrayList<String> toReturn = new ArrayList<String>();

        //Patterns for matching
        Pattern classPattern = Pattern.compile("knowtator_mention_class");
        Pattern referencePattern = Pattern.compile("knowtator_mention_annotation");

        //The two pieces of information that we need.
        String classOfText = null;
        String instanceNum = null;

        //Go through all lines that represent this dictionary entry
        for (String infoLine : theInfo)
        {
            //Line matchers
            Matcher matchClass = classPattern.matcher(infoLine);
            Matcher matchReference = referencePattern.matcher(infoLine);

            //If this is a reference# line extract the number
            if (matchReference.find())
            {
                String[] divided = infoLine.split("_");
                instanceNum = divided[divided.length - 1].split("]")[0];
                instanceNum = infoLine.substring(infoLine.indexOf("["), infoLine.lastIndexOf("]"));
            }
            //if this is a class line than extract the class
            else if (matchClass.find())
            {
                String[] temp = infoLine.split(" ");
                classOfText = temp[1].substring(0, temp[1].indexOf(")"));
            }
        }
        //If the instance number is not null add it to the ArrayList
        if (instanceNum != null)
        {
            toReturn.add(instanceNum);
        }

        //If the classOfText is not null then transform it to be in the set {Test, Problem, Treatment}
        if (classOfText != null)
        {
            //Test pattern matcher
            Pattern testPattern = Pattern.compile("Test", Pattern.CASE_INSENSITIVE);
            Matcher testClass = testPattern.matcher(classOfText);
            //Problem pattern matcher
            Pattern problemPattern = Pattern.compile("Problem", Pattern.CASE_INSENSITIVE);
            Matcher problemClass = problemPattern.matcher(classOfText);
            //Treatment pattern matcher
            Pattern treatmentPattern = Pattern.compile("Treatment", Pattern.CASE_INSENSITIVE);
            Matcher treatmentClass = treatmentPattern.matcher(classOfText);
            //Check what kind of instance it is and set the class to that.
            if (testClass.find())
            {
                classOfText = "Test";
            }
            else if (problemClass.find())
            {
                classOfText = "Problem";
            }
            else if (treatmentClass.find())
            {
                classOfText = "Treatment";
            }
            toReturn.add(classOfText);
        }

        //If it was created successfully then return it.
        if (toReturn.size() == 2)
        {
            return toReturn;
        }
        //If not created successfully return null
        else
        {
            user.setErrorMessages(user.getErrorMessages() + "[ERROR](Invalid mentionID->class format) Check near line " + user.getLineNum() + "\n");
            return null;
        }
    }

    public String fileExtractor(String file)
    {
        String[] afterSplit = file.split("\\[|]");
        return afterSplit[1];
    }

    /** This method will extract annotation information from the lines that constitute an annotation
     * object.
     *
     * @param theInfo - All of the lines that represent an annotation
     * @param theAnnotators - All annotators that we are currently looking for.
     * @param m - The map of InstanceNumbers to Classes
     * @return - A line that is ready to be put in a .annotations file if extracted correctly(The text and the
     * classOfText are guaranteed to be initialized), otherwise null.
     */
    public Article annotationsExtractor(ArrayList<String> theInfo, ArrayList<Annotator> theAnnotators, ArrayList<String> theClasses, ArrayList<String> chosenFiles)
    {
        //Pattern to find the annotator for this annotations
        Pattern annotatorPattern = Pattern.compile("knowtator_annotation_annotator");
        boolean chosenAnnotator = false;
        Map<String, String> m = user.getWordsToClass();

        //Variables that represent an annotation
        String textClass = "\"n/a\"";
        String date = "\"n/a\"";
        String range = "\"n/a\"";
        String text = "\"n/a\"";
        String source = "\"n/a\"";
        String annotatorInFile = "\"n/a\"";
        Annotator thisAnnotator = null;


        if (theAnnotators != null)
        {
            //Search through all of the lines in an attempt to find the line that specifies the annotator
            for (String searchAnnotator : theInfo)
            {
                //The matcher for an annotator line
                Matcher matchAnnotator = annotatorPattern.matcher(searchAnnotator);

                //If this is an annotator line then check to see if the annotator is one of our chosen
                //filter annotators
                if (matchAnnotator.find())
                {
                    //Should be something like: (knowtator_annotation_annotator [beth1_Instance_0])
                    String[] temp = searchAnnotator.split("\\[|]");
                    String annotatorInFileName = temp[1];
                    for (Annotator annotator : theAnnotators)
                    {
                        if (annotator.getAnnotatorFileNames().contains(annotatorInFileName))
                        {
                            thisAnnotator = annotator;
                            annotatorInFile = annotatorInFileName;
                            chosenAnnotator = true;
                        }
                    }
                }
            }
        }
        else
        {
            chosenAnnotator = true;
        }

        //If this annotation was not done by one of the chosen annotators then return null
        if (!chosenAnnotator)
        {
            return null;
        }

        //Patterns for each line we care about
        Pattern startPattern = Pattern.compile("knowtator\\+annotation");
        //Pattern mentionPattern = Pattern.compile("knowtator_annotated_mention");
        Pattern datePattern = Pattern.compile("knowtator_annotation_creation_date");
        Pattern rangePattern = Pattern.compile("knowtator_annotation_span");
        Pattern textPattern = Pattern.compile("knowtator_annotation_text ");
        Pattern sourcePattern = Pattern.compile("knowtator_annotation_text_source");

        //Assume each line is valid until proven otherwise
        boolean valid = true;



        //Search through all of the lines to extract the information needed for an annotation
        for (String infoLine : theInfo)
        {
            //Matchers for each line
            Matcher matchStart = startPattern.matcher(infoLine);
            //Matcher matchMention = mentionPattern.matcher(infoLine);
            Matcher matchDate = datePattern.matcher(infoLine);
            Matcher matchRange = rangePattern.matcher(infoLine);
            Matcher matchText = textPattern.matcher(infoLine);
            Matcher matchSource = sourcePattern.matcher(infoLine);
            Matcher matchAnnotator = annotatorPattern.matcher(infoLine);

            if (annotatorInFile.equals("\"n/a\"") && matchAnnotator.find())
            {
                //Should be something like: (knowtator_annotation_annotator [beth1_Instance_0])
                String[] temp = infoLine.split("\\[|]");
                String annotatorInFileName = temp[1];
                annotatorInFile = annotatorInFileName;
                for (Annotator annotator : user.getAllAnnotators())
                {
                    for (String inFile : annotator.getAnnotatorFileNames())
                    {
                        if (inFile.equals(annotatorInFile))
                        {
                            thisAnnotator = annotator;
                            break;
                        }
                    }
                }
            }
            //Attempt to find the Start line which contains the instance number which corresponds to the key
            //in the map that maps to the Class of this annotation
            else if (textClass.equals("\"n/a\"") && matchStart.find())
            {
                //Should be something like: ([beth1_Instance_10001] of  knowtator+annotation
                String[] temp = infoLine.split("_");
                String containsInstance = temp[temp.length - 1];
                String[] temp2 = containsInstance.split("]");
                String instanceNum = infoLine.substring(infoLine.indexOf("["), infoLine.lastIndexOf("]"));
                //Check to make sure it is a class that we are filtering with.
                if (theClasses != null && theClasses.contains(m.get(instanceNum)))
                {
                    textClass = m.get(instanceNum);
                }
                else if (theClasses == null)
                {
                    textClass = m.get(instanceNum);
                }
                //If it is not a filter class than return null
                else
                {
                    return null;
                }
            }
            //Attempt to find the date line
            else if (date.equals("\"n/a\"") && matchDate.find())
            {
                //Date - The Creation date of the annotation
                //Should be something like: (knowtator_annotation_creation_date "Mon Mar 08 20:27:07 MST 2010")

                //Split using the " makes so we don't have to do any substrings
                String[] found = infoLine.split("\"");

                //Make sure that it is in the proper form.
                if (found.length >= 2)
                {
                    date = found[1];
                }
                //If it is not in the right form don't use it
                else
                {
                    System.out.println(infoLine);
                    valid = false;
                }
            }
            //Attempt to find the character span line
            else if (range.equals("\"n/a\"") && matchRange.find())
            {
                //Span - The character span of the annotated text
                //Should be something like: (knowtator_annotation_span "260|299")

                //Split using the " makes so we don't have to do any substrings
                String[] found = infoLine.split("\"");
                //Make sure it is in the proper form
                if (found.length >= 2 && valid)
                {
                    range = found[1];
                }
                //If it is not in the proper form don't use it
                else
                {
                    System.out.println(infoLine);
                    valid = false;
                }

            }
            //Attempt to find the line that contains the text for the annotation
            else if (text.equals("\"n/a\"") && matchText.find())
            {
                //Text - The actual annotated text
                //Should be something like 	(knowtator_annotation_text "Right parietal occipital temporal tumor")

                //Split using the " makes so we don't have to do any substrings
                String[] found = infoLine.split("\"");
                //Make sure the text has the proper form
                if (found.length >= 2 && valid)
                {
                    text = found[1];
                }
                //If it doesn't have the proper form don't use it.
                else
                {
                    System.out.println(infoLine);
                    valid = false;
                }
            }
            //Attempt to find the line that corresponds to the source file for this annotation
            else if (source.equals("\"n/a\"") && matchSource.find())
            {
                //Source - the file the text came from
                //Should be something like: (knowtator_annotation_text_source [101407944+PUMC.txt]))

                String[] found = infoLine.split(" ");
                //Make sure the text has the proper form.
                if (found.length >= 2 && valid)
                {

                    source = found[1];
                    //Make sure it is at least long enough to contain the [])) or else
                    // it is not valid
                    if (source.length() >= 4)
                    {
                        //Strip off the [ from the beginning and the ])) from the end.
                        int start = source.indexOf("[") + 1;
                        int end = source.lastIndexOf("]");
                        source = source.substring(start, end);
                        if (chosenFiles != null)
                        {
                            if (!chosenFiles.contains(source))
                            {
                                return null;
                            }
                        }
                    }
                    else
                    {
                        System.out.println(infoLine);
                        valid = false;
                    }
                }
            }
            //A line that we don't care about
            else
            {
                //System.out.println(toCheck);
            }
        }

        //If the line was valid then return the annotation text and textclass must have been initialized for it to be valid.
        if (thisAnnotator != null && valid && !text.equals("\"n/a\"") && !textClass.equals("\"n/a\"") && !source.equals("\"n/a\""))
        {
            String annotatorID = "";
            for (Annotator annotator : user.getAllAnnotators())
            {
                for (String inFile : annotator.getAnnotatorFileNames())
                {
                    if (annotatorInFile.equals(inFile))
                    {
                        annotatorID = annotator.getAnnotatorID();
                    }
                }

            }
            if(text.toLowerCase().equals("bilirubin"))
                System.out.println(textClass);
            int mention = env.Parameters.getLatestUsedMentionID();
            env.Parameters.updateLatestUsedMentionID(mention + 1);
            Article article = new Article(source);
            preAnnotate.format.Annotation aTest = new preAnnotate.format.Annotation(String.valueOf(mention), thisAnnotator, range, text.trim().toLowerCase(), date, textClass, source);
            int start = Integer.parseInt(range.split("\\|")[0]);
            int end = Integer.parseInt(range.split("\\|")[1]);
            resultEditor.annotations.Annotation forArticle = new resultEditor.annotations.Annotation(String.valueOf(mention), text.trim().toLowerCase(), start, end, thisAnnotator.getAnnotatorName(), thisAnnotator.getAnnotatorID(),textClass,date);
            article.addAnnotation(forArticle);
            return article;
        }
        //If it was not valid return null
        else
        {
            user.setErrorMessages(user.getErrorMessages() + "[ERROR](Couldn't create annotation) Check near line " + user.getLineNum() + "\n");
            return null;
        }

    }

    /** This class will extract the information about an annotator and return an
     * annotator object
     *
     * @param theInfo - The lines that constitute an annotator
     * @return - An Annotator object that represents these lines, or null if not created properly.
     */
    public Annotator annotatorExtractor(ArrayList<String> theInfo)
    {
        //The information required for an annotator object
        String name = null;
        String inFile = null;
        String ID = null;

        //The annotator object to return
        Annotator toReturn = null;

        //The patterns we need to find
        Pattern humanInFile = Pattern.compile("knowtator\\+human\\+annotator");
        Pattern humanID = Pattern.compile("knowtator_annotator_id");
        Pattern humanName = Pattern.compile("knowtator_annotation_annotator_firstname");

        //Go through each line of the information to extract what we need.
        for (String searchLine : theInfo)
        {
            //Initialize matchers
            Matcher findHumanInFile = humanInFile.matcher(searchLine);
            Matcher findHumanID = humanID.matcher(searchLine);
            Matcher findHumanName = humanName.matcher(searchLine);

            //If this line contains the inFile name then extract and store it
            if (findHumanInFile.find())
            {
                String[] temp = searchLine.split("\\[|]");
                if (temp.length == 3)
                {
                    inFile = temp[1];
                }
            }
            //If this line contains the name of the annotator extract and store it
            else if (findHumanName.find())
            {
                String[] temp = searchLine.split("\"");
                if (temp.length == 3)
                {
                    name = temp[1];
                }
            }
            //If this line contains the ID of the annotator extract and store it.
            else if (findHumanID.find())
            {
                String[] temp = searchLine.split("\"");
                if (temp.length == 3)
                {
                    ID = temp[1];
                }
            }
        }
        //We must have at least he in File name to make a valid Annotator
        if (inFile != null)
        {
            //If other variables are null then assign them a placeholder value
            if (ID == null)
            {
                ID = "NO ID";
            }
            if (name == null)
            {
                name = "NO NAME";
            }
            toReturn = new Annotator(name, ID, inFile);
        }
        else
        {
            user.setErrorMessages(user.getErrorMessages() + "[ERROR](Couldn't create annotator) Check near line " + user.getLineNum() + "\n");
        }
        return toReturn;

    }

    /** This method takes in lines representing a consensus annotator and extracts the relevant information
     *
     * @param theInfo - the info that represents a consensus Annotator
     * @return - An Annotator object that represents this consensus annotator if created successfully, otherwise null is returned.
     */
    public Annotator consensusExtractor(ArrayList<String> theInfo)
    {
        //Things we are looking for
        String name = null;
        String inFile = null;

        //Object to return
        Annotator toReturn = null;

        //Patterns we need to match
        Pattern consensusInFile = Pattern.compile("knowtator_team_annotator");
        Pattern consensusName = Pattern.compile("knowtator_set_name");

        //Search all of our info for the information we need
        for (String searchLine : theInfo)
        {
            //Create matchers
            Matcher findConsensusName = consensusName.matcher(searchLine);
            Matcher findConsensusInFile = consensusInFile.matcher(searchLine);

            //If this is a line that contains the name of the consensus set extract and store it
            if (findConsensusName.find())
            {
                //Should be something like (knowtator_set_name "consensus set")
                String[] tempInfo = searchLine.split("\"");
                if (tempInfo.length == 3)
                {
                    name = tempInfo[1];
                }
            }
            //If this line is a line that contains the inFile name than extract and store it
            else if (findConsensusInFile.find())
            {
                String[] temp = searchLine.split("\\[|]");
                if (temp.length == 3)
                {
                    inFile = temp[1];
                }

            }
        }
        //We must have at least the inFile name to create a valid annotator object
        if (inFile != null)
        {
            if (name == null)
            {
                name = "NO NAME";
            }
            toReturn = new Annotator(name, "set", inFile);
        }
        else
        {
            user.setErrorMessages(user.getErrorMessages() + "[ERROR](Couldn't create consensus annotator) Check near line " + user.getLineNum() + "\n");
        }
        return toReturn;
    }
    //</editor-fold>
}

