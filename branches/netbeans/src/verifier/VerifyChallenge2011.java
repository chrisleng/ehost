/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package verifier;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import resultEditor.annotations.SpanDef;

/**
 *
 * @author Kyle
 */
public class VerifyChallenge2011
{
    private Article mNew,mOld;
    private File mText;
    private String text;
    private ArrayList<String> inExisting = new ArrayList<String>();
    private ArrayList<Annotation> toReturn = new ArrayList<Annotation>();
    private BufferedWriter writeOn;
    public VerifyChallenge2011(Article aNew, Article anOld, File matchingText, BufferedWriter writeWith)
    {
        writeOn = writeWith;
        mNew = aNew;
        aNew.baseArticle = anOld;
        mOld = anOld;
        mText = matchingText;
        for(String s : env.Parameters.verifierComparisonSettings.getComparisonClasses())
        {
            inExisting.add(s);
        }
        //inExisting.addAll(Arrays.asList(env.Parameters.verifierComparisonSettings.getComparisonClasses().toArray()));
        //inExisting.add("Treatment");
        //inExisting.add("Test");
        //inExisting.add("Problem");
    }
    public VerifyChallenge2011(Article aNew, File matchingText, BufferedWriter writeWith)
    {
        writeOn = writeWith;
        mNew = aNew;
        mOld = aNew.baseArticle;
        mText= matchingText;
        for(String s : env.Parameters.verifierComparisonSettings.getComparisonClasses())
        {
            inExisting.add(s);
        }
    }
    public ArrayList<Annotation> outputVerify() throws FileNotFoundException, IOException
    {

        Scanner s;
        s = new Scanner(mText);
        StringBuilder builder = new StringBuilder();
        writeOn.write("***************** Processing: " + mText.getName() +"*********************\n");
        System.out.println("***************** Processing: " + mText.getName() +"*********************");
        while(s.hasNext())
        {
            builder.append((s.nextLine() + "\n"));
        }
        text = builder.toString();
        for(int i = 0; i< mNew.annotations.size();i++)
        {
            Annotation annot = mNew.annotations.get(i);
            if(env.Parameters.verifierComparisonSettings.getCheckSpaceBothSides())
            {
                checkSpaceBothSides(annot);
            }
            if(env.Parameters.verifierComparisonSettings.getSearchForGhosts())
            {
                checkBadSpans(annot);
            }
            if(env.Parameters.verifierComparisonSettings.getCheckDuplicates())
            {
                checkDuplicates(i,mNew);
            }
            if(env.Parameters.verifierComparisonSettings.getSearchForOneSpaceBetweenPersons())
            {
                checkOtherPerson(i, mNew);
            }
            //Verify that all new annotations of a given type exist in old
            if(env.Parameters.verifierComparisonSettings.getCheckExistsInOld())
            {
                if(!checkMatchingExists(annot, mOld))
                {
                    annot.verifierSuggestion.add("New Problem,Treatment, or test: does not exist in 2010 XML");
                    writeOn.write("ERROR(New Treatment,Problem,Test does not exist in 2010 XML): " + identify(annot, false) + "\n");
                    log.LoggingToFile.log( Level.SEVERE,"ERROR(New Treatment,Problem,Test does not exist in 2010 XML): " + identify(annot, false));
                }
            }
        }
        //Verify that all old annotations exist in the new Article
        if(env.Parameters.verifierComparisonSettings.getCheckExistsInNew())
        {
            for(Annotation annot: mOld.annotations)
            {
                if(!checkMatchingExists(annot, mNew))
                {
                    writeOn.write("ERROR(Old Treatment,Problem,Test does not exist in 2011 XML): " + identify(annot, true) + "\n");
                    log.LoggingToFile.log( Level.SEVERE,"ERROR(Old Treatment,Problem,Test does not exist in 2011 XML): " + identify(annot, true));
                }
            }
        }
        //writeOn.write("\n");
        System.out.println();
        return toReturn;
    }
    private void checkDuplicates(int index, Article art) throws IOException
    {
        Annotation annot = art.annotations.get(index);
        for(int i = 0; i< art.annotations.size();i++)
        {
            if(i == index)
                continue;
            Annotation compareTo = art.annotations.get(i);
            if(annot.annotationText == null ||compareTo.annotationText == null)
                continue;
            if(( annot.spanset == null ) || (compareTo.spanset == null ) )
                continue;
            
            if(annot.spanset.isDuplicates( compareTo.spanset ))
            {
                annot.verifierSuggestion.add("Duplicate Annotation");
                writeOn.write("ERROR(Duplicate annotation): " + identify(annot, true) + "\n");
                log.LoggingToFile.log( Level.SEVERE,"ERROR(Duplicate annotation): " + identify(annot, true));
            }
        }
    }
    private boolean checkMatchingExists(Annotation annot, Article art)
    {
        if(!inExisting.contains(annot.annotationclass))
        {
            return true;
        }
        for(Annotation existing:art.annotations)
        {
            if(annot.annotationclass.equals(existing.annotationclass))
            {
                if(annot.spanstart == existing.spanstart && annot.spanend == existing.spanend)
                {
                   return true;
                }
            }
        }
        return false;
    }
    private void checkBadSpans(Annotation annot) throws IOException
    {
        if(((annot.spanstart == annot.spanend) && annot.spanend == -1) || annot.spanstart < 0 || annot.spanend< 0 || annot.spanend >= text.length())
        {
                annot.verifierSuggestion.add("Ghost Annotation");
                annot.spanstart = 1;
                annot.spanend = 2;
            writeOn.write("ERROR(Ghost Annotation): " + identify(annot,true));
            log.LoggingToFile.log( Level.SEVERE,"ERROR(Ghost Annotation): " + identify(annot,true));
        }
    }
    private void checkOtherPerson(int index, Article art) throws IOException
    {
        Annotation current = art.annotations.get(index);
        if(!current.annotationclass.equals("Person"))
            return;
        for(Annotation annot: art.annotations)
        {
            if(!annot.annotationclass.equals("Person"))
                continue;
            if(annot.spanstart - 1 == current.spanend && text.charAt(current.spanend) != '\n')
            {
                annot.verifierSuggestion.add("Only one space between between this Person and the previous Person.");

                current.verifierSuggestion.add("Only one space between between this Person and the next Person.");
                writeOn.write("ERROR(Only one space between Persons): First: " + identify(current,true) + " Second: " + identify(annot,true));
                log.LoggingToFile.log( Level.SEVERE,"ERROR(Only one space between Persons): First: " + identify(current,true) + " Second: " + identify(annot,true));
            }
        }
    }
    private void checkSpaceBothSides(Annotation annot) throws IOException
    {
        /*
//@@@@        int start = annot.spanstart;
        int end = annot.spanend;
        char after;
        char before;
        if(start -1 < 0)
        {
            before = ' ';
        }
        else
        {
            before = text.charAt(start-1);
        }
        if(end + 1 >= text.length())
        {
            after = ' ';
        }
        else
        {
            after = text.charAt(end);
        }
        if((before != ' ' && before != '\n' && before != '\t') || (after != ' ' && after != '\n' && after != '\t'))
        {
                annot.verifierSuggestion.add("This annotation is not surrounded by Whitespace.");
                writeOn.write("ERROR(Annotation not surrounded by spaces): "+ identify(annot,true)+"\n");
            log.LoggingToFile.log( Level.SEVERE,"ERROR(Annotation not surrounded by spaces): "+ identify(annot,true));
        }
        * 
        */

    }
    
    
    private String identify(Annotation annot, boolean save)
    {
        if(save)
            this.toReturn.add(annot);
        String toReturn = "";
        
        if( annot.spanset != null ){
            for( int i=0; i<annot.spanset.size(); i++ ){
                SpanDef span = annot.spanset.getSpanAt(i);
                if( span == null )
                    continue;
                toReturn += span.start + "|" + span.end + " ";
            }            
        }

        return toReturn;
    }


}
