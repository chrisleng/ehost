package preAnnotate.outDated;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** This class will order relations in .rel files. The first thing it will do is sort PIP relations
 *  so that the text on the left precedes the text on the right(according to the offsets).
 *  i.e.
 *  <pre>
 *  c="constipation" 51:13 51:13||r="PIP"||c="prolonged ileus" 51:7 51:8
 *  becomes:
 *  c="prolonged ileus" 51:7 51:8||r="PIP"||c="constipation" 51:13 51:13
 *  </pre>
 *  Then the relations are sorted against eachother so that they are in order of where they appear in the original
 *  document. i.e.
 *  <pre>
 *  c="management" 46:11 46:11||r="TrAP"||c="her chronic renal insufficiency" 46:13 46:16
 *  c="seldane" 35:0 35:0||r="NoneTrP"||c="gastrointestinal distress" 35:2 35:3
 *  becomes:
 *  c="seldane" 35:0 35:0||r="NoneTrP"||c="gastrointestinal distress" 35:2 35:3
 *  c="management" 46:11 46:11||r="TrAP"||c="her chronic renal insufficiency" 46:13 46:16
 *  </pre>
 *
 * @author Kyle
 *
 */
public class OrderRelations
{

    /** The main entry point for this program. This is where the parameters are extracted and the
     * appropriate .rel sorting functions are called.
     *
     * @param args - One argument specifying the path name of the .rel file or the folder containing
     * .rel files.
     */
    public static void main(String[] args)
    {
        //The source file where the annotations are located.
        File relations;
        //The name and path of the .annotations file.
        String filename;
        //Confirm that only one parameter was passed in
        if (args.length == 1)
        {
            // create a file from the passed in filename.
            filename = args[0];
            relations = new File(filename);
        }
        // if more or less than one parameter tell user that the program requires one
        // and exit
        else
        {
            log.LoggingToFile.log( Level.SEVERE,"This program requires 1 command line arguement which has the pathname for the .rel file or the pathname for the folder containing .rel files.");
            return;
        }
        //Recursively traverse this file object to see if it contains .rel files.
        int totalFiles = recursiveTraversal(relations);
        //Print out how many .rel files were found.
        log.LoggingToFile.log( Level.INFO, totalFiles + " .rel files found and ordered.");


    }

