/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.genHtml;

import java.util.Vector;

/**
 *
 * @author leng
 */
public class Classes {
    
    public Vector<String> allclasses = new Vector<String>();

    public void add(String classname)
    {
        if((classname==null)||(classname.trim().length()<1))
            return;

        classname = classname.trim();

        if(allclasses==null)
        {
            allclasses = new Vector<String>();
            allclasses.add(classname);
        }

        for(int i=0; i<allclasses.size();i++){
            if(allclasses.get(i)==null)
                continue;
            if(allclasses.get(i).trim().compareTo(classname)==0)
                return;
        }

        allclasses.add( classname );

    }
}