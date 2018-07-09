/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotations;

/**
 *
 * @author Kyle
 */
public class suggestion
{

    private String text;
    private int start;
    private int end;
    private String explanation;

    public suggestion(String theText, int theStart, int theEnd, String explained) {
        this.text = theText;
        this.start = theStart;
        this.end = theEnd;
        this.explanation = explained;
    }
    

    @Override
    public String toString()  {
        return this.text;
    }


    public boolean equals(Object toCompare) {
        try{
            suggestion c = (suggestion)toCompare;
            if(start == c.start && end == c.end && text.equals(c.text))
                return true;
            return false;
        }catch(Exception ex){
            return false;
        }
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return the explanation
     */
    public String getExplanation() {
        return explanation;
    }
}
