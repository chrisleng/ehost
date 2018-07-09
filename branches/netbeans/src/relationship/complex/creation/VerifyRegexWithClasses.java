/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relationship.complex.creation;

import relationship.complex.dataTypes.ComplexRelImportReturn;
import resultEditor.annotationClasses.Depot;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Kyle
 */
public class VerifyRegexWithClasses
{

    // <editor-fold defaultstate="collapsed" desc="Public Functions">
    /**
     * Check that the passed in string is a valid regular expresion.  It will be considered valid
     * if the only non-regex characters are the characters of a class name and the regular expression
     * can be compiled.
     *
     * @param regex - the regular expression to check for validity
     * @return - true if it is valid, false otherwise.
     */
    public static boolean CheckValidRegex(String regex)
    {
        //Remove classes from regex
        String regexNoClass = ReplaceClassWithAt(regex);
        boolean compiles = verifyARegularExpression(regexNoClass);
        boolean onlyClass = checkNonClass(regexNoClass);
        System.out.println(compiles + " " + onlyClass);
        return (compiles && onlyClass);
    }

    /**
     * Get an input verifier for regular expressions that only contain classes and
     * regular expressions
     * @return an input verifier for a JComponent.
     */
    public static InputVerifier getInputVerifier()
    {
        InputVerifier verifier = new InputVerifier()
        {

            public boolean verify(JComponent input)
            {
                final JTextComponent source = (JTextComponent) input;
                String text = source.getText();
                return CheckValidRegex(text);
            }
        };
        return verifier;
        //};
    }

    /**
     * Find all regex characters in all strings in makeReady and add the before string
     * before all regex characters.  This can be used to negate regex chars(with before = \\\\).
     * @param makeReady - Strings to make ready for regex
     * @param before - String to add to before regex characters
     * @return
     */
    public static String[] Regexerate(String[] makeReady, String before)
    {
        String[] toReturn = new String[makeReady.length];
        for (int i = 0; i < makeReady.length; i++)
        {
            String regex = makeReady[i];
            regex = regex.replaceAll("\\[", before + "[");
            regex = regex.replaceAll("\\]", before + "]");
            regex = regex.replaceAll("\\,", before + ",");
            regex = regex.replaceAll("\\:", before + ":");
            regex = regex.replaceAll("\\.", before + ".");
            regex = regex.replaceAll("\\-", before + "-");
            regex = regex.replaceAll("\\=", before + "=");
            regex = regex.replaceAll("\\*", before + "*");
            regex = regex.replaceAll("\\@", before + "@");
            regex = regex.replaceAll("\\&", before + "&");
            regex = regex.replaceAll("\\+", before + "+");
            regex = regex.replaceAll("\\%", before + "%");
            toReturn[i] = regex;
        }
        return toReturn;
    }

    /**
     * This function will rearrange strings so that given any string(x) that is a substring of string(y),
     * x will appear after y.
     * <pre>
     *      Example: given {Problems, Problem, Prob, Independent}
     *               return {Problems, Independent, Problem, Prob}
     *               *Exact location of Independent is undefined only relative positions of substrings
     *                is guaranteed.
     * </pre>
     * @param allClasses - the strings to arrange
     * @return - rearranged strings
     */
    public static ArrayList<String> arrangeStrings(ArrayList<String> allClasses)
    {
        ArrayList<String> rearranged = new ArrayList<String>();
        Matcher matcher;

        //Keep looping until all classes have been rearranged
        while (allClasses.size() > 0)
        {
            for (int j = 0; j < allClasses.size(); j++)
            {
                String referenceClass = allClasses.get(j);
                boolean found = false;
                //Search for a class that the current class is a substring of
                for (int i = 0; i < allClasses.size(); i++)
                {
                    if (i == j)
                    {
                        continue;
                    }
                    //Create matcher to find referenceClass inside of class at j
                    matcher = Pattern.compile(referenceClass).matcher(allClasses.get(i));
                    if (matcher.find())
                    {
                        found = true;
                        break;
                    }
                }
                //If this Class is not a substring of another class then it is safe to add to the list
                if (!found)
                {
                    rearranged.add(referenceClass);
                    allClasses.remove(j);
                    j--;
                }
            }
        }
        return rearranged;
    }

