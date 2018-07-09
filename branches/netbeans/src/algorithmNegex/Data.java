/*
 * data.java
 *
 * Created on June 7, 2006, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package algorithmNegex;

/**
 *
 * @author VHASLCSouthB
 */

/*** To retreive note text from the database***/


import java.sql.*;
import java.util.logging.Level;

/*** Use Data Source Name "slcblind" to connect to access database ****/
/*** Use Data Source Name "btblind" to connnect to access database ****/
/*** Use Data Source Name "bt_work" to connnect to SLC data warehouse ****/
/*** Use Data Source Name "bt_balt" to connect to the Baltimore data warehouse  ****/
/*** Use new data source created from raw data in Acccess "finalsample"  ****/

public class Data {
    private static Connection con;
    private static Object TYPE_SCROLL_INSENSITIVE;
    
    static
    {
        /*        String dsn="jdbc:odbc:MUE2"; */
                 String dsn="jdbc:odbc:btblind"; 
        /*      String dsn="jdbc:odbc:slcblind"; */
        /*      String dsn="jdbc:odbc:finalsample"; */
        /*      String dsn="jdbc:odbc:BT_WORK"; */
        /*      String dsn="jdbc:odbc:bt_balt"; */
                String driver="sun.jdbc.odbc.JdbcOdbcDriver";
        
        try {
            Class.forName(driver);
            con=DriverManager.getConnection(dsn);
            
        } catch(Exception e) {
            System.out.println("Error in connection "+e);
        }
    }
   
    //Use appropriate statement to retrieve data from the database.
    public static ResultSet display(String ssn) {
        
        ResultSet rs=null;
        try {
            
            //PreparedStatement stmt=con.prepareStatement("Select NNUM, KEY_TIU_DOCUMENT, KEY_PATIENT as rnumber, TITLE, REPORT_TEXT as notetext FROM BAL_30ZIP_NOTES_2003Q4 order by KEY_TIU_DOCUMENT");
            //PreparedStatement stmt=con.prepareStatement("Select nnum, KEY_TIU_DOCUMENT, KEY_PATIENT as rnumber, TITLE, REPORT_TEXT as notetext FROM BAL_30ZIP_NOTES_2006Q1 order by KEY_TIU_DOCUMENT");
            //PreparedStatement stmt=con.prepareStatement("Select nnum, rnumber, title, notetext FROM baltnegextest order by nnum");
            //PreparedStatement stmt=con.prepareStatement("SELECT * FROM newpnotes_slc_umls2 where nnum=? order by nnum");
            //PreparedStatement stmt=con.prepareStatement("SELECT * FROM newpnotes_slc_umls2 order by nnum");
            //PreparedStatement stmt=con.prepareStatement("SELECT * FROM newpnotes_slc_umlstest order by nnum");
            //PreparedStatement stmt=con.prepareStatement("SELECT * FROM newpnotes_balt_umls2 order by nnum");
            //PreparedStatement stmt=con.prepareStatement("SELECT nnum, rnumber, notetext FROM newpnotes_balt_umls2 order by nnum");
            //PreparedStatement stmt=con.prepareStatement("SELECT * FROM newpnotes_balt_umls2 where nnum=? order by nnum"); 
            //PreparedStatement stmt=con.prepareStatement("SELECT  KEY_TIU_DOCUMENT_8925 as nnum, PATIENT_02_IEN as rnumber, REPORT_TEXT_2 as NOTETEXT FROM FinalNoteSample");
            //  PreparedStatement stmt=con.prepareStatement("SELECT  TIUDocumentIEN as nnum, TIUDocumentSID as rnumber, ReportText as NOTETEXT FROM MUE_TIUSAMPLE2");
            PreparedStatement stmt=con.prepareStatement("SELECT  reportid as nnum, PATIENT_IEN as rnumber, Note as NOTETEXT FROM training_docs");

            rs=stmt.executeQuery();
            stmt.setString(1,ssn);
            
        } catch(Exception e) {
            log.LoggingToFile.log(Level.SEVERE, "error in selecting from table:"+e);
            
        }
        return rs;
    }
}