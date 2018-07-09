/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env;

import java.util.TreeSet;

/**
 *
 * @author Kyle
 */
public class ComparisonSettings
{
    private TreeSet<String> comparisonClasses;
    private String directoryString;

    //Special search types
    private Boolean checkExistsInNew = false;
    private Boolean checkExistsInOld = false;
    private Boolean searchForGhosts = false;
    private Boolean searchForOneSpaceBetweenPersons = false;
    private Boolean checkDuplicates = false;
    private Boolean checkSpaceBothSides = false;

    public ComparisonSettings()
    {
        //Do any initializing
        comparisonClasses = new TreeSet<String>();
        directoryString = "";
        //checkExistsInNew = false;
        //checkExistsInOld = false;
    }
    public ComparisonSettings(String fromConfigs)
    {
        comparisonClasses = new TreeSet<String>();
        String[] fields = fromConfigs.split("@@@@");
        directoryString = fields[0];
        String[] tempClasses = fields[1].split("<@@>");
        if(tempClasses != null)
            for(String s: tempClasses)
            {
                if(!s.equals(""))
                    this.comparisonClasses.add(s);
            }

        this.checkExistsInNew = Boolean.parseBoolean(fields[2]);
        this.checkExistsInOld = Boolean.parseBoolean(fields[3]);
        this.searchForGhosts = Boolean.parseBoolean(fields[4]);
        this.searchForOneSpaceBetweenPersons = Boolean.parseBoolean(fields[5]);
        this.checkDuplicates = Boolean.parseBoolean(fields[6]);
        this.checkSpaceBothSides = Boolean.parseBoolean(fields[7]);

    }
    public String configString()
    {
        String toReturn = "";
        toReturn += getDirectoryString();
        toReturn += "@@@@";
        for(String s: getComparisonClasses())
        {
            toReturn += s +"<@@>";
        }
        toReturn +="@@@@";
        toReturn+= Boolean.toString(getCheckExistsInNew());

        toReturn +="@@@@";
        toReturn += Boolean.toString(getCheckExistsInOld());

        toReturn +="@@@@";
        toReturn+= Boolean.toString(searchForGhosts);

        toReturn +="@@@@";
        toReturn+= Boolean.toString(searchForOneSpaceBetweenPersons);

        toReturn +="@@@@";
        toReturn+= Boolean.toString(checkDuplicates);

        toReturn +="@@@@";
        toReturn+= Boolean.toString(checkSpaceBothSides);
        return toReturn;
    }

    public void AddClass(String s)
    {
        getComparisonClasses().add(s);
    }

    public void RemoveClass(String s)
    {
        getComparisonClasses().remove(s);
    }

    /**
     * @return the comparisonClasses
     */
    public TreeSet<String> getComparisonClasses() {
        return comparisonClasses;
    }

    /**
     * @param comparisonClasses the comparisonClasses to set
     */
    public void setComparisonClasses(TreeSet<String> comparisonClasses) {
        this.setComparisonClasses(comparisonClasses);
    }

    /**
     * @return the checkExistsInNew
     */
    public Boolean getCheckExistsInNew() {
        return checkExistsInNew;
    }

    /**
     * @param checkExistsInNew the checkExistsInNew to set
     */
    public void setCheckExistsInNew(Boolean checkExistsInNew) {
        this.checkExistsInNew = checkExistsInNew;
    }

    /**
     * @return the checkExistsInOld
     */
    public Boolean getCheckExistsInOld() {
        return checkExistsInOld;
    }

    /**
     * @param checkExistsInOld the checkExistsInOld to set
     */
    public void setCheckExistsInOld(Boolean checkExistsInOld) {
        this.checkExistsInOld = checkExistsInOld;
    }

    /**
     * @return the directoryString
     */
    public String getDirectoryString() {
        return directoryString;
    }

    /**
     * @param directoryString the directoryString to set
     */
    public void setDirectoryString(String directoryString) {
        this.directoryString = directoryString;
    }

    /**
     * @return the searchForGhosts
     */
    public Boolean getSearchForGhosts() {
        return searchForGhosts;
    }

    /**
     * @param searchForGhosts the searchForGhosts to set
     */
    public void setSearchForGhosts(Boolean searchForGhosts) {
        this.searchForGhosts = searchForGhosts;
    }

    /**
     * @return the searchForOneSpaceBetweenPersons
     */
    public Boolean getSearchForOneSpaceBetweenPersons() {
        return searchForOneSpaceBetweenPersons;
    }

    /**
     * @param searchForOneSpaceBetweenPersons the searchForOneSpaceBetweenPersons to set
     */
    public void setSearchForOneSpaceBetweenPersons(Boolean searchForOneSpaceBetweenPersons) {
        this.searchForOneSpaceBetweenPersons = searchForOneSpaceBetweenPersons;
    }

    /**
     * @return the checkDuplicates
     */
    public Boolean getCheckDuplicates() {
        return checkDuplicates;
    }

    /**
     * @param checkDuplicates the checkDuplicates to set
     */
    public void setCheckDuplicates(Boolean checkDuplicates) {
        this.checkDuplicates = checkDuplicates;
    }

    /**
     * @return the checkSpaceBothSides
     */
    public Boolean getCheckSpaceBothSides() {
        return checkSpaceBothSides;
    }

    /**
     * @param checkSpaceBothSides the checkSpaceBothSides to set
     */
    public void setCheckSpaceBothSides(Boolean checkSpaceBothSides) {
        this.checkSpaceBothSides = checkSpaceBothSides;
    }
    

}
