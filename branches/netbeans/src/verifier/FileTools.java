package verifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class FileTools
{
	/**
	 * Get all of the files from xmlDir that match files in namesToMatch. So any
	 * file in xmlDir whos stripped name equals the stripped name of a file in
	 * namesToMatch will be returned.
	 *
	 * @param xmlDir
	 *            - All of the files in this directory could potentially be
	 *            returned.
	 * @param namesToMatch
	 *            - The files that are trying to be matched against.
	 * @return - all matching files from xmlDir
	 */
	public static ArrayList<File> getMatchingFiles(File[] potentialMatches, File[] namesToMatch)
	{
		ArrayList<File> toReturn = new ArrayList<File>();
		// File[] potentialMatches = xmlDir.listFiles();
		ArrayList<String> names = new ArrayList<String>();
		for (File file : namesToMatch)
		{
			names.add(getStrippedName(file));
		}
		for (File file : potentialMatches)
		{
			String name = getStrippedName(file);
			if (names.contains(name))
			{
				toReturn.add(file);
			}
		}
		return toReturn;
	}

	/**
	 * Get the stripped name of the file at the given path. This will be the
	 * name of the file with all path information and extension information
	 * stripped off.
	 *
	 * @param getNameOf
	 *            - the file to get the name of.
	 * @return - the name of the file.
	 */
	public static String getStrippedName(File getNameOf)
	{
		String temp = getNameOf.getName();
		int lastIndex = temp.indexOf('.');
		if (lastIndex > 0)
		{
			return temp.substring(0, lastIndex);
		}
		return temp;
	}

	/**
	 * Return all files in matchFiles that have a stripped name that matches the
	 * name of toMatch
	 *
	 * @param toMatch
	 *            - the file to find matches for
	 * @param matchFiles
	 *            - the list of files to find matches in
	 * @return - the matching files
	 */
	public static ArrayList<File> matchFileWithFiles(File toMatch, Collection<File> matchFiles)
	{
		ArrayList<File> matches = new ArrayList<File>();
		String matchName = getStrippedName(toMatch);
		for (File file : matchFiles)
		{
			if (getStrippedName(file).equals(matchName))
				matches.add(file);
		}
		return matches;
	}
}
