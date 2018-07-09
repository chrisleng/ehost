/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package debugTools;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class Annotations {

    /**This method is designed to print out how many articles and how many
     * annotations belong to them. Usually it is used to test program.
     */
    public static void debug_echoResult() {

        System.out.println("");

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();

        // #### 1 :: tell us how many articles are in memory after NLP processing.
        ArrayList<resultEditor.annotations.Article> articles =  depot.getAllArticles();
        if(articles==null){
            log.LoggingToFile.log( Level.WARNING, "~~~~ DEBUG ~~~~: there is no article in memory!!!");
        }else{
            log.LoggingToFile.log( Level.INFO,"~~~~ DEBUG ~~~~: there are "
                    + articles.size()
                    +" articles in memory!!!");
        }

        // #### 2: tell us how many annotations we have in each article
        for(resultEditor.annotations.Article article: articles){

            if(article==null){
                log.LoggingToFile.log( Level.WARNING, "~~~~ DEBUG ~~~~: A NULL article is found!!!");
                continue;
            }

            if(article.annotations == null){
                log.LoggingToFile.log( Level.WARNING, "~~~~ DEBUG ~~~~: there is no any annotations in article {"
                        + article.filename
                        + "} as article.annotations==null!!!");
                continue;
            }

            log.LoggingToFile.log( Level.INFO, "~~~~ DEBUG ~~~~: there are "
                    + article.annotations.size()
                    + "annotations in article {"
                    + article.filename
                    + "}.");


        }



    }
}