    /** Recursively finds all .rel files within a given folder. Any .rel file that is found
     * is then sorted.
     *
     * @param fileObject
     * @return - an integer representing the number of files found.
     */
    public static int recursiveTraversal(File fileObject)
    {
        //Keep track of how many .rel files are found.
        int total = 0;
        //If the File is a Folder than open it up and recursively call
        // this function on everything inside it.
        if (fileObject.isDirectory())
        {
            File allFiles[] = fileObject.listFiles();
            for (File aFile : allFiles)
            {
                total += recursiveTraversal(aFile);
            }
        }
        //If the file is a .rel file then open it up and sort it.
        else if (fileObject.isFile())
        {
            //Get the absolute path name for this file
            String source = fileObject.getAbsolutePath();
            String extension = "";
            //If the filename is long enough to have a .rel extension then get the extension
            if (source.length() > 4)
            {
                extension = source.substring(source.length() - 4);
            }
            //Make sure the extension is .rel
            if (extension.equals(".rel"))
            {
                //Notify user that .rel file has been located.
                log.LoggingToFile.log( Level.INFO, ".rel file found: " + source);
                //Create a scanner from the .pins file
                Scanner s = createScanner(fileObject);
                //verify scanner created correctly
                if (s == null)
                {
                    log.LoggingToFile.log( Level.SEVERE, "File Not Found: Terminating Program " + fileObject.getAbsolutePath());
                    return 0;
                }
                //Create an arrayList to put all of the lines in.
                ArrayList<String> lines = new ArrayList<String>();
                //Add all of the lines in the file to the array
                while (s.hasNext())
                {
                    lines.add(s.nextLine());
                }
                //Make sure all PIP relations have the first word span from the text first
                PIPReorder(lines);
                //Make sure all of the lines are ordered by the order that they appear in the document
                LineReorder(lines);
                //Clear out the file and write the ordered lines.
                BufferedWriter out;
                try
                {
                    out = new BufferedWriter(new FileWriter(fileObject, false));
                    for (int i = 0; i < lines.size(); i++)
                    {
                        //write to the file
                        out.write(lines.get(i) + "\n");
                    }
                    out.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                //Return 1 to indicate that one .rel file has been found.
                return 1;

            }
        }
        //Return the total number of .rel files in this folder.
        return total;
    }

    /**  This function attempts to create a Scanner object
     * for a file object
     *
     * @param file - Creates a scanner from the given file
     * @return the Scanner for the file, null if unable to open file
     */
    private static Scanner createScanner(File file)
    {
        //Attempt to create a scanner from the file
        Scanner s = null;
        try
        {
            s = new Scanner(file);
        }
        // If file cannot be found throw an exception and return null
        catch (FileNotFoundException e)
        {
            log.LoggingToFile.log( Level.SEVERE, "file not found: " + e.getMessage());
            return null;
        }

        return s;

    }

    /** This function takes an ArrayList of relations using the following format:
     * <c="text" 1:2 1:3||r="aRelation"||c="text" 2:3 2:5> and sorts them according to the
     * line and word offsets from the original document.
     * There can be a maximum of 999,999 lines and 999 words per line.
     *
     * @param lines - all of the lines from a .rel file.
     */
    private static void LineReorder(ArrayList<String> lines)
    {
        // These ArrayLists will hold the offset information from each line.  Leading zeros will be added
        // so that the offsets can be sorted by their location in the text. Line offsets will occupy 6 digits
        // and word offsets will occupy three, leading zeros will be added if necessary.
        // If the line was the following <c="text" 1:2 1:3||r="aRelation"||c="text" 2:3 2:5>
        // Then the firstOffsets ArrayList would have 000001 002 000001 003 at the index of this line
        // And the secondOffsets ArrayList would have 000002 003 000002 005 at the index of this line
        ArrayList<Long> firstOffsets = new ArrayList<Long>();
        ArrayList<Long> secondOffsets = new ArrayList<Long>();

        //Loop through all of the lines in the lines ArrayList to extract all of the offset information
        for (int i = 0; i < lines.size(); i++)
        {
            String aLine = lines.get(i);
            String[] aSplitLine = aLine.split("\\|\\|");
            //Splitting the first entry with " " gives us something like:
            //c="text", 1:2, 1:3 (there may be more than three though if 'text' has spaces
            String[] firstSplit = aSplitLine[0].split(" ");
            String[] secondSplit = aSplitLine[2].split(" ");
            //This will give us the first set of parse the offsets from 1:2 , 1:3 to the form shown above.
            Long firstOffset = parseOffsets(firstSplit[firstSplit.length - 2], firstSplit[firstSplit.length - 1]);
            Long secondOffset = parseOffsets(secondSplit[secondSplit.length - 2], secondSplit[secondSplit.length - 1]);
            //Add the offsets to the ArrayLists
            firstOffsets.add(firstOffset);
            secondOffsets.add(secondOffset);
        }
        //Sort all lines with respect to their first offsets
        selectionSort(lines, firstOffsets, secondOffsets, 0, firstOffsets.size());
        //Initialize looping variables.
        long previous = firstOffsets.get(0);
        int startIndex = 0;
        //Loop through all of the lines to see if their were ties in the first offsets.
        //sort all ties with respect to their second offsets.
        for (int i = 1; i < firstOffsets.size(); i++)
        {
            //If this offset = the last offset then keep going until we hit a change in offsets.
            if (firstOffsets.get(i) == previous)
            {
                continue;
            }
            else
            {
                //If there was more than one element than the elements need to be sorted
                //with respect to their second set of offsets.
                if (startIndex != i - 1)
                {
                    selectionSort(lines, secondOffsets, firstOffsets, startIndex, i);
                }
            }
            //Set variables for the next loop
            previous = firstOffsets.get(i);
            startIndex = i;
        }



    }

    /** Parse offsets into a form that can be used for sorting.
     *
     * @param start - the starting offset should be of the form "startLine:startWord" where
     * startLine and startWord are integers
     * @param end - the ending offset should be of the form "endLine:endWord" where endLine
     * and endWord are integers
     * @return - A long representing the offsets where line offsets occupy six digits, and
     * word offsets occupy three so leading zeros will be added if necessary. If start offset was
     *  3:4 and end offset was 3:5 then the return would contain 000003004000003005 in a long.
     */
    private static Long parseOffsets(String start, String end)
    {
        //Split using the ':' so we have the Values "LineOffset", "WordOffset" in an array
        String[] startOffsets = start.split(":");
        String[] endOffsets = end.split(":");
        //Extract the integer value for the line and word index and add leading zeros
        //The starting line offset
        for (int j = startOffsets[0].length(); j < 6; j++)
        {
            startOffsets[0] = "0" + startOffsets[0];
        }
        //The starting word offset
        for (int j = startOffsets[1].length(); j < 3; j++)
        {
            startOffsets[1] = "0" + startOffsets[1];
        }
        //The end line offset
        for (int j = endOffsets[0].length(); j < 6; j++)
        {
            endOffsets[0] = "0" + endOffsets[0];
        }
        //the end word offset
        for (int j = endOffsets[1].length(); j < 3; j++)
        {
            endOffsets[1] = "0" + endOffsets[1];
        }
        //Put all of the offsets into a String
        String tempLineWord = startOffsets[0] + startOffsets[1] + endOffsets[0] + endOffsets[1];
        //Parse the string to be a long and return it
        return Long.parseLong(tempLineWord);
    }

    /** This function will sort all passed in ArrayLists from the startIndex to the endIndex according to the
     * Long values in the SortBy ArrayList
     *
     * @param lines - The lines from the .rel file ( any sorting of the sortBy ArrayList will also occur in this ArrayList)
     * @param sortBy - The ArrayList of longs which will be used to sort the arrays
     * @param keepOrder - The order if this arrayList will be changed along with the sortBy arrayList
     * @param startIndex - The index used as the start position for the sorting
     * @param endIndex - The index used as the stop position for the sorting
     */
    private static void selectionSort(ArrayList<String> lines, ArrayList<Long> sortBy, ArrayList<Long> keepOrder, int startIndex, int endIndex)
    {
        //Loop through all of the integers in our range
        for (int i = startIndex; i < endIndex; i++)
        {
            //Assume that this one is the correct choice and try to prove it wrong.
            long smallest = sortBy.get(i);
            //The index of the smallest number found so far.
            int indexOfSmallest = i;
            //Loop through all elements after this element to see if it is smaller
            for (int j = i + 1; j < endIndex; j++)
            {
                //If an element is smaller than the one we've found so far set it to be the smallest
                //and set the index to be the index of the smallest.
                if (sortBy.get(j) < smallest)
                {
                    smallest = sortBy.get(j);
                    indexOfSmallest = j;
                }
            }
            //Rearrange all of the ArrayLists so that i contains the smallest remaining integer
            // in the sortBy array
            long temp = sortBy.get(i);
            sortBy.set(i, sortBy.get(indexOfSmallest));
            sortBy.set(indexOfSmallest, temp);
            //reorder lines array
            String tempLine = lines.get(i);
            lines.set(i, lines.get(indexOfSmallest));
            lines.set(indexOfSmallest, tempLine);
            //reorder keepOrder array.
            long tempSecond = keepOrder.get(i);
            keepOrder.set(i, keepOrder.get(indexOfSmallest));
            keepOrder.set(indexOfSmallest, tempSecond);
        }
    }

    /** This function will Reorder all PIP lines so that the text that
     * appears first in the original document is the first text that is represented
     * in the PIP relational lines.
     *
     * @param lines - An arrayList of lines following this form :
     * <pre>
     * 	c="text" 1:2 1:3||r="aRelation"||c="text" 2:3 2:5
     * </pre>
     *
     */
    private static void PIPReorder(ArrayList<String> lines)
    {
        for (int i = 0; i < lines.size(); i++)
        {
            String aLine = lines.get(i);
            //Find lines that have the relation 'PIP'
            Pattern pattern = Pattern.compile("r=\"PIP\"", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(aLine);
            //If we find a line with PIP make sure it is ordered correctly.
            if (matcher.find())
            {
                //Each line should be something like
                ////****c="text" 1:2 1:3||r="PIP"||c="text" 2:3 2:5****
                //Split using || gives us an Array with three entries
                //c="text" 1:2 1:3, r="PIP" ,c="text" 2:3 2:5
                String[] aSplitLine = aLine.split("\\|\\|");
                //Splitting the first entry with " " gives us something like:
                //c="text", 1:2, 1:3 (there may be more than three though if 'text' has spaces
                String[] firstSplit = aSplitLine[0].split(" ");
                //Use the first Line:word(1:2) in the above example.
                String temp = firstSplit[firstSplit.length - 2];

                //Split using the ':' so we have 1,2 in an array
                String[] tempArray = temp.split(":");
                //Extract the integer value for the line and word index
                int firstStartLine = Integer.parseInt(tempArray[0]);
                int firstStartWord = Integer.parseInt(tempArray[1]);
                //Repeat above process for the endPosition of first text span
                //In the **** example it would be 1:3
                temp = firstSplit[firstSplit.length - 1];
                tempArray = temp.split(":");
                int firstEndLine = Integer.parseInt(tempArray[0]);
                int firstEndWord = Integer.parseInt(tempArray[1]);
                //Repeat above process for the startPosition of the second text span
                //In the **** example it would be 2:3
                String[] secondSplit = aSplitLine[2].split(" ");
                temp = secondSplit[secondSplit.length - 2];
                tempArray = temp.split(":");
                int secondStartLine = Integer.parseInt(tempArray[0]);
                int secondStartWord = Integer.parseInt(tempArray[1]);
                //Repeat above process for the endPosition of the second text span
                //In the **** example it would be 2:5
                temp = secondSplit[secondSplit.length - 1];
                tempArray = temp.split(":");
                int secondEndLine = Integer.parseInt(tempArray[0]);
                int secondEndWord = Integer.parseInt(tempArray[1]);

                //Compare Line,word to make sure it is in the right order
                int rightOrder = intComparer(firstStartLine, firstStartWord, secondStartLine, secondStartWord);
                //If intComparer returns one then this line is in the right order so move on.
                if (rightOrder == 1)
                {
                    continue;
                }
                //If intComparer returns -1 then it is not in the right order and needs to be flipped
                else if (rightOrder == -1)
                {
                    lines.set(i, aSplitLine[2] + "||" + aSplitLine[1] + "||" + aSplitLine[0]);
                    //Debugging - print out all PIP lines that are out of order.
                    //System.out.println(lines.get(i));
                }
                //If it's zero then we need to check the end offsets
                else if (rightOrder == 0)
                {
                    rightOrder = intComparer(firstEndLine, firstEndWord, secondEndLine, secondEndWord);
                    //If it's one then it's in the right order so do nothing.
                    if (rightOrder == 1)
                    {
                        continue;
                    }
                    //If it is -1 then it's in the wrong order and needs to be flipped.
                    else if (rightOrder == -1)
                    {
                        lines.set(i, aSplitLine[2] + "||" + aSplitLine[1] + "||" + aSplitLine[0]);
                    }
                    //If it's zero than do nothing
                    else if (rightOrder == 0)
                    {
                        continue;
                    }
                }
            }
        }
    }

    /** This function will determine if an offset A(line, word) comes before or after an offset B(line,word)
     *
     * @param lineA - The line offset of A
     * @param wordA - The word offset of A
     * @param lineB - The line offset of B
     * @param wordB - The word offset of B
     * @return 1 if A < B, 0 if A = B, and -1 if A > B.
     */
    private static int intComparer(int lineA, int wordA, int lineB, int wordB)
    {
        //compare line offsets
        if (lineB > lineA)
        {
            return 1;
        }
        else if (lineA > lineB)
        {
            return -1;
        }
        //If line offsets are the same compare word offsets.
        else if (lineA == lineB)
        {
            if (wordB > wordA)
            {
                return 1;
            }
            else if (wordA > wordB)
            {
                return -1;
            }
            else if (wordA == wordB)
            {
                return 0;
            }
        }
        return 3;
    }
}
