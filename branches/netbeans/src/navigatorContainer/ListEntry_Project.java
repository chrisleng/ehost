/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package navigatorContainer;

import java.awt.Image;
import java.io.File;

/**
 *
 * @author leng
 */
public class ListEntry_Project {
  /**directory of the project*/
  private final File folder;
  private final Image image;
  /**number of corpus. Count *.txt file under the project directory. 
   * -2 means this vaiable didn't get initilized;
   * -1 means error(s) occurred while trying to count the ".txt" files;
   * 0-infinite means the number of corpus.
   */
  private int numberOfCorpus=-2;

  public ListEntry_Project(File folder, Image image, int _numberOfCorpus) {
    this.folder = folder;
    this.image = image;
    this.numberOfCorpus = _numberOfCorpus;
    //if(image==null)
    //    System.out.println("error 1102021335::no image resoure!!!");
  }


  /**Return the number of corpus. Count *.txt file under the project
   * directory.
   * 
   * @return 
   *    -2          : means this vaiable didn't get initilized;
   *    -1          : means error(s) occurred while trying to count
   *                  the ".txt" files;
   *    0-infinite  : means the number of corpus.
   */
  public int getNumberOfCorpus(){
      return this.numberOfCorpus;
  }

  public String getTitle() {
      String str = folder.getName();
      return str;
  }

  public File getFolder(){
    return folder;
  }

  public Image getImage() {
    return image;
  }

  
  // Override standard toString method to give a useful result
    @Override
  public String toString() {
    return folder.getName();
  }
}