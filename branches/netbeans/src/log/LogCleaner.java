package log;

import java.io.File;

/**
 * This class is used to remove lck files and log files which are too old or too
 * large before we start eHOST.
 *
 * @author Jianwei Chris Leng, 2011-10-13
 * @since JDK 1.6
 */
public class LogCleaner {

    /**
     * Start check files and implement our policy to remove lck files and
     * backup/remove old log files.
     */
    public void doit() {
        try {
            // get all files under current 
            File[] files = getFiles();

            // delete possible lck files
            cleaning(files);


        } catch (Exception ex) {
            System.out.println("1205311329:: failed. Error:." + ex.getMessage());
        }
    }

    /**
     * Return the files under the root folder of eHOST.
     *
     * @return files under the root folder of eHOST.
     * @throws Exception.
     */
    private File[] getFiles() throws Exception {
        try {
            File currentDir = new File(".");
            File[] files = currentDir.listFiles();
            return files;
        } catch (Exception ex) {
            throw new Exception("1205311328:: fail to get the root folder of eHOST.");
        }
    }

    /**
     * Delete any lck file under the root folder of eHOST.
     *
     * @param files the listed files that are existing under the root folder of
     * eHOST.
     */
    private void cleaning(File[] files) throws Exception {
        try {
            for (File file : files) {
                String filename = file.getName();
                // System.out.println(file.getAbsoluteFile());
                
                // The file can't be the ". (current folder)" or "..(upper folder)".
                if ( (filename.compareTo(".") == 0) || (filename.compareTo("..") == 0) ) {
                    continue;
                }
                
                // The file must be a "file", it can't be a "folder".
                if (file.isDirectory()) {
                    continue;
                }

                // delete lck files
                if (filename.length() > 4) {
                    String last4Chars = filename.substring(filename.length() - 4, filename.length());

                    if (last4Chars.compareTo(".lck") == 0) {
                        System.out.println( "Deletling: lck files : [" + file.getAbsoluteFile()+ "]");
                        file.delete();
                        continue;
                    }
                }

                if (filename.length() > 5) {

                    int i = filename.lastIndexOf('.');
                    if ((i > -1) && (i < (filename.length() - 1))) {
                        String extName = filename.substring(i + 1, filename.length());
                        //System.out.println("extension name = [" + extName + "]");

                        if (extName == null) {
                            continue;
                        }

                        if (extName.length() == 4) {


                            String first4 = extName.substring(0, 4);
                            if (first4.compareTo(".lck") == 0) {
                                System.out.println( "Deletling: lck files : [" + file.getAbsoluteFile()+ "]");
                                file.delete();
                                continue;
                            }
                        }
                    }
                }

                // remove historical logs
                if (filename.contains(".log.")) {
                    System.out.println( "Deletling: log files : [" + file.getAbsoluteFile()+ "]");
                    file.delete();
                    continue;
                }
            }

        } catch (Exception ex) {

            // if fail to delete lck log files, print the details and throw out
            // an error.
            ex.printStackTrace();
            throw new Exception("1205311330:: fail to delete lck files.");

        }
    }
}
