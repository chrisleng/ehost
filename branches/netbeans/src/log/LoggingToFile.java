/*
 * LoggingToFile.java
 *
 * Created on Aug 12, 2011, 08:18:29 AM
 */

package log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This method is designed to use Java Logging system to save logs into
 * "eHOST.log" under the installation folder of eHOST.
 *
 *
 * @author leng
 *
 * Created on Aug 12, 2011, 08:18:29 AM
 */
public class LoggingToFile {

    /**the static logger object that we can called everywhere to write log.*/
    public static Logger logger = null;

    /**The filename of the log file*/
    private static String log_filename = "eHOST.log";

    /**return a string to report current current date and time*/
    private static String CurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    /**while we ininted a logger, we keep the object in static object of
     * Logger in this class.
     */
    public static void setLogger(Logger logger){
        LoggingToFile.logger = logger;
    }


    public static void log(Level level, String text){
        //temporary comment these lines to disable log output
        //if(logger!=null)
        //    logger.log( level, text);
    }


    /**
     * allocate the path of the object of Logger
     * @param logger
     * @throws SecurityException
     * @throws IOException
     */
    public static void setLogingProperties(Logger logger) throws SecurityException, IOException {
        setLogingProperties(logger);

        //Level.ALL
    }

    /**return the filename of the log file of eHOST system*/
    public static String getLogName(){
        return log_filename;
    }

    /**
     * allocate the path of the object of Logger
     * @param   logger
     * @param   level
     *          The output level in final output
     * @throws  SecurityException
     * @throws  IOException
     */
    public static void setLogingProperties(Logger logger,Level level) {
        /*FileHandler fh;
        try {
            fh = new FileHandler(getLogName(),true);
            logger.addHandler(fh);//output file
            //logger.setLevel(level);
            fh.setFormatter(new SimpleFormatter());//output format
            //logger.addHandler(new ConsoleHandler());//output to console
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "SecurityException", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"fail to get access to the log file", e);
        }*/
    }




}