    /**
     * Attempt to compile and run a regular expression, return true if it compiled successfully,
     * false otherwise
     * @param _regularExpression - the expression to test
     * @return - true if succeeds, false otherwise.
     */
    public static boolean verifyARegularExpression(String _regularExpression)
    {
        //Test string... regular expression will be compiled for this string
        String currsent = "I am a TESTer.";

        Matcher matcher;
        boolean match_found;

        //Attempt to compile the regular expression for the test string.
        try
        {
            matcher = Pattern.compile(_regularExpression, Pattern.CASE_INSENSITIVE).matcher(currsent);
            match_found = matcher.find();
            while (match_found)
            {
                match_found = matcher.find();
            }

        }
        //If there is an exception then the regular expression is bad so return false.
        catch (Exception e)
        {
            return false;
        }

        //No exception when using regular expression so return true
        return true;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private functions">

    /**
     * Check a string that has already been run thorugh ReplaceClassWithAt to make sure
     * there are only regex symbols and "@@@@" strings left. This test is to make sure
     * the regular expression is conforming to the guideline that it only has classes
     * and regex expressions in it.
     *
     * @see #ReplaceClassWithAt(java.lang.String)
     * @param regex - the String to check
     * @return - True if it only contains "@@@@" and regex characters, false otherwise
     */
    private static boolean checkNonClass(String regex)
    {
        regex = regex.replaceAll("\\[", "");
        regex = regex.replaceAll("\\|", "");
        regex = regex.replaceAll("\\]", "");
        regex = regex.replaceAll("\\(", "");
        regex = regex.replaceAll("\\)", "");
        regex = regex.replaceAll("\\,", "");
        regex = regex.replaceAll("\\:", "");
        regex = regex.replaceAll("\\.", "");
        regex = regex.replaceAll("\\-", "");
        regex = regex.replaceAll("\\=", "");
        regex = regex.replaceAll("\\*", "");
        regex = regex.replaceAll("\\@", "");
        regex = regex.replaceAll("\\&", "");
        regex = regex.replaceAll("\\+", "");
        regex = regex.replaceAll("\\%", "");
        regex = regex.replaceAll("@@@@", "");
        if (regex.length() == 0)
        {
            return true;
        }
        return false;
    }
    /**
     * Check to see if a set of Strings can all be represented
     * in a given regular expression.
     * @param regex - the regular expression
     * @param classSpew - all of the strings
     * @return - true if the regular expression encompasses all strings in classSpew,
     * false otherwise.
     */
    public static boolean checkRegexFitsAll(String regex, TreeSet<String> classSpew)
    {
        Object[] temp = classSpew.toArray();
        String[] safe = new String[temp.length];
        for(int i = 0; i< temp.length; i++)
        {
            safe[i] = (String)temp[i];
        }
        Matcher matcher;
        safe = Regexerate(safe, "\\\\\\\\\\\\");
        for(String s: safe)
        {
            try
            {
                matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(s);
                if(matcher.matches())
                    continue;
                else return false;

            }
            //If there is an exception then the regular expression is bad so return false.
            catch (Exception e)
            {
                return false;
            }
        }
        return true;
    }
    /**
     * Replace all classes with "@@@@" String
     * @param regex - the regex
     * @return
     */
    private static String ReplaceClassWithAt(String regex)
    {
        //Get all classes in the current schema
        Depot depot = new Depot();
        String[] allClassesTemp = depot.getAnnotationClasssnamesString();

        //add 3 '\' before each regex symbol in a class
        allClassesTemp = Regexerate(allClassesTemp, "\\\\\\\\\\\\");

        //Move the classes into an ArrayList
        ArrayList<String> allClasses = new ArrayList<String>();
        for (String s : allClassesTemp)
        {
            allClasses.add(s);
        }

        //Arrange the classes so that any class that is a substring of another class
        //appears after it
        ArrayList<String> arranged = arrangeStrings(allClasses);

        //Replace all classes with @@@@
        for (String s : arranged)
        {
            regex = regex.replaceAll("\\(" + s + "\\)", "@@@@");
        }

        //Return regex with all classes replaced by @@@@
        return regex;

    }

    /**
     * Test method
     * @param args
     */
    private static void test(String[] args)
    {
        Boolean test1 = CheckValidRegex("(Problem)*Problems+");
        Boolean test2 = CheckValidRegex("al;jgieh");
        System.out.println(test1);
        System.out.println(test2);
    }
    //</editor-fold>
}
