package verifier;

import java.util.ArrayList;

/**
 * Used to do a QualityCheck this method simply has a method that will return
 * true or false if the given string contains a "s", or a relative pronoun,
 * Possessive pronoun, article, or preposition if you pass all of the String[]
 * containing such in with the string.
 *
 * @author Timothy Ryan Beavers
 * date - March 15th 2010
 *
 */
public class QualityCheck
{
    /**
     * This function will check for matches between a String and each entry in an
     * ArrayList of Strings.
     *
     * @param str - the String to check for matches.
     * @param checkWords - All Strings to check against.
     * @return - true if a match is found, false otherwise.
     */
    public static boolean check(String str, ArrayList<String> checkWords)
    {
        if (str != null) // so long as you wern't sent anything null
        {
            str = str.toLowerCase(); //lower case the word

            for (int i = 0; i < checkWords.size(); i++)
            {
                String start = "";
                if (checkWords.get(i).length() >= 6)
                {
                    start = checkWords.get(i).substring(0, 6);
                }
                if (start.equals("<char>"))
                {
                    String characters = checkWords.get(i).substring(6);
                    for (int j = 0; j < characters.length(); j++)
                    {
                        char reference = characters.charAt(j);
                        for (int originalIndex = 0; originalIndex < str.length(); originalIndex++)
                        {
                            if (reference == str.charAt(originalIndex))
                            {
                                return true;
                            }
                        }
                    }
                }
                if (str.equals(checkWords.get(i).toLowerCase().trim()))  //compare against every word
                {
                    return true; // if at any time you hit one break out and return true
                }
            }
        }
        return false;
    }
    /**
     * This function will compare any entries preceded by a <char> tag to find character matches
     * within the passed in string.
     * @param source - the text to match
     * @param dictionary - the dictionary to use to find matches
     */
    public static int matchCharByChar(String source, ArrayList<String> dictionary)
    {
        int farthestFound = -1;
        for(String phrase: dictionary)
        {

        //See if this dictionary entry is supposed to be check character by character
            String start = "";

            //Extract the first six characters if this word has six characters
            if (phrase.length() >= 6)
            {
                start = phrase.substring(0, 6);
            }
            //If the first six characters equals <char> then check character by character.
            if (start.equals("<char>"))
            {
                int findMatch = matchChars(source, phrase.substring(6));
                if(findMatch >= 0)
                    farthestFound = Math.max(farthestFound, findMatch);
            }
        }
        return farthestFound;
    }
    private static int counter = 0;
    /**
     * This function will find any matches between the source String[] and the ArrayList of checkPhrases.
     * If the number of matches exceeds the allowed variable then this funciton wil return true and false otherwise.
     *
     * @param source - The text to match.
     * @param checkPhrases - the phrases to match with.
     * @param allowed - the number of matches allowed.
     * @return - true if the number of matches exceeds the number of matches allowed, false otherwise.
     */
    public static int checkPhrase(String[] source, ArrayList<String> checkPhrases, int allowed)
    {
        counter = 0;
        if (source == null)
        {
            return -1;
        }
        //Loop through all phrases that we're checking against, and try to find a match within our source
        for (String phrase : checkPhrases)
        {
            //See if this dictionary entry is supposed to be check character by character
            String start = "";

            //Extract the first six characters if this word has six characters
            if (phrase.length() >= 6)
            {
                start = phrase.substring(0, 6);
            }
            //If the first six characters equals <char> then check character by character.
            if (start.equals("<char>"))
            {
                String toCheck = "";
                //Concatenate source words to check for matching characters
                for (int i = 0; i< source.length; i++)
                {
                    String word = source[i];
                    //This function call will increment counter if matches are found.
                    matchChars(word, phrase.substring(6));
                    //If we've found enough matches return true.
                    if (counter > allowed)
                    {
                        return i;
                    }
                    toCheck += word;
                }
                

                
            }
            //split into words
            String[] checkWords = phrase.trim().split(" ");

            //If there aren't enough words in source for their to be a match then just
            //continue on with the loop.
            if(checkWords.length > source.length)
                continue;

            //Before the loop we're trying to match index 0.
            int currentMatchWordIndex = 0;

            //loop through all of our source words and try to match up the first words
            //****Could probably replace this with regex and save some lines of code... and time****
            for (int sourceIndex = 0; sourceIndex < source.length; sourceIndex++)
            {
                if (source[sourceIndex] == null)
                {
                    break;
                }
                String sourceCompare = source[sourceIndex].trim().toLowerCase();
                String referenceCompare = checkWords[currentMatchWordIndex].trim().toLowerCase();
                //Check for equality
                if (sourceCompare.equals(referenceCompare))
                {
                    //If this word matches then increment our current index we're one word closer to a complete match.
                    currentMatchWordIndex++;

                    //If out currentMatchWordIndex is equal to the length then we have a complete match.
                    //so return true if we've exceeded the allowed amount.
                    if (currentMatchWordIndex == checkWords.length)
                    {
                        //Increment our match counter
                        counter++;
                        //If we've exceeded the allowed instances of this dictionary then return true.
                        if (counter > allowed)
                        {
                            return sourceIndex;
                        }
                        //reset sourceIndex to one after the start of this match and keep going.
                        sourceIndex = sourceIndex - (currentMatchWordIndex - 1);
                        currentMatchWordIndex = 0;
                    }
                }
                //If we had a previous match going but this word didn't match reset Match counter
                //And restart comparison starting with the word after the first match for ongoing match.
                else if (currentMatchWordIndex > 0)
                {
                    sourceIndex = sourceIndex - (currentMatchWordIndex - 1);
                    currentMatchWordIndex = 0;
                }
            }
        }
        return -1;
    }
    /**
     * Ths method will attempt to match each character in chars with a character
     * in source.  If a match is found then the counter will be incremented.
     * @param source - The text from an annotation
     * @param chars - The text from a dictionary entry
     */
    private static int matchChars(String source, String chars)
    {
        int foundOne = -1;
        //For each character in the chars array try to find a match within the source array.
        for (int j = 0; j < chars.length(); j++)
        {
            char reference = chars.charAt(j);
            for (int originalIndex = 0; originalIndex < source.length(); originalIndex++)
            {
                if (reference == source.charAt(originalIndex))
                {
                    foundOne = originalIndex;
                    counter++;
                }
            }
        }
        return foundOne;
    }
}



